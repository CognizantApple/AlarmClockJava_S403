package backend;

public abstract class AlarmConstants {
	public static final String HOUR = "time_hour";
    public static final String MINUTE = "time_minute";
    public static final String TIME_PICKER = "time_picker";

    // Constants used to format and display the time
    public static final String HOUR_MIN_12 = "h:mm";
    public static final String SEC_12 = ":ss aa";
    public static final String NOSEC_12 = "aa";


    // Current format for the date. it's... DEBATE-DATE..Able.
    public static final String DATE_WEEK_D_M_Y = "EEEE dd MMMM, yyyy";

    // Name of the preferences file name, and a few variables.
    // The latter two probably won't be useful in later releases.
    public static final String PREFS_NAME = "AlarmClockFile";
    public static final String ALARM_ID = "AlarmId";
    public static final String ALARM_TIME = "AlarmTime";
    public static final String ALARM_SET = "AlarmSet";
    public static final String DISPLAY_STYLE = "DisplayStyle";
    public static final String ALARM_REPEAT = "AlarmRepeat";

    public static final String AM_CHOICE = "AM";
    public static final String PM_CHOICE = "PM";

    // used in the time display and snooze length bundles
    public static final String SNZ_MINUTE = "snooze_minute";
    public static final String DISPLAY_PICKER = "display_picker";

    /*
    public static final int TWELVE = 0;
    public static final int TWENTY_FOUR = 1;
    public static final int ANALOG = 2;
	*/
    public static final long MINUTE_MILLIS = 60000;

    public static final String DEFAULT_LABEL = "";
    public static final int DEFAULT_SNOOZE_LENGTH = 2;

    //Ok i'm really sorry but ever since i realized you could store all the information
    //about when an alarm repeats in a single byte, i haven't been able to let that idea go
    //if this strategy makes your eyes water and your heart ache, i'll change it to something else.
    public static final short REPEAT_ENABLED =   0x0080;   //(byte) 0b1000_0000;
    public static final short SUNDAY =           0x0040;   //(byte) 0b0100_0000;
    public static final short MONDAY =           0x0020;   //(byte) 0b0010_0000;
    public static final short TUESDAY =          0x0010;   //(byte) 0b0001_0000;
    public static final short WEDNESDAY =        0x0008;   //(byte) 0b0000_1000;
    public static final short THURSDAY =         0x0004;   //(byte) 0b0000_0100;
    public static final short FRIDAY =           0x0002;   //(byte) 0b0000_0010;
    public static final short SATURDAY =         0x0001;   //(byte) 0b0000_0001;

    public static short[] WEEK_ARRAY = {SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY};
    public static String[] WEEK_LABELS = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};

    public static final short ALLDAYS =          0x007F;   //(byte) 0x0111_1111;

    // used to keep track of the ongoing alarm IDs
    public static final String PERSISTENT_ALARM_ID = "persistent_ID";
}
