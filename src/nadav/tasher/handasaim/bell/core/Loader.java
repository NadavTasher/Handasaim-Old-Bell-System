package nadav.tasher.handasaim.bell.core;

import okhttp3.*;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class Loader {
    public static final String remoteSettings = "https://nockio.com/h/bell/main/settings.json";
    public static final File localSettings = new File(System.getProperty("user.dir"), "settings.json");

    public static void reloadSettings() {
        new Download(remoteSettings, localSettings, new Download.Callback() {
            @Override
            public void onSuccess(File file) {
                Settings.reload();
                if(Settings.requeue()){
                    download(Settings.queue());
                }
            }

            @Override
            public void onFailure(Exception e) {
            }
        }).execute();
    }

    private static void download(ArrayList<String> queue){

    }

    public static void request(Request request, Callback callback) {
        getHttpsClient().newCall(request).enqueue(callback);
    }

    public static OkHttpClient getHttpsClient() {
        return new OkHttpClient.Builder().connectionSpecs(Arrays.asList(ConnectionSpec.MODERN_TLS, ConnectionSpec.COMPATIBLE_TLS)).build();
    }

    public static OkHttpClient getHttpClient() {
        return new OkHttpClient();
    }

    public static class Download {
        private String source;
        private File destination;
        private Callback callback;
        private Exception exception;

        public Download(String source, File destination, Callback callback) {
            this.source = source;
            this.destination = destination;
            this.callback = callback;
        }

        public void execute() {
            new Thread(() -> {
                try {
                    Response response;
                    if (source.startsWith("http://")) {
                        response = getHttpClient().newCall(new Request.Builder().url(source).build()).execute();
                    } else {
                        response = getHttpsClient().newCall(new Request.Builder().url(source).build()).execute();
                    }
                    if (response.code() != 200) {
                        exception = new Exception("The server responded with " + response.code());
                        if (response.body() != null) {
                            response.body().close();
                        }
                        onPost(false);
                    } else {
                        if (response.body() != null) {
                            FileOutputStream outputStream = new FileOutputStream(destination);
                            outputStream.write(response.body().bytes());
                            outputStream.close();
                            response.body().close();
                            onPost(true);
                        } else {
                            exception = new Exception("The response body was empty");
                            onPost(false);
                        }
                    }
                } catch (Exception e) {
                    exception = e;
                    onPost(false);
                }
            }).start();
        }

        private void onPost(Boolean success) {
            if (callback != null) {
                if (success) {
                    callback.onSuccess(destination);
                } else {
                    callback.onFailure(exception);
                }
            }
        }

        public interface Callback {
            void onSuccess(File file);

            void onFailure(Exception e);
        }
    }
}
