package studios.tkoh.portfolio.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import studios.tkoh.portfolio.dto.publicapi.ContactRequest;
import studios.tkoh.portfolio.dto.response.ApiResponse;
import studios.tkoh.portfolio.exception.ResourceNotFoundException;
import studios.tkoh.portfolio.model.ContactMessage;
import studios.tkoh.portfolio.model.Profile;
import studios.tkoh.portfolio.repository.ContactMessageRepo;
import studios.tkoh.portfolio.repository.ProfileRepo;
import studios.tkoh.portfolio.service.EmailService;

/**
 *
 * @author Studios TKOH!
 */
@RestController
@RequestMapping("/api/portfolios")
@RequiredArgsConstructor
public class PublicPortfolioController {

    private final ProfileRepo profileRepository;
    private final ContactMessageRepo contactMessageRepository;
    private final EmailService emailService;

    @PostMapping("/{slug}/contact")
    public ResponseEntity<ApiResponse<Void>> handleContactForm(
            @PathVariable String slug,
            @Valid @RequestBody ContactRequest contactRequest) {

        Profile profile = profileRepository.findBySlug(slug)
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

    // Aquí irán los otros endpoints públicos (GET /api/portfolios, GET /api/portfolios/{slug}, etc.)
}
