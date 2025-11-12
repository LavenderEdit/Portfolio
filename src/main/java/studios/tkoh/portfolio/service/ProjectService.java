package studios.tkoh.portfolio.service;

import java.util.List;
import studios.tkoh.portfolio.dto.project.ProjectCreateRequest;
import studios.tkoh.portfolio.dto.project.ProjectDto;
import studios.tkoh.portfolio.dto.project.ProjectUpdateRequest;
import studios.tkoh.portfolio.service.generic.DtoCrudService;

/**
 *
 * @author Studios TKOH!
 */
public interface ProjectService extends DtoCrudService<ProjectDto, ProjectCreateRequest, ProjectUpdateRequest, Long> {

    ProjectDto associateSkills(Long projectId, List<Long> skillIds);

    ProjectDto updateCoverImageUrl(Long projectId, Long profileId, String newCoverImageUrl);
}
