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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author Joan Lavender
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
        Profile profile = profileRepo.findAll().stream().findFirst().orElse(null);
        model.addAttribute("profile", profile);
        model.addAttribute("socials", socialRepo.findAllByProfileIdOrderBySortOrderAsc(profile.getId()));
        model.addAttribute("categories", catRepo.findAllByOrderBySortOrderAsc());
        model.addAttribute("projects", projectRepo.findAllByOrderByFeaturedDescSortOrderAsc());
        model.addAttribute("experiences", expRepo.findAllByOrderByStartDateDesc());
        model.addAttribute("education", eduRepo.findAllByOrderByStartDateDesc());
        model.addAttribute("contactForm", new ContactForm());
        return "index";
    }
}
