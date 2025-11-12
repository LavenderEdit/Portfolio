package studios.tkoh.portfolio.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import studios.tkoh.portfolio.dto.profile.ProfileDto;
import studios.tkoh.portfolio.dto.profile.ProfileUpdateRequest;
import studios.tkoh.portfolio.exception.ResourceNotFoundException;
import studios.tkoh.portfolio.mapper.ProfileMapper;
import studios.tkoh.portfolio.model.Profile;
import studios.tkoh.portfolio.repository.ProfileRepo;
import studios.tkoh.portfolio.service.ProfileService;

/**
 *
 * @author Studios TKOH!
 */
@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepo profileRepository;
    private final ProfileMapper profileMapper;

    @Override
    @Transactional(readOnly = true)
    public ProfileDto getMyProfile(Long profileId) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile", "id", profileId));

        return profileMapper.toDto(profile);
    }

    @Override
    @Transactional
    public ProfileDto updateMyProfile(Long profileId, ProfileUpdateRequest request) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile", "id", profileId));

        profileMapper.updateEntityFromDto(request, profile);

        Profile savedProfile = profileRepository.save(profile);

        return profileMapper.toDto(savedProfile);
    }

    @Override
    @Transactional
    public ProfileDto updateAvatarUrl(Long profileId, String newAvatarUrl) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile", "id", profileId));
        profile.setAvatarUrl(newAvatarUrl);
        Profile savedProfile = profileRepository.save(profile);
        return profileMapper.toDto(savedProfile);
    }

    @Override
    @Transactional
    public ProfileDto updateResumeUrl(Long profileId, String newResumeUrl) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile", "id", profileId));
        profile.setResumeUrl(newResumeUrl);
        Profile savedProfile = profileRepository.save(profile);
        return profileMapper.toDto(savedProfile);
    }

    @Override
    @Transactional
    public ProfileDto updateContactEmail(Long profileId, String newContactEmail) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile", "id", profileId));
        profile.setContactEmail(newContactEmail);
        Profile savedProfile = profileRepository.save(profile);
        return profileMapper.toDto(savedProfile);
    }
}
