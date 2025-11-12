package studios.tkoh.portfolio.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import studios.tkoh.portfolio.dto.social.SocialLinkCreateRequest;
import studios.tkoh.portfolio.dto.social.SocialLinkDto;
import studios.tkoh.portfolio.dto.social.SocialLinkUpdateRequest;
import studios.tkoh.portfolio.exception.ResourceNotFoundException;
import studios.tkoh.portfolio.mapper.SocialLinkMapper;
import studios.tkoh.portfolio.model.Profile;
import studios.tkoh.portfolio.model.SocialLink;
import studios.tkoh.portfolio.repository.ProfileRepo;
import studios.tkoh.portfolio.repository.SocialLinkRepo;
import studios.tkoh.portfolio.security.CustomUserDetails;
import studios.tkoh.portfolio.service.SocialLinkService;

/**
 *
 * @author Studios TKOH!
 */
@Service
@RequiredArgsConstructor
public class SocialLinkServiceImpl implements SocialLinkService {

    private final SocialLinkRepo socialLinkRepository;
    private final ProfileRepo profileRepository;
    private final SocialLinkMapper socialLinkMapper;

    @Override
    @Transactional
    public SocialLinkDto create(SocialLinkCreateRequest create) {
        CustomUserDetails user = getAuthenticatedUser();
        Profile profile = profileRepository.getReferenceById(user.getProfileId());

        SocialLink socialLink = socialLinkMapper.toEntity(create);
        socialLink.setProfile(profile);

        SocialLink savedLink = socialLinkRepository.save(socialLink);
        return socialLinkMapper.toDto(savedLink);
    }

    @Override
    @Transactional
    public SocialLinkDto update(SocialLinkUpdateRequest updated) {
        CustomUserDetails user = getAuthenticatedUser();
        Long profileId = user.getProfileId();

        SocialLink socialLink = socialLinkRepository.findByIdAndProfileId(updated.id(), profileId)
                .orElseThrow(() -> new ResourceNotFoundException("SocialLink", "id", updated.id()));

        socialLinkMapper.updateEntityFromDto(updated, socialLink);
        SocialLink savedLink = socialLinkRepository.save(socialLink);
        return socialLinkMapper.toDto(savedLink);
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        CustomUserDetails user = getAuthenticatedUser();
        Long profileId = user.getProfileId();

        SocialLink socialLink = socialLinkRepository.findByIdAndProfileId(id, profileId)
                .orElseThrow(() -> new ResourceNotFoundException("SocialLink", "id", id));

        socialLinkRepository.delete(socialLink);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public SocialLinkDto findDtoById(Long id) {
        CustomUserDetails user = getAuthenticatedUser();
        Long profileId = user.getProfileId();

        return socialLinkRepository.findByIdAndProfileId(id, profileId)
                .map(socialLinkMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("SocialLink", "id", id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SocialLinkDto> findAllDto() {
        CustomUserDetails user = getAuthenticatedUser();
        Long profileId = user.getProfileId();

        return socialLinkRepository.findAllByProfileIdOrderBySortOrderAsc(profileId)
                .stream()
                .map(socialLinkMapper::toDto)
                .toList();
    }

    private CustomUserDetails getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails)) {
            throw new IllegalStateException("No se pudo obtener la información del usuario autenticado.");
        }
        return (CustomUserDetails) authentication.getPrincipal();
    }
}
