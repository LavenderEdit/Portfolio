package studios.tkoh.portfolio.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import studios.tkoh.portfolio.controller.generic.DtoCrudController;
import studios.tkoh.portfolio.dto.response.ApiResponse;
import studios.tkoh.portfolio.dto.social.SocialLinkCreateRequest;
import studios.tkoh.portfolio.dto.social.SocialLinkDto;
import studios.tkoh.portfolio.dto.social.SocialLinkUpdateRequest;
import studios.tkoh.portfolio.service.SocialLinkService;

/**
 *
 * @author Studios TKOH!
 */
@RestController
@RequestMapping("/api/me/social-links")
@RequiredArgsConstructor
public class SocialLinkController implements DtoCrudController<SocialLinkDto, SocialLinkCreateRequest, SocialLinkUpdateRequest, Long> {

    private final SocialLinkService socialLinkService;

    @Override
    public ResponseEntity<ApiResponse<List<SocialLinkDto>>> listAll() {
        List<SocialLinkDto> links = socialLinkService.findAllDto();
        return ResponseEntity.ok(ApiResponse.ok("Redes sociales obtenidas exitosamente", links));
    }

    @Override
    public ResponseEntity<ApiResponse<SocialLinkDto>> getById(@PathVariable Long id) {
        SocialLinkDto link = socialLinkService.findDtoById(id);
        return ResponseEntity.ok(ApiResponse.ok("Red social obtenida", link));
    }

    @Override
    public ResponseEntity<ApiResponse<SocialLinkDto>> create(@Valid @RequestBody SocialLinkCreateRequest create) {
        SocialLinkDto newLink = socialLinkService.create(create);
        return new ResponseEntity<>(ApiResponse.ok("Red social creada exitosamente", newLink), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ApiResponse<SocialLinkDto>> update(@Valid @RequestBody SocialLinkUpdateRequest updated) {
        SocialLinkDto updatedLink = socialLinkService.update(updated);
        return ResponseEntity.ok(ApiResponse.ok("Red social actualizada exitosamente", updatedLink));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SocialLinkDto>> update(@PathVariable Long id, @Valid @RequestBody SocialLinkCreateRequest createData) {
        SocialLinkUpdateRequest updateRequest = new SocialLinkUpdateRequest(
                id,
                createData.platform(),
                createData.url(),
                createData.sortOrder()
        );
        SocialLinkDto updatedLink = socialLinkService.update(updateRequest);
        return ResponseEntity.ok(ApiResponse.ok("Red social actualizada exitosamente", updatedLink));
    }

    @Override
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        socialLinkService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok("Red social eliminada exitosamente"));
    }
}
