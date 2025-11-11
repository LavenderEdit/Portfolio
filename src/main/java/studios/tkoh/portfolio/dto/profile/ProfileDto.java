package studios.tkoh.portfolio.dto.profile;

/**
 *
 * @author Studios TKOH!
 */
public record ProfileDto(
        Long id,
        String slug,
        String fullName,
        String headline,
        String bio,
        String contactEmail,
        String location,
        String avatarUrl,
        String resumeUrl) {

}
