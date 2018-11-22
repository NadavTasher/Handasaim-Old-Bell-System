package nadav.tasher.handasaim.bell;

import nadav.tasher.handasaim.bell.core.Ringtone;
import nadav.tasher.handasaim.bell.core.Settings;

import static nadav.tasher.handasaim.bell.core.Schedule.ring;

public class Main {

    public static final double VERSION=0.1;

    public static void main(String[] args) {
        if(args.length>0) {
            if(args[0].equals("play")) {
                Settings.load();
                String command="";
                int ring=ring();
                if(ring!=-1){
                    Ringtone ringtone=Settings.getRingtone(ring);
                    String timestamp="";
                    int minute= (int) (ringtone.getTime()/60);
                    double seconds=(ringtone.getTime()%60);
                    timestamp="00:"+minute+":"+seconds;
                    command="ffplay -i "+ringtone.getFile().toString()+" -ss "+timestamp+" -t 20 -nodisp -autoexit";
                }else{
                    command="echo Not The Time Yet";
                }
                System.out.print(command);
            }else if(args[0].equals("update")){
                Settings.reload();
                System.out.println("echo Updated");
            }else{
                System.out.println("echo Unknown Command.");
            }
        }
    }
}
