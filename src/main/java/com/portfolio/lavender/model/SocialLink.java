package com.portfolio.lavender.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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
public class SocialLink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false)
    private Profile profile;
    @Column(nullable = false, length = 50)
    private String platform;
    @Column(nullable = false, length = 512)
    private String url;
    private int sortOrder;
}
