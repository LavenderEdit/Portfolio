package studios.tkoh.portfolio.dto.publicapi;

/**
 *
 * @author Studios TKOH!
 */
public record PortfolioPublicDto(
        String slug,
        String fullName,
        String headline,
        String avatarUrl,
        boolean tkohCollab) {

}
