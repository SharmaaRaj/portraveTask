package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.asserts.SoftAssert;

import java.util.ArrayList;
import java.util.Collections;
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
    By sortDropDown = By.tagName("select");

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
            System.out.println(itemName+" product details added to the entity");
            productDetails.add(inventoryItemDetails);
        }
        System.out.println("Total number of product is "+productDetails.size());
    }

    public void validateEachProduct() {
        try{
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
            System.out.println("All product information's are matching with product details in Landing page");
            productDetails.clear();
            InventoryItemDetails reset = new InventoryItemDetails();
            driver.manage().deleteAllCookies();
        }
        catch (Throwable e){
            InventoryItemDetails resetEntity = new InventoryItemDetails();
            productDetails.clear();
            driver.manage().deleteAllCookies();
            System.out.println("Failure occured " +e);
            throw e;
        }

    }

    public void sorting(String selectSortingOption){
        WebElement selectDropdown = driver.findElement(sortDropDown);
        Select selectTagDropDown = new Select(selectDropdown);
        selectTagDropDown.selectByVisibleText(selectSortingOption);

        switch (selectSortingOption){
            case "Name (A to Z)" : {
                List<WebElement> productListName = driver.findElements(productName);
                List<String> actualProductName = new ArrayList<>();

                for(WebElement product : productListName){
                    actualProductName.add(product.getText());
                }

                List<String> expectedProductName = new ArrayList<>(actualProductName);
                Collections.sort(expectedProductName);

                softAssert.assertEquals(actualProductName,expectedProductName,actualProductName+" is not matching with "+expectedProductName);
                softAssert.assertAll();
                System.out.println("Sorting 'Name (A to Z)' passed successfully");
                break;
            }

            case "Name (Z to A)" : {
                List<WebElement> productListName = driver.findElements(productName);
                List<String> actualProductName = new ArrayList<>();

                for(WebElement product : productListName){
                    actualProductName.add(product.getText());
                }

                List<String> expectedProductName = new ArrayList<>(actualProductName);
                Collections.sort(expectedProductName,Collections.reverseOrder());

                softAssert.assertEquals(actualProductName,expectedProductName,actualProductName+" is not matching with "+expectedProductName);
                softAssert.assertAll();
                System.out.println("Sorting 'Name (Z to A)' passed successfully");
                break;
            }

            case "Price (low to high)" : {
                List<WebElement> productListPrice = driver.findElements(productPrice);
                List<Double> actualProductPrice = new ArrayList<>();

                for(WebElement productPrice : productListPrice){
                    actualProductPrice.add(Double.valueOf(productPrice.getText().replace("$","").trim()));
                }

                List<Double> expectedProductPrice = new ArrayList<>(actualProductPrice);
                Collections.sort(expectedProductPrice);

                softAssert.assertEquals(actualProductPrice,expectedProductPrice,actualProductPrice+" is not matching with "+expectedProductPrice);
                softAssert.assertAll();
                System.out.println("Sorting 'Price (low to high)' passed successfully");
                break;
            }

            case "Price (high to low)" : {
                List<WebElement> productListPrice = driver.findElements(productPrice);
                List<Double> actualProductPrice = new ArrayList<>();

                for(WebElement productPrice : productListPrice){
                    actualProductPrice.add(Double.valueOf(productPrice.getText().replace("$","").trim()));
                }

                List<Double> expectedProductPrice = new ArrayList<>(actualProductPrice);
                Collections.sort(expectedProductPrice,Collections.reverseOrder());

                softAssert.assertEquals(actualProductPrice,expectedProductPrice,actualProductPrice+" is not matching with "+expectedProductPrice);
                softAssert.assertAll();
                System.out.println("Sorting 'Price (high to low)' passed successfully");
                break;
            }

            default:{
                System.out.println("No Such Sorting option declared");
            }
        }
    }

    public int addProductToCart(List<String> productNameInfo){
        int count = 0;
        List<WebElement> productListName = driver.findElements(productName);
        List<String> actualProductName = new ArrayList<>();
        for(String product : productNameInfo){
            for(WebElement productElement : productListName){
                if(product.equalsIgnoreCase(productElement.getText())){
                    driver.findElement(addToCartBtn).click();
                    count++;
                    break;
                }
            }
        }
        return count;
    }
}
