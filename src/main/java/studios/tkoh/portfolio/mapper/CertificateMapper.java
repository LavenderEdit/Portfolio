package studios.tkoh.portfolio.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import studios.tkoh.portfolio.dto.certificate.CertificateCreateRequest;
import studios.tkoh.portfolio.dto.certificate.CertificateDto;
import studios.tkoh.portfolio.dto.certificate.CertificateUpdateRequest;
import studios.tkoh.portfolio.model.Certificate;

/**
 *
 * @author Studios TKOH!
 */
@Mapper(componentModel = "spring")
public interface CertificateMapper {

    @Mapping(source = "education.id", target = "educationId")
    CertificateDto toDto(Certificate certificate);

    @Mapping(target = "profile", ignore = true)
    @Mapping(target = "education", ignore = true)
    @Mapping(target = "imageUrl", ignore = true)
    @Mapping(target = "fileId", ignore = true)
    Certificate toEntity(CertificateCreateRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "profile", ignore = true)
    @Mapping(target = "education", ignore = true)
    @Mapping(target = "imageUrl", ignore = true)
    @Mapping(target = "fileId", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(CertificateUpdateRequest dto, @MappingTarget Certificate entity);
}
