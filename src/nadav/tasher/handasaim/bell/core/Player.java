package nadav.tasher.handasaim.bell.core;

import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class Player {

    private static Media ringtone;
    private static MediaPlayer mediaPlayer;

    public static void play(int ring){
        try {
            ringtone = new Media(Settings.getRingtone(ring).toURI().toString());
            mediaPlayer = new MediaPlayer(ringtone);
            mediaPlayer.play();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void init(){
        new JFXPanel();
    }

}
