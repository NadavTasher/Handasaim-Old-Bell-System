package nadav.tasher.handasaim.bell.core;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import static nadav.tasher.handasaim.bell.core.Utils.md5;
import static nadav.tasher.handasaim.bell.core.Utils.readFile;

public class Settings {
    public static final String QUEUE = "queue";
    private static final String ringtoneFileName = "RingtoneXX.mp3";
    private static final String remoteSettings = "https://nockio.com/h/bell/main/settings.json", remoteFolder = "https://nockio.com/h/bell/rings/";
    private static final File homeDirectory = new File(System.getProperty("user.dir"));
    private static final File localSettings = new File(homeDirectory, "settings.json");
    private static final File ringDirectory = new File(homeDirectory, "ringtones");
    private static JSONObject currentSettings = null;
    private static ArrayList<Ringtone> queue = new ArrayList<>();

    public static void load() {
        try {
            currentSettings = new JSONObject(readFile(localSettings));
            queue = queueForSettings(currentSettings);
        } catch (Exception e) {
        }
    }

    public static void update() {
        if (localSettings.exists()) {
            load();
        }
        if (!ringDirectory.exists()) ringDirectory.mkdirs();
        downloadRemote();
    }

    private static ArrayList<Ringtone> queueForSettings(JSONObject settings) {
        ArrayList<Ringtone> ringtones = new ArrayList<>();
        if (settings != null) {
            if (settings.has(QUEUE)) {
                JSONArray queue = settings.getJSONArray(QUEUE);
                for (int ringtone = 0; ringtone < queue.length(); ringtone++) {
                    Ringtone newRingtone = new Ringtone(queue.getJSONObject(ringtone));
                    newRingtone.setFile(new File(ringDirectory, ringtoneFileName.replace("XX", String.valueOf(ringtone))));
                    ringtones.add(newRingtone);
                }
            }
        }
        return ringtones;
    }

    private static ArrayList<Utils.Download> downloadsForQueue(ArrayList<Ringtone> queue) {
        ArrayList<Utils.Download> downloads = new ArrayList<>();
        for (Ringtone ringtone : queue) {
            String md5 = md5(ringtone.getFile());
            if (md5 == null || !md5.toUpperCase().equals(ringtone.getMD5().toUpperCase())) {
                System.out.println(ringtone.getLink());
                downloads.add(new Utils.Download(remoteFolder + ringtone.getLink(), ringtone.getFile(), null));
            }
        }
        return downloads;
    }

    public static Ringtone getRingtone(int index) {
        if (index >= 0 && index < queue.size()) return queue.get(index);
        return new Ringtone().setFile(null).setLink(null).setTime(0);
    }

    public static int length() {
        return currentSettings.optInt("length", 25);
    }

    public static boolean muted() {
        return currentSettings.optBoolean("mute",false);
    }

    private static void downloadRemote() {
        new Utils.Download(remoteSettings, localSettings, new Utils.Download.Callback() {
            @Override
            public void onSuccess(File file) {
                load();
                for (Utils.Download download : downloadsForQueue(queue)) {
                    download.execute();
                }
            }

            @Override
            public void onFailure(Exception e) {
            }
        }).execute();
    }
}
