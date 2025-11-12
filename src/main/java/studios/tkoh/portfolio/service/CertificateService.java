package studios.tkoh.portfolio.service;

import studios.tkoh.portfolio.dto.certificate.CertificateCreateRequest;
import studios.tkoh.portfolio.dto.certificate.CertificateDto;
import studios.tkoh.portfolio.dto.certificate.CertificateUpdateRequest;
import studios.tkoh.portfolio.service.generic.DtoCrudService;

/**
 *
 * @author Studios TKOH!
 */
public interface CertificateService extends DtoCrudService<CertificateDto, CertificateCreateRequest, CertificateUpdateRequest, Long> {

    CertificateDto updateCertificateFile(Long certificateId, Long profileId, String imageUrl, String fileId);
}
