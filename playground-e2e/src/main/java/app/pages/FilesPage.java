package app.pages;

import framework.base.BasePage;
import framework.utils.Downloads;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.nio.file.Path;

public class FilesPage extends BasePage {
    private final By fileInput = By.cssSelector("[data-test='file-input']");
    private final By uploadBtn = By.cssSelector("[data-test='upload-btn']");
    private final By lastUpload = By.cssSelector("[data-test='upload-result']");
    private final By dlCsv = By.cssSelector("[data-test='download-csv']");
    private final By dlTxt = By.cssSelector("[data-test='download-txt']");

    public FilesPage(WebDriver driver) {
        super(driver);
    }

    public FilesPage open() {
        super.open("/files");
        return this;
    }

    public void upload(Path file) {
        driver.findElement(fileInput).sendKeys(file.toAbsolutePath().toString());
        click(uploadBtn);
    }

    public void assertLastUploadContains(String name) {
        assertText(lastUpload, name);
    }

    public void downloadCsv() {
        click(dlCsv);
    }

    public void downloadTxt() {
        click(dlTxt);
    }
}
