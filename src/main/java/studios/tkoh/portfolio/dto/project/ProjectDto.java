package studios.tkoh.portfolio.dto.project;

import java.time.LocalDate;
import java.util.Set;
import studios.tkoh.portfolio.dto.skill.SkillDto;

/**
 *
 * @author Studios TKOH!
 */
public record ProjectDto(
        Long id,
        String title,
        String slug,
        String summary,
        String description,
        String repoUrl,
        String liveUrl,
        String coverImage,
        LocalDate startDate,
        LocalDate endDate,
        boolean featured,
        int sortOrder,
        Set<SkillDto> skills) {

}
