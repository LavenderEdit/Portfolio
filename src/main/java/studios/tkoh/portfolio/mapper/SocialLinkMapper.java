package studios.tkoh.portfolio.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import studios.tkoh.portfolio.dto.social.SocialLinkCreateRequest;
import studios.tkoh.portfolio.dto.social.SocialLinkDto;
import studios.tkoh.portfolio.dto.social.SocialLinkUpdateRequest;
import studios.tkoh.portfolio.model.SocialLink;

/**
 *
 * @author Studios TKOH!
 */
@Mapper(componentModel = "spring")
public interface SocialLinkMapper {

    SocialLinkDto toDto(SocialLink socialLink);

    @Mapping(target = "profile", ignore = true)
    SocialLink toEntity(SocialLinkCreateRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "profile", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(SocialLinkUpdateRequest dto, @MappingTarget SocialLink entity);
}
