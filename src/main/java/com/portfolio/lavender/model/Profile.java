package com.portfolio.lavender.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 80, unique = true)
    private String slug;
    @Column(nullable = false, length = 120)
    private String fullName;
    @Column(nullable = false, length = 160)
    private String headline;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String bio;
    @Column(nullable = false, length = 160)
    private String email;
    private String location;
    private String avatarUrl;
    private String resumeUrl;
}
