package nadav.tasher.handasaim.bell.core;

import java.util.Timer;
import java.util.TimerTask;

import static nadav.tasher.handasaim.bell.core.Schedule.ring;

public class Watch {

    private static Timer timer=new Timer();
    private static final int LOOP_TIME=20000;
    private static int lastRing=-1;

    public static void init(){
        timer.scheduleAtFixedRate(new TimerTask() {
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
        },0,LOOP_TIME);
    }

}
