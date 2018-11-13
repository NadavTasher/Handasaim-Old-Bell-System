package nadav.tasher.handasaim.bell.core;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

import static nadav.tasher.handasaim.bell.core.Utils.readFile;

public class Settings {
    public static final String QUEUE = "queue";
    private static final File defaultRing = new File(Settings.class.getClassLoader().getResource("nadav/tasher/handasaim/bell/resources/default.mp3").getFile());
    private static final String remoteSettings = "https://nockio.com/h/bell/main/settings.json";
    private static final File homeDirectory = new File(System.getProperty("user.dir"));
    private static final File localSettings = new File(homeDirectory, "settings.json");
    private static final File ringDirectory = new File(homeDirectory, "ringtones");
    private static JSONObject lastSettings = null;
    private static JSONObject currentSettings = null;

    public static void reload() {
        if (localSettings.exists()) {
            currentSettings = new JSONObject(readFile(localSettings));
        }
        if (!ringDirectory.exists()) ringDirectory.mkdirs();
        downloadRemote();
    }

    private static void downloadQueue(ArrayList<String> newQueue) {
        for (int s = 0; s < newQueue.size(); s++) {
            if (newQueue.get(s) != null) {
                System.out.println("Downloading " + newQueue.get(s) + " into ring " + s);
                new Utils.Download(newQueue.get(s), new File(ringDirectory, "ring" + s + ".mp3"), new Utils.Download.Callback() {
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

    public static File getRingtone(int index) {
        File wannabeRingtone = new File(ringDirectory, "ring" + index + ".mp3");
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
                    ArrayList<String> compared = compareQueues(lastSettings, currentSettings);
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

    private static ArrayList<String> compareQueues(JSONObject lastSettings, JSONObject currentSettings) {
        try {
            if (currentSettings != null) {
                if (currentSettings.has(QUEUE)) {
                    ArrayList<String> currentQueue = new ArrayList<>();
                    JSONArray currentArray = currentSettings.getJSONArray(QUEUE);
                    for (int i = 0; i < currentArray.length(); i++) currentQueue.add(currentArray.getString(i));
                    if (lastSettings != null) {
                        if (lastSettings.has(QUEUE)) {
                            ArrayList<String> lastQueue = new ArrayList<>();
                            JSONArray lastArray = lastSettings.getJSONArray(QUEUE);
                            for (int i = 0; i < lastArray.length(); i++) lastQueue.add(lastArray.getString(i));
                            if (lastQueue.size() != currentQueue.size()) {
                                return currentQueue;
                            } else {
                                for (int i = 0; i < currentQueue.size(); i++) {
                                    if (currentQueue.get(i).equals(lastQueue.get(i))) currentQueue.set(i, null);
                                }
                                return currentQueue;
                            }
                        }else{
                            return currentQueue;
                        }
                    } else {
                        return currentQueue;
                    }
                }
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    private static void emptyRingtones() {
        File[] list = ringDirectory.listFiles();
        if (list != null) {
            for (File file : list) {
                try {
                    Files.delete(file.toPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
