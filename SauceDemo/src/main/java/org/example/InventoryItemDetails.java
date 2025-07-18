package org.example;

import lombok.Data;


@Data
//Lombok plugin used to provide getter and setter
public class InventoryItemDetails {

    private String itemName;
    private String itemDescription;
    private String imageSourceUrl;
    private String itemPrice;
}
