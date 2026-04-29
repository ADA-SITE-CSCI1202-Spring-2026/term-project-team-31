package manager;

import java.util.*;
import model.Ingredient;
import model.MenuItem;


public class InventoryManager {

    private final Map<Ingredient, Integer> inventory = new LinkedHashMap<>();

    public static final int RESTOCK_AMOUNT = 5;

    public static final double RESTOCK_COST = 10.0;

    public InventoryManager() {
        inventory.put(Ingredient.BUNS,       10);
        inventory.put(Ingredient.PATTIES,    10);
        inventory.put(Ingredient.CHEESE,     10);
        inventory.put(Ingredient.LETTUCE,    10);
        inventory.put(Ingredient.CHICKEN,    10);
        inventory.put(Ingredient.OIL,        10);
        inventory.put(Ingredient.COLA_SYRUP, 10);
        inventory.put(Ingredient.CUPS,       10);
    }


    public boolean hasSufficientIngredients(MenuItem item) {
        for (Map.Entry<Ingredient, Integer> entry : item.getRequiredIngredients().entrySet()) {
            int available = inventory.getOrDefault(entry.getKey(), 0);
            if (available < entry.getValue()) {
                return false;
            }
        }
        return true;
    }


    public Ingredient findMissingIngredient(MenuItem item) {
        for (Map.Entry<Ingredient, Integer> entry : item.getRequiredIngredients().entrySet()) {
            int available = inventory.getOrDefault(entry.getKey(), 0);
            if (available < entry.getValue()) {
                return entry.getKey();
            }
        }
        return null;
    }


    public void consumeIngredients(MenuItem item) {
        for (Map.Entry<Ingredient, Integer> entry : item.getRequiredIngredients().entrySet()) {
            int current = inventory.getOrDefault(entry.getKey(), 0);
            inventory.put(entry.getKey(), Math.max(0, current - entry.getValue()));
        }
    }


    public void restock(Ingredient ingredient) {
        int current = inventory.getOrDefault(ingredient, 0);
        inventory.put(ingredient, current + RESTOCK_AMOUNT);
    }

    public int getStock(Ingredient ingredient) {
        return inventory.getOrDefault(ingredient, 0);
    }

    public Map<Ingredient, Integer> getSnapshot() {
        return Collections.unmodifiableMap(inventory);
    }

    public List<Ingredient> getAllIngredients() {
        return new ArrayList<>(inventory.keySet());
    }


    public String serialize() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Ingredient, Integer> e : inventory.entrySet()) {
            if (sb.length() > 0) sb.append(",");
            sb.append(e.getKey().name()).append("=").append(e.getValue());
        }
        return sb.toString();
    }

    public void deserialize(String data) {
        inventory.clear();
        for (String pair : data.split(",")) {
            String[] parts = pair.split("=");
            if (parts.length == 2) {
                try {
                    Ingredient ing = Ingredient.valueOf(parts[0].trim());
                    int qty = Integer.parseInt(parts[1].trim());
                    inventory.put(ing, qty);
                } catch (Exception ignored) {}
            }
        }
    }
}