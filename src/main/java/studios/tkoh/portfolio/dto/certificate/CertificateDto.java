package studios.tkoh.portfolio.dto.certificate;

/**
 *
 * @author Studios TKOH!
 */
public record CertificateDto(
        Long id,
        Long educationId,
        String name,
        String description,
        String imageUrl,
        String fileId) {

}
