package framework.driver;

import framework.config.Config;
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
import java.time.Duration;
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
            // CI environment specific arguments
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--disable-gpu");
            options.addArguments("--disable-extensions");
            options.addArguments("--disable-plugins");
            options.addArguments("--disable-background-timer-throttling");
            options.addArguments("--disable-backgrounding-occluded-windows");
            options.addArguments("--disable-renderer-backgrounding");
            options.addArguments("--disable-ipc-flooding-protection");
            options.addArguments("--disable-hang-monitor");
            options.addArguments("--disable-prompt-on-repost");
            options.addArguments("--disable-domain-reliability");
            options.addArguments("--remote-allow-origins=*");
            // Additional stability arguments for sandboxed environments
            options.addArguments("--disable-blink-features=AutomationControlled");
            options.addArguments("--disable-infobars");
            options.addArguments("--disable-notifications");
            options.addArguments("--disable-popup-blocking");
            options.addArguments("--disable-translate");
            options.addArguments("--disable-default-apps");
            options.addArguments("--disable-background-networking");
            options.addArguments("--disable-sync");
            options.addArguments("--no-first-run");
            options.addArguments("--no-default-browser-check");
            options.addArguments("--disable-search-engine-choice-screen");
            options.addArguments("--disable-setuid-sandbox");
            options.addArguments("--disable-features=dbus");
            options.addArguments("--disable-audio-output");
            options.addArguments("--disable-crash-reporter");
            options.addArguments("--disable-component-update");
            // Disable DevTools to avoid CDP version compatibility warnings with Chrome v139
            options.addArguments("--disable-dev-tools");
            options.addArguments("--disable-logging");
            options.addArguments("--disable-web-security");
            options.addArguments("--disable-features=VizDisplayCompositor");
            options.setExperimentalOption("useAutomationExtension", false);
            options.setExperimentalOption("excludeSwitches", java.util.Arrays.asList("enable-automation"));
            
            Map<String, Object> prefs = new HashMap<>();
            prefs.put("download.default_directory", downloads);
            prefs.put("download.prompt_for_download", false);
            options.setExperimentalOption("prefs", prefs);
            return options;
        }
    }

    private static WebDriver createLocalDriver(String browser, MutableCapabilities options) {
        if ("firefox".equalsIgnoreCase(browser)) {
            // Skip WebDriverManager for Firefox in CI environment to avoid network issues
            // Assumes geckodriver is available in PATH
            return new FirefoxDriver((FirefoxOptions) options);
        } else {
            // Skip WebDriverManager for Chrome in CI environment to avoid network issues
            // Assumes chromedriver is available in PATH  
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
