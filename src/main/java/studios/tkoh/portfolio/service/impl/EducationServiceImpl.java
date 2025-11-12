package studios.tkoh.portfolio.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import studios.tkoh.portfolio.dto.education.EducationCreateRequest;
import studios.tkoh.portfolio.dto.education.EducationDto;
import studios.tkoh.portfolio.dto.education.EducationUpdateRequest;
import studios.tkoh.portfolio.exception.ResourceNotFoundException;
import studios.tkoh.portfolio.mapper.EducationMapper;
import studios.tkoh.portfolio.model.Education;
import studios.tkoh.portfolio.model.Profile;
import studios.tkoh.portfolio.repository.EducationRepo;
import studios.tkoh.portfolio.repository.ProfileRepo;
import studios.tkoh.portfolio.security.CustomUserDetails;
import studios.tkoh.portfolio.service.EducationService;

/**
 *
 * @author Studios TKOH!
 */
@Service
@RequiredArgsConstructor
public class EducationServiceImpl implements EducationService {

    private final EducationRepo educationRepository;
    private final ProfileRepo profileRepository;
    private final EducationMapper educationMapper;

    @Override
    @Transactional
    public EducationDto create(EducationCreateRequest create) {
        CustomUserDetails user = getAuthenticatedUser();
        Profile profile = profileRepository.getReferenceById(user.getProfileId());

        Education education = educationMapper.toEntity(create);
        education.setProfile(profile);

        Education savedEducation = educationRepository.save(education);
        return educationMapper.toDto(savedEducation);
    }

    @Override
    @Transactional
    public EducationDto update(EducationUpdateRequest updated) {
        CustomUserDetails user = getAuthenticatedUser();
        Long profileId = user.getProfileId();

        Education education = educationRepository.findByIdAndProfileId(updated.id(), profileId)
                .orElseThrow(() -> new ResourceNotFoundException("Education", "id", updated.id()));

        educationMapper.updateEntityFromDto(updated, education);
        Education savedEducation = educationRepository.save(education);
        return educationMapper.toDto(savedEducation);
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        CustomUserDetails user = getAuthenticatedUser();
        Long profileId = user.getProfileId();

        Education education = educationRepository.findByIdAndProfileId(id, profileId)
                .orElseThrow(() -> new ResourceNotFoundException("Education", "id", id));

        educationRepository.delete(education);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public EducationDto findDtoById(Long id) {
        CustomUserDetails user = getAuthenticatedUser();
        Long profileId = user.getProfileId();

        return educationRepository.findByIdAndProfileId(id, profileId)
                .map(educationMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Education", "id", id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EducationDto> findAllDto() {
        CustomUserDetails user = getAuthenticatedUser();
        Long profileId = user.getProfileId();

        return educationRepository.findAllByProfileIdOrderByStartDateDesc(profileId)
                .stream()
                .map(educationMapper::toDto)
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
