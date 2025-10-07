package com.portfolio.lavender.controller;

import com.portfolio.lavender.model.Profile;
import com.portfolio.lavender.model.Project;
import com.portfolio.lavender.repository.ProfileRepo;
import com.portfolio.lavender.repository.ProjectRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

/**
 *
 * @author Joan Lavender
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/portfolio/{profileSlug}/projects")
public class ProjectController {

    private final ProjectRepo repo;
    private final ProfileRepo profileRepo;

    @GetMapping("/{slug}")
    public String detail(@PathVariable String profileSlug, @PathVariable String slug, Model model) {
        Profile profile = profileRepo.findBySlug(profileSlug)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Project p = repo.findByProfileSlugAndSlug(profileSlug, slug)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        model.addAttribute("profile", profile);
        model.addAttribute("project", p);
        model.addAttribute("title", p.getTitle() + " · " + profile.getFullName());
        model.addAttribute("metaDescription", p.getSummary());
        return "project/detail";
    }
}
