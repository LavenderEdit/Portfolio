package com.portfolio.lavender.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Studios TKOH!
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(indexes = {
    @Index(name = "idx_project_featured", columnList = "profile_id, featured, sort_order")
}, uniqueConstraints = @UniqueConstraint(name = "uk_project_profile_slug", columnNames = {"profile_id", "slug"}))
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Profile profile;
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
    @OrderBy("sortOrder ASC, name ASC")
    private Set<Skill> skills = new LinkedHashSet<>();

}
