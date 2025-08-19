package framework.waits;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class Waits {
    private final WebDriver driver;
    private final Duration timeout = Duration.ofSeconds(10);

    public Waits(WebDriver driver) {
        this.driver = driver;
    }

    private WebDriverWait wait() {
        return new WebDriverWait(driver, timeout);
    }

    public WebElement visible(By locator) {
        return wait().until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public WebElement clickable(By locator) {
        return wait().until(ExpectedConditions.elementToBeClickable(locator));
    }

    public WebElement present(By locator) {
        return wait().until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    public boolean textToBe(By locator, String text) {
        return wait().until(ExpectedConditions.textToBe(locator, text));
    }

    public boolean attributeToBe(By locator, String attr, String value) {
        return wait().until(ExpectedConditions.attributeToBe(locator, attr, value));
    }

    public boolean urlContains(String fragment) {
        return wait().until(ExpectedConditions.urlContains(fragment));
    }

    public void ajaxDone() {
        wait().until(d -> {
            Object res = ((JavascriptExecutor) d).executeScript("return window.jQuery ? jQuery.active == 0 : true");
            return Boolean.TRUE.equals(res);
        });
    }
}
