package app.tests.debug;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.Test;

public class DirectChromeTest {

    @Test
    public void directChromeTest() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-features=dbus");
        options.addArguments("--disable-audio-output");
        options.addArguments("--disable-setuid-sandbox");
        
        WebDriver driver = null;
        try {
            System.out.println("Creating ChromeDriver...");
            driver = new ChromeDriver(options);
            System.out.println("ChromeDriver created successfully");
            
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