package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.asserts.SoftAssert;

import java.util.ArrayList;
import java.util.List;

public class LandingPageObjects {
    WebDriver driver;

    public LandingPageObjects(WebDriver driver) {
        this.driver = driver;
    }

    SoftAssert softAssert = new SoftAssert();

    List<InventoryItemDetails> productDetails = new ArrayList<>();

    //All products information in Landing Page

    By inventoryItemLists = By.xpath("//div[@class='inventory_item']");
    By imageSrc = By.xpath("//img[@class='inventory_item_img']");
    By productName = By.className("inventory_item_name");
    By productDescription = By.xpath("//div[@class='inventory_item_desc']");
    By productPrice = By.xpath("//div[@class='inventory_item_price']");
    By addToCartBtn = By.xpath("//button[contains(@class,'btn_inventory')]");

    //Detail product information page
    By productImageSrc = By.xpath("//*[@class='inventory_details_container'] //img");
    By productTitle = By.xpath("//*[@class='inventory_details_container'] //div[@data-test='inventory-item-name']");
    By productDesc = By.xpath("//*[@class='inventory_details_container'] //div[@data-test='inventory-item-desc']");
    By productCost = By.xpath("//*[@class='inventory_details_container'] //div[@data-test='inventory-item-price']");
    By backToProducts = By.id("back-to-products");


    public void inventoryItems() {
        List<WebElement> inventoryItemList = driver.findElements(inventoryItemLists);
        for (int i = 0; i < inventoryItemList.size(); i++) {
            InventoryItemDetails inventoryItemDetails = new InventoryItemDetails();
            String imageSourceUrl = driver.findElements(imageSrc).get(i).getAttribute("src");
            String itemName = driver.findElements(productName).get(i).getText();
            String itemDescription = driver.findElements(productDescription).get(i).getText();
            String itemPrice = driver.findElements(productPrice).get(i).getText();

            //set item information in inventory item class file
            inventoryItemDetails.setImageSourceUrl(imageSourceUrl);
            inventoryItemDetails.setItemName(itemName);
            inventoryItemDetails.setItemDescription(itemDescription);
            inventoryItemDetails.setItemPrice(itemPrice);
            productDetails.add(inventoryItemDetails);
        }
        System.out.println("---------------------------------------------------------------");
    }

    public void validateEachProduct() {
        for (InventoryItemDetails product : productDetails) {

            System.out.println("Product name selected is " + product.getItemName());

            WebElement productTitleInfo = driver.findElement(By.xpath("//div[contains(text(),'" + product.getItemName() + "')]"));
            productTitleInfo.click();
            softAssert.assertEquals(driver.findElement(productImageSrc).getAttribute("src"), product.getImageSourceUrl());
            softAssert.assertEquals(driver.findElement(productTitle).getText(), (product.getItemName()));
            softAssert.assertEquals(driver.findElement(productDesc).getText(), product.getItemDescription());
            softAssert.assertEquals(driver.findElement(productCost).getText(), product.getItemPrice());
            softAssert.assertAll();
            driver.findElement(backToProducts).click();
        }
        System.out.println("---------------------------------------------------------------");
    }
}
