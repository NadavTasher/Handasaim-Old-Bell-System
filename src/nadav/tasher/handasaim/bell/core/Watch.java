package nadav.tasher.handasaim.bell.core;

import java.util.Timer;
import java.util.TimerTask;

import static nadav.tasher.handasaim.bell.core.Schedule.ring;

public class Watch {
    private static final int LOOP_TIME_BELL=10000;
    private static final int LOOP_TIME_SETTINGS=1200000;
    private static int lastRing=-1;

    public static void initBell(){
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try{
                    int currentRing=ring();
                    if(currentRing==0){
                        lastRing=-1;
                    }
                    if(currentRing>lastRing) {
                        if (currentRing != -1) {
                            lastRing=currentRing;
                            Player.play(currentRing);
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        },0,LOOP_TIME_BELL);
    }

    public static void initSettings(){
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Settings.reload();
            }
        },0,LOOP_TIME_SETTINGS);
    }

}
