package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.asserts.SoftAssert;

import java.time.Duration;
import java.util.List;

public class CartPageObject {

    WebDriver driver;
    SoftAssert softAssert;

    public CartPageObject(WebDriver driver) {
        this.driver = driver;
    }

    //Cart Icon Object details
    By cartIcon = By.id("shopping_cart_container");
    By cartIconProductCount = By.className("shopping_cart_badge");
    By continueShopping = By.id("continue-shopping");
    By checkout = By.id("checkout");

    public void cartIconProductCount(int expectedCount) {
        softAssert = new SoftAssert();
        WebElement cartIconCout = driver.findElement(cartIconProductCount);
        WebElement cartIconCount = new WebDriverWait(driver,Duration.ofSeconds(10)).pollingEvery(Duration.ofMillis(200)).until(ExpectedConditions.visibilityOf(cartIconCout));
        int actualCount = Integer.parseInt(cartIconCount.getText());
        softAssert.assertEquals(actualCount, expectedCount, actualCount + " is not matched with " + expectedCount);
        softAssert.assertAll();
    }

    public void navigateToCartPage() {
        driver.findElement(cartIcon).click();
        System.out.println("Navigated to cart page from landing page");
    }

    public void validateProductPresent(List<String> expectedProductInfo) {

        softAssert = new SoftAssert();
        for (String product : expectedProductInfo) {
            WebElement actualProduct = driver.findElement(By.xpath("//div[@class='cart_item'] //*[contains(text(),'" + product + "')]"));

            //explicit wait implementation
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
            wait.pollingEvery(Duration.ofMillis(500)).until(ExpectedConditions.visibilityOf(actualProduct));

            String actualProductName = actualProduct.getText();
            softAssert.assertEquals(actualProductName, product);

        }
    }

    public void removeProduct(List<String> expectedProductInfo) {

        for (String product : expectedProductInfo) {
            WebElement actualProduct = driver.findElement(By.xpath("//*[contains(text(),'" + product + "')]/ancestor::div[@class='cart_item']  //button"));
            actualProduct.click();
            System.out.println(product+" removed from cart page");
        }

    }

    public void returnToShopptingPage() {
        WebElement continueShoppingbtn = driver.findElement(continueShopping);
        continueShoppingbtn.click();
        System.out.println("Navigated to landing page from cart page");
    }

    public void toCheckOutPage() {
        WebElement checkoutBtn = driver.findElement(checkout);
        checkoutBtn.click();
        System.out.println("Navigated to check out page from cart page");
    }
}
