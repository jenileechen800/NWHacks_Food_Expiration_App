package com.hfad.nwhacks_food_expiration_app;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class FoodList {
    private final Set<FoodItem> foods;

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

    public boolean addFoodItem(FoodItem foodItem) {
        return foods.add(foodItem);
    }

    public void clearList() {
        foods.clear();
    }

    public Map<String, Integer> getStringList() {
        Map<String, Integer> map = new HashMap<>();
        for (FoodItem food : foods) {
            map.put(food.getItemType(), food.getExpiryTime());
        }

        return new HashMap<>(map);
    }


}
