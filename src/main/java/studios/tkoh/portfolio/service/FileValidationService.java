package studios.tkoh.portfolio.service;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public interface FileValidationService {

    void validate(MultipartFile file, UploadKind kind) throws IOException;

    enum UploadKind {
        IMAGE,
        DOCUMENT
    }
}
