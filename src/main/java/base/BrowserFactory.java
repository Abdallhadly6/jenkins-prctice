package base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;

import org.testng.annotations.Parameters;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

public class BrowserFactory {

    public static WebDriver driver;
    public static Properties prop;

    @Parameters({"browserType"})
    public WebDriver getDriver(String Browser) throws IOException {
        FileReader file = new FileReader("src/main/resources/config.Properties");
        prop = new Properties();
        prop.load(file);

        switch (Browser.toLowerCase()) {
            case "chrome":
                driver = new ChromeDriver();
                break;
            case "firefox":
                driver = new FirefoxDriver();
                break;
            case "edge":
                driver = new EdgeDriver();
                break;
            case "safari":
                driver = new SafariDriver();
                break;
            default:
                throw new IllegalArgumentException("Please add browser");
        }

        driver.manage().window().maximize();
        driver.manage().deleteAllCookies();

        int implicitWait = Integer.parseInt(prop.getProperty("implicitWaitInSeconds"));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
        driver.get(prop.getProperty("url"));

        return driver;
    }
}
