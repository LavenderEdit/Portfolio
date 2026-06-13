package studios.tkoh.portfolio.security;

import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;
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

    public Cookie createAccessTokenCookie(String value, int maxAgeSeconds) {
        return createCookie(ACCESS_TOKEN_COOKIE, value, maxAgeSeconds);
    }

    public Cookie createRefreshTokenCookie(String value, int maxAgeSeconds) {
        return createCookie(REFRESH_TOKEN_COOKIE, value, maxAgeSeconds);
    }

    public Cookie clearAccessTokenCookie() {
        return createCookie(ACCESS_TOKEN_COOKIE, "", 0);
    }

    public Cookie clearRefreshTokenCookie() {
        return createCookie(REFRESH_TOKEN_COOKIE, "", 0);
    }

    private Cookie createCookie(String name, String value, int maxAgeSeconds) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setSecure(secure);
        cookie.setPath("/");
        cookie.setMaxAge(maxAgeSeconds);
        if (!domain.isBlank()) {
            cookie.setDomain(domain);
        }
        cookie.setAttribute("SameSite", sameSite);
        return cookie;
    }
}
