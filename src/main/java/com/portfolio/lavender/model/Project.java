package com.portfolio.lavender.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Joan Lavender
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(indexes = @Index(name = "uk_project_slug", columnList = "slug", unique = true))
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 140)
    private String title;
    @Column(nullable = false, length = 160)
    private String slug;
    @Column(nullable = false, length = 280)
    private String summary;
    @Column(columnDefinition = "TEXT")
    private String description;
    private String repoUrl;
    private String liveUrl;
    private String coverImage;
    private java.time.LocalDate startDate;
    private java.time.LocalDate endDate;
    private boolean featured;
    private int sortOrder;

    @ManyToMany
    @JoinTable(name = "project_skill",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id"))
    private java.util.Set<Skill> skills = new java.util.HashSet<>();

}
