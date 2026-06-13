package studios.tkoh.portfolio.dto.response;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ApiResponseTest {

    @Test
    void standardErrorIncludesCodePathAndRequestId() {
        ApiResponse<Void> response = ApiResponse.error(
                "RATE_LIMITED",
                "Demasiadas solicitudes",
                "/api/auth/login",
                "req-123"
        );

        assertThat(response.isSuccess()).isFalse();
        assertThat(response.getErrorCode()).isEqualTo("RATE_LIMITED");
        assertThat(response.getMessage()).isEqualTo("Demasiadas solicitudes");
        assertThat(response.getPath()).isEqualTo("/api/auth/login");
        assertThat(response.getRequestId()).isEqualTo("req-123");
        assertThat(response.getTimestamp()).isNotNull();
    }
}
