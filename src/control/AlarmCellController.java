package control;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import backend.Alarm;
import backend.AlarmConstants;
import backend.AlarmManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
/**
	 * This is sorta like the internal class for cards
	 * in the old recyclerview for the android app.
	 * 
	 * Handles the appearance and onclick dealies for alarm cards in the list.
	 * @author 
	 *
	 */
public class AlarmCellController {

	// some bananas way to get the pieces out of a hashmap of information passed to this thing.
	// kind of some crazy stackoverflow shit. don't worry, not gonna be on the final lmfao
    public static Integer ALC_ALARM = 1, ALC_CONTROLLER = 2, ALC_INDEX = 3;

    @SuppressWarnings("unused")
	private MainTimeViewController mom;
    // I think this will be the index of the alarm in the list
    private int alarmIndex;
    
    // actual ID of the alarm
    private String alarmID;
   
    private SimpleDateFormat HMFormat = new SimpleDateFormat(AlarmConstants.HOUR_MIN_12 + " " + AlarmConstants.NOSEC_12);;
    
	@FXML
    private Button editButton, deleteButton;

    @FXML
    private Pane root;
    
    @FXML
    private Label alarmTimeLabel;
    
    @FXML
    private RadioButton enabledButton;
    
    

    /**
     * constructor to set this card up. uses a hashmap containing the current position of the alarm,
     * the alarm itself, and the MainTimeViewController
     * @param alarmItem
     */
    AlarmCellController(HashMap<Integer, Object> alarmItem) {
    	
    	FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("AlarmListItem.fxml"));
        loader.setController(this);
        try
        {
            loader.load();
        }

        catch (IOException e)
        {
            e.printStackTrace();
        }
        
        Alarm alarm = (Alarm) alarmItem.get(ALC_ALARM);
        alarmID = alarm.getId();
        alarmIndex = (int) alarmItem.get(ALC_INDEX);
        mom = (MainTimeViewController) alarmItem.get(ALC_CONTROLLER);
    	AlarmManager manager = AlarmManager.getInstance();
    	
    	setUpCardDisplay(alarmID);
    	
    	deleteButton.setOnAction(event -> {
    		
            manager.deleteAlarm(alarmID);
			manager.saveAlarmsList();
    		manager.setRefreshNeeded(true);
    		
        });

        editButton.setOnAction(event ->
        {
        	
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../AlarmSettingsView.fxml"));
                fxmlLoader.setController(new AlarmSettingsController(alarmID));
                Parent root = fxmlLoader.load();
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
            
        });
        
        enabledButton.setOnAction(event ->
        {
        	if (enabledButton.isSelected()){
        		manager.getAlarm(alarmIndex).enable();
        	}
        	else{
        		manager.getAlarm(alarmIndex).disable();
        	}
			manager.saveAlarmsList();
        });
    	
    }
    /**
     * Sets up the card to display information related to the given alarm
     * @param alarmID The id of the alarm to have it's info displayed.
     * @throws IllegalArgumentException
     */
    public void setUpCardDisplay(String alarmID) throws IllegalArgumentException {
    	
    	AlarmManager manager = AlarmManager.getInstance();
    	Alarm alarm = manager.getAlarm(alarmID);
    	
    	Calendar c = Calendar.getInstance();
    	c.set(Calendar.HOUR_OF_DAY, alarm.getHour());
    	c.set(Calendar.MINUTE, alarm.getMinute());
    	// Set up the time display
        alarmTimeLabel.setText(HMFormat.format(c.getTime()));
        
        // Set up whether the alarm appears enabled or not.
        enabledButton.setSelected(alarm.isEnabled());
        
    }

    /** it's groot. */
    public Pane getRoot()
    {
        return root;
    }

}
