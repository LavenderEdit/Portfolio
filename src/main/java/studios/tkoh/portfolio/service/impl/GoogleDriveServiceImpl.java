package studios.tkoh.portfolio.service.impl;

import com.google.api.client.http.InputStreamContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.Permission;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import jakarta.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import studios.tkoh.portfolio.config.GoogleDriveConfig;
import studios.tkoh.portfolio.dto.upload.UploadResponse;
import studios.tkoh.portfolio.service.GoogleDriveService;

/**
 *
 * @author Studios TKOH!
 */
@Service
@RequiredArgsConstructor
public class GoogleDriveServiceImpl implements GoogleDriveService {

    private final GoogleDriveConfig driveConfig;
    private Drive driveService;

    private static final String APPLICATION_NAME = "Portfolio Hub API";

    @PostConstruct
    public void init() throws IOException, GeneralSecurityException {
        this.driveService = buildDriveService();
    }

    private Drive buildDriveService() throws IOException, GeneralSecurityException {
        InputStream credentialsStream = GoogleDriveServiceImpl.class
                .getResourceAsStream(driveConfig.getServiceAccountKeyPath());

        if (credentialsStream == null) {
            throw new FileNotFoundException("Recurso de clave de cuenta de servicio no encontrado: "
                    + driveConfig.getServiceAccountKeyPath());
        }

        GoogleCredentials credentials = GoogleCredentials.fromStream(credentialsStream)
                .createScoped(Collections.singletonList(DriveScopes.DRIVE_FILE));

        NetHttpTransport httpTransport = new NetHttpTransport();
        return new Drive.Builder(httpTransport, GsonFactory.getDefaultInstance(), new HttpCredentialsAdapter(credentials))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    @Override
    public UploadResponse uploadFile(MultipartFile multipartFile, String folderId, String uniqueFilename)
            throws IOException, GeneralSecurityException {

        // 1. Crear metadatos del archivo
        File fileMetadata = new File();
        fileMetadata.setName(uniqueFilename);
        fileMetadata.setParents(Collections.singletonList(folderId));

        // 2. Crear contenido del archivo
        InputStreamContent mediaContent = new InputStreamContent(
                multipartFile.getContentType(),
                new ByteArrayInputStream(multipartFile.getBytes())
        );

        // 3. Subir el archivo
        File uploadedFile = driveService.files().create(fileMetadata, mediaContent)
                .setFields("id")
                .execute();

        String fileId = uploadedFile.getId();

        // 4. Hacer el archivo públicamente legible (fundamental)
        Permission permission = new Permission()
                .setType("anyone")
                .setRole("reader");
        driveService.permissions().create(fileId, permission).execute();

        // 5. Devolver DTO con ID y URL pública
        return new UploadResponse(fileId, getPublicViewUrl(fileId));
    }

    @Override
    public String getPublicViewUrl(String fileId) {
        return "https://drive.google.com/uc?export=view&id=" + fileId;
    }
}
