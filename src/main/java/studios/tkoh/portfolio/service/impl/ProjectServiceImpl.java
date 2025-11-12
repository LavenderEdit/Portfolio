package studios.tkoh.portfolio.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import studios.tkoh.portfolio.dto.project.ProjectCreateRequest;
import studios.tkoh.portfolio.dto.project.ProjectDto;
import studios.tkoh.portfolio.dto.project.ProjectUpdateRequest;
import studios.tkoh.portfolio.exception.ResourceNotFoundException;
import studios.tkoh.portfolio.mapper.ProjectMapper;
import studios.tkoh.portfolio.model.Profile;
import studios.tkoh.portfolio.model.Project;
import studios.tkoh.portfolio.repository.ProfileRepo;
import studios.tkoh.portfolio.repository.ProjectRepo;
import studios.tkoh.portfolio.security.CustomUserDetails;
import studios.tkoh.portfolio.service.ProjectService;

/**
 *
 * @author Studios TKOH!
 */
@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepo projectRepository;
    private final ProfileRepo profileRepository;
    private final ProjectMapper projectMapper;

    @Override
    @Transactional
    public ProjectDto create(ProjectCreateRequest create) {
        CustomUserDetails user = getAuthenticatedUser();
        Profile profile = profileRepository.getReferenceById(user.getProfileId());

        Project project = projectMapper.toEntity(create);
        project.setProfile(profile);
        project.setSlug(generateUniqueSlug(user.getProfileId(), create.title()));

        Project savedProject = projectRepository.save(project);
        return projectMapper.toDto(savedProject);
    }

    @Override
    @Transactional
    public ProjectDto update(ProjectUpdateRequest updated) {
        CustomUserDetails user = getAuthenticatedUser();
        Long profileId = user.getProfileId();

        Project project = projectRepository.findByIdAndProfileId(updated.id(), profileId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", updated.id()));

        projectMapper.updateEntityFromDto(updated, project);
        Project savedProject = projectRepository.save(project);
        return projectMapper.toDto(savedProject);
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        CustomUserDetails user = getAuthenticatedUser();
        Long profileId = user.getProfileId();

        Project project = projectRepository.findByIdAndProfileId(id, profileId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", id));

        projectRepository.delete(project);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public ProjectDto findDtoById(Long id) {
        CustomUserDetails user = getAuthenticatedUser();
        Long profileId = user.getProfileId();

        return projectRepository.findByIdAndProfileId(id, profileId)
                .map(projectMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectDto> findAllDto() {
        CustomUserDetails user = getAuthenticatedUser();
        Long profileId = user.getProfileId();

        return projectRepository.findAllByProfileIdOrderBySortOrderAsc(profileId)
                .stream()
                .map(projectMapper::toDto)
                .toList();
    }

    private CustomUserDetails getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails)) {
            throw new IllegalStateException("No se pudo obtener la información del usuario autenticado.");
        }
        return (CustomUserDetails) authentication.getPrincipal();
    }

    private String generateUniqueSlug(Long profileId, String title) {
        String baseSlug = title.toLowerCase()
                .replaceAll("\\s+", "-")
                .replaceAll("[^a-z0-9\\-]", "")
                .replaceAll("^-|-$", "");

        if (baseSlug.length() > 150) {
            baseSlug = baseSlug.substring(0, 150);
        }
        if (baseSlug.isEmpty()) {
            baseSlug = "project";
        }

        String slug = baseSlug;
        int counter = 1;
        while (projectRepository.existsByProfileIdAndSlug(profileId, slug)) {
            String slugSuffix = "-" + counter;
            int maxBaseLength = 160 - slugSuffix.length();
            if (baseSlug.length() > maxBaseLength) {
                baseSlug = baseSlug.substring(0, maxBaseLength);
            }
            slug = baseSlug + slugSuffix;
            counter++;
        }
        return slug;
    }
}
