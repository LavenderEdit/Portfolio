package studios.tkoh.portfolio.dto.publicapi;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 *
 * @author Studios TKOH!
 */
public record ContactRequest(
        @NotBlank(message = "El nombre no puede estar vacío")
        @Size(max = 120)
        String name,
        @NotBlank(message = "El email no puede estar vacío")
        @Email
        @Size(max = 160)
        String email,
        @NotBlank(message = "El mensaje no puede estar vacío")
        @Size(max = 5000)
        String message) {

}
