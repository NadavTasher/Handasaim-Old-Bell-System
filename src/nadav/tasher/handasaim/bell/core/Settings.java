package nadav.tasher.handasaim.bell.core;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;

import static nadav.tasher.handasaim.bell.core.Utils.readFile;

public class Settings {
    private static final File defaultRing = new File(Settings.class.getClassLoader().getResource("nadav/tasher/handasaim/bell/resources/default.mp3").getFile());
    private static final String remoteSettings = "https://nockio.com/h/bell/main/settings.json";
    private static final File localSettings = new File(System.getProperty("user.dir"), "settings.json");
    private static JSONObject lastSettings = null;
    private static JSONObject currentSettings = null;

    public static void reload() {
        if (localSettings.exists()) {
            currentSettings = new JSONObject(readFile(localSettings));
        }
        downloadRemote();
    }

    private static void downloadQueue() {
        JSONArray ringList = currentSettings.getJSONArray("ringtones");
        for (int s = 0; s < ringList.length(); s++) {
            System.out.println("Downloading " + ringList.getString(s) + " into ring " + s);
            new Utils.Download(ringList.getString(s), new File(localSettings.getParentFile(), "ring" + s + ".mp3"), new Utils.Download.Callback() {
                @Override
                public void onSuccess(File file) {
                    System.out.println("Done " + file.toString());
                }

                @Override
                public void onFailure(Exception e) {
                }
            });
        }
    }

    public static File getRingtone(int index) {
        File wannabeRingtone = new File(localSettings.getParentFile(), "ring" + index + ".mp3");
        if (wannabeRingtone.exists()) {
            return wannabeRingtone;
        }
        return defaultRing;
    }

    private static void downloadRemote() {
        new Utils.Download(remoteSettings, localSettings, new Utils.Download.Callback() {
            @Override
            public void onSuccess(File file) {
                lastSettings = currentSettings;
                try {
                    currentSettings = new JSONObject(readFile(file));
                    downloadQueue();
                } catch (Exception e) {
                    currentSettings = lastSettings;
                }
            }

            @Override
            public void onFailure(Exception e) {
            }
        }).execute();
    }
}
