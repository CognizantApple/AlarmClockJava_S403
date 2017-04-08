package control;

import backend.Alarm;
import backend.AlarmConstants;
import backend.AlarmManager;
import java.io.IOException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
	 * This is sorta like the internal class for cards
	 * in the old recyclerview for the android app.
	 * Handles the appearance and onclick dealies for alarm cards in the list.
	 * @author 
	 *
	 */
public class AlarmCellController {

	/**
	 * Identifiers used to store pieces of alarm data in a hashmap.
	 */
	public static Integer ALC_ALARM = 1; 
	public static Integer ALC_CONTROLLER = 2; 
	public static Integer ALC_INDEX = 3;

    @SuppressWarnings("unused")
	private MainTimeViewController mom;
    // I think this will be the index of the alarm in the list
    private int alarmIndex;
    
    // actual ID of the alarm
    private String alarmId;
   
    private SimpleDateFormat hmFormat = 
    		new SimpleDateFormat(AlarmConstants.HOUR_MIN_12 + " " + AlarmConstants.NOSEC_12);
    
	@FXML
    private Button editButton;
    @FXML
    private Button deleteButton;

    @FXML
    private Pane root;
    
    @FXML
    private Label alarmTimeLabel;
    
    @FXML
    private Label alarmDaysLabel;
    
    @FXML
    private Label alarmLabel;
    
    @FXML
    private RadioButton enabledButton;
    
    

    /**
     * constructor to set this card up. uses a hashmap containing the current position of the alarm,
     * the alarm itself, and the MainTimeViewController
     * @param alarmItem A Hashmap containing the alarm, it's index, and the main controller
     */
    AlarmCellController(HashMap<Integer, Object> alarmItem) {
    	
    	FXMLLoader loader = new FXMLLoader(getClass().getClassLoader()
    			.getResource("AlarmListItem.fxml"));
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        Alarm alarm = (Alarm) alarmItem.get(ALC_ALARM);
        alarmId = alarm.getId();
        alarmIndex = (int) alarmItem.get(ALC_INDEX);
        mom = (MainTimeViewController) alarmItem.get(ALC_CONTROLLER);
    	AlarmManager manager = AlarmManager.getInstance();
    	
    	setUpCardDisplay(alarmId);
    	
    	deleteButton.setOnAction(event -> {
    		
            manager.deleteAlarm(alarmId);
			manager.saveAlarmsList();
    		manager.setRefreshNeeded(true);
    		
        });

        editButton.setOnAction(event -> {
        	
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass()
                		.getResource("../AlarmSettingsView.fxml"));
                fxmlLoader.setController(new AlarmSettingsController(alarmId));
                Parent root = fxmlLoader.load();
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
            
        });
        
        enabledButton.setOnAction(event -> {
        	if (enabledButton.isSelected()) {
        		manager.getAlarm(alarmIndex).enable();
        	} else {
        		manager.getAlarm(alarmIndex).disable();
        	}
			manager.saveAlarmsList();
        });
    	
    }
    
    /**
     * Sets up the card to display information related to the given alarm
     * @param alarmId The id of the alarm to have it's info displayed.
     * @throws IllegalArgumentException if the alarm ID is invalid
     */
    public void setUpCardDisplay(String alarmId) throws IllegalArgumentException {
    	
    	AlarmManager manager = AlarmManager.getInstance();
    	Alarm alarm = manager.getAlarm(alarmId);
    	
    	Calendar c = Calendar.getInstance();
    	c.set(Calendar.HOUR_OF_DAY, alarm.getHour());
    	c.set(Calendar.MINUTE, alarm.getMinute());
    	// Set up the time display
    	alarmTimeLabel.setText(hmFormat.format(c.getTime()));
        
        alarmDaysLabel.setText(alarm.repeatDaysToString());
        alarmLabel.setText(alarm.getAlarmLabel());
        
        // Set up whether the alarm appears enabled or not.
        enabledButton.setSelected(alarm.isEnabled());
        
    }

    /** it's groot. */
    public Pane getRoot() {
        return root;
    }

}
