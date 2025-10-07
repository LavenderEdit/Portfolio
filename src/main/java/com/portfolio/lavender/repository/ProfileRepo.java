package com.portfolio.lavender.repository;

import com.portfolio.lavender.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Joan Lavender
 */
public interface ProfileRepo extends JpaRepository<Profile, Long> {
}
