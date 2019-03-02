package nadav.tasher.handasaim.bell;

import nadav.tasher.handasaim.bell.core.OTA;
import nadav.tasher.handasaim.bell.core.Ringtone;
import nadav.tasher.handasaim.bell.core.Settings;

import static nadav.tasher.handasaim.bell.core.Schedule.currentRingtone;
import static nadav.tasher.handasaim.bell.core.Utils.filter;

public class Main {
    public static final double VERSION = 0.5;

    public static void main(String[] args) {
        if (args.length > 0) {
            switch (args[0]) {
                case "play":
                    Settings.load();
                    String command;
                    int ring = currentRingtone();
                    if (args.length > 1) {
                        try {
                            ring = Integer.parseInt(args[1]);
                        } catch (Exception ignored) {
                        }
                    }
                    if (ring != -1) {
                        Ringtone ringtone = Settings.getRingtone(ring);
                        String timestamp;
                        int minute = (int) (ringtone.getTime() / 60);
                        double seconds = (ringtone.getTime() % 60);
                        timestamp = "00:" + minute + ":" + seconds;
                        command = "ffplay -i " + filter(ringtone.getFile().toString()) + " -ss " + timestamp + " -t " + Settings.length() + " -v error -nodisp -autoexit";
                    } else {
                        command = "echo Not The Time Yet";
                    }
                    System.out.print(command);
                    break;
                case "update":
                    Settings.update();
                    System.out.println("echo Updated");
                    break;
                case "checkforupdate":
                    OTA.check();
                    System.out.println("echo OTA Looped.");
                    break;
                default:
                    System.out.println("echo Unknown Command.");
                    break;
            }
        }
    }
}
