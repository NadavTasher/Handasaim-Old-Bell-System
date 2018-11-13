package nadav.tasher.handasaim.bell.core;

import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.util.Arrays;

public class Utils {
    public static String readFile(File file) {
        StringBuilder output = new StringBuilder();
        try {
            Files.lines(file.toPath()).forEach(s -> output.append(s).append('\n'));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output.toString();
    }

    public static class Download {
        private String source;
        private File destination;
        private Download.Callback callback;
        private Exception exception;

        public Download(String source, File destination, Download.Callback callback) {
            this.source = source;
            this.destination = destination;
            this.callback = callback;
        }

        public static OkHttpClient getHttpsClient() {
            return new OkHttpClient.Builder().connectionSpecs(Arrays.asList(ConnectionSpec.MODERN_TLS, ConnectionSpec.COMPATIBLE_TLS)).build();
        }

        public static OkHttpClient getHttpClient() {
            return new OkHttpClient();
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
                    e.printStackTrace();
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
