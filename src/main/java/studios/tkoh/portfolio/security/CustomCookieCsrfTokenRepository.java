package studios.tkoh.portfolio.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.util.StringUtils;

public class CustomCookieCsrfTokenRepository implements CsrfTokenRepository {

    private static final String DEFAULT_CSRF_COOKIE_NAME = "XSRF-TOKEN";

    private final CookieCsrfTokenRepository delegate;
    private final String cookieDomain;
    private final boolean secure;
    private final String sameSite;

    public CustomCookieCsrfTokenRepository(String cookieDomain, boolean secure, String sameSite) {
        this.delegate = new CookieCsrfTokenRepository();
        this.cookieDomain = cookieDomain == null ? "" : cookieDomain.trim();
        this.secure = secure;
        this.sameSite = sameSite == null || sameSite.isBlank() ? "Lax" : sameSite.trim();
    }

    @Override
    public CsrfToken generateToken(HttpServletRequest request) {
        return this.delegate.generateToken(request);
    }

    @Override
    public void saveToken(CsrfToken token, HttpServletRequest request, HttpServletResponse response) {
        if (token == null) {
            ResponseCookie.ResponseCookieBuilder cookieBuilder = ResponseCookie.from(DEFAULT_CSRF_COOKIE_NAME, "")
                    .maxAge(0)
                    .path("/")
                    .secure(this.secure)
                    .sameSite(this.sameSite);

            if (StringUtils.hasText(this.cookieDomain)) {
                cookieBuilder.domain(this.cookieDomain);
            }

            response.addHeader(HttpHeaders.SET_COOKIE, cookieBuilder.build().toString());
        } else {
            ResponseCookie.ResponseCookieBuilder cookieBuilder = ResponseCookie.from(DEFAULT_CSRF_COOKIE_NAME, token.getToken())
                    .maxAge(-1) // Session cookie
                    .httpOnly(false)
                    .path("/")
                    .secure(this.secure)
                    .sameSite(this.sameSite);

            if (StringUtils.hasText(this.cookieDomain)) {
                cookieBuilder.domain(this.cookieDomain);
            }

            response.addHeader(HttpHeaders.SET_COOKIE, cookieBuilder.build().toString());
        }
    }

    @Override
    public CsrfToken loadToken(HttpServletRequest request) {
        return this.delegate.loadToken(request);
    }
}
