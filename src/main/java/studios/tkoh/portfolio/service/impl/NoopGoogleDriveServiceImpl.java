package studios.tkoh.portfolio.service.impl;

import java.io.IOException;
import java.security.GeneralSecurityException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import studios.tkoh.portfolio.dto.upload.UploadResponse;
import studios.tkoh.portfolio.service.GoogleDriveService;

@Service
@ConditionalOnProperty(prefix = "google.drive", name = "enabled", havingValue = "false")
public class NoopGoogleDriveServiceImpl implements GoogleDriveService {

    @Override
    public UploadResponse uploadFile(MultipartFile multipartFile, String folderId, String uniqueFilename)
            throws IOException, GeneralSecurityException {
        throw new IllegalStateException("Google Drive integration is disabled.");
    }

    @Override
    public UploadResponse uploadFile(MultipartFile multipartFile, String folderId, String uniqueFilename, boolean publiclyReadable)
            throws IOException, GeneralSecurityException {
        throw new IllegalStateException("Google Drive integration is disabled.");
    }

    @Override
    public String getPublicViewUrl(String fileId) {
        throw new IllegalStateException("Google Drive integration is disabled.");
    }

    @Override
    public byte[] downloadFile(String fileId) throws IOException, GeneralSecurityException {
        throw new IllegalStateException("Google Drive integration is disabled.");
    }
}
