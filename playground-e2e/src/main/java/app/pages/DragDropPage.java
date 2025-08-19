package app.pages;

import framework.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;

public class DragDropPage extends BasePage {
    private final By drag = By.cssSelector("#drag");
    private final By drop = By.cssSelector("#drop");

    public DragDropPage(WebDriver driver) {
        super(driver);
    }

    public DragDropPage open() {
        open("/drag-drop");
        return this;
    }

    public void dragIntoDrop() {
        new Actions(driver).dragAndDrop(find(drag), find(drop)).perform();
    }

    public void assertDropped() {
        assertText(drop, "dropped");
    }
}
