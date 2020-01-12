package com.hfad.nwhacks_food_expiration_app;

import java.util.Comparator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class FoodList {
    private Set<FoodItem> foods;

    public FoodList(){
        Comparator<FoodItem> comparator = (f1, f2) -> (f1.getExpiryTime() == f2.getExpiryTime()) ? 0 :
                f1.getExpiryTime() < f2.getExpiryTime() ? -f1.getExpiryTime() : f2.getExpiryTime();
        this.foods = new TreeSet<>(comparator);
    }

    public void decAllFood() {
        for (FoodItem food : foods) {
            food.decrementExpiryTime();
        }
    }

}
