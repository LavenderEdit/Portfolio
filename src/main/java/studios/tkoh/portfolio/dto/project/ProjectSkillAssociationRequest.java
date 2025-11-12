package studios.tkoh.portfolio.dto.project;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

/**
 *
 * @author Studios TKOH!
 */
public record ProjectSkillAssociationRequest(
        @NotEmpty(message = "La lista de skill IDs no puede estar vacía")
        List<Long> skillIds) {

}
