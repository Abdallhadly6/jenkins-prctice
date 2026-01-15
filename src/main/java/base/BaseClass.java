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
        ExtentReportManager.setExtent();  // تهيئة إعدادات التقرير
    }

    @AfterSuite
    public void afterSuite() throws MessagingException {
        ExtentReportManager.endReport();  // إنهاء التقرير وعمل flush
        EmailManager.sendMail();
    }

    @Parameters({"browserType"})
    @BeforeMethod
    public void setUp( String brsType) throws IOException {
        factory = new BrowserFactory();
        driver = factory.getDriver(brsType);
        loginPage = new LoginPage(driver);
    }

    @AfterMethod
    public void tearDown(){
        driver.quit();
    }
}
