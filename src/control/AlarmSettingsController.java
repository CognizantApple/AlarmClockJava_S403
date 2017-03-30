package control;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
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
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
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
	private RadioButton repeatButton;
	@FXML
	private HBox weekdayButtons;
	@FXML
	private ToggleButton sunButton, monButton, tueButton, wedButton, thuButton, friButton, satButton;
	@FXML
	private ChoiceBox<String> ringtoneChoiceBox;
	@FXML
	private TextField titleField;
	@FXML
	private Button setButton;
	@FXML
	private Button cancelButton;
	
	/**
	 * The ID of the alarm being changed.
	 */
	private String alarmID;
	
	/**
	 * the initial repeat settings of the given alarm
	 */
	private short repeatSettings;
	
	private String alarmLabel;
	
	private String ringtone;
	
	public AlarmSettingsController(String alarmID){
		this.alarmID = alarmID;
	}
	
	@Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void initialize(URL arg0, ResourceBundle arg1) {

        AlarmManager alarmManager = AlarmManager.getInstance();
        int defaultHour = alarmManager.getAlarm(alarmID).getHour();
        int defaultMinute = alarmManager.getAlarm(alarmID).getMinute();
        repeatSettings = alarmManager.getAlarm(alarmID).getRepeatSettings();
        alarmLabel = alarmManager.getAlarm(alarmID).getAlarmLabel();
        ringtone = alarmManager.getAlarm(alarmID).getRingtone();
        setAlarmRepeatSettings();
        setAlarmTitle();
        setAlarmRingtone();
        
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
	
	/**
	 * Loads the list of ringtone names from the media folder,
	 * and populates the drop-down menu with available choices.
	 * Sets the current choice to whatever the user had selected previously.
	 */
	public void setAlarmRingtone(){
		ArrayList<String> availableOptions = findRingtones(AlarmConstants.RINGTONE_DIR);
		
		ObservableList<String> options = FXCollections.observableArrayList
        		(availableOptions);
		
		ringtoneChoiceBox.setValue(availableOptions.get(0));
		
		for (String option : availableOptions){
			if (option.equals(ringtone.substring(0, ringtone.length()-4))){
				ringtoneChoiceBox.setValue(option);
				break;
			}
		}
        ringtoneChoiceBox.setItems(options);
	}
	
	public ArrayList<String> findRingtones( String dirName){
		ArrayList<String> textFiles = new ArrayList<String>();
		  File dir = new File(dirName);
		  for (File file : dir.listFiles()) {
		    if (file.getName().endsWith((".mp3"))) {
		      textFiles.add(file.getName().substring(0, file.getName().length()-4));
		    }
		  }
		  return textFiles;

    }
	
	/**
     * @Author: Andy
     * applies the current alarm repeat settings to the day toggle display
     */
    private void setAlarmRepeatSettings(){
        sunButton.setSelected((repeatSettings & AlarmConstants.SUNDAY) != 0);
        monButton.setSelected((repeatSettings & AlarmConstants.MONDAY) != 0);
        tueButton.setSelected((repeatSettings & AlarmConstants.TUESDAY) != 0);
        wedButton.setSelected((repeatSettings & AlarmConstants.WEDNESDAY) != 0);
        thuButton.setSelected((repeatSettings & AlarmConstants.THURSDAY) != 0);
        friButton.setSelected((repeatSettings & AlarmConstants.FRIDAY) != 0);
        satButton.setSelected((repeatSettings & AlarmConstants.SATURDAY) != 0);
        if ((repeatSettings & AlarmConstants.REPEAT_ENABLED) != 0){
        	weekdayButtons.setVisible(true);
            repeatButton.setSelected(true);
        }
        else{
        	weekdayButtons.setVisible(false);
            repeatButton.setSelected(false);
        }
    }
    /**
     * @Author: Andy
     * observes the states of the day repeat buttons, and compacts their 'information' into repeatSettings
     */
    private void setRepeatSettingsFromToggles(){
        repeatSettings = 0x0000;
        if (repeatButton.isSelected()) { repeatSettings |= AlarmConstants.REPEAT_ENABLED; }
        if (sunButton.isSelected()) { repeatSettings |= AlarmConstants.SUNDAY; }
        if (monButton.isSelected()) { repeatSettings |= AlarmConstants.MONDAY; }
        if (tueButton.isSelected()) { repeatSettings |= AlarmConstants.TUESDAY; }
        if (wedButton.isSelected()) { repeatSettings |= AlarmConstants.WEDNESDAY; }
        if (thuButton.isSelected()) { repeatSettings |= AlarmConstants.THURSDAY; }
        if (friButton.isSelected()) { repeatSettings |= AlarmConstants.FRIDAY; }
        if (satButton.isSelected()) { repeatSettings |= AlarmConstants.SATURDAY; }
    }
    
    private void setAlarmTitle(){
        titleField.setText(alarmLabel);
    }
	
	public void setClicked(ActionEvent event) {

		String amfm = (String) amfmChoiceBox.getValue();
		
		// get the hour from the spinner and convert it to 24 hour
        int hour = hourSpinner.getValue();
        if (hour == 12) { hour = 0;}
        if (amfm.equals(AlarmConstants.PM_CHOICE)) { hour += 12; }
        
        
        int minute = minuteSpinner.getValue();

        String tone = ringtoneChoiceBox.getValue() + ".mp3";
        String label = titleField.getText();
        
        // Find the alarm in question and modify it.
        AlarmManager alarmManager = AlarmManager.getInstance();
        setRepeatSettingsFromToggles();
        // Update the settings. for now, just use the default repeat settings
        alarmManager.getAlarm(alarmID).changeSettings
        (repeatSettings, hour, minute, tone, label);
        
        alarmManager.saveAlarmsList();
        alarmManager.setRefreshNeeded(true);

        // Close window
	    ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
    }
	
	/**
     * handles clicking of cancel. closes the window.
     */
   public void cancelClicked(ActionEvent event) {
	    ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
   }
   
   /**
    * handles clicking of repeat. sets whether the list of weekday buttons are visible.
    */
  public void repeatClicked(ActionEvent event) {
	  if (repeatButton.isSelected()){
      	weekdayButtons.setVisible(true);
          repeatButton.setSelected(true);
      }
      else{
      	weekdayButtons.setVisible(false);
          repeatButton.setSelected(false);
      }
  }
  
  /**
   * handles clicking of the sunday repeat toggleButton.
   */
 public void sunClicked(ActionEvent event) {
	 //TODO something pretty
 }
 /**
  * handles clicking of the sunday repeat toggleButton.
  */
public void monClicked(ActionEvent event) {
	 //TODO something pretty
}
/**
 * handles clicking of the sunday repeat toggleButton.
 */
public void tueClicked(ActionEvent event) {
	 //TODO something pretty
}
/**
 * handles clicking of the sunday repeat toggleButton.
 */
public void wedClicked(ActionEvent event) {
	 //TODO something pretty
}
/**
 * handles clicking of the sunday repeat toggleButton.
 */
public void thuClicked(ActionEvent event) {
	 //TODO something pretty
}
/**
 * handles clicking of the sunday repeat toggleButton.
 */
public void friClicked(ActionEvent event) {
	 //TODO something pretty
}
/**
 * handles clicking of the sunday repeat toggleButton.
 */
public void satClicked(ActionEvent event) {
	 //TODO something pretty
}
   
   
   
}