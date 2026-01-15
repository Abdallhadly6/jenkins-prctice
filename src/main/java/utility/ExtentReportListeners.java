package utility;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import base.BaseClass;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExtentReportListeners extends ExtentReportManager implements ITestListener {

    private String captureScreenshot(WebDriver driver, String methodName) {
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss-SSS").format(new Date());
        String screenshotName = methodName + timeStamp;
        try {
            ScreenShotUtil.takeScreenShot(driver, screenshotName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return screenshotName;
    }

    private WebDriver getDriverFromResult(ITestResult result) {
        Object currentClass = result.getInstance();
        return ((BaseClass) currentClass).getDriver();
    }


    @Override
    public void onTestStart(ITestResult result) {
        test = extent.createTest(result.getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        WebDriver driver = getDriverFromResult(result);
        String screenshotName = captureScreenshot(driver, result.getName());
        if (result.getStatus() == ITestResult.SUCCESS) {
            test.log(Status.PASS, "Pass Test case is: " + result.getName());
            test.log(Status.PASS , "<a href = '"+System.getProperty("user.dir") + "/test-reports/" + screenshotName + ".png"+ "'><span class = 'lable info'> SnapShot</span></a>");

        }
    }

    @Override
    public void onTestFailure(ITestResult result) {
        WebDriver driver = getDriverFromResult(result);
        String screenshotName = captureScreenshot(driver, result.getName());
        if (result.getStatus() == ITestResult.FAILURE) {
            test.log(Status.FAIL,
                    MarkupHelper.createLabel(result.getName() + " - Test Case Failed", ExtentColor.RED));
            test.log(Status.FAIL,
                    MarkupHelper.createLabel(result.getThrowable() + " - Test Case Failed", ExtentColor.RED));
            test.log(Status.FAIL , "<a href = '"+System.getProperty("user.dir") + "/test-reports/" + screenshotName + ".png"+ "'><span class = 'lable info'> SnapShot</span></a>");

        }

    }

    @Override
    public void onTestSkipped(ITestResult result) {
        WebDriver driver = getDriverFromResult(result);
        String screenshotName = captureScreenshot(driver, result.getName());
        if (result.getStatus() == ITestResult.SKIP) {
            test.log(Status.SKIP, "Skipped Test case is: " + result.getName());
            test.log(Status.SKIP,
                    MarkupHelper.createLabel(result.getThrowable() + " - Test Case Skipped", ExtentColor.RED));
            test.log(Status.SKIP , "<a href = '"+System.getProperty("user.dir") + "/test-reports/" + screenshotName + ".png"+ "'><span class = 'lable info'> SnapShot</span></a>");

        }
    }

    @Override
    public void onStart(ITestContext context) {
        System.out.println("Test Suite started!");
    }

    @Override
    public void onFinish(ITestContext context) {
        System.out.println("Test Suite Finished!");
    }
}

