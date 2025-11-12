package studios.tkoh.portfolio.service;

import studios.tkoh.portfolio.dto.experience.ExperienceCreateRequest;
import studios.tkoh.portfolio.dto.experience.ExperienceDto;
import studios.tkoh.portfolio.dto.experience.ExperienceUpdateRequest;
import studios.tkoh.portfolio.service.generic.DtoCrudService;

/**
 *
 * @author Studios TKOH!
 */
public interface ExperienceService extends DtoCrudService<ExperienceDto, ExperienceCreateRequest, ExperienceUpdateRequest, Long> {

}
