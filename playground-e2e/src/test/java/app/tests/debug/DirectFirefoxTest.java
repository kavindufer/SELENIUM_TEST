package app.tests.debug;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.annotations.Test;

public class DirectFirefoxTest {

    @Test
    public void directFirefoxTest() {
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("-headless");
        
        WebDriver driver = null;
        try {
            System.out.println("Creating FirefoxDriver...");
            driver = new FirefoxDriver(options);
            System.out.println("FirefoxDriver created successfully");
            
            System.out.println("Navigating to localhost:3000...");
            driver.get("http://localhost:3000");
            System.out.println("Page title: " + driver.getTitle());
            System.out.println("Test completed successfully");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }
}