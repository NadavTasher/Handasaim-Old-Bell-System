package nadav.tasher.handasaim.bell;

import nadav.tasher.handasaim.bell.core.Player;
import nadav.tasher.handasaim.bell.core.Settings;
import nadav.tasher.handasaim.bell.core.Watch;

public class Main {

    public static void main(String[] args) {
        Player.init();
        Settings.load();
        Watch.initSettings();
        Watch.initBell();
        Player.play(0);
    }
}
