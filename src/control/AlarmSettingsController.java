package control;

import java.net.URL;
import java.util.ResourceBundle;

import backend.AlarmConstants;
import backend.AlarmManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;

/**
 * Controller for changing the settings of an alarm.
 * This is the guy that pops up when you press edit.
 * TODO: add weekly/daily alarm settings to this thing and also a label.
 * @author 
 *
 */
public class AlarmSettingsController implements Initializable{

	@FXML
	private Spinner<Integer> hourSpinner, minuteSpinner;
	@FXML
	private ChoiceBox<String> amfmChoiceBox;
	@FXML
	private Button setButton;
	@FXML
	private Button cancelButton;
	
	/**
	 * The ID of the alarm being changed.
	 */
	private String alarmID;
	
	public AlarmSettingsController(String alarmID){
		this.alarmID = alarmID;
	}
	
	@Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void initialize(URL arg0, ResourceBundle arg1) {

        AlarmManager alarmManager = AlarmManager.getInstance();
        int defaultHour = alarmManager.getAlarm(alarmID).getHour();
        int defaultMinute = alarmManager.getAlarm(alarmID).getMinute();
        String defaultAMPM;
        //convert 24 hour to 12 hour for display purposes
        if (defaultHour >= 12){
        	defaultAMPM = AlarmConstants.PM_CHOICE;
        	if(defaultHour > 12){
        		defaultHour -= 12;
        	}
        }
        else{
        	defaultAMPM = AlarmConstants.AM_CHOICE;
        	if(defaultHour == 0){
        		defaultHour = 12;
        	}
        }
        
        SpinnerValueFactory.IntegerSpinnerValueFactory factory;
        TextFormatter formatter;
        // hourSpinner setup
        factory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 12, defaultHour);
        hourSpinner.setValueFactory(factory);
        hourSpinner.setEditable(true);

        formatter = new TextFormatter(factory.getConverter(), factory.getValue());
        hourSpinner.getEditor().setTextFormatter(formatter);
        factory.valueProperty().bindBidirectional(formatter.valueProperty());
        

        // minuteSpinner setup
        factory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, defaultMinute);
        minuteSpinner.setValueFactory(factory);
        minuteSpinner.setEditable(true);

        formatter = new TextFormatter(factory.getConverter(), factory.getValue());
        minuteSpinner.getEditor().setTextFormatter(formatter);
        factory.valueProperty().bindBidirectional(formatter.valueProperty());
        
        //amfmChoiceBox setup
        ObservableList<String> options = FXCollections.observableArrayList
        		(AlarmConstants.AM_CHOICE, AlarmConstants.PM_CHOICE);
        amfmChoiceBox.setValue(defaultAMPM);
        amfmChoiceBox.setItems(options);
        
        
    }
	
	public void setClicked(ActionEvent event) {

		String amfm = (String) amfmChoiceBox.getValue();
		
		// get the hour from the spinner and convert it to 24 hour
        int hour = hourSpinner.getValue();
        if (hour == 12) { hour = 0;}
        if (amfm.equals(AlarmConstants.PM_CHOICE)) { hour += 12; }
        
        
        int minute = minuteSpinner.getValue();

        // Find the alarm in question and modify it.
        AlarmManager alarmManager = AlarmManager.getInstance();
        
        // Update the settings. for now, just use the default repeat settings
        alarmManager.getAlarm(alarmID).changeSettings(AlarmConstants.ALLDAYS, hour, minute);
        alarmManager.saveAlarmsList();
        alarmManager.setRefreshNeeded(true);

        // Close window
	    ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
    }
	
	/**
     * hanldes clicking of cancel. closes the window.
     */
   public void cancelClicked(ActionEvent event) {
	    ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
   }
}