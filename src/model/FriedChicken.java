

package model;

import java.util.LinkedHashMap;
import java.util.Map;


public class FriedChicken extends MenuItem {

    public FriedChicken() {
        super("Fried Chicken");
    }

    @Override
    public Map<Ingredient, Integer> getRequiredIngredients() {
        Map<Ingredient, Integer> req = new LinkedHashMap<>();
        req.put(Ingredient.CHICKEN, 2);
        req.put(Ingredient.OIL,     1);
        return req;
    }

    @Override
    public String getTypeTag() {
        return "FRIED_CHICKEN";
    }

    @Override
    public String getRequiredProcessorType() {
        return "FRYER";
    }

    @Override
    public double getRevenue() {
        return 7.00;
    }
}
