package studios.tkoh.portfolio.dto.publicapi;

/**
 *
 * @author Studios TKOH!
 */
public record ProjectSummaryDto(
        String title,
        String slug,
        String summary,
        String coverImage,
        boolean featured) {

}
