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
import studios.tkoh.portfolio.dto.education.EducationCreateRequest;
import studios.tkoh.portfolio.dto.education.EducationDto;
import studios.tkoh.portfolio.dto.education.EducationUpdateRequest;
import studios.tkoh.portfolio.dto.response.ApiResponse;
import studios.tkoh.portfolio.service.EducationService;

/**
 *
 * @author Studios TKOH!
 */
@RestController
@RequestMapping("/api/me/education")
@RequiredArgsConstructor
public class EducationController implements DtoCrudController<EducationDto, EducationCreateRequest, EducationUpdateRequest, Long> {

    private final EducationService educationService;

    @Override
    public ResponseEntity<ApiResponse<List<EducationDto>>> listAll() {
        List<EducationDto> educations = educationService.findAllDto();
        return ResponseEntity.ok(ApiResponse.ok("Formaciones obtenidas exitosamente", educations));
    }

    @Override
    public ResponseEntity<ApiResponse<EducationDto>> getById(@PathVariable Long id) {
        EducationDto education = educationService.findDtoById(id);
        return ResponseEntity.ok(ApiResponse.ok("Formación obtenida", education));
    }

    @Override
    public ResponseEntity<ApiResponse<EducationDto>> create(@Valid @RequestBody EducationCreateRequest create) {
        EducationDto newEducation = educationService.create(create);
        return new ResponseEntity<>(ApiResponse.ok("Formación creada exitosamente", newEducation), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ApiResponse<EducationDto>> update(@Valid @RequestBody EducationUpdateRequest updated) {
        EducationDto updatedEducation = educationService.update(updated);
        return ResponseEntity.ok(ApiResponse.ok("Formación actualizada exitosamente", updatedEducation));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<EducationDto>> update(@PathVariable Long id, @Valid @RequestBody EducationCreateRequest createData) {
        EducationUpdateRequest updateRequest = new EducationUpdateRequest(
                id,
                createData.institution(),
                createData.degree(),
                createData.field(),
                createData.startDate(),
                createData.endDate(),
                createData.description()
        );
        EducationDto updatedEducation = educationService.update(updateRequest);
        return ResponseEntity.ok(ApiResponse.ok("Formación actualizada exitosamente", updatedEducation));
    }

    @Override
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        educationService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok("Formación eliminada exitosamente"));
    }
}
