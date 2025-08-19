package app.pages;

import framework.base.BasePage;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ShadowDomPage extends BasePage {
    private final By host = By.cssSelector("my-field");

    public ShadowDomPage(WebDriver driver) {
        super(driver);
    }

    public ShadowDomPage open() {
        open("/shadow-dom");
        return this;
    }

    private SearchContext shadowRoot() {
        WebElement h = find(host);
        return h.getShadowRoot();
    }

    public void setField(String text) {
        SearchContext shadow = shadowRoot();
        WebElement input = shadow.findElement(By.cssSelector("input"));
        input.clear();
        input.sendKeys(text);
        shadow.findElement(By.cssSelector("button")).click();
    }

    public void assertPill(String expected) {
        String pill = shadowRoot().findElement(By.cssSelector(".pill")).getText();
        Assertions.assertThat(pill).contains(expected);
    }
}
