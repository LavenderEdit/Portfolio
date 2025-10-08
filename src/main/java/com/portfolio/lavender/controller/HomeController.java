package com.portfolio.lavender.controller;

import com.portfolio.lavender.dto.simple.ContactForm;
import com.portfolio.lavender.model.Profile;
import com.portfolio.lavender.repository.EducationRepo;
import com.portfolio.lavender.repository.ExperienceRepo;
import com.portfolio.lavender.repository.ProfileRepo;
import com.portfolio.lavender.repository.ProjectRepo;
import com.portfolio.lavender.repository.SkillCategoryRepo;
import com.portfolio.lavender.repository.SocialLinkRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

/**
 *
 * @author Studios TKOH!
 */
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ProfileRepo profileRepo;
    private final SocialLinkRepo socialRepo;
    private final SkillCategoryRepo catRepo;
    private final ProjectRepo projectRepo;
    private final ExperienceRepo expRepo;
    private final EducationRepo eduRepo;

    @GetMapping("/")
    public String home(Model model) {
        var profiles = profileRepo.findAll(Sort.by("fullName").ascending());
        if (profiles.size() == 1) {
            return "redirect:/portfolio/" + profiles.get(0).getSlug();
        }
        model.addAttribute("title", "Portafolios");
        model.addAttribute("profiles", profiles);
        model.addAttribute("metaDescription", "Explora múltiples portafolios creados con Spring Boot y Thymeleaf.");
        return "profiles/index";
    }

    @GetMapping("/portfolio/{slug}")
    public String portfolio(@PathVariable String slug, Model model) {
        Profile profile = profileRepo.findBySlug(slug)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        model.addAttribute("profile", profile);
        model.addAttribute("title", profile.getFullName() + " | Portafolio");
        model.addAttribute("metaDescription", profile.getHeadline());
        model.addAttribute("socials", socialRepo.findAllByProfileSlugOrderBySortOrderAsc(slug));
        model.addAttribute("categories", catRepo.findAllByProfileSlugOrderBySortOrderAsc(slug));
        model.addAttribute("projects", projectRepo.findAllByProfileSlugOrderByFeaturedDescSortOrderAsc(slug));
        model.addAttribute("experiences", expRepo.findAllByProfileSlugOrderByStartDateDesc(slug));
        model.addAttribute("education", eduRepo.findAllByProfileSlugOrderByStartDateDesc(slug));
        if (!model.containsAttribute("contactForm")) {
            model.addAttribute("contactForm", new ContactForm());
        }
        return "portfolio/index";
    }
}
