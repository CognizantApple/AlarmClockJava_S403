package backend;

import java.util.TimerTask;
import control.MainTimeViewController;

/**
 * Thread that runs all the dang time and
 * keeps the dang time updated. basically
 * just by telling the mainTimeViewController
 * what to do :)
 * @author andys
 *
 */
public class TimeUpdater extends TimerTask{


	    private MainTimeViewController mom;

	    // TODO: Now that this thread runs from a fragment, need rootView of that fragment to access Views. Do we still need cont and act params?
	    public TimeUpdater(MainTimeViewController mom){
	        this.mom = mom;
	    }

	    @Override
	    public void run() {
            mom.updateTimeAndCheckAlarms();
	    }
	  
}
