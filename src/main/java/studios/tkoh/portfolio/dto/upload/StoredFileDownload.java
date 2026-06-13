package studios.tkoh.portfolio.dto.upload;

public record StoredFileDownload(
        String filename,
        String contentType,
        byte[] content) {
}
