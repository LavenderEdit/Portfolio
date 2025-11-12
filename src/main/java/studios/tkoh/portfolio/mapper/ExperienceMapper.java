package studios.tkoh.portfolio.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import studios.tkoh.portfolio.dto.experience.ExperienceCreateRequest;
import studios.tkoh.portfolio.dto.experience.ExperienceDto;
import studios.tkoh.portfolio.dto.experience.ExperienceUpdateRequest;
import studios.tkoh.portfolio.model.Experience;

/**
 *
 * @author Studios TKOH!
 */
@Mapper(componentModel = "spring")
public interface ExperienceMapper {

    ExperienceDto toDto(Experience experience);

    @Mapping(target = "profile", ignore = true)
    Experience toEntity(ExperienceCreateRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "profile", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(ExperienceUpdateRequest dto, @MappingTarget Experience entity);
}
