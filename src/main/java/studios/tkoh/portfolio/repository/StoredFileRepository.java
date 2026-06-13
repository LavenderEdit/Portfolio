package studios.tkoh.portfolio.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import studios.tkoh.portfolio.model.StoredFile;

public interface StoredFileRepository extends JpaRepository<StoredFile, Long> {

    Optional<StoredFile> findByIdAndProfileId(Long id, Long profileId);
}
