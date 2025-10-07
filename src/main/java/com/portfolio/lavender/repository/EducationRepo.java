package com.portfolio.lavender.repository;

import com.portfolio.lavender.model.Education;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Joan Lavender
 */
public interface EducationRepo extends JpaRepository<Education, Long> {

    List<Education> findAllByOrderByStartDateDesc();
}
