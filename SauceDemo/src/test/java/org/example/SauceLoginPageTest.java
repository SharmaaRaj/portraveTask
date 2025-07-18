package org.example;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Arrays;

public class SauceLoginPageTest {

    WebDriver driver;
    WebDriverManager wbDriver = new WebDriverManager();
    SauceLoginPageObject sauceLoginPageObject;
    LandingPageObjects landingPageObjects;
    CartPageObject cartPageObject;

    public SauceLoginPageTest(){
        this.driver=wbDriver.returnDriverInfo();
        sauceLoginPageObject = new SauceLoginPageObject(driver);;
        landingPageObjects = new LandingPageObjects(driver);
        cartPageObject = new CartPageObject(driver);
    }



    @DataProvider(name = "LoginTestData")
    public Object[][] loginDetails(){
        Object[][] loginTestData = {
                {"standard_user","secret_sauce"},
                {"performance_glitch_user","secret_sauce"},
                {"error_user","secret_sauce"},
                {"visual_user","secret_sauce"},
                {"problem_user","secret_sauce"}
        };
        return loginTestData;
    }

    @Test(priority = 1)
    public void launchUrl(){
        driver.get("https://www.saucedemo.com/");
        driver.manage().window().maximize();
    }


    //Scenario - 1 : Authenticate user with Valid and Invalid credentials
    //ExpectedResult -> script should be passed only for "standard_user"

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

    @Test(dataProvider = "loginAuthentication",dependsOnMethods = {"launchUrl"})
    public void loginDetails(String UserName, String Password, String evaluationType){
        sauceLoginPageObject.validateUserDetails(UserName,Password,evaluationType);
    }


    /* Scenario - 2 : Check Landing page displayed after valid login
        and validate each item is navigating to its corresponding page details
        then validate product name, image src, product description and product price

        ExpectedResult -> script should be passed only for "standard_user and performance_glitch_user"
     */

    @Test(dataProvider = "LoginTestData",dependsOnMethods ={"launchUrl"},alwaysRun = true)
    public void landingPageCardNaviagtion(String userName, String password){
        sauceLoginPageObject.signInProcess(userName,password);
        landingPageObjects.inventoryItems();
        landingPageObjects.validateEachProduct();
        sauceLoginPageObject.signOut();
    }

    /* Scenario - 3 : Sort [A-Z], Sort [Z-A], Sort [Price Low - High] and Sort [Price High - Low]
        ExpectedResult -> script should be passed only for "standard_user and performance_glitch_user"
     */
    @Test(priority = 2,dependsOnMethods = {"launchUrl"},dataProvider = "LoginTestData")
    public void validateSorting(String userName, String password){
        sauceLoginPageObject.signInProcess(userName,password);
        landingPageObjects.sorting("Price (low to high)");
        landingPageObjects.sorting("Name (A to Z)");
        landingPageObjects.sorting("Price (high to low)");
        landingPageObjects.sorting("Name (Z to A)");
        sauceLoginPageObject.signOut();
    }


    /*Scenario - 4 : Add single/multiple product to cart, then validate number value in cart icon
    and validate all added products are displayed in cart part
     */

    @Test(dependsOnMethods = {"launchUrl"})
    public void cartDetails(){
        sauceLoginPageObject.signInProcess("standard_user","secret_sauce");
       int productCount = landingPageObjects.addProductToCart(Arrays.asList(new String[]{"Sauce Labs Backpack", "Sauce Labs Fleece Jacket"}));
        cartPageObject.cartIconProductCount(productCount);
    }

}
