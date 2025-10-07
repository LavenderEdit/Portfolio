package com.portfolio.lavender.controller;

import com.portfolio.lavender.dto.simple.ContactForm;
import com.portfolio.lavender.model.ContactMessage;
import com.portfolio.lavender.repository.ContactMessageRepo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author Joan Lavender
 */
@Controller
@RequiredArgsConstructor
public class ContactController {

    private final ContactMessageRepo repo;

    @PostMapping("/contact")
    public String submit(@Valid @ModelAttribute("contactForm") ContactForm form,
            BindingResult br, RedirectAttributes ra) {
        if (br.hasErrors()) {
            ra.addFlashAttribute("org.springframework.validation.BindingResult.contactForm", br);
            ra.addFlashAttribute("contactForm", form);
            return "redirect:/#contact";
        }
        ContactMessage msg = new ContactMessage();
        msg.setName(form.getName());
        msg.setEmail(form.getEmail());
        msg.setMessage(form.getMessage());
        repo.save(msg);
        ra.addFlashAttribute("contactSuccess", true);
        return "redirect:/#contact";
    }

}
