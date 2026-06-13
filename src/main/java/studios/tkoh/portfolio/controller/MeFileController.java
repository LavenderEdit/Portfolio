package studios.tkoh.portfolio.controller;

import java.io.IOException;
import java.security.GeneralSecurityException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import studios.tkoh.portfolio.dto.upload.StoredFileDownload;
import studios.tkoh.portfolio.service.UploadService;

@RestController
@RequestMapping("/api/me/files")
@RequiredArgsConstructor
public class MeFileController {

    private final UploadService uploadService;

    @GetMapping("/{storedFileId}")
    public ResponseEntity<byte[]> downloadPrivateFile(@PathVariable Long storedFileId)
            throws IOException, GeneralSecurityException {
        StoredFileDownload file = uploadService.downloadPrivateFile(storedFileId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.contentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + safeFilename(file.filename()) + "\"")
                .body(file.content());
    }

    private String safeFilename(String filename) {
        return filename == null ? "download" : filename.replace("\"", "").replace("\r", "").replace("\n", "");
    }
}
