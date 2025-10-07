package com.portfolio.lavender.repository;

import com.portfolio.lavender.model.SocialLink;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Joan Lavender
 */
@Repository
public interface SocialLinkRepo extends JpaRepository<SocialLink, Long> {

    @EntityGraph(attributePaths = "profile")
    List<SocialLink> findAllByProfileSlugOrderBySortOrderAsc(String slug);
}
