package utility;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {

    private int count = 0;
    private static final int MAX_RETRIES = 1;

    @Override
    public boolean retry(ITestResult result) {
        if (count < MAX_RETRIES) {
            count++;
            return true; // عيد تشغيل التيست
        }
        return false; // متعيدش
    }
}
