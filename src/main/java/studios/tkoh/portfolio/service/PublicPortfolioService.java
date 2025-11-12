package studios.tkoh.portfolio.service;

import java.util.List;
import studios.tkoh.portfolio.dto.project.ProjectDto;
import studios.tkoh.portfolio.dto.publicapi.PortfolioDetailDto;
import studios.tkoh.portfolio.dto.publicapi.PortfolioPublicDto;

/**
 *
 * @author Studios TKOH!
 */
public interface PublicPortfolioService {

    List<PortfolioPublicDto> getAllPublicProfiles();

    PortfolioDetailDto getFullPortfolioBySlug(String slug);

    ProjectDto getPublicProjectBySlugs(String profileSlug, String projectSlug);
}
