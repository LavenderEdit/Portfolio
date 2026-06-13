package studios.tkoh.portfolio.controller;

import java.io.IOException;
import java.security.GeneralSecurityException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import studios.tkoh.portfolio.dto.certificate.CertificateDto;
import studios.tkoh.portfolio.dto.profile.ProfileDto;
import studios.tkoh.portfolio.dto.project.ProjectDto;
import studios.tkoh.portfolio.dto.response.ApiResponse;
import studios.tkoh.portfolio.dto.skill.SkillDto;
import studios.tkoh.portfolio.service.UploadService;

/**
 *
 * @author Studios TKOH!
 */
@RestController
@RequestMapping("/api/me/upload")
@RequiredArgsConstructor
public class UploadController {

    private final UploadService uploadService;

    @PostMapping("/avatar")
    public ResponseEntity<ApiResponse<ProfileDto>> uploadAvatar(@RequestParam("file") MultipartFile file) {
        try {
            ProfileDto updatedProfile = uploadService.uploadAvatar(file);
            return ResponseEntity.ok(ApiResponse.ok("Avatar actualizado exitosamente", updatedProfile));
        } catch (IOException | GeneralSecurityException e) {
            return new ResponseEntity<>(ApiResponse.error("UPLOAD_ERROR", "Error al subir avatar", "/api/me/upload/avatar", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/resume")
    public ResponseEntity<ApiResponse<ProfileDto>> uploadResume(@RequestParam("file") MultipartFile file) {
        try {
            ProfileDto updatedProfile = uploadService.uploadResume(file);
            return ResponseEntity.ok(ApiResponse.ok("Currículum actualizado exitosamente", updatedProfile));
        } catch (IOException | GeneralSecurityException e) {
            return new ResponseEntity<>(ApiResponse.error("UPLOAD_ERROR", "Error al subir curriculum", "/api/me/upload/resume", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/project/{projectId}/cover")
    public ResponseEntity<ApiResponse<ProjectDto>> uploadProjectCover(
            @PathVariable Long projectId,
            @RequestParam("file") MultipartFile file) {
        try {
            ProjectDto updatedProject = uploadService.uploadProjectCover(projectId, file);
            return ResponseEntity.ok(ApiResponse.ok("Portada de proyecto actualizada", updatedProject));
        } catch (IOException | GeneralSecurityException e) {
            return new ResponseEntity<>(ApiResponse.error("UPLOAD_ERROR", "Error al subir portada", "/api/me/upload/project/" + projectId + "/cover", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/skill/{skillId}/icon")
    public ResponseEntity<ApiResponse<SkillDto>> uploadSkillIcon(
            @PathVariable Long skillId,
            @RequestParam("file") MultipartFile file) {
        try {
            SkillDto updatedSkill = uploadService.uploadSkillIcon(skillId, file);
            return ResponseEntity.ok(ApiResponse.ok("Icono de skill actualizado", updatedSkill));
        } catch (IOException | GeneralSecurityException e) {
            return new ResponseEntity<>(ApiResponse.error("UPLOAD_ERROR", "Error al subir icono", "/api/me/upload/skill/" + skillId + "/icon", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/certificate/{certificateId}/file")
    public ResponseEntity<ApiResponse<CertificateDto>> uploadCertificateFile(
            @PathVariable Long certificateId,
            @RequestParam("file") MultipartFile file) {
        try {
            CertificateDto updatedCertificate = uploadService.uploadCertificateFile(certificateId, file);
            return ResponseEntity.ok(ApiResponse.ok("Archivo de certificado subido", updatedCertificate));
        } catch (IOException | GeneralSecurityException e) {
            return new ResponseEntity<>(ApiResponse.error("UPLOAD_ERROR", "Error al subir archivo", "/api/me/upload/certificate/" + certificateId + "/file", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
