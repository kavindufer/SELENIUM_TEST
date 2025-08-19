package framework.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class JS {
    private final WebDriver driver;
    private final JavascriptExecutor js;

    public JS(WebDriver driver) {
        this.driver = driver;
        this.js = (JavascriptExecutor) driver;
    }

    public void scrollIntoView(WebElement element) {
        js.executeScript("arguments[0].scrollIntoView(true);", element);
    }

    public void setValue(By locator, String value) {
        WebElement el = driver.findElement(locator);
        js.executeScript("arguments[0].value=arguments[1];", el, value);
    }

    public Object exec(String script, Object... args) {
        return js.executeScript(script, args);
    }
}
