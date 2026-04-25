package model;

import java.util.LinkedHashMap;
import java.util.Map;


public class Beverage extends MenuItem {

    public Beverage() {
        super("Fountain Drink");
    }

    @Override
    public Map<Ingredient, Integer> getRequiredIngredients() {
        Map<Ingredient, Integer> req = new LinkedHashMap<>();
        req.put(Ingredient.COLA_SYRUP, 1);
        req.put(Ingredient.CUPS,       1);
        return req;
    }

    @Override
    public String getTypeTag() {
        return "BEVERAGE";
    }

    @Override
    public String getRequiredProcessorType() {
        return "DISPENSER";
    }

    @Override
    public double getRevenue() {
        return 2.50;
    }
}
