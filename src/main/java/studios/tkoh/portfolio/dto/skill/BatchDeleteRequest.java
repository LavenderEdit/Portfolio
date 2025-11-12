package studios.tkoh.portfolio.dto.skill;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

/**
 *
 * @author Studios TKOH!
 */
public record BatchDeleteRequest(
        @NotEmpty(message = "La lista de IDs no puede estar vacía")
        List<Long> ids) {

}
