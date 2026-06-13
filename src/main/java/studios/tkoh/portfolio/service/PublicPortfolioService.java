package studios.tkoh.portfolio.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import studios.tkoh.portfolio.dto.project.ProjectDto;
import studios.tkoh.portfolio.dto.publicapi.PortfolioDetailDto;
import studios.tkoh.portfolio.dto.publicapi.PortfolioPublicDto;

/**
 *
 * @author Studios TKOH!
 */
public interface PublicPortfolioService {

    Page<PortfolioPublicDto> getAllPublicProfiles(Pageable pageable);

    PortfolioDetailDto getFullPortfolioBySlug(String slug);

    ProjectDto getPublicProjectBySlugs(String profileSlug, String projectSlug);
}
