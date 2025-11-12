package studios.tkoh.portfolio.service;

import studios.tkoh.portfolio.dto.project.ProjectCreateRequest;
import studios.tkoh.portfolio.dto.project.ProjectDto;
import studios.tkoh.portfolio.dto.project.ProjectUpdateRequest;
import studios.tkoh.portfolio.service.generic.DtoCrudService;

/**
 *
 * @author Studios TKOH!
 */
public interface ProjectService extends DtoCrudService<ProjectDto, ProjectCreateRequest, ProjectUpdateRequest, Long> {

}
