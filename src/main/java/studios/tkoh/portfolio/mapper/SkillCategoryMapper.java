package studios.tkoh.portfolio.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import studios.tkoh.portfolio.dto.skill.SkillCategoryCreateRequest;
import studios.tkoh.portfolio.dto.skill.SkillCategoryDto;
import studios.tkoh.portfolio.dto.skill.SkillCategoryUpdateRequest;
import studios.tkoh.portfolio.model.SkillCategory;

/**
 *
 * @author Studios TKOH!
 */
@Mapper(componentModel = "spring", uses = {SkillMapper.class})
public interface SkillCategoryMapper {

    SkillCategoryDto toDto(SkillCategory category);

    @Mapping(target = "profile", ignore = true)
    @Mapping(target = "skills", ignore = true)
    SkillCategory toEntity(SkillCategoryCreateRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "profile", ignore = true)
    @Mapping(target = "skills", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(SkillCategoryUpdateRequest dto, @MappingTarget SkillCategory entity);
}
