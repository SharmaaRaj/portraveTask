package org.example;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import java.util.Arrays;

public class SauceLoginPageTest {

    WebDriver driver;
    WebDriverManager wbDriver;
    SauceLoginPageObject sauceLoginPageObject;
    LandingPageObjects landingPageObjects;
    CartPageObject cartPageObject;
    CheckOutPageObjects checkOutPageObjects;


    @BeforeMethod
    public void setUpConfig(){
        wbDriver = new WebDriverManager();
        driver=wbDriver.returnDriverInfo();

        driver.get("https://www.saucedemo.com/");
        driver.manage().window().maximize();

        sauceLoginPageObject = new SauceLoginPageObject(driver);;
        landingPageObjects = new LandingPageObjects(driver);
        cartPageObject = new CartPageObject(driver);
        checkOutPageObjects = new CheckOutPageObjects(driver);
    }

    @AfterMethod
    public void driverClose(){
        if(driver != null){
            driver.close();
        }
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


    //Scenario - 1 : Authenticate the user with both valid and invalid credentials.

    @DataProvider(name = "loginAuthentication")
    public Object[][] loginData(){
        Object[][] values = {
                {"standard_user","secret_sauce","ValidUser"},
                {"","secret_sauce","WithoutUsername"},
                {"locked_out_user","","WithoutPassword"},
                {"","","WithoutUsernameAndPassword"},
                {"","secret_sauce","ErrorMessageDisappear"},
                {"locked_out_user","secret_sauce","InValidUser"}
        };
        return values;
    }

    @Test(dataProvider = "loginAuthentication",priority = 1)
    public void loginDetails(String UserName, String Password, String evaluationType){
        sauceLoginPageObject.validateUserDetails(UserName,Password,evaluationType);
    }

    /* Scenario - 2 : Verify product sorting functionality:
    Sort [A to Z], Sort [Z to A], Sort [Price: Low to High], Sort [Price: High to Low]
     */

    @Test(dataProvider = "LoginTestData",priority = 4)
    public void validateSorting(String userName, String password){
        sauceLoginPageObject.signInProcess(userName,password);
        landingPageObjects.sorting("Price (low to high)");
        landingPageObjects.sorting("Name (A to Z)");
        landingPageObjects.sorting("Price (high to low)");
        landingPageObjects.sorting("Name (Z to A)");
        sauceLoginPageObject.signOut();
    }

    /* Scenario - 3 : After successful login, validate the landing page and ensure that:
        1. Each product navigates to its correct detail page
        2. Product name, image source, description, and price are displayed correctly
     */

    @Test(dataProvider = "LoginTestData",alwaysRun = true,priority = 2)
    public void landingPageCardNaviagtion(String userName, String password){
        sauceLoginPageObject.signInProcess(userName,password);
        landingPageObjects.inventoryItems();
        landingPageObjects.validateEachProduct();
        sauceLoginPageObject.signOut();
    }


    /*Scenario - 4 : Add single/multiple products to the cart
    Verify the cart icon displays the correct product count
    Then Logout and login with a different user
    Then login again with the original account and verify the cart item count remains the same
     */

    @Test(priority = 3)
    public void cartDetails(){
        sauceLoginPageObject.signInProcess("standard_user","secret_sauce");
        int productCount = landingPageObjects.addProductToCart(Arrays.asList(new String[]{"Sauce Labs Backpack", "Sauce Labs Fleece Jacket"}));
        cartPageObject.cartIconProductCount(productCount);
        sauceLoginPageObject.signOut();
        sauceLoginPageObject.signInProcess("performance_glitch_user","secret_sauce");
        sauceLoginPageObject.signOut();
        sauceLoginPageObject.signInProcess("standard_user","secret_sauce");
        cartPageObject.cartIconProductCount(2);
        sauceLoginPageObject.signOut();
    }


    /*Scenario - 5 :
    1. Add a product to the cart and verify its presence
    2. Remove a specific product and confirm it is removed from the cart page
    3. Validate that the same product can be added again successfully
      */

    @Test(dataProvider = "LoginTestData",alwaysRun = true,priority = 5)
    public void validateCardPageProductDetails(String userName, String password){
        sauceLoginPageObject.signInProcess(userName,password);
        landingPageObjects.addProductToCart(Arrays.asList(new String[]{"Sauce Labs Backpack", "Sauce Labs Fleece Jacket"}));
        cartPageObject.cartIconProductCount(2);
        cartPageObject.navigateToCartPage();
        cartPageObject.validateProductPresent(Arrays.asList(new String[]{"Sauce Labs Backpack", "Sauce Labs Fleece Jacket"}));
        cartPageObject.removeProduct(Arrays.asList(new String[]{"Sauce Labs Fleece Jacket"}));
        cartPageObject.validateProductPresent(Arrays.asList(new String[]{"Sauce Labs Backpack"}));
        cartPageObject.returnToShopptingPage();
        cartPageObject.cartIconProductCount(1);
        landingPageObjects.addProductToCart(Arrays.asList(new String[]{"Sauce Labs Fleece Jacket"}));
        cartPageObject.navigateToCartPage();
        cartPageObject.validateProductPresent(Arrays.asList(new String[]{"Sauce Labs Backpack", "Sauce Labs Fleece Jacket"}));
        sauceLoginPageObject.signOut();
    }

    /*
    Scenario - 6 : End to End Scenario - Positive Flow from Login to Checkout and Order confirmation
     */

    @Test(dataProvider = "LoginTestData",alwaysRun = true,priority = 6)
    public void endToendScenario(String userName, String password){
        sauceLoginPageObject.signInProcess(userName,password);
        landingPageObjects.addProductToCart(Arrays.asList(new String[]{"Sauce Labs Backpack", "Sauce Labs Fleece Jacket"}));
        cartPageObject.cartIconProductCount(2);
        cartPageObject.navigateToCartPage();
        cartPageObject.toCheckOutPage();
        checkOutPageObjects.enterInformation(userName);
        checkOutPageObjects.clickFinish();
        checkOutPageObjects.validateConfirmationMessage();
        sauceLoginPageObject.signOut();
    }

    /*
    Scenario - 7 : validate session information
     */
    @Test(dataProvider = "LoginTestData",alwaysRun = true)
    public void validateUserNameInSession(String userName, String password){
        sauceLoginPageObject.signInProcess(userName,password);
        sauceLoginPageObject.validateSession(userName);
        sauceLoginPageObject.signOut();
    }

    /*
    Scenario - 8 : validate custom getText created method using input and button tagged element
     */
    @Test
    public void validategetText(){
        SoftAssert softAssert = new SoftAssert();
        sauceLoginPageObject.UserName("standard_user");
        sauceLoginPageObject.Password("secret_sauce");

        String actualPageTitleTxt = driver.findElement(sauceLoginPageObject.loginPageTitle).getText();
        String expectedPageTitleTxt = overrideSeleniumGetText(driver.findElement(sauceLoginPageObject.loginPageTitle));
        String actualTextUserName = driver.findElement(sauceLoginPageObject.usernameElement).getText();
        String expectedTextUserName = overrideSeleniumGetText(driver.findElement(sauceLoginPageObject.usernameElement));
        String actualTextPassword = driver.findElement(sauceLoginPageObject.passwordElement).getText();
        String expectedTextPassword = overrideSeleniumGetText(driver.findElement(sauceLoginPageObject.passwordElement));
        String actualTextLoginBtn = driver.findElement(sauceLoginPageObject.loginElement).getText();
        String expectedTextLoginBtn = overrideSeleniumGetText(driver.findElement(sauceLoginPageObject.loginElement));

        System.out.println("Selenium '.getText' output for Page Title is "+actualPageTitleTxt);
        System.out.println("Custom method 'getText' output for Page Title field is "+expectedPageTitleTxt);
        System.out.println("Selenium '.getText' output for UserName textBox input field is "+actualTextUserName);
        System.out.println("Custom method 'getText' output for UserName textBox input field is "+expectedTextUserName);
        System.out.println("Selenium '.getText' output for Password textBox input field is "+actualTextPassword);
        System.out.println("Custom method 'getText' output for Password textBox input field is "+expectedTextPassword);
        System.out.println("Selenium '.getText' output for Login button field is "+actualTextLoginBtn);
        System.out.println("Custom method 'getText' output for Login button field is "+expectedTextLoginBtn);
    }

    public String overrideSeleniumGetText(WebElement elementDetails){
        String text = elementDetails.getText().trim();
        if(text.isEmpty()){
           text = elementDetails.getAttribute("value").trim();
        }
        return text;
    }
}
