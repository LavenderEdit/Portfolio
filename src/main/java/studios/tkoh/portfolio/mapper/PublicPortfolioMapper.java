package studios.tkoh.portfolio.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import studios.tkoh.portfolio.dto.publicapi.PortfolioDetailDto;
import studios.tkoh.portfolio.dto.publicapi.PortfolioPublicDto;
import studios.tkoh.portfolio.dto.publicapi.ProjectSummaryDto;
import studios.tkoh.portfolio.model.Profile;
import studios.tkoh.portfolio.model.Project;

/**
 *
 * @author Studios TKOH!
 */
@Mapper(componentModel = "spring")
public interface PublicPortfolioMapper {

    @Mapping(target = "socialLinks", ignore = true)
    @Mapping(target = "skillCategories", ignore = true)
    @Mapping(target = "projects", ignore = true)
    @Mapping(target = "experiences", ignore = true)
    @Mapping(target = "education", ignore = true)
    PortfolioDetailDto profileToDetailDto(Profile profile);

    PortfolioPublicDto profileToPublicDto(Profile profile);

    @Mapping(source = "coverImage", target = "coverImage")
    ProjectSummaryDto projectToSummaryDto(Project project);
}
