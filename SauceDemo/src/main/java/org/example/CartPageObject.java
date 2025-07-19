package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.asserts.SoftAssert;

public class CartPageObject {

    WebDriver driver;
    SoftAssert softAssert = new SoftAssert();

    public CartPageObject(WebDriver driver){
        this.driver = driver;
    }

    //Cart Icon Object details
    By cartIcon = By.id("shopping_cart_container");
    By cartIconProductCount = By.className("shopping_cart_badge");

    public void cartIconProductCount(int expectedCount){
        int actualCount = Integer.parseInt(driver.findElement(cartIconProductCount).getText());
        softAssert.assertEquals(actualCount,expectedCount,actualCount+" is not matched with "+expectedCount);
        softAssert.assertAll();
    }

    public void navigateToCartPage(){
        driver.findElement(cartIcon).click();
    }

}
