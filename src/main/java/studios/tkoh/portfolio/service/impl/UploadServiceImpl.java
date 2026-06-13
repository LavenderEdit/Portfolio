package studios.tkoh.portfolio.service.impl;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.Clock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import studios.tkoh.portfolio.config.GoogleDriveConfig;
import studios.tkoh.portfolio.dto.certificate.CertificateDto;
import studios.tkoh.portfolio.dto.profile.ProfileDto;
import studios.tkoh.portfolio.dto.project.ProjectDto;
import studios.tkoh.portfolio.dto.skill.SkillDto;
import studios.tkoh.portfolio.dto.upload.StoredFileDownload;
import studios.tkoh.portfolio.dto.upload.UploadResponse;
import studios.tkoh.portfolio.exception.ResourceNotFoundException;
import studios.tkoh.portfolio.mapper.SkillMapper;
import studios.tkoh.portfolio.model.Profile;
import studios.tkoh.portfolio.model.Skill;
import studios.tkoh.portfolio.model.StoredFile;
import studios.tkoh.portfolio.repository.ProfileRepo;
import studios.tkoh.portfolio.repository.SkillRepo;
import studios.tkoh.portfolio.repository.StoredFileRepository;
import studios.tkoh.portfolio.security.CurrentUserProvider;
import studios.tkoh.portfolio.service.CertificateService;
import studios.tkoh.portfolio.service.FileValidationService;
import studios.tkoh.portfolio.service.FileValidationService.UploadKind;
import studios.tkoh.portfolio.service.GoogleDriveService;
import studios.tkoh.portfolio.service.ProjectService;
import studios.tkoh.portfolio.service.ProfileService;
import studios.tkoh.portfolio.service.UploadService;

@Service
@RequiredArgsConstructor
public class UploadServiceImpl implements UploadService {

    private final GoogleDriveService driveService;
    private final GoogleDriveConfig driveConfig;
    private final ProfileService profileService;
    private final ProjectService projectService;
    private final CertificateService certificateService;
    private final SkillRepo skillRepository;
    private final ProfileRepo profileRepository;
    private final StoredFileRepository storedFileRepository;
    private final SkillMapper skillMapper;
    private final FileValidationService fileValidationService;
    private final CurrentUserProvider currentUserProvider;
    private final Clock clock;

    @Override
    @Transactional
    public ProfileDto uploadAvatar(MultipartFile file) throws IOException, GeneralSecurityException {
        Long profileId = currentUserProvider.currentUser().getProfileId();
        Profile profile = getProfile(profileId);
        fileValidationService.validate(file, UploadKind.IMAGE);

        UploadResponse response = upload(file, profile.getSlug(), "avatar", driveConfig.getFolders().getUserAvatars(), true);
        saveMetadata(profile, response, file, "AVATAR", "PUBLIC");
        return profileService.updateAvatarUrl(profileId, response.publicUrl());
    }

    @Override
    @Transactional
    public ProfileDto uploadResume(MultipartFile file) throws IOException, GeneralSecurityException {
        Long profileId = currentUserProvider.currentUser().getProfileId();
        Profile profile = getProfile(profileId);
        fileValidationService.validate(file, UploadKind.DOCUMENT);

        UploadResponse response = upload(file, profile.getSlug(), "resume", driveConfig.getFolders().getUserResumes(), false);
        StoredFile storedFile = saveMetadata(profile, response, file, "RESUME", "PRIVATE");
        return profileService.updateResumeUrl(profileId, privateFileUrl(storedFile.getId()));
    }

    @Override
    @Transactional
    public ProjectDto uploadProjectCover(Long projectId, MultipartFile file) throws IOException, GeneralSecurityException {
        Long profileId = currentUserProvider.currentUser().getProfileId();
        Profile profile = getProfile(profileId);
        ProjectDto project = projectService.findDtoById(projectId);
        fileValidationService.validate(file, UploadKind.IMAGE);

        UploadResponse response = upload(file, project.slug(), "cover", driveConfig.getFolders().getProjectsCover(), true);
        saveMetadata(profile, response, file, "PROJECT_COVER", "PUBLIC");
        return projectService.updateCoverImageUrl(projectId, profileId, response.publicUrl());
    }

    @Override
    @Transactional
    public SkillDto uploadSkillIcon(Long skillId, MultipartFile file) throws IOException, GeneralSecurityException {
        Long profileId = currentUserProvider.currentUser().getProfileId();
        Profile profile = getProfile(profileId);
        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new ResourceNotFoundException("Skill", "id", skillId));
        if (!skill.getCategory().getProfile().getId().equals(profileId)) {
            throw new ResourceNotFoundException("Skill", "id", skillId);
        }
        fileValidationService.validate(file, UploadKind.IMAGE);

        UploadResponse response = upload(file, skill.getName(), "icon", driveConfig.getFolders().getSkillsIcon(), true);
        saveMetadata(profile, response, file, "SKILL_ICON", "PUBLIC");
        skill.setIcon(response.publicUrl());
        return skillMapper.toDto(skillRepository.save(skill));
    }

    @Override
    @Transactional
    public CertificateDto uploadCertificateFile(Long certificateId, MultipartFile file) throws IOException, GeneralSecurityException {
        Long profileId = currentUserProvider.currentUser().getProfileId();
        Profile profile = getProfile(profileId);
        CertificateDto certificate = certificateService.findDtoById(certificateId);
        fileValidationService.validate(file, UploadKind.DOCUMENT);

        UploadResponse response = upload(file, certificate.name(), "certificate", driveConfig.getFolders().getCertificates(), false);
        StoredFile storedFile = saveMetadata(profile, response, file, "CERTIFICATE", "PRIVATE");
        return certificateService.updateCertificateFile(certificateId, profileId, privateFileUrl(storedFile.getId()), response.fileId());
    }

    @Override
    @Transactional(readOnly = true)
    public StoredFileDownload downloadPrivateFile(Long storedFileId) throws IOException, GeneralSecurityException {
        Long profileId = currentUserProvider.currentUser().getProfileId();
        StoredFile storedFile = storedFileRepository.findByIdAndProfileId(storedFileId, profileId)
                .orElseThrow(() -> new ResourceNotFoundException("StoredFile", "id", storedFileId));
        byte[] content = driveService.downloadFile(storedFile.getGoogleFileId());
        return new StoredFileDownload(storedFile.getOriginalFilename(), storedFile.getContentType(), content);
    }

    private UploadResponse upload(MultipartFile file, String identifier, String type, String folderId, boolean publiclyReadable)
            throws IOException, GeneralSecurityException {
        String filename = generateUniqueFilename(identifier, type, file.getOriginalFilename());
        return driveService.uploadFile(file, folderId, filename, publiclyReadable);
    }

    private StoredFile saveMetadata(Profile profile, UploadResponse response, MultipartFile file, String type, String visibility) {
        StoredFile storedFile = new StoredFile();
        storedFile.setProfile(profile);
        storedFile.setGoogleFileId(response.fileId());
        storedFile.setPublicUrl(response.publicUrl());
        storedFile.setFileType(type);
        storedFile.setVisibility(visibility);
        storedFile.setOriginalFilename(file.getOriginalFilename() == null ? "upload" : file.getOriginalFilename());
        storedFile.setContentType(file.getContentType() == null ? "application/octet-stream" : file.getContentType());
        storedFile.setSizeBytes(file.getSize());
        storedFile.setCreatedAt(clock.instant());
        return storedFileRepository.save(storedFile);
    }

    private Profile getProfile(Long profileId) {
        return profileRepository.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile", "id", profileId));
    }

    private String privateFileUrl(Long storedFileId) {
        return "/api/me/files/" + storedFileId;
    }

    private String generateUniqueFilename(String entityIdentifier, String type, String originalFilename) {
        String cleanIdentifier = entityIdentifier.toLowerCase().replaceAll("[^a-z0-9\\-]", "");
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
        }
        return String.format("%s_%s_%d%s", cleanIdentifier, type, System.currentTimeMillis(), extension);
    }
}
