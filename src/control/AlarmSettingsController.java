package control;

import backend.AlarmConstants;
import backend.AlarmManager;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

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
public class AlarmSettingsController implements Initializable {

	@FXML
	private Spinner<Integer> hourSpinner;
	@FXML
	private Spinner<Integer> minuteSpinner;
	@FXML
	private ChoiceBox<String> amfmChoiceBox;
	@FXML
	private RadioButton repeatButton;
	@FXML
	private HBox weekdayButtons;
	@FXML
	private ToggleButton sunButton, monButton, tueButton, wedButton, thuButton, friButton, satButton;
	
	private ToggleButton[] buttonsList;
	
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
	private String alarmId;
	
	/**
	 * the repeat settings of the given alarm.
	 */
	private short repeatSettings;
	
	private String alarmLabel;
	
	private String ringtone;
	
	/**
	 * Default constructor, sets the id of the alarm having it's settings changed.
	 * @param alarmId The id of the alarm having it's settings changed
	 */
	public AlarmSettingsController(String alarmId) {
		this.alarmId = alarmId;
	}
	
	private void setButtonsList() {
		ToggleButton list[] = {sunButton, monButton, tueButton, wedButton, thuButton, friButton, satButton};
		buttonsList = list;
	}
	
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
    public void initialize(URL arg0, ResourceBundle arg1) {
		setButtonsList();
        AlarmManager alarmManager = AlarmManager.getInstance();
        int defaultHour = alarmManager.getAlarm(alarmId).getHour();
        int defaultMinute = alarmManager.getAlarm(alarmId).getMinute();
        repeatSettings = alarmManager.getAlarm(alarmId).getRepeatSettings();
        alarmLabel = alarmManager.getAlarm(alarmId).getAlarmLabel();
        ringtone = alarmManager.getAlarm(alarmId).getRingtone();
        setAlarmRepeatSettings();
        setAlarmTitle();
        setAlarmRingtone();
        
        String defaultAMPM;
        //convert 24 hour to 12 hour for display purposes
        if (defaultHour >= 12) {
        	defaultAMPM = AlarmConstants.PM_CHOICE;
        	if (defaultHour > 12) {
        		defaultHour -= 12;
        	}
        } else {
        	defaultAMPM = AlarmConstants.AM_CHOICE;
        	if (defaultHour == 0) {
        		defaultHour = 12;
        	}
        }
        
        SpinnerValueFactory.IntegerSpinnerValueFactory factory;
        // hourSpinner setup
        factory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 12, defaultHour);
        hourSpinner.setValueFactory(factory);
        hourSpinner.setEditable(true);

        TextFormatter formatter;
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
        ObservableList<String> options = FXCollections.observableArrayList(AlarmConstants.AM_CHOICE,
        		AlarmConstants.PM_CHOICE);
        amfmChoiceBox.setValue(defaultAMPM);
        amfmChoiceBox.setItems(options);
        
        
    }
	
	/**
	 * Loads the list of ringtone names from the media folder,
	 * and populates the drop-down menu with available choices.
	 * Sets the current choice to whatever the user had selected previously.
	 */
	public void setAlarmRingtone() {
		ArrayList<String> availableOptions = findRingtones(AlarmConstants.RINGTONE_DIR);
		
		ObservableList<String> options = 
				FXCollections.observableArrayList(availableOptions);
		
		ringtoneChoiceBox.setValue(availableOptions.get(0));
		
		for (String option : availableOptions) {
			if (option.equals(ringtone.substring(0, ringtone.length() - 4))) {
				ringtoneChoiceBox.setValue(option);
				break;
			}
		}
        ringtoneChoiceBox.setItems(options);
	}
	
	/**
	 * Finds and returns the names of ringtones in the media folder.
	 * @param dirName The directory to search for ringtones. default 'media'
	 * @return The names of .mp3 files under dirName
	 */
	public ArrayList<String> findRingtones(String dirName) {
		ArrayList<String> textFiles = new ArrayList<String>();
		  File dir = new File(dirName);
		  for (File file : dir.listFiles()) {
		    if (file.getName().endsWith((".mp3"))) {
		      textFiles.add(file.getName().substring(0, file.getName().length() - 4));
		    }
		  }
		  return textFiles;

    }
	
	/**
	 * applies the current alarm repeat settings to the day toggle display.
	 */
	private void setAlarmRepeatSettings() {
    	for (int i = 0; i < 7; i++) {
    		buttonsList[i].setSelected((repeatSettings & AlarmConstants.WEEK_ARRAY[i]) != 0);
    		buttonsList[i].setStyle(buttonsList[i].isSelected()
    				? "-fx-base: lightgreen;" : "-fx-base: lightgray;");
    	}
        if ((repeatSettings & AlarmConstants.REPEAT_ENABLED) != 0) {
        	weekdayButtons.setVisible(true);
            repeatButton.setSelected(true);
        } else {
        	weekdayButtons.setVisible(false);
            repeatButton.setSelected(false);
        }
    }
    
    /**
     * observes the states of the day repeat buttons.
     * compacts their information into the repeatSettings variable
     */
    private void setRepeatSettingsFromToggles() {
        repeatSettings = 0x0000;
        if (repeatButton.isSelected()) {
        	repeatSettings |= AlarmConstants.REPEAT_ENABLED; 
        }

    	for (int i = 0; i < 7; i++) {
    		if (buttonsList[i].isSelected()) { 
    			repeatSettings |= AlarmConstants.WEEK_ARRAY[i];
    		}
    		;
    	}
    }
    
    private void setAlarmTitle() {
        titleField.setText(alarmLabel);
    }
	
    /**
     * Handles the clicking of the 'Set' button.
     * Applies the selected settings to the alarm.
     * @param event The clicking event
     */
    public void setClicked(ActionEvent event) {

		String amfm = (String) amfmChoiceBox.getValue();
		
		// get the hour from the spinner and convert it to 24 hour
		int hour = hourSpinner.getValue();
        if (hour == 12) {
        	hour = 0; 
        }
        if (amfm.equals(AlarmConstants.PM_CHOICE)) {
        	hour += 12; 
        }
        
        
        int minute = minuteSpinner.getValue();

        String tone = ringtoneChoiceBox.getValue() + ".mp3";
        String label = titleField.getText();
        
        // Find the alarm in question and modify it.
        AlarmManager alarmManager = AlarmManager.getInstance();
        setRepeatSettingsFromToggles();
        // Update the settings. for now, just use the default repeat settings
        alarmManager.getAlarm(alarmId).changeSettings(repeatSettings, hour, minute, tone, label);
        
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
	  if (repeatButton.isSelected()) {
      	weekdayButtons.setVisible(true);
          repeatButton.setSelected(true);
	  } else {
      	weekdayButtons.setVisible(false);
          repeatButton.setSelected(false);
      }
  }

	 /**
	 * handles clicking of the sunday repeat toggleButton.
	 */
	 public void sunClicked(ActionEvent event) {
		 sunButton.setStyle(sunButton.isSelected() 
				 ? "-fx-base: lightgreen;" : "-fx-base: lightgray;");
	 }
	 
	/**
	* handles clicking of the monday repeat toggleButton.
	*/
	public void monClicked(ActionEvent event) {
		 monButton.setStyle(monButton.isSelected() 
				 ? "-fx-base: lightgreen;" : "-fx-base: lightgray;");
	}
	
	/**
	 * handles clicking of the tuesday repeat toggleButton.
	 */
	public void tueClicked(ActionEvent event) {
		 tueButton.setStyle(tueButton.isSelected() 
				 ? "-fx-base: lightgreen;" : "-fx-base: lightgray;");
	}
	
	/**
	 * handles clicking of the wednesday repeat toggleButton.
	 */
	public void wedClicked(ActionEvent event) {
		 wedButton.setStyle(wedButton.isSelected()
				 ? "-fx-base: lightgreen;" : "-fx-base: lightgray;");
	}
	
	/**
	 * handles clicking of the thursday repeat toggleButton.
	 */
	public void thuClicked(ActionEvent event) {
		 thuButton.setStyle(thuButton.isSelected()
				 ? "-fx-base: lightgreen;" : "-fx-base: lightgray;");
	}
	
	/**
	 * handles clicking of the friday repeat toggleButton.
	 */
	public void friClicked(ActionEvent event) {
		 friButton.setStyle(friButton.isSelected() 
				 ? "-fx-base: lightgreen;" : "-fx-base: lightgray;");
	}
	
	/**
	 * handles clicking of the saturday repeat toggleButton.
	 */
	public void satClicked(ActionEvent event) {
		 satButton.setStyle(satButton.isSelected()
				 ? "-fx-base: lightgreen;" : "-fx-base: lightgray;");
	}
   
   
   
}