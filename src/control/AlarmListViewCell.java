package control;

import java.util.HashMap;

import javafx.application.Platform;
import javafx.scene.control.ListCell;

/**
 * ok i don't even know what kind of stack overflow magic this is.
 * This is like.... an actual cell in the list. it's appearance gets.. updated.
 * @author andys
 *
 */
public class AlarmListViewCell extends ListCell<HashMap<Integer, Object>>{
	
	@Override
    public void updateItem(HashMap<Integer, Object> hashmap, boolean empty)
    {
    	super.updateItem(hashmap, empty);

        if (hashmap != null)
        {     	
        	AlarmCellController ali = new AlarmCellController(hashmap);
        	//Just some goofy ass javaFX bs
    		Platform.runLater(new Runnable() {
    		    @Override
    		    public void run() {
    				// Update UI here.
    	            setGraphic(ali.getRoot());
    		    }
    		});
        } else {
        	setGraphic(null);
        }
    }
}
