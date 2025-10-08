package com.portfolio.lavender.controller;

import com.portfolio.lavender.dto.simple.ContactForm;
import com.portfolio.lavender.model.ContactMessage;
import com.portfolio.lavender.model.Profile;
import com.portfolio.lavender.repository.ContactMessageRepo;
import com.portfolio.lavender.repository.ProfileRepo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author Studios TKOH!
 */
@Controller
@RequiredArgsConstructor
public class ContactController {

    private final ContactMessageRepo repo;
    private final ProfileRepo profileRepo;

    @PostMapping("/portfolio/{slug}/contact")
    public String submit(@PathVariable String slug, @Valid @ModelAttribute("contactForm") ContactForm form,
            BindingResult br, RedirectAttributes ra) {
        Profile profile = profileRepo.findBySlug(slug)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (br.hasErrors()) {
            ra.addFlashAttribute("org.springframework.validation.BindingResult.contactForm", br);
            ra.addFlashAttribute("contactForm", form);
            return "redirect:/portfolio/" + slug + "#contact";
        }
        ContactMessage msg = new ContactMessage();
        msg.setName(form.getName());
        msg.setEmail(form.getEmail());
        msg.setMessage(form.getMessage());
        msg.setProfile(profile);
        repo.save(msg);
        ra.addFlashAttribute("contactSuccess", true);
        return "redirect:/portfolio/" + slug + "#contact";
    }

}
