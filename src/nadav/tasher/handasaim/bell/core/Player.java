package nadav.tasher.handasaim.bell.core;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class Player {
    private static final double ringtoneLength = 20;
    private static Ringtone ringtone;
    private static Media ringtoneMedia;
    private static Timeline fader;
    private static MediaPlayer mediaPlayer;

    public static void play(int ring) {
        try {
            ringtone = Settings.getRingtone(ring);
            ringtoneMedia = new Media(ringtone.getFile().toURI().toString());
            mediaPlayer = new MediaPlayer(ringtoneMedia);
            mediaPlayer.setStartTime(Duration.seconds(ringtone.getTime()));
            mediaPlayer.setStopTime(Duration.seconds(ringtone.getTime() + ringtoneLength));
            fader = new Timeline(
                    new KeyFrame(Duration.seconds(ringtoneLength),
                            new KeyValue(mediaPlayer.volumeProperty(), 0)));
            fader.play();
            mediaPlayer.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void init() {
        new JFXPanel();
    }
}
