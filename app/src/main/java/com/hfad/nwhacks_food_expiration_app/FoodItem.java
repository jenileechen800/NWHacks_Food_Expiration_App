package com.hfad.nwhacks_food_expiration_app;

public class FoodItem {
    private String itemType;
    private int timeUntilExpire;


    public FoodItem(String itemType, String ForegroundC, String BackGroundC) {
        this.itemType = itemType;
        this.timeUntilExpire = expiryDate(ForegroundC, BackGroundC, itemType);

    }

    private int expiryDate (String ForegroundC, String BackGroundC, String itemType) {
        int expiryTime = 0;
        if(itemType == "banana") {
            if (ForegroundC == "green") {
                expiryTime = 5;
            }
        }

        return expiryTime;
    }

    public void decrementExpiryTime() {
        timeUntilExpire--;
    }

    public int getExpiryTime() { return timeUntilExpire; }



}