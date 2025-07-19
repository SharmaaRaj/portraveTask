package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.asserts.SoftAssert;

public class CheckOutPageObjects {

    WebDriver driver;
    SoftAssert softAssert;

    public CheckOutPageObjects(WebDriver driver) {
        this.driver = driver;
    }

    //check out page object
    By firstNametxt = By.id("first-name");
    By lastNametxt = By.id("last-name");
    By postalCodetxt = By.id("postal-code");
    By cancelBtn = By.id("cancel");
    By continueBtn = By.id("continue");


    //checkout page overview object
    By finishBtn = By.id("finish");

    //check out complete page object
     By titleTxt = By.className("complete-header");
     By decriptionTxt = By.className("complete-text");
     By homeBtn = By.id("back-to-products");


    public void enterInformation(String userName){
        WebElement firstNameTxt = driver.findElement(firstNametxt);
        WebElement lastNameTxt = driver.findElement(lastNametxt);
        WebElement postalCodeTxt = driver.findElement(postalCodetxt);
        WebElement continueBtnn = driver.findElement(continueBtn);

        String[] userNameDetail = userName.split("_");
        String firstName = userNameDetail[0].trim();
        String lastName = userNameDetail[0].trim();

        firstNameTxt.sendKeys(firstName);
        lastNameTxt.sendKeys(lastName);
        postalCodeTxt.sendKeys("6009899");
        continueBtnn.click();
        System.out.println("All details updated in check out page and navigated to check out Overview page");
    }

    public void clickFinish(){
        WebElement finishbtn = driver.findElement(finishBtn);
        finishbtn.click();
        System.out.println("Navigated to check out complete page");
    }

    public void validateConfirmationMessage(){
        String confirmationTitle = driver.findElement(titleTxt).getText();
        String confirmationDescription = driver.findElement(decriptionTxt).getText();
        softAssert = new SoftAssert();
        softAssert.assertEquals(confirmationTitle.trim(),"Thank you for your order!");
        softAssert.assertEquals(confirmationDescription.trim(),"Your order has been dispatched, and will arrive just as fast as the pony can get there!");
        softAssert.assertAll();
        System.out.println("Order Placed and got confirmation message as "+confirmationTitle+"\n"+confirmationDescription);
    }

}
