package studios.tkoh.portfolio.dto.skill;

/**
 *
 * @author Studios TKOH!
 */
public record SkillDto(
        Long id,
        String name,
        Long globalSkillId,
        short level,
        String icon) {

}
