package framework.driver;

import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * Small helper to allow custom WebDriverManager configuration if needed.
 */
public final class WebDriverManagerExtension {
    private WebDriverManagerExtension() {}

    public static void setup(String browser) {
        if ("firefox".equalsIgnoreCase(browser)) {
            WebDriverManager.firefoxdriver().setup();
        } else {
            WebDriverManager.chromedriver().setup();
        }
    }
}
