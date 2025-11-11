package studios.tkoh.portfolio.dto.profile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 *
 * @author Studios TKOH!
 */
public record ProfileUpdateRequest(
        @NotBlank(message = "El nombre completo no puede estar vacío")
        @Size(max = 120)
        String fullName,
        @NotBlank(message = "El titular no puede estar vacío")
        @Size(max = 160)
        String headline,
        @NotBlank(message = "La biografía no puede estar vacía")
        @Size(max = 5000)
        String bio,
        @NotBlank(message = "El email de contacto no puede estar vacío")
        @Email
        @Size(max = 160)
        String contactEmail,
        @Size(max = 100)
        String location) {

}
