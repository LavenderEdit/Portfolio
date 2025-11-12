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
            return new ResponseEntity<>(ApiResponse.error("Error al subir avatar: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/resume")
    public ResponseEntity<ApiResponse<ProfileDto>> uploadResume(@RequestParam("file") MultipartFile file) {
        try {
            ProfileDto updatedProfile = uploadService.uploadResume(file);
            return ResponseEntity.ok(ApiResponse.ok("Currículum actualizado exitosamente", updatedProfile));
        } catch (IOException | GeneralSecurityException e) {
            return new ResponseEntity<>(ApiResponse.error("Error al subir currículum: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
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
            return new ResponseEntity<>(ApiResponse.error("Error al subir portada: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
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
            return new ResponseEntity<>(ApiResponse.error("Error al subir icono: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
