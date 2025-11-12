package studios.tkoh.portfolio.service.impl;

import java.io.IOException;
import java.security.GeneralSecurityException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import studios.tkoh.portfolio.config.GoogleDriveConfig;
import studios.tkoh.portfolio.dto.profile.ProfileDto;
import studios.tkoh.portfolio.dto.project.ProjectDto;
import studios.tkoh.portfolio.dto.skill.SkillDto;
import studios.tkoh.portfolio.dto.upload.UploadResponse;
import studios.tkoh.portfolio.exception.ResourceNotFoundException;
import studios.tkoh.portfolio.mapper.SkillMapper;
import studios.tkoh.portfolio.model.Profile;
import studios.tkoh.portfolio.model.Skill;
import studios.tkoh.portfolio.repository.ProfileRepo;
import studios.tkoh.portfolio.repository.SkillRepo;
import studios.tkoh.portfolio.security.CustomUserDetails;
import studios.tkoh.portfolio.service.GoogleDriveService;
import studios.tkoh.portfolio.service.ProjectService;
import studios.tkoh.portfolio.service.ProfileService;
import studios.tkoh.portfolio.service.UploadService;

/**
 *
 * @author Studios TKOH!
 */
@Service
@RequiredArgsConstructor
public class UploadServiceImpl implements UploadService {

    private final GoogleDriveService driveService;
    private final GoogleDriveConfig driveConfig;
    private final ProfileService profileService;
    private final ProjectService projectService;
    private final SkillRepo skillRepository;
    private final ProfileRepo profileRepository;
    private final SkillMapper skillMapper;

    @Override
    @Transactional
    public ProfileDto uploadAvatar(MultipartFile file) throws IOException, GeneralSecurityException {
        CustomUserDetails user = getAuthenticatedUser();
        Profile profile = getProfile(user.getProfileId());

        String filename = generateUniqueFilename(profile.getSlug(), "avatar", file.getOriginalFilename());
        String folderId = driveConfig.getFolders().getUserAvatars();

        UploadResponse response = driveService.uploadFile(file, folderId, filename);

        return profileService.updateAvatarUrl(user.getProfileId(), response.publicUrl());
    }

    @Override
    @Transactional
    public ProfileDto uploadResume(MultipartFile file) throws IOException, GeneralSecurityException {
        CustomUserDetails user = getAuthenticatedUser();
        Profile profile = getProfile(user.getProfileId());

        String filename = generateUniqueFilename(profile.getSlug(), "resume", file.getOriginalFilename());
        String folderId = driveConfig.getFolders().getUserResumes();

        UploadResponse response = driveService.uploadFile(file, folderId, filename);

        return profileService.updateResumeUrl(user.getProfileId(), response.publicUrl());
    }

    @Override
    @Transactional
    public ProjectDto uploadProjectCover(Long projectId, MultipartFile file) throws IOException, GeneralSecurityException {
        CustomUserDetails user = getAuthenticatedUser();
        Long profileId = user.getProfileId();

        // 1. Verificar propiedad y obtener DTO (el DTO ya tiene el slug)
        ProjectDto project = projectService.findDtoById(projectId);

        String filename = generateUniqueFilename(project.slug(), "cover", file.getOriginalFilename());
        String folderId = driveConfig.getFolders().getProjectsCover();

        UploadResponse response = driveService.uploadFile(file, folderId, filename);

        // 2. Delegamos la actualización al ProjectService
        return projectService.updateCoverImageUrl(projectId, profileId, response.publicUrl());
    }

    @Override
    @Transactional
    public SkillDto uploadSkillIcon(Long skillId, MultipartFile file) throws IOException, GeneralSecurityException {
        CustomUserDetails user = getAuthenticatedUser();
        Long profileId = user.getProfileId();

        // 1. Verificar propiedad del skill
        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new ResourceNotFoundException("Skill", "id", skillId));

        if (!skill.getCategory().getProfile().getId().equals(profileId)) {
            throw new ResourceNotFoundException("Skill", "id", skillId);
        }

        String filename = generateUniqueFilename(skill.getName(), "icon", file.getOriginalFilename());
        String folderId = driveConfig.getFolders().getSkillsIcon();

        UploadResponse response = driveService.uploadFile(file, folderId, filename);

        skill.setIcon(response.publicUrl());
        Skill savedSkill = skillRepository.save(skill);

        return skillMapper.toDto(savedSkill);
    }

    // --- Helpers ---
    private CustomUserDetails getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails)) {
            throw new IllegalStateException("No se pudo obtener la información del usuario autenticado.");
        }
        return (CustomUserDetails) authentication.getPrincipal();
    }

    private Profile getProfile(Long profileId) {
        return profileRepository.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile", "id", profileId));
    }

    private String generateUniqueFilename(String entityIdentifier, String type, String originalFilename) {
        String cleanIdentifier = entityIdentifier.toLowerCase().replaceAll("[^a-z0-9\\-]", "");
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        return String.format("%s_%s_%d%s", cleanIdentifier, type, System.currentTimeMillis(), extension);
    }
}
