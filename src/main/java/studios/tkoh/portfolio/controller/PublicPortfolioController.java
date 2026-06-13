package studios.tkoh.portfolio.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import studios.tkoh.portfolio.dto.project.ProjectDto;
import studios.tkoh.portfolio.dto.publicapi.ContactRequest;
import studios.tkoh.portfolio.dto.publicapi.PortfolioDetailDto;
import studios.tkoh.portfolio.dto.publicapi.PortfolioPublicDto;
import studios.tkoh.portfolio.dto.response.ApiResponse;
import studios.tkoh.portfolio.exception.ResourceNotFoundException;
import studios.tkoh.portfolio.model.ContactMessage;
import studios.tkoh.portfolio.model.Profile;
import studios.tkoh.portfolio.repository.ContactMessageRepo;
import studios.tkoh.portfolio.repository.ProfileRepo;
import studios.tkoh.portfolio.service.EmailService;
import studios.tkoh.portfolio.service.PublicPortfolioService;

@RestController
@RequestMapping("/api/portfolios")
@RequiredArgsConstructor
public class PublicPortfolioController {

    private final PublicPortfolioService publicPortfolioService;
    private final ProfileRepo profileRepository;
    private final ContactMessageRepo contactMessageRepository;
    private final EmailService emailService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<PortfolioPublicDto>>> getAllPortfolios(
            @RequestParam(required = false) String search,
            @PageableDefault(size = 20) Pageable pageable) {
        Page<PortfolioPublicDto> portfolios = publicPortfolioService.getAllPublicProfiles(search, pageable);
        return ResponseEntity.ok(ApiResponse.ok("Portafolios públicos obtenidos", portfolios));
    }

    @GetMapping("/{slug}")
    public ResponseEntity<ApiResponse<PortfolioDetailDto>> getPortfolioBySlug(@PathVariable String slug) {
        PortfolioDetailDto portfolio = publicPortfolioService.getFullPortfolioBySlug(slug);
        return ResponseEntity.ok(ApiResponse.ok("Portafolio obtenido exitosamente", portfolio));
    }

    @GetMapping("/{profileSlug}/projects/{projectSlug}")
    public ResponseEntity<ApiResponse<ProjectDto>> getProjectDetails(
            @PathVariable String profileSlug,
            @PathVariable String projectSlug) {
        ProjectDto project = publicPortfolioService.getPublicProjectBySlugs(profileSlug, projectSlug);
        return ResponseEntity.ok(ApiResponse.ok("Detalle del proyecto obtenido", project));
    }

    @PostMapping("/{slug}/contact")
    public ResponseEntity<ApiResponse<Void>> handleContactForm(
            @PathVariable String slug,
            @Valid @RequestBody ContactRequest contactRequest) {

        Profile profile = profileRepository.findBySlugAndPortfolioStatus(slug, "PUBLISHED")
                .orElseThrow(() -> new ResourceNotFoundException("Profile", "slug", slug));

        ContactMessage message = new ContactMessage();
        message.setName(contactRequest.name());
        message.setEmail(contactRequest.email());
        message.setMessage(contactRequest.message());
        message.setProfile(profile);
        message.setStatus("NEW");
        ContactMessage savedMessage = contactMessageRepository.save(message);

        emailService.sendContactNotification(profile, savedMessage);

        return ResponseEntity.ok(ApiResponse.ok("Mensaje enviado exitosamente"));
    }
}
