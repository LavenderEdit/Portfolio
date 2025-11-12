package studios.tkoh.portfolio.service;

import studios.tkoh.portfolio.dto.education.EducationCreateRequest;
import studios.tkoh.portfolio.dto.education.EducationDto;
import studios.tkoh.portfolio.dto.education.EducationUpdateRequest;
import studios.tkoh.portfolio.service.generic.DtoCrudService;

/**
 *
 * @author Studios TKOH!
 */
public interface EducationService extends DtoCrudService<EducationDto, EducationCreateRequest, EducationUpdateRequest, Long> {

}
