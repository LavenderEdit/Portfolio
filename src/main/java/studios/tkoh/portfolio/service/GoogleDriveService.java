package studios.tkoh.portfolio.service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import org.springframework.web.multipart.MultipartFile;
import studios.tkoh.portfolio.dto.upload.UploadResponse;

/**
 *
 * @author Studios TKOH!
 */
public interface GoogleDriveService {

    UploadResponse uploadFile(MultipartFile file, String folderId, String uniqueFilename)
            throws IOException, GeneralSecurityException;

    String getPublicViewUrl(String fileId);
}
