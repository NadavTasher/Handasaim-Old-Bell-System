package nadav.tasher.handasaim.bell.core;

import java.io.File;

public class Ringtone {
    private double time = 0;
    private File file = null;
    private String link = null;

    public Ringtone() {
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
        this.link = link;
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null) {
            if (obj instanceof Ringtone) {
                Ringtone compare = (Ringtone) obj;
                return getFile().toString().equals(compare.getFile().toString()) && getLink().equals(compare.getLink());
            }
        }
        return false;
    }
}
