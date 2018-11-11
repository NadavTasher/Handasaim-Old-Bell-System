package nadav.tasher.handasaim.bell.core;

import java.io.File;

public class Settings {

    private static final File defaultRing=new File(Settings.class.getClassLoader().getResource("nadav/tasher/handasaim/bell/resources/default.mp3").getFile());

    public static File getRing(int ring){
        return defaultRing;
    }

}
