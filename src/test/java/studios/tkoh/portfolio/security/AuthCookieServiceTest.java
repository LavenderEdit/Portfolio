package studios.tkoh.portfolio.security;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletResponse;

class AuthCookieServiceTest {

    @Test
    void createsHttpOnlySecureAccessCookieWithConfiguredSameSite() {
        AuthCookieService service = new AuthCookieService("api.local", true, "Strict");
        MockHttpServletResponse response = new MockHttpServletResponse();

        service.addAccessTokenCookie(response, "access-token", 900);

        String setCookieHeader = response.getHeader(HttpHeaders.SET_COOKIE);
        assertThat(setCookieHeader).isNotNull();
        assertThat(setCookieHeader).contains(AuthCookieService.ACCESS_TOKEN_COOKIE + "=access-token");
        assertThat(setCookieHeader).contains("HttpOnly");
        assertThat(setCookieHeader).contains("Secure");
        assertThat(setCookieHeader).contains("Domain=api.local");
        assertThat(setCookieHeader).contains("Path=/");
        assertThat(setCookieHeader).contains("Max-Age=900");
        assertThat(setCookieHeader).contains("SameSite=Strict");
    }

    @Test
    void createsClearingCookieForLogout() {
        AuthCookieService service = new AuthCookieService("", false, "Lax");
        MockHttpServletResponse response = new MockHttpServletResponse();

        service.clearAccessTokenCookie(response);

        String setCookieHeader = response.getHeader(HttpHeaders.SET_COOKIE);
        assertThat(setCookieHeader).isNotNull();
        assertThat(setCookieHeader).contains(AuthCookieService.ACCESS_TOKEN_COOKIE + "=");
        assertThat(setCookieHeader).contains("Max-Age=0");
        assertThat(setCookieHeader).contains("HttpOnly");
        assertThat(setCookieHeader).contains("Path=/");
        assertThat(setCookieHeader).doesNotContain("Secure");
    }
}
