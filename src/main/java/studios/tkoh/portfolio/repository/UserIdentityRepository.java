package studios.tkoh.portfolio.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import studios.tkoh.portfolio.model.UserIdentity;

@Repository
public interface UserIdentityRepository extends JpaRepository<UserIdentity, Long> {

    @org.springframework.data.jpa.repository.EntityGraph(attributePaths = {"user"})
    Optional<UserIdentity> findByProviderAndProviderSubject(String provider, String providerSubject);
}
