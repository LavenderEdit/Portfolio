package studios.tkoh.portfolio.dto.social;

/**
 *
 * @author Studios TKOH!
 */
public record SocialLinkDto(
        Long id,
        String platform,
        String url,
        int sortOrder) {

}
