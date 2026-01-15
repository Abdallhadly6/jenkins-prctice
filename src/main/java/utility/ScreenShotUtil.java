package utility;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.io.FileHandler;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ScreenShotUtil {
    public static void takeScreenShot(WebDriver driver , String screenShotName) throws IOException {
        TakesScreenshot screenshot = (TakesScreenshot) driver;
        File sourceScreenShotPath = screenshot.getScreenshotAs(OutputType.FILE);
        String targetPath = System.getProperty("user.dir") + "/test-reports/" + screenShotName  + ".png";
        File destinationScreenShotPath = new File(targetPath);
        FileHandler.copy(sourceScreenShotPath,destinationScreenShotPath);
        System.out.println("ScreenShot Captured Successfully" + destinationScreenShotPath);
    }

}
