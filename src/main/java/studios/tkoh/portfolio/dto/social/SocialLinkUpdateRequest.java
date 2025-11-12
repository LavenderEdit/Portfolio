package studios.tkoh.portfolio.dto.social;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

/**
 *
 * @author Studios TKOH!
 */
public record SocialLinkUpdateRequest(
        @NotNull(message = "El ID es obligatorio para actualizar")
        Long id,
        @NotBlank(message = "La plataforma no puede estar vacía")
        @Size(max = 50)
        String platform,
        @NotBlank(message = "La URL no puede estar vacía")
        @Size(max = 512)
        @URL(message = "Debe ser una URL válida")
        String url,
        @NotNull(message = "El orden no puede ser nulo")
        int sortOrder) {

}
