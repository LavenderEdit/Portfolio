package studios.tkoh.portfolio.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import studios.tkoh.portfolio.dto.project.ProjectCreateRequest;
import studios.tkoh.portfolio.dto.project.ProjectDto;
import studios.tkoh.portfolio.dto.project.ProjectUpdateRequest;
import studios.tkoh.portfolio.model.Project;

/**
 *
 * @author Studios TKOH!
 */
@Mapper(componentModel = "spring", uses = {SkillMapper.class})
public interface ProjectMapper {

    ProjectDto toDto(Project project);

    @Mapping(target = "profile", ignore = true)
    @Mapping(target = "slug", ignore = true)
    @Mapping(target = "skills", ignore = true)
    Project toEntity(ProjectCreateRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "profile", ignore = true)
    @Mapping(target = "slug", ignore = true)
    @Mapping(target = "skills", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(ProjectUpdateRequest dto, @MappingTarget Project entity);
}
