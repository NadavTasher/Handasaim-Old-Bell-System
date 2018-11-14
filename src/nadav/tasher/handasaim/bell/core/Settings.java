package nadav.tasher.handasaim.bell.core;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import static nadav.tasher.handasaim.bell.core.Utils.readFile;

public class Settings {
    public static final String QUEUE = "queue", LINK = "link", TIME = "time";
    private static final String ringtoneFileName = "Ringtone(XX).mp3";
    private static final File defaultRing = new File(Settings.class.getClassLoader().getResource("nadav/tasher/handasaim/bell/resources/default.mp3").getFile());
    private static final String remoteSettings = "https://nockio.com/h/bell/main/settings.json";
    private static final File homeDirectory = new File(System.getProperty("user.dir"));
    private static final File localSettings = new File(homeDirectory, "settings.json");
    private static final File ringDirectory = new File(homeDirectory, "ringtones");
    private static JSONObject lastSettings = null;
    private static JSONObject currentSettings = null;
    private static ArrayList<Ringtone> queue = new ArrayList<>();

    public static void reload() {
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
                    Ringtone newRingtone = new Ringtone();
                    try {
                        JSONObject currentRingtone = queue.getJSONObject(ringtone);
                        if (currentRingtone.has(LINK)) {
                            newRingtone.setLink(currentRingtone.getString(LINK));
                        }
                        if (currentRingtone.has(TIME)) {
                            newRingtone.setTime(currentRingtone.getNumber(TIME).doubleValue());
                        }
                    } catch (Exception ignored) {
                    }
                    newRingtone.setFile(new File(ringDirectory, ringtoneFileName.replace("XX", String.valueOf(ringtone))));
                    ringtones.add(newRingtone);
                }
            }
        }
        return ringtones;
    }

    private static void downloadQueue(ArrayList<Ringtone> newQueue) {
        for (int s = 0; s < newQueue.size(); s++) {
            // Check If Download Is Needed
            if (newQueue.get(s) != null) {
                // Download
                System.out.println("Downloading " + newQueue.get(s).getLink() + " into " + newQueue.get(s).getFile().getName());
                new Utils.Download(newQueue.get(s).getLink(), newQueue.get(s).getFile(), new Utils.Download.Callback() {
                    @Override
                    public void onSuccess(File file) {
                        System.out.println("Done " + file.toString());
                    }

                    @Override
                    public void onFailure(Exception e) {
                        e.printStackTrace();
                    }
                }).execute();
            }
        }
    }

    public static Ringtone getRingtone(int index) {
        if (index < queue.size()) return queue.get(index);
        return new Ringtone().setFile(defaultRing).setLink(null).setTime(0);
    }

    public static void load() {
        currentSettings = new JSONObject(readFile(localSettings));
        queue = queueForSettings(currentSettings);
    }

    private static void downloadRemote() {
        new Utils.Download(remoteSettings, localSettings, new Utils.Download.Callback() {
            @Override
            public void onSuccess(File file) {
                lastSettings = currentSettings;
                try {
                    load();
                    ArrayList<Ringtone> compared = compareQueues(queueForSettings(lastSettings), queueForSettings(currentSettings));
                    if (compared != null) {
                        downloadQueue(compared);
                    }
                } catch (Exception e) {
                    currentSettings = lastSettings;
                }
            }

            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
            }
        }).execute();
    }

    private static ArrayList<Ringtone> compareQueues(ArrayList<Ringtone> last, ArrayList<Ringtone> current) {
        try {
            if (current != null) {
                if (last != null) {
                    if (current.size() == last.size()) {
                        for (int index = 0; index < current.size(); index++) {
                            if (current.get(index) != null) {
                                if (current.get(index).equals(last.get(index))) {
                                    current.set(index, null);
                                }
                            }
                        }
                    }
                }
                return current;
            }
        } catch (Exception ignored) {
        }
        return null;
    }
}
