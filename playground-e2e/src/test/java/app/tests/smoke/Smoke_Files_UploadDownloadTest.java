package app.tests.smoke;

import app.pages.FilesPage;
import framework.base.BaseTest;
import framework.utils.Downloads;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Smoke_Files_UploadDownloadTest extends BaseTest {

    @Test(groups = "smoke")
    public void uploadAndDownload() throws Exception {
        FilesPage page = new FilesPage(driver).open();
        Path temp = Files.createTempFile("upload", ".txt");
        Files.writeString(temp, "hello");
        page.upload(temp);
        page.assertLastUploadContains(temp.getFileName().toString());

        // Navigate back to files page to access download links
        page.open();
        
        Downloads dl = new Downloads(Paths.get(cfg.downloadsDir()).toAbsolutePath());
        dl.clear();
        page.downloadCsv();
        Path csv = dl.waitForFile("sample.csv", 5);
        Assertions.assertThat(dl.readCsv(csv).get(0)[0]).isNotEmpty();
    }
}
