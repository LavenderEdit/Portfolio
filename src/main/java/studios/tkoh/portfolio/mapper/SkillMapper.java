package studios.tkoh.portfolio.mapper;

import org.mapstruct.Mapper;
import studios.tkoh.portfolio.dto.skill.SkillDto;
import studios.tkoh.portfolio.model.Skill;

/**
 *
 * @author Studios TKOH!
 */
@Mapper(componentModel = "spring")
public interface SkillMapper {

    SkillDto toDto(Skill skill);
}
