package nadav.tasher.handasaim.bell.core;

import org.json.JSONObject;

import java.io.File;

import static nadav.tasher.handasaim.bell.core.Utils.filter;

public class Ringtone {
    private double time = 0;
    private File file = null;
    private String link = null;
    private String md5 = null;

    public Ringtone() {
    }

    public Ringtone(JSONObject object) {
        setTime(object.optDouble("time", 0));
        setLink(object.optString("link", null));
        setMD5(object.optString("md5", null));
    }

    public String getMD5() {
        return md5;
    }

    public Ringtone setMD5(String md5) {
        this.md5 = md5;
        return this;
    }

    public double getTime() {
        return time;
    }

    public Ringtone setTime(double time) {
        this.time = time;
        return this;
    }

    public File getFile() {
        return file;
    }

    public Ringtone setFile(File file) {
        this.file = file;
        return this;
    }

    public String getLink() {
        return link;
    }

    public Ringtone setLink(String link) {
        this.link = filter(link);
        return this;
    }
}
