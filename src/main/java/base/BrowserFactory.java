package base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;

import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

public class BrowserFactory {

    public static WebDriver driver;
    public static Properties prop;

    public WebDriver getDriver(String browser) throws IOException {
        // Load config properties
        FileReader file = new FileReader("src/main/resources/config.Properties");
        prop = new Properties();
        prop.load(file);

        boolean headless = Boolean.parseBoolean(System.getProperty("headless", "true"));
        browser = browser.toLowerCase();

        switch (browser) {
            case "chrome":
                ChromeOptions chromeOptions = new ChromeOptions();
                if (headless) {
                    chromeOptions.addArguments("--headless=new", "--no-sandbox", "--disable-dev-shm-usage", "--window-size=1920,1080");
                }
                driver = new ChromeDriver(chromeOptions);
                break;

            case "firefox":
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                if (headless) firefoxOptions.addArguments("-headless");
                driver = new FirefoxDriver(firefoxOptions);
                break;

            case "edge":
                EdgeOptions edgeOptions = new EdgeOptions();
                if (headless) edgeOptions.addArguments("--headless=new", "--no-sandbox", "--disable-dev-shm-usage", "--window-size=1920,1080");
                driver = new EdgeDriver(edgeOptions);
                break;

            case "safari":
                driver = new SafariDriver(); // Safari doesn’t support headless
                break;

            default:
                throw new IllegalArgumentException("Unsupported browser: " + browser);
        }

        driver.manage().window().maximize();
        driver.manage().deleteAllCookies();
        int implicitWait = Integer.parseInt(prop.getProperty("implicitWaitInSeconds", "10"));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
        driver.get(prop.getProperty("url"));

        return driver;
    }
}
