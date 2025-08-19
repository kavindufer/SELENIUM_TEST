package app.pages;

import framework.base.BasePage;
import framework.config.Config;
import framework.utils.Retry429;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.io.IOException;

public class DynamicPage extends BasePage {
    private final By loadBtn = By.cssSelector("[data-test='load']");
    private final By items = By.cssSelector("[data-test='item']");
    private final By delayedText = By.cssSelector("[data-test='delayed-text']");

    public DynamicPage(WebDriver driver) {
        super(driver);
    }

    public void openDynamicLoading() {
        open("/dynamic-loading");
    }

    public void startLoading() {
        click(loadBtn);
        wait.visible(items);
    }

    public void openDelayed() {
        open("/delayed");
        wait.visible(delayedText);
    }

    public String delayedText() {
        return find(delayedText).getText();
    }

    public String callFlaky() {
        String url = Config.get().baseUrl() + "/flaky";
        try {
            return Retry429.getWithRetry(url, 5);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
