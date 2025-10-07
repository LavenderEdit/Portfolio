package com.portfolio.lavender.repository;

import com.portfolio.lavender.model.Project;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Joan Lavender
 */
public interface ProjectRepo extends JpaRepository<Project, Long> {

    Optional<Project> findBySlug(String slug);

    List<Project> findAllByOrderByFeaturedDescSortOrderAsc();
}
