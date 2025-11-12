package studios.tkoh.portfolio.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import studios.tkoh.portfolio.dto.education.EducationCreateRequest;
import studios.tkoh.portfolio.dto.education.EducationDto;
import studios.tkoh.portfolio.dto.education.EducationUpdateRequest;
import studios.tkoh.portfolio.model.Education;

/**
 *
 * @author Studios TKOH!
 */
@Mapper(componentModel = "spring")
public interface EducationMapper {

    EducationDto toDto(Education education);

    @Mapping(target = "profile", ignore = true)
    Education toEntity(EducationCreateRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "profile", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(EducationUpdateRequest dto, @MappingTarget Education entity);
}
