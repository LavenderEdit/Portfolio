package com.portfolio.lavender.repository;

import com.portfolio.lavender.model.Experience;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Joan Lavender
 */
public interface ExperienceRepo extends JpaRepository<Experience, Long> {

    List<Experience> findAllByOrderByStartDateDesc();
}
