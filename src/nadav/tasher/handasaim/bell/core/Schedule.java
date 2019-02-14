package nadav.tasher.handasaim.bell.core;

import java.util.Calendar;

public class Schedule {
    public static final int[] ringTimes = {465, 510, 555, 600, 615, 660, 705, 730, 775, 820, 830, 875, 920, 930, 975, 1020, 1065, 1110};

    public static int currentRingtone() {
        for (int time=0;time<ringTimes.length;time++){
            if(minuteOfDay()==ringTimes[time])return time;
        }
        return -1;
    }

    public static int minuteOfDay() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        return hour * 60 + minute;
    }
}
