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
import com.google.auth.oauth2.UserCredentials;
import jakarta.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import studios.tkoh.portfolio.dto.upload.UploadResponse;
import studios.tkoh.portfolio.service.GoogleDriveService;

/**
 *
 * @author Studios TKOH!
 */
@Service
@RequiredArgsConstructor
public class GoogleDriveServiceImpl implements GoogleDriveService {

    private Drive driveService;

    @Value("${google.drive.oauth.client-id}")
    private String clientId;

    @Value("${google.drive.oauth.client-secret}")
    private String clientSecret;

    @Value("${google.drive.oauth.refresh-token}")
    private String refreshToken;

    private static final String APPLICATION_NAME = "Portfolio Hub API";

    @PostConstruct
    public void init() throws IOException, GeneralSecurityException {
        this.driveService = buildDriveService();
    }

    private Drive buildDriveService() throws IOException, GeneralSecurityException {

        // Ya no usamos el archivo JSON, usamos las credenciales del USUARIO (OAuth)
        GoogleCredentials credentials = UserCredentials.newBuilder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .setRefreshToken(refreshToken)
                .build()
                .createScoped(Collections.singletonList(DriveScopes.DRIVE_FILE));

        // Refrescamos el token para asegurarnos de que tenemos uno de acceso válido
        credentials.refreshAccessToken();

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
