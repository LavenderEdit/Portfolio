package studios.tkoh.portfolio.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 *
 * @author Studios TKOH!
 */
public record SetPasswordRequest(
        @NotBlank(message = "La contraseña no puede estar vacía")
        @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
        String newPassword
) {
}
