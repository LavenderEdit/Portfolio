package studios.tkoh.portfolio.dto.skill;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 *
 * @author Studios TKOH!
 */
public record SkillUpdateRequest(
        @NotNull(message = "El ID es obligatorio para actualizar")
        Long id,
        @NotBlank(message = "El nombre del skill no puede estar vacío")
        @Size(max = 80)
        String name,
        @NotNull(message = "El nivel no puede ser nulo")
        @Min(0)
        @Max(100)
        short level,
        @Size(max = 255)
        String icon,
        @NotNull(message = "El orden no puede ser nulo")
        int sortOrder) {

}
