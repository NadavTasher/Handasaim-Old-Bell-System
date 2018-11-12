package nadav.tasher.handasaim.bell.core;

import org.json.JSONObject;

import java.io.File;

public class Settings {

    private static final File defaultRing=new File(Settings.class.getClassLoader().getResource("nadav/tasher/handasaim/bell/resources/default.mp3").getFile());

    private static JSONObject lastSettings=null;
    private static JSONObject currentSettings=null;

    public static File getRing(int ring){
        return defaultRing;
    }

    public static void reload(){
        currentSettings=new JSONObject();
    }

}
