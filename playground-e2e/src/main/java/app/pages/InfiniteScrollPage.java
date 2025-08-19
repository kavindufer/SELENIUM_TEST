package app.pages;

import framework.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class InfiniteScrollPage extends BasePage {
    private final By items = By.cssSelector("[data-test='scroll-item']");
    private final By status = By.cssSelector("[data-test='status']");

    public InfiniteScrollPage(WebDriver driver) {
        super(driver);
    }

    public InfiniteScrollPage open() {
        open("/scroll");
        return this;
    }

    public void loadAll() {
        int count = driver.findElements(items).size();
        while (true) {
            js.exec("window.scrollTo(0, document.body.scrollHeight)");
            wait.present(items);
            int newCount = driver.findElements(items).size();
            if (newCount == count || status().contains("end")) {
                break;
            }
            count = newCount;
        }
    }

    public String status() {
        return find(status).getText();
    }
}
