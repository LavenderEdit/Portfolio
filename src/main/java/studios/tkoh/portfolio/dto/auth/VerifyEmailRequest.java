package studios.tkoh.portfolio.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record VerifyEmailRequest(
        @NotBlank(message = "El token es obligatorio")
        String token) {
}
