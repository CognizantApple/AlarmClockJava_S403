package control;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Timer;

import backend.Alarm;
import backend.AlarmConstants;
import backend.AlarmManager;
import backend.TimeUpdater;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Great big time and alarm view ensemble with cheese. Does a heck of a lot of 
 * stuff, doggo. you better believe it.
 * @author 
 *
 */
public class MainTimeViewController implements Initializable{


    private SimpleDateFormat HMFormat;
    private SimpleDateFormat SSFormat;
    private SimpleDateFormat dateFormat;
    
    // This is a completely silly solution to prevent alarms going off
    // more than once in the current minute. essentially, alarms are only
    // checked to go off whenever the currentMinute changes.
    private int currentMinute;
    
    //private AlarmNotificationController alarmNotificationController;
    
	@FXML
	private Label timeLabel;    
	@FXML
	private Label secondsLabel;
	
	@FXML
	private Label dateLabel;
	
	@FXML
	private Button newAlarmButton;
	
	@FXML
	private ListView<HashMap<Integer, Object>> alarmsListView;
	public static ObservableList<HashMap<Integer, Object>> observableAlarmList = FXCollections.observableArrayList();
	
	private AlarmManager alarmManager;
    private Timer fatherTime;

	
	/**
	 * Default constructor
	 */
	public MainTimeViewController() {
		HMFormat = new SimpleDateFormat(AlarmConstants.HOUR_MIN_12);
		SSFormat = new SimpleDateFormat(AlarmConstants.SEC_12);
		dateFormat = new SimpleDateFormat(AlarmConstants.DATE_WEEK_D_M_Y);
        alarmManager = AlarmManager.getInstance();
        alarmManager.recoverAlarmsList();

    }
	


    public void initPanel(Pane panel) {
        this.fatherTime = new Timer();
		fatherTime.schedule(new TimeUpdater(this), 0, 200);
    }
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// set the current Minute to initally be some ol bs
		// so that the first time around alarms are checked.
		currentMinute = -1;
		redrawAlarmListView();
	}

	/**
	 * Called by the thread running all the dang time.
	 * This updates the UI and checks if any alarms need activating.
	 * @param time
	 */
	public void updateTimeAndCheckAlarms() {
		Calendar c = Calendar.getInstance();
		
		//Just some goofy ass javaFX bs
		Platform.runLater(new Runnable() {
		    @Override
		    public void run() {
				// Update UI here.
				timeLabel.setText(HMFormat.format(c.getTime()));
				secondsLabel.setText(SSFormat.format(c.getTime()));
				dateLabel.setText(dateFormat.format(c.getTime()));
		    }
		});
		if(alarmManager.isRefreshNeeded()){
			redrawAlarmListView();
		}
		
		
		checkAlarms();
		
	}
	
	/**
	 * Compares the current time to the set times of all enabled
	 * alarms, and enables them as necessary.
	 */
	private void checkAlarms(){
		Calendar c = Calendar.getInstance();
		int currentHour = c.get(Calendar.HOUR_OF_DAY);
		int currentMinute = c.get(Calendar.MINUTE);
		int currentDay = c.get(Calendar.DAY_OF_WEEK);
		
		// Only bother checking if alarms should go off if the current minute has changed.
		if(this.currentMinute != currentMinute){
			this.currentMinute = currentMinute;
			for (Alarm a : alarmManager.getAllAlarms()){
				if(a.isEnabled()){
					if(a.isSnoozing()){
						// don't need to check the day
						if(currentHour == a.getTempSnoozeHour() && currentMinute == a.getTempSnoozeMinute()){
							activateAlarmEnsemble(a);
						}
					}
					else{
						if(currentHour == a.getHour() && currentMinute == a.getMinute()){
							// If the alarm is repeating, we need to make sure the alarm is enabled for the current day.
							if(a.isRepeatEnabled()){
								if(a.isEnabledForDay(currentDay-1)){
									activateAlarmEnsemble(a);
								}
							}
							else{
								activateAlarmEnsemble(a);
							}
						}
					}
				}
					
			}
		}
				
	}
	
	/**
	 * activates an alarm, calls the AlarmNotificationController
	 * to do it's magic and stuff. each new alarm that goes off will have 
	 * it's own window.
	 * @param a
	 */
	private void activateAlarmEnsemble(Alarm a){
			Platform.runLater(new Runnable() {
			    @Override
			    public void run() {
			    	try{
				    	a.activate();
				    	FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../AlarmNotification.fxml"));
			            Parent root = fxmlLoader.load();
			            Stage stage = new Stage();
			            stage.setScene(new Scene(root));
			            AlarmNotificationController alarmNotificationController;
			            alarmNotificationController = fxmlLoader.getController();
			            
			            stage.setOnCloseRequest(event -> {
			            	alarmNotificationController.getPlayer().stop();
			            });
			            
			            alarmNotificationController.setActiveAlarmID(a.getId());
			            alarmNotificationController.startUp();
			            stage.show();
		            } catch (IOException e) {
		            	e.printStackTrace();
		            }
			    }
			});
            
        
		
	}
	
	
	public void newAlarmButtonClick() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../NewAlarmView.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	/**
	 * This function updates how the list of alarms looks. i've done my best to make it bug-free
	 * but it wiggs out a little and shows ghosts sometimes. functionality is all good, though.
	 */
	public void redrawAlarmListView(){
        alarmManager.setRefreshNeeded(false);
        MainTimeViewController mom = this;
        Platform.runLater(new Runnable() {
		    @Override
		    public void run() {
		    	observableAlarmList = FXCollections.observableArrayList();
				HashMap<Integer, Object> alarmData = new HashMap<Integer, Object>();
				ArrayList<Alarm> alarms = AlarmManager.getInstance().getAllAlarms();
				alarmsListView.setItems(null);

				for (int i = 0; i < alarms.size(); i++)
				{
				    alarmData.put(AlarmCellController.ALC_ALARM, alarms.get(i));
				    alarmData.put(AlarmCellController.ALC_CONTROLLER, mom);
				    alarmData.put(AlarmCellController.ALC_INDEX, i);
				    
				    observableAlarmList.add(alarmData);
				    alarmData = new HashMap<Integer, Object>();
				}

				alarmsListView.setItems(observableAlarmList);
				alarmsListView.setCellFactory(param -> new AlarmListViewCell());
		    }
		});
	}
	
	

}
