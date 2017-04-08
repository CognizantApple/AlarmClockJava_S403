package control;

import backend.Alarm;
import backend.AlarmConstants;
import backend.AlarmManager;
import java.net.URL;
import java.util.ResourceBundle;
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
 * Controller for the little dialog that comes up for a new
 * alarm. less detailed than the settings available for
 * a real alarm having it's settings edited.
 * @author 
 *
 */
public class NewAlarmController implements Initializable {

	@FXML
	private Spinner<Integer> hourSpinner;
	@FXML
	private Spinner<Integer> minuteSpinner;
	@FXML
	private ChoiceBox<String> amfmChoiceBox;
	@FXML
	private Button setButton;
	@FXML
	private Button cancelButton;
	
	
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
    public void initialize(URL arg0, ResourceBundle arg1) {
        SpinnerValueFactory.IntegerSpinnerValueFactory factory;
        // hourSpinner setup
        factory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 12, 1);
        hourSpinner.setValueFactory(factory);
        hourSpinner.setEditable(true);

        TextFormatter formatter;
        formatter = new TextFormatter(factory.getConverter(), factory.getValue());
        hourSpinner.getEditor().setTextFormatter(formatter);
        factory.valueProperty().bindBidirectional(formatter.valueProperty());

        // minuteSpinner setup
        factory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0);
        minuteSpinner.setValueFactory(factory);
        minuteSpinner.setEditable(true);

        formatter = new TextFormatter(factory.getConverter(), factory.getValue());
        minuteSpinner.getEditor().setTextFormatter(formatter);
        factory.valueProperty().bindBidirectional(formatter.valueProperty());
        
        //amfmChoiceBox setup
        ObservableList<String> options = FXCollections.observableArrayList(
        		AlarmConstants.AM_CHOICE, AlarmConstants.PM_CHOICE);
        amfmChoiceBox.setValue(AlarmConstants.AM_CHOICE);
        amfmChoiceBox.setItems(options);
        
        
    }
	
	/**
	 * add that new alarm.
	 * @param event Clicking event.
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

        // Make a new alarm and add it to the list.
        AlarmManager alarmManager = AlarmManager.getInstance();
        
        Alarm alarm = new Alarm(hour, minute);

        alarmManager.addAlarm(alarm);
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
