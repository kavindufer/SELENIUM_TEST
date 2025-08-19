package app.pages;

import framework.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CsrfPage extends BasePage {
    private final By token = By.cssSelector("input[name='csrf']");
    private final By message = By.cssSelector("[data-test='message']");
    private final By submit = By.cssSelector("[data-test='submit']");
    private final By success = By.cssSelector("[data-test='csrf-success']");

    public CsrfPage(WebDriver driver) {
        super(driver);
    }

    public CsrfPage open() {
        open("/csrf-form");
        return this;
    }

    public String readToken() {
        return driver.findElement(token).getAttribute("value");
    }

    public void submitMessage(String msg) {
        type(message, msg);
        click(submit);
    }

    public void assertSuccess() {
        assertText(success, "success");
    }
}
