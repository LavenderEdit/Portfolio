package studios.tkoh.portfolio.service.impl;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class NoopGoogleDriveServiceImplTest {

    private final NoopGoogleDriveServiceImpl service = new NoopGoogleDriveServiceImpl();

    @Test
    void uploadFileFailsExplicitlyWhenGoogleDriveIsDisabled() {
        assertThatThrownBy(() -> service.uploadFile(null, "folder-id", "avatar.png"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Google Drive integration is disabled.");
    }

    @Test
    void getPublicViewUrlFailsExplicitlyWhenGoogleDriveIsDisabled() {
        assertThatThrownBy(() -> service.getPublicViewUrl("file-id"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Google Drive integration is disabled.");
    }
}
