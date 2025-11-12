package studios.tkoh.portfolio.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import studios.tkoh.portfolio.dto.profile.ProfileDto;
import studios.tkoh.portfolio.dto.response.ApiResponse;
import studios.tkoh.portfolio.service.ProfileService;

/**
 *
 * @author Studios TKOH!
 */
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final ProfileService profileService;

    @PostMapping("/profiles/{profileId}/toggle-collaborator")
    public ResponseEntity<ApiResponse<ProfileDto>> toggleCollaboratorStatus(@PathVariable Long profileId) {
        ProfileDto updatedProfile = profileService.toggleTkohCollaborator(profileId);
        return ResponseEntity.ok(ApiResponse.ok("Estado de colaborador actualizado", updatedProfile));
    }
}
