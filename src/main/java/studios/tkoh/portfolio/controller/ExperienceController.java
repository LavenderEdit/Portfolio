package studios.tkoh.portfolio.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import studios.tkoh.portfolio.controller.generic.DtoCrudController;
import studios.tkoh.portfolio.dto.experience.ExperienceCreateRequest;
import studios.tkoh.portfolio.dto.experience.ExperienceDto;
import studios.tkoh.portfolio.dto.experience.ExperienceUpdateRequest;
import studios.tkoh.portfolio.dto.response.ApiResponse;
import studios.tkoh.portfolio.service.ExperienceService;

/**
 *
 * @author Studios TKOH!
 */
@RestController
@RequestMapping("/api/me/experience")
@RequiredArgsConstructor
public class ExperienceController implements DtoCrudController<ExperienceDto, ExperienceCreateRequest, ExperienceUpdateRequest, Long> {

    private final ExperienceService experienceService;

    @Override
    public ResponseEntity<ApiResponse<List<ExperienceDto>>> listAll() {
        List<ExperienceDto> experiences = experienceService.findAllDto();
        return ResponseEntity.ok(ApiResponse.ok("Experiencias obtenidas exitosamente", experiences));
    }

    @Override
    public ResponseEntity<ApiResponse<ExperienceDto>> getById(@PathVariable Long id) {
        ExperienceDto experience = experienceService.findDtoById(id);
        return ResponseEntity.ok(ApiResponse.ok("Experiencia obtenida", experience));
    }

    @Override
    public ResponseEntity<ApiResponse<ExperienceDto>> create(@Valid @RequestBody ExperienceCreateRequest create) {
        ExperienceDto newExperience = experienceService.create(create);
        return new ResponseEntity<>(ApiResponse.ok("Experiencia creada exitosamente", newExperience), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ApiResponse<ExperienceDto>> update(@Valid @RequestBody ExperienceUpdateRequest updated) {
        ExperienceDto updatedExperience = experienceService.update(updated);
        return ResponseEntity.ok(ApiResponse.ok("Experiencia actualizada exitosamente", updatedExperience));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ExperienceDto>> update(@PathVariable Long id, @Valid @RequestBody ExperienceCreateRequest createData) {
        ExperienceUpdateRequest updateRequest = new ExperienceUpdateRequest(
                id,
                createData.company(),
                createData.role(),
                createData.location(),
                createData.startDate(),
                createData.endDate(),
                createData.current(),
                createData.description()
        );
        ExperienceDto updatedExperience = experienceService.update(updateRequest);
        return ResponseEntity.ok(ApiResponse.ok("Experiencia actualizada exitosamente", updatedExperience));
    }

    @Override
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        experienceService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok("Experiencia eliminada exitosamente"));
    }
}
