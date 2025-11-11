package studios.tkoh.portfolio.service;

import studios.tkoh.portfolio.dto.profile.ProfileDto;
import studios.tkoh.portfolio.dto.profile.ProfileUpdateRequest;

/**
 *
 * @author Studios TKOH!
 */
public interface ProfileService {

    ProfileDto getMyProfile(Long profileId);

    ProfileDto updateMyProfile(Long profileId, ProfileUpdateRequest request);
}
