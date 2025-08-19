package framework.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class Shadow {
    private final WebDriver driver;

    public Shadow(WebDriver driver) {
        this.driver = driver;
    }

    public WebElement getShadowHost(By locator) {
        return driver.findElement(locator);
    }

    public SearchContext getShadowRoot(WebElement host) {
        return host.getShadowRoot();
    }

    public WebElement findInShadow(By hostLocator, By innerLocator) {
        WebElement host = getShadowHost(hostLocator);
        return getShadowRoot(host).findElement(innerLocator);
    }
}
