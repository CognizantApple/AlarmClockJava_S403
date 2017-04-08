package backend;

import control.MainTimeViewController;
import java.util.TimerTask;

/**
 * Thread that runs all the dang time and
 * keeps the dang time updated. basically
 * just by telling the mainTimeViewController
 * what to do :)
 * @author andys
 *
 */
public class TimeUpdater extends TimerTask {


	    private MainTimeViewController mom;

	    
	    public TimeUpdater(MainTimeViewController mom) {
	        this.mom = mom;
	    }

	    @Override
	    public void run() {
            mom.updateTimeAndCheckAlarms();
	    }
	  
}
