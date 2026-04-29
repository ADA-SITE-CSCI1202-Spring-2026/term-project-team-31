package persistance;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import manager.InventoryManager;
import manager.OrderQueue;
import model.*;


public class SaveLoadManager {

    private static final String SAVE_FILE = "save_state.txt";


    public static String save(double money, InventoryManager inventory, OrderQueue orderQueue) {
        try {
            List<String> lines = new ArrayList<>();
            lines.add("MONEY=" + String.format("%.2f", money));
            lines.add("INVENTORY=" + inventory.serialize());


            StringBuilder queueLine = new StringBuilder("QUEUE=");
            List<MenuItem> items = orderQueue.peekAll();
            for (int i = 0; i < items.size(); i++) {
                if (i > 0) queueLine.append(",");
                queueLine.append(items.get(i).getTypeTag());
            }
            lines.add(queueLine.toString());

            Files.write(Paths.get(SAVE_FILE), lines);
            return "Game saved to " + SAVE_FILE;
        } catch (IOException e) {
            return "ERROR: Save failed  " + e.getMessage();
        }
    }


    public static LoadResult load() {
        try {
            List<String> lines = Files.readAllLines(Paths.get(SAVE_FILE));

            double money = 100.0;
            String inventoryData = null;
            List<MenuItem> restoredQueue = new ArrayList<>();

            for (String line : lines) {
                if (line.startsWith("MONEY=")) {
                    money = Double.parseDouble(line.substring(6).trim());
                } else if (line.startsWith("INVENTORY=")) {
                    inventoryData = line.substring(10).trim();
                } else if (line.startsWith("QUEUE=")) {
                    String queueData = line.substring(6).trim();
                    if (!queueData.isEmpty()) {
                        for (String tag : queueData.split(",")) {
                            MenuItem item = createFromTag(tag.trim());
                            if (item != null) restoredQueue.add(item);
                        }
                    }
                }
            }
            return new LoadResult(true, money, inventoryData, restoredQueue, "Game loaded successfully. " + restoredQueue.size() + " orders restored.");
        } catch (IOException e) {
            return new LoadResult(false, 0, null, null, "ERROR: Load failed — " + e.getMessage());
        }
    }

    public static MenuItem createFromTag(String tag) {
        return switch (tag) {
            case "BURGER"       -> new Burger();
            case "FRIED_CHICKEN"-> new FriedChicken();
            case "BEVERAGE"     -> new Beverage();
            default             -> null;
        };
    }

    public static class LoadResult {
        public final boolean success;
        public final double money;
        public final String inventoryData;  
        public final List<MenuItem> queue;
        public final String message;

        public LoadResult(boolean success, double money, String inventoryData,
                          List<MenuItem> queue, String message) {
            this.success = success;
            this.money = money;
            this.inventoryData = inventoryData;
            this.queue = queue;
            this.message = message;
        }
    }
}