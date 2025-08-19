package app.pages;

import framework.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class TablePage extends BasePage {
    private final By filter = By.cssSelector("[data-test='filter-input']");
    private final By sortName = By.cssSelector("[data-test='sort-name']");
    private final By sortPrice = By.cssSelector("[data-test='sort-price']");
    private final By next = By.cssSelector("[data-test='next']");
    private final By prev = By.cssSelector("[data-test='prev']");
    private final By total = By.cssSelector("[data-test='total']");

    public TablePage(WebDriver driver) {
        super(driver);
    }

    public TablePage open() {
        super.open("/table");
        return this;
    }

    public void filter(String query) {
        type(filter, query);
    }

    public void sortBy(String column) {
        if ("name".equalsIgnoreCase(column)) {
            click(sortName);
        } else if ("price".equalsIgnoreCase(column)) {
            click(sortPrice);
        }
    }

    public void nextPage() { click(next); }
    public void prevPage() { click(prev); }

    public String totalLabel() { return find(total).getText(); }
}
