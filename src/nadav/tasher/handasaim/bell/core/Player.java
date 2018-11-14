package nadav.tasher.handasaim.bell.core;

import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class Player {
    private static Ringtone ringtone;
    private static Media ringtoneMedia;
    private static MediaPlayer mediaPlayer;

    public static void play(int ring) {
        try {
            ringtone = Settings.getRingtone(ring);
            ringtoneMedia = new Media(ringtone.getFile().toURI().toString());
            mediaPlayer = new MediaPlayer(ringtoneMedia);
            mediaPlayer.setStartTime(new Duration(1000 * ringtone.getTime()));
            mediaPlayer.setStopTime(new Duration(1000 * (ringtone.getTime()+20)));
            mediaPlayer.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void init() {
        new JFXPanel();
    }
}
