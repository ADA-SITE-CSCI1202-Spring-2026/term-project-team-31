package processor;

import model.MenuItem;


public class Fryer implements IAppliance {

    @Override
    public String getName() {
        return "Fryer";
    }

    @Override
    public boolean canProcess(MenuItem item) {
        return "FRYER".equals(item.getRequiredProcessorType());
    }

    @Override
    public String processTask(MenuItem item) {
        return "[Fryer] Deep-fried " + item.getName() + " — crispy and golden.";
    }
}