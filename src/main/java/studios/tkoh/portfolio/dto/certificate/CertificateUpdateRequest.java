package studios.tkoh.portfolio.dto.certificate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 *
 * @author Studios TKOH!
 */
public record CertificateUpdateRequest(
        @NotNull(message = "El ID es obligatorio para actualizar")
        Long id,
        @NotBlank(message = "El nombre no puede estar vacío")
        @Size(max = 255)
        String name,
        @Size(max = 65535)
        String description,
        Long educationId) {

}
