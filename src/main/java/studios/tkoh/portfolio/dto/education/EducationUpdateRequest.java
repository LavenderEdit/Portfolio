package studios.tkoh.portfolio.dto.education;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

/**
 *
 * @author Studios TKOH!
 */
public record EducationUpdateRequest(
        @NotNull(message = "El ID es obligatorio para actualizar")
        Long id,
        @NotBlank(message = "El nombre de la institución no puede estar vacío")
        @Size(max = 150)
        String institution,
        @NotBlank(message = "El título o grado no puede estar vacío")
        @Size(max = 150)
        String degree,
        @Size(max = 150)
        String field,
        @NotNull(message = "La fecha de inicio es obligatoria")
        LocalDate startDate,
        LocalDate endDate,
        @Size(max = 65535)
        String description) {

}
