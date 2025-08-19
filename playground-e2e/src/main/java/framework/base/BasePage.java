package framework.base;

import framework.config.Config;
import framework.utils.JS;
import framework.waits.Waits;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public abstract class BasePage {
    protected final WebDriver driver;
    protected final Waits wait;
    protected final JS js;
    private final Config cfg = Config.get();

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new Waits(driver);
        this.js = new JS(driver);
    }

    protected void open(String path) {
        driver.get(cfg.baseUrl() + path);
    }

    protected WebElement find(By locator) {
        return wait.visible(locator);
    }

    protected void click(By locator) {
        find(locator).click();
    }

    protected void type(By locator, String text) {
        WebElement el = find(locator);
        el.clear();
        el.sendKeys(text);
    }

    protected void assertText(By locator, String expected) {
        Assertions.assertThat(find(locator).getText()).contains(expected);
    }

    protected void assertUrlContains(String fragment) {
        Assertions.assertThat(driver.getCurrentUrl()).contains(fragment);
    }
}
