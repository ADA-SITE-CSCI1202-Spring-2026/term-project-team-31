package model;

import java.util.Map;

// Base for everything on the menu — subclasses fill in the specifics
public abstract class MenuItem {

    private final String name;

    public MenuItem(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    // What and how much is needed to make this item
    public abstract Map<Ingredient, Integer> getRequiredIngredients();

    // Short string key used when saving/loading orders (e.g. "BURGER")
    public abstract String getTypeTag();

    // Which station handles this — GRILL, FRYER, DISPENSER, etc.
    public abstract String getRequiredProcessorType();

    // How much this item adds to revenue when completed
    public abstract double getRevenue();

    @Override
    public String toString() {
        return name;
    }
}
