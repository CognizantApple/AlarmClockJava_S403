package backend;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * This class more or less serves as a global
 * alarm storage data structure. it's a singleton!
 * @author 
 *
 */
public class AlarmManager {

	static private AlarmManager instance = new AlarmManager();
	
	private ArrayList<Alarm> alarms;
	
	/**
     * Super secret private default constructor.
     */
	private AlarmManager() {
        this.alarms = new ArrayList<>();
    }
	
	/**
     * Get a singleton instance of AlarmManager.
     * @return an instance of AlarmManager.
     */
	static public AlarmManager getInstance() {
    	if (instance == null) {
    		instance = new AlarmManager();
    	}
    	return instance;
    }
    
    public void addAlarm(Alarm a) {
    	alarms.add(a);
    }
    
    /**
     * This is the dumbest thing u will ever see in your whole life. whenever
     * a change is made that indicates the list of alarms needs to be visually
     * refreshed, a change is made to this flag. Once the MainTimeViewController
     * makes the change, the flag is set to false again. wow.
     */
    private boolean viewNeedsRefresh = false;
    
    public boolean isRefreshNeeded() {
    	return viewNeedsRefresh;
    }
    
    public void setRefreshNeeded(boolean refresh) {
    	viewNeedsRefresh = refresh;
    }
    
    /**
     * Replaces the current list of alarms with the given one.
     * @param alarms 
     * 		list of alarms
     */
    public void replaceAlarmList(List<Alarm> alarms) {
        this.alarms = new ArrayList<>(alarms);
    }
    
    /**
     * Deletes an alarm.
     * @param alarmId The ID of the alarm to be deleted.
     */
    public void deleteAlarm(String alarmId) {
    	Alarm deadMeat = getAlarm(alarmId);
    	alarms.remove(deadMeat);
    }

    /**
     * Retrieve an alarm by index.
     * @param i - the index of the alarm to return
     * @return the alarm at index i
     */
    public Alarm getAlarm(int i) {
      return alarms.get(i);
    }
    
    /**
     * Provides access to an alarm by id.
     * @param id the id of the requested Alarm
     * @return the Alarm in the list of alarms which has the given id
     */
    public Alarm getAlarm(String id) {
        for (Alarm a : alarms) {
            if (a.getId().equals(id)) {
            	return a;
            }
        }
        throw new IllegalArgumentException("No Alarm matches the given ID.");
    }
    
    /**
     * Get all the alarms currently saved, as a list of Alarm objects.
     * @return the list of all alarms.
     */
    public ArrayList<Alarm> getAllAlarms() {
    	return alarms;
    }
    
    /**
     * Retrieve the number of alarms currently saved.
     * @return the current number of alarms.
     */
    public int getNumberOfAlarms() {
    	return alarms.size();
    }
    
    /**
     * Saves the current list of alarms to a JSON (GSON? lol) object.
     */
    public void saveAlarmsList() {
    	
        Gson gson = new Gson();
        String json = gson.toJson(alarms);
    	
    	String jsonLocation = new File("").getAbsolutePath().concat("/alarms.json");
    	try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(jsonLocation));
            writer.write(json);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Saves the current list of alarms to a JSON (GSON? lol) object.
     */
    public void recoverAlarmsList() {

    	String jsonLocation = new File("").getAbsolutePath().concat("/alarms.json");
    	try {
            Gson gson = new Gson();
            BufferedReader reader = new BufferedReader(new FileReader(jsonLocation));
            Type alarmType = new TypeToken<List<Alarm>>() {
            	}.getType();
            List<Alarm> alarms = gson.fromJson(reader, alarmType);
            if (alarms.size() > 0) {
            	AlarmManager.getInstance().replaceAlarmList(alarms);
            }
        } catch (Exception e) {
            //Expected to get File not found error
        }
        
    }
}
