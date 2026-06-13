package studios.tkoh.portfolio.service.impl;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import studios.tkoh.portfolio.service.FileValidationService.UploadKind;

class FileValidationServiceImplTest {

    private final FileValidationServiceImpl service = new FileValidationServiceImpl();

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(service, "maxImageSize", "2MB");
        ReflectionTestUtils.setField(service, "maxDocumentSize", "5MB");
    }

    @Test
    void acceptsPngWhenExtensionContentTypeAndMagicBytesMatch() {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "avatar.png",
                "image/png",
                new byte[]{(byte) 0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A}
        );

        assertThatCode(() -> service.validate(file, UploadKind.IMAGE)).doesNotThrowAnyException();
    }

    @Test
    void rejectsSvgForImageUploads() {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "avatar.svg",
                "image/svg+xml",
                "<svg></svg>".getBytes()
        );

        assertThatThrownBy(() -> service.validate(file, UploadKind.IMAGE))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("extension");
    }

    @Test
    void rejectsPdfDisguisedAsPng() {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "avatar.png",
                "image/png",
                "%PDF-1.7".getBytes()
        );

        assertThatThrownBy(() -> service.validate(file, UploadKind.IMAGE))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("contenido");
    }

    @Test
    void acceptsPdfForDocumentUploads() {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "resume.pdf",
                "application/pdf",
                "%PDF-1.7".getBytes()
        );

        assertThatCode(() -> service.validate(file, UploadKind.DOCUMENT)).doesNotThrowAnyException();
    }
}
