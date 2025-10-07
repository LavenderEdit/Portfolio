package com.portfolio.lavender.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import java.util.LinkedHashSet;
import java.util.Set;
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
public class SkillCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Profile profile;
    @Column(nullable = false, length = 80)
    private String name;
    private int sortOrder;
    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    @OrderBy("sortOrder ASC, name ASC")
    private Set<Skill> skills = new LinkedHashSet<>();
}
