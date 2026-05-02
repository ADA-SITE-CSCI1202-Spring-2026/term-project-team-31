package processor;

import model.MenuItem;


public interface IAppliance {

    String getName();

    boolean canProcess(MenuItem item);


    String processTask(MenuItem item);
}
