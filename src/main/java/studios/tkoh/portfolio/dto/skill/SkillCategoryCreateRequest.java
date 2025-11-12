package studios.tkoh.portfolio.dto.skill;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 *
 * @author Studios TKOH!
 */
public record SkillCategoryCreateRequest(
        @NotBlank(message = "El nombre de la categoría no puede estar vacío")
        @Size(max = 80)
        String name,
        @NotNull(message = "El orden no puede ser nulo")
        int sortOrder) {

}
