package com.portfolio.lavender.repository;

import com.portfolio.lavender.model.Profile;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Joan Lavender
 */
@Repository
public interface ProfileRepo extends JpaRepository<Profile, Long> {
    
    Optional<Profile> findBySlug(String slug);

    boolean existsBySlug(String slug);
}
