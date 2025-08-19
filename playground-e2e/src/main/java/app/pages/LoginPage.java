package app.pages;

import framework.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {
    private final By username = By.cssSelector("[data-test='username']");
    private final By password = By.cssSelector("[data-test='password']");
    private final By loginBtn = By.cssSelector("[data-test='login-btn']");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public LoginPage open() {
        super.open("/login");
        return this;
    }

    public void loginAs(String user, String pass) {
        type(username, user);
        type(password, pass);
        click(loginBtn);
    }
}
