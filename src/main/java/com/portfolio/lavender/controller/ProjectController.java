package com.portfolio.lavender.controller;

import com.portfolio.lavender.model.Project;
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
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectRepo repo;

    @GetMapping("/{slug}")
    public String detail(@PathVariable String slug, Model model) {
        Project p = repo.findBySlug(slug).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        model.addAttribute("project", p);
        return "project-detail";
    }
}
