package studios.tkoh.portfolio.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author Studios TKOH!
 */
@Configuration
@ConfigurationProperties(prefix = "google.drive")
@Getter
@Setter
public class GoogleDriveConfig {

    private String serviceAccountKeyPath;
    private Folders folders = new Folders();

    @Getter
    @Setter
    public static class Folders {

        private String userAvatars;
        private String userResumes;
        private String projectsCover;
        private String skillsIcon;
        private String certificates;
    }
}
