package control;

import java.util.HashMap;

import javafx.application.Platform;
import javafx.scene.control.ListCell;

/**
 * This represents an actual cell in the list of alarms.
 * The constructor called here relies upon the AlarmCellController to actually
 * be filled out with the correct information.
 * @author andys
 *
 */
public class AlarmListViewCell extends ListCell<HashMap<Integer, Object>> {
	
	@Override
    public void updateItem(HashMap<Integer, Object> hashmap, boolean empty) {
    	super.updateItem(hashmap, empty);

        if (hashmap != null) {     	
        	AlarmCellController ali = new AlarmCellController(hashmap);
        	
    		Platform.runLater(new Runnable() {
    		    @Override
    		    public void run() {
    		    	
    		    	// Update the UI here
    		    	setGraphic(ali.getRoot());
    		    }
    		});
        } else {
        	setGraphic(null);
        }
    }
}
