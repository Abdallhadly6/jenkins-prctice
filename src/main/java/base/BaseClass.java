package base;

import lombok.Getter;
import org.testng.annotations.*;
import org.openqa.selenium.WebDriver;
import pages.LoginPage;
import utility.EmailManager;
import utility.ExtentReportManager;
import utility.FileUtils;

import javax.mail.MessagingException;
import java.io.File;
import java.io.IOException;

public class BaseClass {

    @Getter
    protected WebDriver driver;
    protected BrowserFactory factory;
    protected LoginPage loginPage;

    @BeforeSuite
    public void beforeSuite() {
        FileUtils.deleteDirectoryIfExists(new File(System.getProperty("user.dir") + "/test-reports"));
        ExtentReportManager.setExtent();  // Initialize report
    }

    @AfterSuite
    public void afterSuite() throws MessagingException {
        ExtentReportManager.endReport();  // End report
        // EmailManager.sendMail(); // Optional
    }

    @BeforeMethod
    public void setUp() throws IOException {
        factory = new BrowserFactory();
        driver = factory.getDriver(); // No parameter needed now
        loginPage = new LoginPage(driver);
    }

    @AfterMethod
    public void tearDown(){
        if (driver != null) {
            driver.quit();
        }
    }
}
