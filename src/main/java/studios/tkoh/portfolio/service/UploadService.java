package studios.tkoh.portfolio.service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import org.springframework.web.multipart.MultipartFile;
import studios.tkoh.portfolio.dto.certificate.CertificateDto;
import studios.tkoh.portfolio.dto.profile.ProfileDto;
import studios.tkoh.portfolio.dto.project.ProjectDto;
import studios.tkoh.portfolio.dto.skill.SkillDto;

/**
 *
 * @author Studios TKOH!
 */
public interface UploadService {

    ProfileDto uploadAvatar(MultipartFile file) throws IOException, GeneralSecurityException;

    ProfileDto uploadResume(MultipartFile file) throws IOException, GeneralSecurityException;

    ProjectDto uploadProjectCover(Long projectId, MultipartFile file) throws IOException, GeneralSecurityException;

    SkillDto uploadSkillIcon(Long skillId, MultipartFile file) throws IOException, GeneralSecurityException;

    CertificateDto uploadCertificateFile(Long certificateId, MultipartFile file) throws IOException, GeneralSecurityException;
}
