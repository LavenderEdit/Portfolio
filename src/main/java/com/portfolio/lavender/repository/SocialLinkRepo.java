package com.portfolio.lavender.repository;

import com.portfolio.lavender.model.SocialLink;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Joan Lavender
 */
public interface SocialLinkRepo extends JpaRepository<SocialLink, Long> {

    List<SocialLink> findAllByProfileIdOrderBySortOrderAsc(Long profileId);
}
