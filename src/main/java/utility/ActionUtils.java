package utility;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;
import org.openqa.selenium.logging.LogEntry;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.*;
import java.util.List;

import org.testng.asserts.SoftAssert;

/**
 * ActionUtils
 * ============
 * A one-stop utility class for ALL possible Selenium actions.
 * Covers: Basic, Advanced, Browser, File, Visual, Network, Utilities.
 *
 * Usage:
 * - Instantiate once per test/page: new ActionUtils(driver, 10)
 * - Use methods directly without rewriting actions in Page Objects
 */
public class ActionUtils {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final Actions actions;
    private final JavascriptExecutor js;
    private final SoftAssert softAssert;

    public ActionUtils(WebDriver driver, int waitTime) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(waitTime));
        this.actions = new Actions(driver);
        this.js = (JavascriptExecutor) driver;
        this.softAssert = new SoftAssert();
    }

    // ============================================================
    // 1. BASIC ELEMENT ACTIONS
    // ============================================================

    /** Click on element when clickable */
    public void click(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    /** Clear text and type new text */
    public void type(By locator, String text) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        el.clear();
        el.sendKeys(text);
    }

    /** Get visible text of element */
    public String getText(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).getText();
    }

    /** Get attribute value of element */
    public String getAttribute(By locator, String attribute) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).getAttribute(attribute);
    }

    /** Get CSS property value of element */
    public String getCssValue(By locator, String css) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).getCssValue(css);
    }

    /** Check if element is displayed */
    public boolean isDisplayed(By locator) {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    /** Check if element is Not displayed */
    public boolean isNotDisplayed(By locator) {
        try {
            return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean isElementClickable(By locator) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(locator));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean isElementEnabled(By locator) {
        try {
            return wait.until(ExpectedConditions.elementToBeClickable(locator)).isEnabled();
        } catch (Exception e) {
            return false;
        }
    }



    // ============================================================
    // 2. CHECKBOXES & RADIOS
    // ============================================================

    /** Select/Deselect checkbox depending on desired state */
    public void selectCheckbox(By locator, boolean shouldSelect) {
        WebElement checkbox = wait.until(ExpectedConditions.elementToBeClickable(locator));
        if (checkbox.isSelected() != shouldSelect) {
            checkbox.click();
        }
    }

    /** Select a radio button if not already selected */
    public void selectRadio(By locator) {
        WebElement radio = wait.until(ExpectedConditions.elementToBeClickable(locator));
        if (!radio.isSelected()) {
            radio.click();
        }
    }

    // ============================================================
    // 3. MOUSE & KEYBOARD
    // ============================================================

    /** Hover mouse over element */
    public void hover(By locator) {
        actions.moveToElement(wait.until(ExpectedConditions.visibilityOfElementLocated(locator))).perform();
    }

    /** Double-click element */
    public void doubleClick(By locator) {
        actions.doubleClick(wait.until(ExpectedConditions.elementToBeClickable(locator))).perform();
    }

    /** Right-click (context click) element */
    public void rightClick(By locator) {
        actions.contextClick(wait.until(ExpectedConditions.elementToBeClickable(locator))).perform();
    }

    /** Drag element from source to target */
    public void dragAndDrop(By source, By target) {
        actions.dragAndDrop(
                wait.until(ExpectedConditions.visibilityOfElementLocated(source)),
                wait.until(ExpectedConditions.visibilityOfElementLocated(target))
        ).perform();
    }

    /** Drag slider horizontally by xOffset */
    public void dragSlider(By locator, int xOffset) {
        WebElement slider = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        actions.dragAndDropBy(slider, xOffset, 0).perform();
    }

    /** Press a keyboard key (e.g., ENTER, ESCAPE) */
    public void pressKey(Keys key) {
        actions.sendKeys(key).perform();
    }

    // ============================================================
    // 4. DROPDOWNS
    // ============================================================

    /** Select dropdown option by visible text */
    public void selectByText(By locator, String text) {
        new Select(wait.until(ExpectedConditions.visibilityOfElementLocated(locator))).selectByVisibleText(text);
    }

    /** Select dropdown option by value attribute */
    public void selectByValue(By locator, String value) {
        new Select(wait.until(ExpectedConditions.visibilityOfElementLocated(locator))).selectByValue(value);
    }

    /** Select dropdown option by index */
    public void selectByIndex(By locator, int index) {
        new Select(wait.until(ExpectedConditions.visibilityOfElementLocated(locator))).selectByIndex(index);
    }

    // ============================================================
    // 5. FRAMES & WINDOWS
    // ============================================================

    /** Switch to an iframe */
    public void switchToFrame(By locator) {
        driver.switchTo().frame(wait.until(ExpectedConditions.visibilityOfElementLocated(locator)));
    }

    /** Switch to nested frame (frame inside another frame) */
    public void switchToFrameInsideFrame(By parent, By child) {
        driver.switchTo().frame(wait.until(ExpectedConditions.visibilityOfElementLocated(parent)))
                .switchTo().frame(wait.until(ExpectedConditions.visibilityOfElementLocated(child)));
    }

    /** Switch back to main content (exit iframe) */
    public void switchToDefaultContent() {
        driver.switchTo().defaultContent();
    }

    /** Switch to window/tab by title */
    public void switchToWindowByTitle(String title) {
        for (String handle : driver.getWindowHandles()) {
            driver.switchTo().window(handle);
            if (driver.getTitle().equals(title)) {
                break;
            }
        }
    }

    /** Open new tab and switch focus */
    public void openNewTab(String url) {
        js.executeScript("window.open(arguments[0], '_blank');", url);
        ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(tabs.size() - 1));
    }

    // ============================================================
    // 6. ALERTS
    // ============================================================

    /** Accept JavaScript alert */
    public void acceptAlert() {
        wait.until(ExpectedConditions.alertIsPresent()).accept();
    }

    /** Dismiss JavaScript alert */
    public void dismissAlert() {
        wait.until(ExpectedConditions.alertIsPresent()).dismiss();
    }

    /** Get text from alert */
    public String getAlertText() {
        return wait.until(ExpectedConditions.alertIsPresent()).getText();
    }

    /** Send text into alert input */
    public void sendKeysToAlert(String text) {
        wait.until(ExpectedConditions.alertIsPresent()).sendKeys(text);
    }

    // ============================================================
    // 7. JAVASCRIPT HELPERS
    // ============================================================

    /** Click element using JavaScript */
    public void jsClick(By locator) {
        js.executeScript("arguments[0].click();", wait.until(ExpectedConditions.elementToBeClickable(locator)));
    }

    /** Highlight element with red border (debugging) */
    public void highlight(By locator) {
        js.executeScript("arguments[0].style.border='3px solid red'", wait.until(ExpectedConditions.visibilityOfElementLocated(locator)));
    }

    /** Scroll element into view */
    public void scrollIntoView(By locator) {
        js.executeScript("arguments[0].scrollIntoView(true);", wait.until(ExpectedConditions.visibilityOfElementLocated(locator)));
    }

    /** Wait until page DOM is fully loaded */
    public void waitForPageLoad() {
        wait.until(webDriver -> js.executeScript("return document.readyState").equals("complete"));
    }

    // ============================================================
    // 8. FILE HANDLING
    // ============================================================

    /** Upload file via input[type="file"] */
    public void uploadFile(By locator, String filePath) {
        wait.until(ExpectedConditions.presenceOfElementLocated(locator)).sendKeys(filePath);
    }

    /** Upload file using Robot (for native OS dialogs) */
    public void uploadWithRobot(String filePath) throws AWTException {
        StringSelection selection = new StringSelection(filePath);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
        Robot robot = new Robot();
        robot.delay(500);
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
    }

    /** Check if file is downloaded into directory */
    public boolean isFileDownloaded(String downloadDir, String fileName) {
        File dir = new File(downloadDir);
        File[] dirContents = dir.listFiles();
        if (dirContents != null) {
            for (File file : dirContents) {
                if (file.getName().equals(fileName)) {
                    return true;
                }
            }
        }
        return false;
    }

    // ============================================================
    // 9. TABLES & LISTS
    // ============================================================

    /** Get number of rows in a table */
    public int getTableRowCount(By locator) {
        return driver.findElements(locator).size();
    }

    /** Get text value of a cell (row, col) */
    public String getCellValue(By tableLocator, int row, int col) {
        return driver.findElement(By.xpath(tableLocator + "//tr[" + row + "]/td[" + col + "]")).getText();
    }

    /** Select value from dynamic list/autosuggestion */
    public void selectFromDynamicList(By locator, String value) {
        List<WebElement> options = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
        for (WebElement option : options) {
            if (option.getText().equalsIgnoreCase(value)) {
                option.click();
                break;
            }
        }
    }

    // ============================================================
    // 10. BROWSER LEVEL
    // ============================================================

    /** Clear all browser cookies */
    public void clearCookies() {
        driver.manage().deleteAllCookies();
    }

    /** Clear browser localStorage */
    public void clearLocalStorage() {
        js.executeScript("window.localStorage.clear();");
    }

    /** Clear browser sessionStorage */
    public void clearSessionStorage() {
        js.executeScript("window.sessionStorage.clear();");
    }

    // ============================================================
    // 11. LOGS & NETWORK
    // ============================================================

    /** Get browser console logs */
    public List<LogEntry> getBrowserLogs() {
        return driver.manage().logs().get("browser").getAll();
    }

    // Note: Full network interception/mocking requires Selenium DevTools API

    // ============================================================
    // 12. VISUAL TESTING
    // ============================================================

    /** Take screenshot of a specific element */
    public void takeElementScreenshot(By locator, String filePath) throws IOException {
        File src = wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).getScreenshotAs(OutputType.FILE);
        File dest = new File(filePath);
        org.openqa.selenium.io.FileHandler.copy(src, dest);
    }

    /** Take full page screenshot */
    public void takeFullScreenshot(String filePath) throws IOException {
        File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        File dest = new File(filePath);
        org.openqa.selenium.io.FileHandler.copy(src, dest);
    }

    // ============================================================
    // 13. SHADOW DOM
    // ============================================================

    /** Access element inside Shadow DOM */
    public WebElement getShadowElement(By host, String shadowCss) {
        WebElement shadowHost = wait.until(ExpectedConditions.visibilityOfElementLocated(host));
        SearchContext shadowRoot = (SearchContext) js.executeScript("return arguments[0].shadowRoot", shadowHost);
        return shadowRoot.findElement(By.cssSelector(shadowCss));
    }

    // ============================================================
    // 14. UTILITIES
    // ============================================================

    /** Generate random email */
    public String generateRandomEmail() {
        return "user" + new Random().nextInt(10000) + "@test.com";
    }

    /** Generate random string of given length */
    public String generateRandomString(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    /** Select a date in input field (yyyy-mm-dd format) */
    public void selectDate(By dateField, String date) {
        WebElement el = wait.until(ExpectedConditions.elementToBeClickable(dateField));
        js.executeScript("arguments[0].value=arguments[1];", el, date);
    }

    // ============================================================
    // 15. ASSERTIONS & RETRY
    // ============================================================

    /** Soft assertion equals */
    public void softAssertEquals(String actual, String expected, String message) {
        softAssert.assertEquals(actual, expected, message);
    }

    /** Run all collected soft assertions */
    public void assertAll() {
        softAssert.assertAll();
    }

    /** Retry mechanism for unstable elements */
    public WebElement retryingFind(By locator, int attempts, int delaySec) {
        WebElement element = null;
        for (int i = 0; i < attempts; i++) {
            try {
                element = driver.findElement(locator);
                break;
            } catch (Exception e) {
                try {
                    Thread.sleep(delaySec * 1000L);
                } catch (InterruptedException ignored) {}
            }
        }
        return element;
    }
}
