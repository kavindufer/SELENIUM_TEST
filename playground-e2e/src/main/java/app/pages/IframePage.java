package app.pages;

import framework.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class IframePage extends BasePage {
    private final By frame = By.cssSelector("iframe");
    private final By button = By.cssSelector("[data-test='iframe-button']");
    private final By title = By.tagName("h1");

    public IframePage(WebDriver driver) {
        super(driver);
    }

    public IframePage open() {
        open("/iframe");
        return this;
    }

    public void clickButtonInFrame() {
        WebElement fr = find(frame);
        driver.switchTo().frame(fr);
        click(button);
        driver.switchTo().defaultContent();
    }

    public void assertTitle(String expected) {
        assertText(title, expected);
    }
}
