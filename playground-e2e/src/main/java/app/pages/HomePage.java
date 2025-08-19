package app.pages;

import framework.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage extends BasePage {
    private final By goLogin = By.cssSelector("[data-test='go-login']");
    private final By goForms = By.cssSelector("[data-test='go-forms']");
    private final By goFiles = By.cssSelector("[data-test='go-files']");
    private final By goTable = By.cssSelector("[data-test='go-table']");

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public HomePage open() {
        super.open("/");
        return this;
    }

    public LoginPage goToLogin() {
        click(goLogin);
        return new LoginPage(driver);
    }

    public FormsPage goToForms() {
        click(goForms);
        return new FormsPage(driver);
    }

    public FilesPage goToFiles() {
        click(goFiles);
        return new FilesPage(driver);
    }

    public TablePage goToTable() {
        click(goTable);
        return new TablePage(driver);
    }
}
