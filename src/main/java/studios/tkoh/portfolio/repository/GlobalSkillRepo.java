package studios.tkoh.portfolio.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import studios.tkoh.portfolio.model.GlobalSkill;

/**
 *
 * @author Studios TKOH!
 */
@Repository
public interface GlobalSkillRepo extends JpaRepository<GlobalSkill, Long> {

    Optional<GlobalSkill> findByNameIgnoreCase(String name);

    List<GlobalSkill> findByNameContainingIgnoreCaseOrderByNameAsc(String fragment);
}
