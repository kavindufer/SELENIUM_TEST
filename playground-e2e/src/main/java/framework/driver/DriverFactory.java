package framework.driver;

import framework.config.Config;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.MutableCapabilities;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class DriverFactory {
    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();
    private static final Config CFG = Config.get();

    public static WebDriver getDriver() {
        return DRIVER.get();
    }

    public static void initDriver() {
        if (DRIVER.get() != null) {
            return;
        }
        String browser = CFG.browser();
        boolean headless = CFG.headless();
        String downloads = Paths.get(CFG.downloadsDir()).toAbsolutePath().toString();
        MutableCapabilities options = getOptions(browser, headless, downloads);
        WebDriver driver;
        String gridUrl = CFG.gridUrl();
        try {
            if (gridUrl != null && !gridUrl.isEmpty()) {
                driver = new RemoteWebDriver(new URL(gridUrl), options);
            } else {
                driver = createLocalDriver(browser, options);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        if (!headless) {
            driver.manage().window().maximize();
        }
        DRIVER.set(driver);
    }

    private static MutableCapabilities getOptions(String browser, boolean headless, String downloads) {
        if ("firefox".equalsIgnoreCase(browser)) {
            FirefoxOptions options = new FirefoxOptions();
            if (headless) {
                options.addArguments("-headless");
            }
            options.addPreference("browser.download.dir", downloads);
            options.addPreference("browser.download.folderList", 2);
            options.addPreference("browser.helperApps.neverAsk.saveToDisk", "text/csv,application/csv,text/plain");
            return options;
        } else {
            ChromeOptions options = new ChromeOptions();
            if (headless) {
                options.addArguments("--headless=new");
            }
            Map<String, Object> prefs = new HashMap<>();
            prefs.put("download.default_directory", downloads);
            prefs.put("download.prompt_for_download", false);
            options.setExperimentalOption("prefs", prefs);
            options.addArguments("--remote-allow-origins=*");
            return options;
        }
    }

    private static WebDriver createLocalDriver(String browser, MutableCapabilities options) {
        if ("firefox".equalsIgnoreCase(browser)) {
            WebDriverManager.firefoxdriver().setup();
            return new FirefoxDriver((FirefoxOptions) options);
        } else {
            WebDriverManager.chromedriver().setup();
            return new ChromeDriver((ChromeOptions) options);
        }
    }

    public static void quitDriver() {
        WebDriver driver = DRIVER.get();
        if (driver != null) {
            driver.quit();
            DRIVER.remove();
        }
    }
}
