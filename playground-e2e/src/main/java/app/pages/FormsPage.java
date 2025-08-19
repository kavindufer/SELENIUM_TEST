package app.pages;

import framework.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

public class FormsPage extends BasePage {
    private final By email = By.cssSelector("[data-test='email']");
    private final By password = By.cssSelector("[data-test='password']");
    private final By plan = By.cssSelector("[data-test='plan']");
    private final By age = By.cssSelector("[data-test='age']");
    private final By submit = By.cssSelector("[data-test='submit']");
    private final By success = By.cssSelector("[data-test='success']");
    private final By error = By.cssSelector("[data-test='error']");

    public FormsPage(WebDriver driver) {
        super(driver);
    }

    public FormsPage open() {
        super.open("/forms/basic");
        return this;
    }

    public void fillEmail(String value) { type(email, value); }
    public void fillPassword(String value) { type(password, value); }
    public void selectPlan(String value) { new Select(find(plan)).selectByValue(value); }
    public void setAge(String value) { type(age, value); }

    public void submit() { click(submit); }

    public void assertSuccessContains(String role) {
        assertText(success, role);
    }

    public String errorMessage() {
        return find(error).getText();
    }
}
