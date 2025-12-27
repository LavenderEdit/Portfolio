package studios.tkoh.portfolio.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import studios.tkoh.portfolio.dto.skill.SkillCreateRequest;
import studios.tkoh.portfolio.dto.skill.SkillDto;
import studios.tkoh.portfolio.dto.skill.SkillUpdateRequest;
import studios.tkoh.portfolio.model.Skill;

/**
 *
 * @author Studios TKOH!
 */
@Mapper(componentModel = "spring")
public interface SkillMapper {

    @Mapping(target = "name", expression = "java(skill.getName())")
    @Mapping(target = "icon", expression = "java(skill.getResolvedIconUrl())")
    @Mapping(target = "globalSkillId", source = "globalSkill.id")
    SkillDto toDto(Skill skill);

    @Mapping(target = "category", ignore = true)
    @Mapping(target = "globalSkill", ignore = true)
    @Mapping(target = "id", ignore = true)
    Skill toEntity(SkillCreateRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "globalSkill", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(SkillUpdateRequest dto, @MappingTarget Skill entity);
}
