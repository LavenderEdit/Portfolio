package studios.tkoh.portfolio.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import studios.tkoh.portfolio.dto.profile.ContactEmailUpdateRequest;
import studios.tkoh.portfolio.dto.profile.ProfileDto;
import studios.tkoh.portfolio.dto.profile.ProfileUpdateRequest;
import studios.tkoh.portfolio.dto.response.ApiResponse;
import studios.tkoh.portfolio.security.CustomUserDetails;
import studios.tkoh.portfolio.service.ProfileService;

/**
 *
 * @author Studios TKOH!
 */
@RestController
@RequestMapping("/api/me")
@RequiredArgsConstructor
public class MeController {

    private final ProfileService profileService;

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<ProfileDto>> getMyProfile(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long profileId = userDetails.getProfileId();

        ProfileDto profileDto = profileService.getMyProfile(profileId);

        return ResponseEntity.ok(ApiResponse.ok("Perfil obtenido exitosamente", profileDto));
    }

    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<ProfileDto>> updateMyProfile(
            Authentication authentication,
            @Valid @RequestBody ProfileUpdateRequest request) {

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long profileId = userDetails.getProfileId();

        ProfileDto updatedDto = profileService.updateMyProfile(profileId, request);

        return ResponseEntity.ok(ApiResponse.ok("Perfil actualizado exitosamente", updatedDto));
    }

    @PutMapping("/settings/contact-email")
    public ResponseEntity<ApiResponse<ProfileDto>> updateContactEmail(
            Authentication authentication,
            @Valid @RequestBody ContactEmailUpdateRequest request) {

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long profileId = userDetails.getProfileId();

        ProfileDto updatedDto = profileService.updateContactEmail(profileId, request.email());

        return ResponseEntity.ok(ApiResponse.ok("Email de contacto actualizado exitosamente", updatedDto));
    }
}
