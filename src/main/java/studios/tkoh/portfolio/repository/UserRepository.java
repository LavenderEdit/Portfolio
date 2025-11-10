package studios.tkoh.portfolio.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import studios.tkoh.portfolio.model.User;

/**
 *
 * @author Studios TKOH!
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u JOIN FETCH u.profile WHERE u.email = :email")
    Optional<User> findByEmailWithProfile(String email);
}
