package app.pages;

import framework.base.BasePage;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AlertsPage extends BasePage {
    private final By alertBtn = By.cssSelector("[data-test='alert']");
    private final By confirmBtn = By.cssSelector("[data-test='confirm']");
    private final By promptBtn = By.cssSelector("[data-test='prompt']");
    private final By result = By.cssSelector("[data-test='alert-result']");

    public AlertsPage(WebDriver driver) {
        super(driver);
    }

    public AlertsPage open() {
        open("/alert");
        return this;
    }

    public void triggerAlert() {
        click(alertBtn);
        Alert a = driver.switchTo().alert();
        a.accept();
    }

    public void triggerConfirm(boolean ok) {
        click(confirmBtn);
        Alert a = driver.switchTo().alert();
        if (ok) a.accept(); else a.dismiss();
    }

    public void triggerPrompt(String text) {
        click(promptBtn);
        Alert a = driver.switchTo().alert();
        a.sendKeys(text);
        a.accept();
    }

    public void assertResult(String expected) {
        assertText(result, expected);
    }
}
