package model;

import java.util.LinkedHashMap;
import java.util.Map;

// Fried chicken — needs 2 chicken pieces per order, oil is consumed each time
public class FriedChicken extends MenuItem {

    public FriedChicken() {
        super("Fried Chicken");
    }

    @Override
    public Map<Ingredient, Integer> getRequiredIngredients() {
        Map<Ingredient, Integer> req = new LinkedHashMap<>();
        req.put(Ingredient.CHICKEN, 2);  // 2 pieces per serving
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
