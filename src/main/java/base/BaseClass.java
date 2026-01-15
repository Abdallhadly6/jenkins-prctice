package base;

import lombok.Getter;
import org.testng.annotations.*;
import org.openqa.selenium.WebDriver;
import pages.LoginPage;
import utility.EmailManager;
import utility.ExtentReportManager;
import utility.FileUtils;

import java.io.File;
import java.io.IOException;

public class BaseClass {

    @Getter
    protected WebDriver driver;
    protected BrowserFactory factory;
    protected LoginPage loginPage;

    protected static String browserType;

    @BeforeSuite
    @Parameters({"browserType"})
    public void beforeSuite(@Optional("chrome") String brs) {
        browserType = brs.toLowerCase();
        String reportPath = System.getProperty("user.dir") + "/test-reports-" + browserType;
        FileUtils.deleteDirectoryIfExists(new File(reportPath));
        ExtentReportManager.setExtent(reportPath);  // browser-specific report folder
    }

    @AfterSuite
    public void afterSuite() {
        ExtentReportManager.endReport();
        EmailManager.sendMail(browserType); // Send email with browser-specific zip
    }

    @BeforeMethod
    @Parameters({"browserType"})
    public void setUp(@Optional("chrome") String brs) throws IOException {
        factory = new BrowserFactory();
        driver = factory.getDriver(brs); // Pass browser to factory
        loginPage = new LoginPage(driver);
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) driver.quit();
    }
}
