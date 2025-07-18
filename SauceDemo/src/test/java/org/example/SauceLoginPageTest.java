package org.example;

import net.bytebuddy.build.Plugin;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class SauceLoginPageTest {

    WebDriver driver;
    WebDriverManager wbDriver = new WebDriverManager();
    SauceLoginPageObject sauceLoginPageObject;
    LandingPageObjects landingPageObjects;

    public SauceLoginPageTest(){
        this.driver=wbDriver.returnDriverInfo();
        sauceLoginPageObject = new SauceLoginPageObject(driver);;
        landingPageObjects = new LandingPageObjects(driver);

    }

    @Test(priority = 1)
    public void launchUrl(){
        driver.get("https://www.saucedemo.com/");
        driver.manage().window().maximize();
    }


    //Scenario - 1 : Authenticate user with Valid and Invalid credentials
    @Test(dataProvider = "loginAuthentication",dependsOnMethods = {"launchUrl"})
    public void loginDetails(String UserName, String Password, String evaluationType){
        sauceLoginPageObject.validateUserDetails(UserName,Password,evaluationType);

    }

    @DataProvider(name = "loginAuthentication")
    public Object[][] loginData(){
        Object[][] values = {
                {"standard_user","secret_sauce","ValidUser"},
                {"locked_out_user","secret_sauce","InValidUser"},
                {"","secret_sauce","WithoutUsername"},
                {"locked_out_user","","WithoutPassword"},
                {"","","WithoutUsernameAndPassword"},
                {"","secret_sauce","ErrorMessageDisappear"}
        };
        return values;
    }

    @DataProvider(name = "LoginTestData")
    public Object[][] loginDetails(){
        Object[][] loginTestData = {
                {"standard_user","secret_sauce"},
                {"problem_user","secret_sauce"},
                {"performance_glitch_user","secret_sauce"},
                {"error_user","secret_sauce"},
                {"visual_user","secret_sauce"},
        };
        return loginTestData;
    }

    //Scenario - 2 : Check Landing page displayed after valid login
    // and validate each item is navigating to its corresponding page details

    @Test(dataProvider = "LoginTestData",alwaysRun = true)
    public void landingPageCardNaviagtion(String userName, String password){
        launchUrl();
        sauceLoginPageObject.signInProcess(userName,password);
        landingPageObjects.inventoryItems();
        landingPageObjects.validateEachProduct();
        sauceLoginPageObject.signOut();

    }
}
