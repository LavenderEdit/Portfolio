package studios.tkoh.portfolio.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Set;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartFile;
import studios.tkoh.portfolio.service.FileValidationService;

@Service
public class FileValidationServiceImpl implements FileValidationService {

    private static final Set<String> IMAGE_EXTENSIONS = Set.of("png", "jpg", "jpeg", "webp");
    private static final Set<String> IMAGE_CONTENT_TYPES = Set.of("image/png", "image/jpeg", "image/webp");

    @Value("${application.upload.max-image-size:2MB}")
    private String maxImageSize;

    @Value("${application.upload.max-document-size:5MB}")
    private String maxDocumentSize;

    @Override
    public void validate(MultipartFile file, UploadKind kind) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("El archivo es obligatorio");
        }
        if (file.getSize() > maxBytes(kind)) {
            throw new IllegalArgumentException("El archivo excede el tamano permitido");
        }
        String extension = extension(file.getOriginalFilename());
        String contentType = file.getContentType() == null ? "" : file.getContentType().toLowerCase(Locale.ROOT);
        byte[] header = readHeader(file);

        if (kind == UploadKind.IMAGE) {
            validateImage(extension, contentType, header);
        } else {
            validateDocument(extension, contentType, header);
        }
    }

    private void validateImage(String extension, String contentType, byte[] header) {
        if (!IMAGE_EXTENSIONS.contains(extension)) {
            throw new IllegalArgumentException("La extension del archivo no esta permitida");
        }
        if (!IMAGE_CONTENT_TYPES.contains(contentType)) {
            throw new IllegalArgumentException("El tipo MIME del archivo no esta permitido");
        }
        if (!isPng(header) && !isJpeg(header) && !isWebp(header)) {
            throw new IllegalArgumentException("El contenido del archivo no coincide con una imagen permitida");
        }
    }

    private void validateDocument(String extension, String contentType, byte[] header) {
        if (!"pdf".equals(extension)) {
            throw new IllegalArgumentException("La extension del archivo no esta permitida");
        }
        if (!"application/pdf".equals(contentType)) {
            throw new IllegalArgumentException("El tipo MIME del archivo no esta permitido");
        }
        if (!isPdf(header)) {
            throw new IllegalArgumentException("El contenido del archivo no coincide con PDF");
        }
    }

    private long maxBytes(UploadKind kind) {
        String configured = kind == UploadKind.IMAGE ? maxImageSize : maxDocumentSize;
        return DataSize.parse(configured).toBytes();
    }

    private String extension(String filename) {
        if (filename == null || !filename.contains(".")) {
            throw new IllegalArgumentException("La extension del archivo no esta permitida");
        }
        return filename.substring(filename.lastIndexOf('.') + 1).toLowerCase(Locale.ROOT);
    }

    private byte[] readHeader(MultipartFile file) throws IOException {
        byte[] header = new byte[12];
        try (InputStream inputStream = file.getInputStream()) {
            int read = inputStream.read(header);
            if (read < 4) {
                throw new IllegalArgumentException("El archivo no tiene contenido valido");
            }
        }
        return header;
    }

    private boolean isPng(byte[] header) {
        return header[0] == (byte) 0x89 && header[1] == 0x50 && header[2] == 0x4E && header[3] == 0x47;
    }

    private boolean isJpeg(byte[] header) {
        return header[0] == (byte) 0xFF && header[1] == (byte) 0xD8 && header[2] == (byte) 0xFF;
    }

    private boolean isWebp(byte[] header) {
        return header[0] == 'R' && header[1] == 'I' && header[2] == 'F' && header[3] == 'F'
                && header[8] == 'W' && header[9] == 'E' && header[10] == 'B' && header[11] == 'P';
    }

    private boolean isPdf(byte[] header) {
        return header[0] == '%' && header[1] == 'P' && header[2] == 'D' && header[3] == 'F';
    }
}
