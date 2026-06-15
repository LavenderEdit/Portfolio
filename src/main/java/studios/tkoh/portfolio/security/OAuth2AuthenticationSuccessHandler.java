package studios.tkoh.portfolio.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import studios.tkoh.portfolio.dto.auth.AuthResponse;
import studios.tkoh.portfolio.model.User;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final OAuth2UserProvisioningService provisioningService;
    private final AuthSessionService authSessionService;
    private final AuthCookieService authCookieService;
    private final JwtService jwtService;

    @Value("${application.security.frontend.allowed-redirects:http://localhost:5173}")
    private String allowedRedirects;

    @Value("${application.security.frontend.default-redirect:http://localhost:5173/auth/callback}")
    private String defaultRedirect;

    @Value("${application.security.jwt.refresh-expiration-days:7}")
    private long refreshExpirationDays;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        String subject = oauth2User.getAttribute("sub");
        String email = oauth2User.getAttribute("email");
        Boolean emailVerified = oauth2User.getAttribute("email_verified");
        String name = oauth2User.getAttribute("name");
        String picture = oauth2User.getAttribute("picture");

        if (subject == null || email == null || Boolean.FALSE.equals(emailVerified)) {
            response.sendRedirect(safeRedirect(defaultRedirect) + "?error=oauth_email_unverified");
            return;
        }

        User user = provisioningService.provisionGoogleUser(subject, email, Boolean.TRUE.equals(emailVerified), name, picture);
        AuthResponse authResponse = authSessionService.createSession(user.getEmail(), request);
        authCookieService.addAccessTokenCookie(response, authResponse.token(), jwtService.getAccessTokenMaxAgeSeconds());
        authCookieService.addRefreshTokenCookie(response, authResponse.refreshToken(), refreshMaxAgeSeconds());

        org.springframework.security.web.csrf.CsrfToken csrfToken = (org.springframework.security.web.csrf.CsrfToken) request.getAttribute(org.springframework.security.web.csrf.CsrfToken.class.getName());
        if (csrfToken != null) {
            csrfToken.getToken(); 
        }

        getRedirectStrategy().sendRedirect(request, response, safeRedirect(defaultRedirect));
    }

    private String safeRedirect(String requestedRedirect) {
        List<String> allowed = Arrays.stream(allowedRedirects.split(","))
                .map(String::trim)
                .filter(value -> !value.isBlank())
                .toList();
        return allowed.stream()
                .filter(requestedRedirect::startsWith)
                .findFirst()
                .map(ignored -> requestedRedirect)
                .orElse(allowed.isEmpty() ? "http://localhost:5173" : allowed.get(0));
    }

    private int refreshMaxAgeSeconds() {
        return Math.toIntExact(refreshExpirationDays * 24 * 60 * 60);
    }
}
