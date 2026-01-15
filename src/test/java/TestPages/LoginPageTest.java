package TestPages;

import org.testng.Assert;
import org.testng.annotations.Test;
import base.BaseClass;

import java.io.IOException;

public class LoginPageTest extends BaseClass {


    @Test
    public void loginSuccess() throws IOException {
        loginPage.login("Test2@hotmail.com" , "Test@123");
        Assert.assertTrue(loginPage.loginNotExist());
    }

    @Test
    public void loginFail() throws IOException {
        loginPage.login("Test2@hotmail.com" , "Test@12");
        loginPage.acceptAlert();
        Assert.assertTrue(loginPage.loginExist());
    }
}
