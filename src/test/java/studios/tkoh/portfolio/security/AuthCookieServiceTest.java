package studios.tkoh.portfolio.security;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;

class AuthCookieServiceTest {

    @Test
    void createsHttpOnlySecureAccessCookieWithConfiguredSameSite() {
        AuthCookieService service = new AuthCookieService("api.local", true, "Strict");

        Cookie cookie = service.createAccessTokenCookie("access-token", 900);

        assertThat(cookie.getName()).isEqualTo(AuthCookieService.ACCESS_TOKEN_COOKIE);
        assertThat(cookie.getValue()).isEqualTo("access-token");
        assertThat(cookie.isHttpOnly()).isTrue();
        assertThat(cookie.getSecure()).isTrue();
        assertThat(cookie.getDomain()).isEqualTo("api.local");
        assertThat(cookie.getPath()).isEqualTo("/");
        assertThat(cookie.getMaxAge()).isEqualTo(900);
        assertThat(cookie.getAttribute("SameSite")).isEqualTo("Strict");
    }

    @Test
    void createsClearingCookieForLogout() {
        AuthCookieService service = new AuthCookieService("", false, "Lax");

        Cookie cookie = service.clearAccessTokenCookie();

        assertThat(cookie.getName()).isEqualTo(AuthCookieService.ACCESS_TOKEN_COOKIE);
        assertThat(cookie.getValue()).isEmpty();
        assertThat(cookie.getMaxAge()).isZero();
        assertThat(cookie.isHttpOnly()).isTrue();
        assertThat(cookie.getPath()).isEqualTo("/");
    }
}
