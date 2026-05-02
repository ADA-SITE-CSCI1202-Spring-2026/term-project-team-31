package processor;

import model.MenuItem;


public class DrinkDispenser implements IAppliance {

    @Override
    public String getName() {
        return "Drink Dispenser";
    }

    @Override
    public boolean canProcess(MenuItem item) {
        return "DISPENSER".equals(item.getRequiredProcessorType());
    }

    @Override
    public String processTask(MenuItem item) {
        return "[Dispenser] Poured " + item.getName() + " — ice cold and refreshing.";
    }
}
