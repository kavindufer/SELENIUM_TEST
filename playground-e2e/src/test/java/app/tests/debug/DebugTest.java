package app.tests.debug;

import framework.base.BaseTest;
import org.testng.annotations.Test;
import org.assertj.core.api.Assertions;

public class DebugTest extends BaseTest {

    @Test
    public void simplePageLoadTest() {
        driver.get("http://localhost:3000");
        System.out.println("Page title: " + driver.getTitle());
        System.out.println("Current URL: " + driver.getCurrentUrl());
        Assertions.assertThat(driver.getTitle()).contains("Selenium Test Playground");
    }
}