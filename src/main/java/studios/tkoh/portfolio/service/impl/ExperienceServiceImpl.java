package studios.tkoh.portfolio.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import studios.tkoh.portfolio.dto.experience.ExperienceCreateRequest;
import studios.tkoh.portfolio.dto.experience.ExperienceDto;
import studios.tkoh.portfolio.dto.experience.ExperienceUpdateRequest;
import studios.tkoh.portfolio.exception.ResourceNotFoundException;
import studios.tkoh.portfolio.mapper.ExperienceMapper;
import studios.tkoh.portfolio.model.Experience;
import studios.tkoh.portfolio.model.Profile;
import studios.tkoh.portfolio.repository.ExperienceRepo;
import studios.tkoh.portfolio.repository.ProfileRepo;
import studios.tkoh.portfolio.security.CustomUserDetails;
import studios.tkoh.portfolio.service.ExperienceService;

/**
 *
 * @author Studios TKOH!
 */
@Service
@RequiredArgsConstructor
public class ExperienceServiceImpl implements ExperienceService {

    private final ExperienceRepo experienceRepository;
    private final ProfileRepo profileRepository;
    private final ExperienceMapper experienceMapper;

    @Override
    @Transactional
    public ExperienceDto create(ExperienceCreateRequest create) {
        CustomUserDetails user = getAuthenticatedUser();
        Profile profile = profileRepository.getReferenceById(user.getProfileId());

        Experience experience = experienceMapper.toEntity(create);
        experience.setProfile(profile);

        Experience savedExperience = experienceRepository.save(experience);
        return experienceMapper.toDto(savedExperience);
    }

    @Override
    @Transactional
    public ExperienceDto update(ExperienceUpdateRequest updated) {
        CustomUserDetails user = getAuthenticatedUser();
        Long profileId = user.getProfileId();

        Experience experience = experienceRepository.findByIdAndProfileId(updated.id(), profileId)
                .orElseThrow(() -> new ResourceNotFoundException("Experience", "id", updated.id()));

        experienceMapper.updateEntityFromDto(updated, experience);
        Experience savedExperience = experienceRepository.save(experience);
        return experienceMapper.toDto(savedExperience);
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        CustomUserDetails user = getAuthenticatedUser();
        Long profileId = user.getProfileId();

        Experience experience = experienceRepository.findByIdAndProfileId(id, profileId)
                .orElseThrow(() -> new ResourceNotFoundException("Experience", "id", id));

        experienceRepository.delete(experience);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public ExperienceDto findDtoById(Long id) {
        CustomUserDetails user = getAuthenticatedUser();
        Long profileId = user.getProfileId();

        return experienceRepository.findByIdAndProfileId(id, profileId)
                .map(experienceMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Experience", "id", id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExperienceDto> findAllDto() {
        CustomUserDetails user = getAuthenticatedUser();
        Long profileId = user.getProfileId();

        return experienceRepository.findAllByProfileIdOrderByStartDateDesc(profileId)
                .stream()
                .map(experienceMapper::toDto)
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
