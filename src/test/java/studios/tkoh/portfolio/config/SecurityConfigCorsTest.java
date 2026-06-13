package studios.tkoh.portfolio.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.lang.reflect.Field;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.cors.CorsConfiguration;

class SecurityConfigCorsTest {

    @Test
    void rejectsBlankOriginsInsteadOfFallingBackToWildcard() throws Exception {
        SecurityConfig config = new SecurityConfig(null, null, null, null);
        setField(config, "allowedOrigins", " ");

        assertThatThrownBy(config::corsConfigurationSource)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("CORS_ALLOWED_ORIGINS");
    }

    @Test
    void keepsCredentialsOnlyWithExplicitOrigins() throws Exception {
        SecurityConfig config = new SecurityConfig(null, null, null, null);
        setField(config, "allowedOrigins", "https://app.example.com,http://localhost:5173");

        CorsConfiguration cors = config.corsConfigurationSource()
                .getCorsConfiguration(new MockHttpServletRequest("GET", "/api/portfolios"));

        assertThat(cors.getAllowedOrigins()).containsExactly("https://app.example.com", "http://localhost:5173");
        assertThat(cors.getAllowedOrigins()).doesNotContain("*");
        assertThat(cors.getAllowCredentials()).isTrue();
    }

    private static void setField(Object target, String name, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(name);
        field.setAccessible(true);
        field.set(target, value);
    }
}
