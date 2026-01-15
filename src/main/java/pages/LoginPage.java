package pages;

import org.openqa.selenium.*;
import utility.ActionUtils;

public class LoginPage {
    WebDriver driver;
    private final ActionUtils actions;
    public LoginPage(WebDriver driver){
        this.driver = driver;
        actions = new ActionUtils(driver , 10);
    }

    By email = By.name("uemail");
    By password = By.name("upassword");
    By loginButton = By.xpath("/html/body/app-root/div/main/app-login/div[2]/div[2]/div/form/button");

    public void login(String Email , String uPassword){
        actions.type(email,Email);
        actions.type(password , uPassword);
        actions.click(loginButton);
    }


    public boolean loginExist() {
        return actions.isDisplayed(loginButton);
    }

    public boolean loginNotExist() {
       return actions.isNotDisplayed(loginButton);
    }


//    public boolean loginNotExist2() {
//        Wait<WebDriver> wait = new FluentWait<>(driver)
//                .withTimeout(Duration.ofSeconds(10))
//                .pollingEvery(Duration.ofMillis(500))
//                .ignoring(NoSuchElementException.class)
//                .ignoring(StaleElementReferenceException.class); // تجاهل الاستال إليمينت
//
//        boolean isGone = wait.until(driver -> {
//            try {
//                WebElement el = driver.findElement(loginButton); // ابحث عن العنصر في كل مرة (جديد)
//                return !el.isDisplayed();
//            } catch (NoSuchElementException | StaleElementReferenceException e) {
//                return true; // إذا اختفى أو العنصر غير موجود فعلاً
//            }
//        });
//
//        return isGone;
//    }


    public void acceptAlert() {
        System.out.println(actions.getAlertText());
        actions.acceptAlert();
    }

}
