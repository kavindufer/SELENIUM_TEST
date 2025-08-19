package app.pages;

import framework.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ProfilePage extends BasePage {
    private final By username = By.cssSelector("[data-test='profile-username']");
    private final By role = By.cssSelector("[data-test='profile-role']");

    public ProfilePage(WebDriver driver) {
        super(driver);
    }

    public void assertUsername(String expected) {
        assertText(username, expected);
    }

    public void assertRole(String expected) {
        assertText(role, expected);
    }
}
