package studios.tkoh.portfolio.service;

import studios.tkoh.portfolio.dto.social.SocialLinkCreateRequest;
import studios.tkoh.portfolio.dto.social.SocialLinkDto;
import studios.tkoh.portfolio.dto.social.SocialLinkUpdateRequest;
import studios.tkoh.portfolio.service.generic.DtoCrudService;

/**
 *
 * @author Studios TKOH!
 */
public interface SocialLinkService extends DtoCrudService<SocialLinkDto, SocialLinkCreateRequest, SocialLinkUpdateRequest, Long> {
}
