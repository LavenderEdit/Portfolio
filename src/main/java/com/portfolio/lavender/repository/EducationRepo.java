package com.portfolio.lavender.repository;

import com.portfolio.lavender.model.Education;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Studios TKOH!
 */
public interface EducationRepo extends JpaRepository<Education, Long> {

    List<Education> findAllByProfileSlugOrderByStartDateDesc(String slug);
}
