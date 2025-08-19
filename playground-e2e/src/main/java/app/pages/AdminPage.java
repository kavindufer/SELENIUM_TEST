package app.pages;

import framework.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AdminPage extends BasePage {
    private final By body = By.tagName("body");

    public AdminPage(WebDriver driver) {
        super(driver);
    }

    public AdminPage open() {
        super.open("/admin");
        return this;
    }

    public void assertForbidden() {
        assertText(body, "403");
    }

    public void assertAdminPage() {
        assertUrlContains("/admin");
    }
}
