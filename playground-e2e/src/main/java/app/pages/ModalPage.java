package app.pages;

import framework.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ModalPage extends BasePage {
    private final By open = By.cssSelector("[data-test='open-modal']");
    private final By ok = By.cssSelector("[data-test='modal-ok']");
    private final By cancel = By.cssSelector("[data-test='modal-cancel']");
    private final By result = By.cssSelector("[data-test='modal-result']");

    public ModalPage(WebDriver driver) {
        super(driver);
    }

    public ModalPage openPage() {
        open("/modal");
        return this;
    }

    public void confirm() {
        click(open);
        click(ok);
    }

    public void cancel() {
        click(open);
        click(cancel);
    }

    public void assertResult(String expected) {
        assertText(result, expected);
    }
}
