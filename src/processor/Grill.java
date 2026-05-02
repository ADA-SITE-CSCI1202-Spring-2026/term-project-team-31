package processor;

import model.MenuItem;


public class Grill implements IAppliance {

    @Override
    public String getName() {
        return "Grill";
    }

    @Override
    public boolean canProcess(MenuItem item) {
        return "GRILL".equals(item.getRequiredProcessorType());
    }

    @Override
    public String processTask(MenuItem item) {
        return "[Grill] Grilled " + item.getName() + " to perfection.";
    }
}