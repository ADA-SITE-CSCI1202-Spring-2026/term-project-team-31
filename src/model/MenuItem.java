package model;

import java.util.Map;


public abstract class MenuItem {

    private String name;

    public MenuItem(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    
    public abstract Map<Ingredient, Integer> getRequiredIngredients();

   
    public abstract String getTypeTag();

    
    public abstract String getRequiredProcessorType();

   
    public abstract double getRevenue();

    @Override
    public String toString() {
        return name;
    }
}