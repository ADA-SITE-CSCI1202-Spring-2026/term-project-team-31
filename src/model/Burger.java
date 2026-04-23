package model;

import java.util.LinkedHashMap;
import java.util.Map;


public class Burger extends MenuItem {

    public Burger() {
        super("Cheeseburger");
    }

    @Override
    public Map<Ingredient, Integer> getRequiredIngredients() {
        Map<Ingredient, Integer> req = new LinkedHashMap<>();
        req.put(Ingredient.BUNS,    1);
        req.put(Ingredient.PATTIES, 1);
        req.put(Ingredient.CHEESE,  1);
        req.put(Ingredient.LETTUCE, 1);
        return req;
    }

    @Override
    public String getTypeTag() {
        return "BURGER";
    }

    @Override
    public String getRequiredProcessorType() {
        return "GRILL";
    }

    @Override
    public double getRevenue() {
        return 8.50;
    }
}
