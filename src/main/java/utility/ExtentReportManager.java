package utility;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ExtentReportManager {

    public static ExtentSparkReporter sparkReporter;
    public static ExtentReports extent;
    public static ExtentTest test;
    static String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());

    public static void setExtent() {
        String reportName = "Test-Execution-Report-" + timestamp + ".html";
        sparkReporter = new ExtentSparkReporter(System.getProperty("user.dir") + "/test-reports/ExtentReport/" + reportName);

        // Title of report
        sparkReporter.config().setDocumentTitle("Test Orange HRM Page");

        // Configure theme and appearance
        sparkReporter.config().setTheme(Theme.DARK);
        sparkReporter.config().setReportName("Selenium Automation Test Execution Result");
        sparkReporter.config().setTimeStampFormat("EEEE, MMMM dd, yyyy, hh:mm:ss a '('zzz')'");

        extent = new ExtentReports();

        // Setting Up Environments
        extent.setSystemInfo("System", "Window 10");
        extent.setSystemInfo("Author", "Abdallh adly");
        extent.setSystemInfo("Build No", "1.0");
        extent.setSystemInfo("Environment", "QA");
        extent.setSystemInfo("Application", "Needai");

        extent.attachReporter(sparkReporter);
    }

    public static void endReport() {
        extent.flush();
    }
}
