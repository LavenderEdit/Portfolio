package studios.tkoh.portfolio.dto.profile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 *
 * @author Studios TKOH!
 */
public record ContactEmailUpdateRequest(
        @NotBlank(message = "El email de contacto no puede estar vacío")
        @Email
        @Size(max = 160)
        String email) {

}
