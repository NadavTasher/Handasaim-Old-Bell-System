package nadav.tasher.handasaim.bell.core;

import java.io.File;
import java.net.URISyntaxException;

import static nadav.tasher.handasaim.bell.Main.VERSION;
import static nadav.tasher.handasaim.bell.core.Utils.readFile;

public class OTA {
    private static final String remoteVersion = "https://nockio.com/h/bell/main/version.txt";
    private static final String remoteSoftware = "https://nockio.com/h/bell/main/latest.jar";
    private static final File homeDirectory = new File(System.getProperty("user.dir"));

    private static File software() throws URISyntaxException {
        return new File(OTA.class.getProtectionDomain().getCodeSource().getLocation().toURI());
    }

    public static void check() {
        new Utils.Download(remoteVersion, new File(homeDirectory, "version.txt"), new Utils.Download.Callback() {
            @Override
            public void onSuccess(File file) {
                if (Double.parseDouble(readFile(file)) > VERSION) {
                    System.out.println("echo Software update found");
                    try {
                        new Utils.Download(remoteSoftware, software(), new Utils.Download.Callback() {
                            @Override
                            public void onSuccess(File file) {
                                System.out.println("echo Updated");
                            }

                            @Override
                            public void onFailure(Exception e) {
                            }
                        }).execute();
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("echo Software update not found");
                }
            }

            @Override
            public void onFailure(Exception e) {
            }
        }).execute();
    }
}
