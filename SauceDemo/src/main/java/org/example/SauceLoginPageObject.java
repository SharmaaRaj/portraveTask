package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.asserts.SoftAssert;

import java.time.Duration;
import java.util.List;

public class SauceLoginPageObject {

    WebDriver driver;
    SoftAssert softAssert = new SoftAssert();

    public SauceLoginPageObject(WebDriver driver) {
        this.driver = driver;
    }

    public void UserName(String userName) {
        WebElement loginUserName = driver.findElement(By.id("user-name"));
        loginUserName.clear();
        loginUserName.sendKeys(userName);
        System.out.println("In Login Page User name entered as " + userName);
    }

    public void Password(String password) {
        WebElement loginPassword = driver.findElement(By.id("password"));
        loginPassword.clear();
        loginPassword.sendKeys(password);
        System.out.println("In Login Page password entered as " + password);
    }

    public void loginButton() {
        WebElement loginButton = driver.findElement(By.id("login-button"));
        loginButton.click();
        System.out.println("Login Successfully");
    }

    public void errorMessages(String expectedErrorMessage) {
        WebElement errorMessage = driver.findElement(By.xpath("//div[@class='error-message-container error']/h3"));
        if (errorMessage.isDisplayed()) {
            String actualErrorMessage = errorMessage.getText().replace("Epic sadface: ", "");
            System.out.println("In Login Page error message get displayed as " + '"' + actualErrorMessage + '"');
            softAssert.assertTrue(actualErrorMessage.equalsIgnoreCase(expectedErrorMessage));
            softAssert.assertAll();
        }
    }

    public void errorMessageCloseIcon() {
        List<WebElement> errorMessage = driver.findElements(By.xpath("//div[@class='error-message-container']"));
        WebElement errorMessageCloseIcon = driver.findElement(By.className("error-button"));
        errorMessageCloseIcon.click();
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.invisibilityOfAllElements(errorMessage));
        softAssert.assertTrue(errorMessage.isEmpty() , "Error message still get displayed in Login Page");
        softAssert.assertAll();
    }

    public void signOut() {
        WebElement burgerMenubtn = driver.findElement(By.id("react-burger-menu-btn"));
        WebElement logOut = driver.findElement(By.id("logout_sidebar_link"));
        burgerMenubtn.click();
        logOut.click();
        driver.manage().deleteAllCookies();
        System.out.println("Logged out Successfully");
        System.out.println("------------------------------------------------------------");
    }

    public void signInProcess(String UserNameInput, String PasswordInput){
        driver.navigate().refresh();
        UserName(UserNameInput);
        Password(PasswordInput);
        loginButton();
    }

    public void validateUserDetails(String UserNameInput, String PasswordInput, String evaluateUserInfo) {
        switch (evaluateUserInfo.toLowerCase()) {
            case "validuser": {
                UserName(UserNameInput);
                Password(PasswordInput);
                loginButton();
                signOut();
                break;
            }
            case "invaliduser": {
                driver.navigate().refresh();
                UserName(UserNameInput);
                Password(PasswordInput);
                loginButton();
                errorMessages("Sorry, this user has been locked out.");
                errorMessageCloseIcon();
                break;
            }
            case "withoutusername":
            case "withoutusernameandpassword": {
                driver.navigate().refresh();
                UserName(UserNameInput);
                Password(PasswordInput);
                loginButton();
                errorMessages("Username is required");
                break;
            }
            case "withoutpassword": {
                driver.navigate().refresh();
                UserName(UserNameInput);
                Password(PasswordInput);
                loginButton();
                errorMessages("Password is required");
                break;
            }
            case "errormessagedisappear": {
                driver.navigate().refresh();
                UserName(UserNameInput);
                Password(PasswordInput);
                loginButton();
                errorMessages("Username is required");
                errorMessageCloseIcon();
                break;
            }
            default:
                System.out.println("Invalid input " + evaluateUserInfo);
        }

        //line breaker for readability in console log
        System.out.println("-------------------------------------------------");
    }

    public void browerClose(){
        driver.close();
    }
}
