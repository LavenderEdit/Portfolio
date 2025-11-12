package studios.tkoh.portfolio.dto.skill;

import java.util.Set;

/**
 *
 * @author Studios TKOH!
 */
public record SkillCategoryDto(
        Long id,
        String name,
        int sortOrder,
        Set<SkillDto> skills) {

}
