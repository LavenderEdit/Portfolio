package studios.tkoh.portfolio.dto.certificate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 *
 * @author Studios TKOH!
 */
public record CertificateCreateRequest(
        @NotBlank(message = "El nombre no puede estar vacío")
        @Size(max = 255)
        String name,
        @Size(max = 65535)
        String description,
        Long educationId) {

}
