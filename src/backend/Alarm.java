package backend;

import com.google.gson.annotations.Expose;
import java.util.Calendar;


public class Alarm {
	
	/**
	 * the days of the week an alarm should repeat. Default value is all days, but disabled.
	 */
	@Expose
    private short repeatSettings = AlarmConstants.ALLDAYS;

	/**
	 * The ID of this alarm. generated randomly.
	 */
	@Expose
    private String id;
    
    /**
     * The hour for which this alarm is set.
     */
    @Expose
    private int hour; // 24-HOUR TIME, DON'T GET TRICKED LOL

    /**
     * The minute for which this alarm is set.
     */
    @Expose
    private int minute;
    
    /**
     * The new minute an alarm was set for, due to being snoozed.
     */
    @Expose
    private int tempSnoozeMinute; 
    /**
     * The new hour an alarm was set for, due to being snoozed.
     */
    @Expose
    private int tempSnoozeHour;

    /**
     * indicates whether the alarm is currently ringing.
     */
    @Expose
    private boolean active;

    /**
     * indicates whether the alarm is snoozing.
     */
    @Expose
    private boolean snoozing;

    /**
     * indicates whether the alarm is enabled (set to go off).
     */
    @Expose
    private boolean enabled;

    @Expose
    private String alarmLabel = AlarmConstants.DEFAULT_LABEL;
    

    @Expose
    private String ringtone = AlarmConstants.DEFAULT_RINGTONE;

    /**
     * standard constructor, creates a unique id for the new alarm.
     * @param hour The (24-hour) hour of the alarm time.
     * @param minute The (0-59) minute of the alarm time.
     */
    public Alarm(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;

        // Make some special unique DNA for the new alarm bb
        this.id = java.util.UUID.randomUUID().toString();

        active = false;
        snoozing = false;
        enabled = true;
    }

    /**
     * Activates the Alarm.
     */
    public void activate() {
        active = true;
        snoozing = false;
    }
    
    /**
     * Stops the ringing alarm.
     */
    public void dismiss() {
        //Set the Alarm switch to "disabled" unless the Alarm is snoozing
        if (!snoozing) {

            // If this alarm repeats, just call the function to enable, which
            // handles the settings for repeating alarms.
            if (isRepeatEnabled()) {
                enable();
            } else {
            
                disable();
            }
        }

        active = false;
        snoozing = false;
    }

    /**
     * Snoozes the alarm.
     * Sets snoozing to true and updates the desired length of snoooooze.
     * @param snoozeLength an integer number of minutes to snooze the alarm for.
     */
    public void snooze(int snoozeLength) {

    	//Compute the temporary time to set an alarm for, based on the current time.
    	Calendar c = Calendar.getInstance();
    	c.set(Calendar.SECOND, 0);
    	c.add(Calendar.MINUTE, snoozeLength);
    	
    	// sets the target snooze time for snoozeLength minutes from the current time.
    	// straight up, dog.
    	tempSnoozeHour = c.get(Calendar.HOUR_OF_DAY);
    	tempSnoozeMinute = c.get(Calendar.MINUTE);

        active = false;
        snoozing = true;
    }



    /**
     * Sets the alarm to go off.
     * now it's basically just a boolean
     * for the alarmManager to check.
     */
    public void enable() {
        enabled = true;
    }
    
    /**
     * Prevents the alarm from going off (cancels it).
     */
    public void disable() {

    	snoozing = false;
        enabled = false;
    }

    /**
     * Current working version of a function that changes alarm settings.
     * later on it'll also probably pass a label. next sprint problems.
     * @param repeatSettings The days of a week an alarm should repeat.
     * @param hour The new hour time of the alarm.
     * @param minute The new minute time of the alarm.
     * @param label The label for this alarm.
     * @param tone The name of the ringtone for this alarm.
     */
    public void changeSettings(short repeatSettings, int hour, int minute, String tone, String label) {
        // Though unnecessary, make things easy on ourselves by treating all updates like a big
        // settings change, at least for now.
        this.repeatSettings = repeatSettings;

        //no repeating enabled if there are no days to repeat!
        if ((this.repeatSettings & AlarmConstants.ALLDAYS) == 0) { this.repeatSettings = 0; }

        //no repeating enabled if there are no days to repeat!
        if ((repeatSettings & AlarmConstants.ALLDAYS) == 0) { repeatSettings = 0; }

        setTime(hour, minute);
        
        setRingtone(tone);
        
        setAlarmLabel(label);
        
        

        // If the alarm is already disabled, we don't have to do anything. Otherwise...
        if (isEnabled()) {
            disable();
            enable();
        }


    }

    public void setTime(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }
    
    public int getTempSnoozeHour() {
        return tempSnoozeHour;
    }

    public int getTempSnoozeMinute() {
        return tempSnoozeMinute;
    }

    public String getId() {
        return id;
    }

    public boolean isActive() {
        return active;
    }

    public boolean isSnoozing() {
        return snoozing;
    }

    public boolean isEnabled() {
        return enabled;
    }
    
    public String getRingtone() {
    	return ringtone;
    }
    
    public void setRingtone(String ringtone) {
    	this.ringtone = ringtone;
    }
    
    public void setAlarmLabel(String label) { 
    	alarmLabel = label;
    }
    
    public String getAlarmLabel() {
    	return alarmLabel;
    }

    public boolean isRepeatEnabled() {
    	return ((repeatSettings & AlarmConstants.REPEAT_ENABLED) == 0) ? false : true; 
    }

    public boolean isEnabledForDay(int day) {
    	return ((AlarmConstants.WEEK_ARRAY[day] & repeatSettings) != 0);
    }
    
    public short getRepeatSettings() {
    	return repeatSettings;
    }

    //public setRingtone() //TODO: implement ability to change ringtone (in future sprint)

    /**
     * For the purpose of display, shows a textual view
     * of the days an alarm is set to repeat.
     * @return A string containing a comma-delimited list of the days an alarm is set for.
     */
    public String repeatDaysToString() {
        String result = " ";
        if (!isRepeatEnabled()) {
            return result;
        }
        boolean first = true;
        for (int i = 0; i < 7; i++) {
            if ((repeatSettings & AlarmConstants.WEEK_ARRAY[i]) != 0) { // enabled for that day
                if (!first) {
                    result += ", ";
                }
                first = false;
                result += (AlarmConstants.WEEK_LABELS[i]);
            }
        }

        return result;
    }
}
