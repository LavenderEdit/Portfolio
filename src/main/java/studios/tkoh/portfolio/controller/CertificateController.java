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
import studios.tkoh.portfolio.dto.certificate.CertificateCreateRequest;
import studios.tkoh.portfolio.dto.certificate.CertificateDto;
import studios.tkoh.portfolio.dto.certificate.CertificateUpdateRequest;
import studios.tkoh.portfolio.dto.response.ApiResponse;
import studios.tkoh.portfolio.service.CertificateService;

/**
 *
 * @author Studios TKOH!
 */
@RestController
@RequestMapping("/api/me/certificates")
@RequiredArgsConstructor
public class CertificateController implements DtoCrudController<CertificateDto, CertificateCreateRequest, CertificateUpdateRequest, Long> {

    private final CertificateService certificateService;

    @Override
    public ResponseEntity<ApiResponse<List<CertificateDto>>> listAll() {
        List<CertificateDto> dtos = certificateService.findAllDto();
        return ResponseEntity.ok(ApiResponse.ok("Certificados obtenidos", dtos));
    }

    @Override
    public ResponseEntity<ApiResponse<CertificateDto>> getById(@PathVariable Long id) {
        CertificateDto dto = certificateService.findDtoById(id);
        return ResponseEntity.ok(ApiResponse.ok("Certificado obtenido", dto));
    }

    @Override
    public ResponseEntity<ApiResponse<CertificateDto>> create(@Valid @RequestBody CertificateCreateRequest create) {
        CertificateDto newDto = certificateService.create(create);
        return new ResponseEntity<>(ApiResponse.ok("Certificado creado. Sube el archivo.", newDto), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ApiResponse<CertificateDto>> update(@Valid @RequestBody CertificateUpdateRequest updated) {
        CertificateDto updatedDto = certificateService.update(updated);
        return ResponseEntity.ok(ApiResponse.ok("Certificado actualizado", updatedDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CertificateDto>> update(@PathVariable Long id, @Valid @RequestBody CertificateCreateRequest createData) {
        CertificateUpdateRequest updateRequest = new CertificateUpdateRequest(
                id,
                createData.name(),
                createData.description(),
                createData.educationId()
        );
        CertificateDto updatedDto = certificateService.update(updateRequest);
        return ResponseEntity.ok(ApiResponse.ok("Certificado actualizado", updatedDto));
    }

    @Override
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        certificateService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok("Certificado eliminado"));
    }
}
