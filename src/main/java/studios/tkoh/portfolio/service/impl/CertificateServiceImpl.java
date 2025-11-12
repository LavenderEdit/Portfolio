package studios.tkoh.portfolio.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import studios.tkoh.portfolio.dto.certificate.CertificateCreateRequest;
import studios.tkoh.portfolio.dto.certificate.CertificateDto;
import studios.tkoh.portfolio.dto.certificate.CertificateUpdateRequest;
import studios.tkoh.portfolio.exception.ResourceNotFoundException;
import studios.tkoh.portfolio.mapper.CertificateMapper;
import studios.tkoh.portfolio.model.Certificate;
import studios.tkoh.portfolio.model.Education;
import studios.tkoh.portfolio.model.Profile;
import studios.tkoh.portfolio.repository.CertificateRepo;
import studios.tkoh.portfolio.repository.EducationRepo;
import studios.tkoh.portfolio.repository.ProfileRepo;
import studios.tkoh.portfolio.security.CustomUserDetails;
import studios.tkoh.portfolio.service.CertificateService;

/**
 *
 * @author Studios TKOH!
 */
@Service
@RequiredArgsConstructor
public class CertificateServiceImpl implements CertificateService {

    private final CertificateRepo certificateRepository;
    private final ProfileRepo profileRepository;
    private final EducationRepo educationRepository;
    private final CertificateMapper certificateMapper;

    @Override
    @Transactional
    public CertificateDto create(CertificateCreateRequest create) {
        CustomUserDetails user = getAuthenticatedUser();
        Profile profile = profileRepository.getReferenceById(user.getProfileId());

        Certificate certificate = certificateMapper.toEntity(create);
        certificate.setProfile(profile);

        if (create.educationId() != null) {
            Education education = educationRepository.findByIdAndProfileId(create.educationId(), user.getProfileId())
                    .orElseThrow(() -> new ResourceNotFoundException("Education", "id", create.educationId()));
            certificate.setEducation(education);
        }

        Certificate saved = certificateRepository.save(certificate);
        return certificateMapper.toDto(saved);
    }

    @Override
    @Transactional
    public CertificateDto update(CertificateUpdateRequest updated) {
        CustomUserDetails user = getAuthenticatedUser();
        Long profileId = user.getProfileId();

        Certificate certificate = certificateRepository.findByIdAndProfileId(updated.id(), profileId)
                .orElseThrow(() -> new ResourceNotFoundException("Certificate", "id", updated.id()));

        certificateMapper.updateEntityFromDto(updated, certificate);

        if (updated.educationId() != null) {
            Education education = educationRepository.findByIdAndProfileId(updated.educationId(), profileId)
                    .orElseThrow(() -> new ResourceNotFoundException("Education", "id", updated.educationId()));
            certificate.setEducation(education);
        } else {
            certificate.setEducation(null);
        }

        Certificate saved = certificateRepository.save(certificate);
        return certificateMapper.toDto(saved);
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        CustomUserDetails user = getAuthenticatedUser();
        Long profileId = user.getProfileId();

        Certificate certificate = certificateRepository.findByIdAndProfileId(id, profileId)
                .orElseThrow(() -> new ResourceNotFoundException("Certificate", "id", id));

        certificateRepository.delete(certificate);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public CertificateDto findDtoById(Long id) {
        CustomUserDetails user = getAuthenticatedUser();
        Long profileId = user.getProfileId();

        return certificateRepository.findByIdAndProfileId(id, profileId)
                .map(certificateMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Certificate", "id", id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CertificateDto> findAllDto() {
        CustomUserDetails user = getAuthenticatedUser();
        Long profileId = user.getProfileId();

        return certificateRepository.findAllByProfileId(profileId)
                .stream()
                .map(certificateMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public CertificateDto updateCertificateFile(Long certificateId, Long profileId, String imageUrl, String fileId) {
        Certificate certificate = certificateRepository.findByIdAndProfileId(certificateId, profileId)
                .orElseThrow(() -> new ResourceNotFoundException("Certificate", "id", certificateId));

        certificate.setImageUrl(imageUrl);
        certificate.setFileId(fileId);

        Certificate saved = certificateRepository.save(certificate);
        return certificateMapper.toDto(saved);
    }

    private CustomUserDetails getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails)) {
            throw new IllegalStateException("No se pudo obtener la información del usuario autenticado.");
        }
        return (CustomUserDetails) authentication.getPrincipal();
    }
}
