package me.lucko.spark.common.util;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.zip.GZIPOutputStream;
import me.lucko.spark.lib.protobuf.AbstractMessageLite;

public class BytebinClient {

    private final String url;

    private final String userAgent;

    public BytebinClient(String url, String userAgent) {
        this.url = url + (url.endsWith("/") ? "" : "/");
        this.userAgent = userAgent;
    }

    private BytebinClient.Content postContent(String contentType, Consumer<OutputStream> consumer, String userAgentExtra) throws IOException {
        String userAgent = userAgentExtra != null ? this.userAgent + "/" + userAgentExtra : this.userAgent;
        URL url = new URL(this.url + "post");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        BytebinClient.Content var8;
        try {
            connection.setConnectTimeout((int) TimeUnit.SECONDS.toMillis(10L));
            connection.setReadTimeout((int) TimeUnit.SECONDS.toMillis(10L));
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", contentType);
            connection.setRequestProperty("User-Agent", userAgent);
            connection.setRequestProperty("Content-Encoding", "gzip");
            connection.connect();
            OutputStream output = connection.getOutputStream();
            try {
                consumer.accept(output);
            } catch (Throwable var15) {
                if (output != null) {
                    try {
                        output.close();
                    } catch (Throwable var14) {
                        var15.addSuppressed(var14);
                    }
                }
                throw var15;
            }
            if (output != null) {
                output.close();
            }
            String key = connection.getHeaderField("Location");
            if (key == null) {
                throw new IllegalStateException("Key not returned");
            }
            var8 = new BytebinClient.Content(key);
        } finally {
            connection.getInputStream().close();
            connection.disconnect();
        }
        return var8;
    }

    public BytebinClient.Content postContent(AbstractMessageLite<?, ?> proto, String contentType, String userAgentExtra) throws IOException {
        return this.postContent(contentType, outputStream -> {
            try {
                OutputStream out = new GZIPOutputStream(outputStream);
                try {
                    proto.writeTo(out);
                } catch (Throwable var6) {
                    try {
                        out.close();
                    } catch (Throwable var5) {
                        var6.addSuppressed(var5);
                    }
                    throw var6;
                }
                out.close();
            } catch (IOException var7) {
                throw new RuntimeException(var7);
            }
        }, userAgentExtra);
    }

    public BytebinClient.Content postContent(AbstractMessageLite<?, ?> proto, String contentType) throws IOException {
        return this.postContent(proto, contentType, null);
    }

    public static final class Content {

        private final String key;

        Content(String key) {
            this.key = key;
        }

        public String key() {
            return this.key;
        }
    }
}