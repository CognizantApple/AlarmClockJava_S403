package control;

import backend.AlarmConstants;
import backend.AlarmManager;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

/**
 * This guy is basically the little interface that pops up when an alarm goes off.
 * @author andys
 *
 */
public class AlarmNotificationController implements Initializable {

    private MediaPlayer player;
    private AlarmManager alarmManager;
    private String activeAlarmId;
    
    @FXML
    private Button dismiss; 
    @FXML
    private Button snooze;
    
    @FXML
    private Label alarmLabel;

	public MediaPlayer getPlayer() {
		return player;
	}
    
	public void setActiveAlarmId(String id) {
		this.activeAlarmId = id;
	}
	
	/**
	 * This function is called when a new alarm is made.
	 * Fills out the details of the alarm label, finds the
	 * alarm ringtone specified and plays it.
	 */
	public void startUp() {
		alarmManager = AlarmManager.getInstance();
		
		String label = alarmManager.getAlarm(activeAlarmId).getAlarmLabel();
		if (label.equals(AlarmConstants.DEFAULT_LABEL)) {
			label = "Alarm going off!";
		}
		alarmLabel.setText(label);
		
		String ringtoneName = alarmManager.getAlarm(activeAlarmId).getRingtone();
		ringtoneName = AlarmConstants.RINGTONE_DIR + "/" + ringtoneName;
		File ringtone = new File(ringtoneName);
		
		//This would be a great place to catch a file not found exception and then
		// set the ringtone file to a default value of some sort.
		Media sound = new Media(ringtone.toURI().toString());
        player = new MediaPlayer(sound);
        player.setVolume(1.0);
        player.setCycleCount(MediaPlayer.INDEFINITE);
        player.play();
	}
	
	/**
	 * Standard constructor. Does not do much.
	 * Most of the heavy lifting of starting an alarm
	 * is done in startUp();
	 */
	public AlarmNotificationController() {
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	}
	
	/**
     * Handles dismissal of alarm.
     */
	public void dismissClick() {

        player.stop();
        alarmManager.getAlarm(activeAlarmId).dismiss();
        //If this changes one of the thingies to be disabled, it should show.
        alarmManager.setRefreshNeeded(true);
		alarmManager.saveAlarmsList();
        
        Stage stage = (Stage) dismiss.getScene().getWindow();
        stage.close();
    }
    
    /**
     * Handles snoozing of alarm.
     */
    public void snoozeClick() {

        player.stop();
        alarmManager.getAlarm(activeAlarmId).snooze(AlarmConstants.DEFAULT_SNOOZE_LENGTH);
		alarmManager.saveAlarmsList();
        
        Stage stage = (Stage) snooze.getScene().getWindow();
        stage.close();
    }
    
}
