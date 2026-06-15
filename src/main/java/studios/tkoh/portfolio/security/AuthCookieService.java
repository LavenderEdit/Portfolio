package studios.tkoh.portfolio.security;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Service
public class AuthCookieService {

    public static final String ACCESS_TOKEN_COOKIE = "portfolio_access";
    public static final String REFRESH_TOKEN_COOKIE = "portfolio_refresh";

    private final String domain;
    private final boolean secure;
    private final String sameSite;

    public AuthCookieService(
            @Value("${application.security.cookie.domain:}") String domain,
            @Value("${application.security.cookie.secure:true}") boolean secure,
            @Value("${application.security.cookie.same-site:Lax}") String sameSite
    ) {
        this.domain = domain == null ? "" : domain.trim();
        this.secure = secure;
        this.sameSite = sameSite == null || sameSite.isBlank() ? "Lax" : sameSite.trim();
    }

    public void addAccessTokenCookie(HttpServletResponse response, String value, int maxAgeSeconds) {
        addCookie(response, ACCESS_TOKEN_COOKIE, value, maxAgeSeconds);
    }

    public void addRefreshTokenCookie(HttpServletResponse response, String value, int maxAgeSeconds) {
        addCookie(response, REFRESH_TOKEN_COOKIE, value, maxAgeSeconds);
    }

    public void clearAccessTokenCookie(HttpServletResponse response) {
        addCookie(response, ACCESS_TOKEN_COOKIE, "", 0);
    }

    public void clearRefreshTokenCookie(HttpServletResponse response) {
        addCookie(response, REFRESH_TOKEN_COOKIE, "", 0);
    }

    private void addCookie(HttpServletResponse response, String name, String value, int maxAgeSeconds) {
        ResponseCookie.ResponseCookieBuilder cookieBuilder = ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(secure)
                .path("/")
                .maxAge(maxAgeSeconds)
                .sameSite(sameSite);

        if (!domain.isBlank()) {
            cookieBuilder.domain(domain);
        }

        response.addHeader(HttpHeaders.SET_COOKIE, cookieBuilder.build().toString());
    }
}
