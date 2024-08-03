package com.mojang.realmsclient.client;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.logging.LogUtils;
import com.mojang.realmsclient.dto.UploadInfo;
import com.mojang.realmsclient.gui.screens.UploadResult;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.client.User;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.Args;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;

public class FileUpload {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final int MAX_RETRIES = 5;

    private static final String UPLOAD_PATH = "/upload";

    private final File file;

    private final long worldId;

    private final int slotId;

    private final UploadInfo uploadInfo;

    private final String sessionId;

    private final String username;

    private final String clientVersion;

    private final UploadStatus uploadStatus;

    private final AtomicBoolean cancelled = new AtomicBoolean(false);

    @Nullable
    private CompletableFuture<UploadResult> uploadTask;

    private final RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout((int) TimeUnit.MINUTES.toMillis(10L)).setConnectTimeout((int) TimeUnit.SECONDS.toMillis(15L)).build();

    public FileUpload(File file0, long long1, int int2, UploadInfo uploadInfo3, User user4, String string5, UploadStatus uploadStatus6) {
        this.file = file0;
        this.worldId = long1;
        this.slotId = int2;
        this.uploadInfo = uploadInfo3;
        this.sessionId = user4.getSessionId();
        this.username = user4.getName();
        this.clientVersion = string5;
        this.uploadStatus = uploadStatus6;
    }

    public void upload(Consumer<UploadResult> consumerUploadResult0) {
        if (this.uploadTask == null) {
            this.uploadTask = CompletableFuture.supplyAsync(() -> this.requestUpload(0));
            this.uploadTask.thenAccept(consumerUploadResult0);
        }
    }

    public void cancel() {
        this.cancelled.set(true);
        if (this.uploadTask != null) {
            this.uploadTask.cancel(false);
            this.uploadTask = null;
        }
    }

    private UploadResult requestUpload(int int0) {
        UploadResult.Builder $$1 = new UploadResult.Builder();
        if (this.cancelled.get()) {
            return $$1.build();
        } else {
            this.uploadStatus.totalBytes = this.file.length();
            HttpPost $$2 = new HttpPost(this.uploadInfo.getUploadEndpoint().resolve("/upload/" + this.worldId + "/" + this.slotId));
            CloseableHttpClient $$3 = HttpClientBuilder.create().setDefaultRequestConfig(this.requestConfig).build();
            UploadResult var8;
            try {
                this.setupRequest($$2);
                HttpResponse $$4 = $$3.execute($$2);
                long $$5 = this.getRetryDelaySeconds($$4);
                if (!this.shouldRetry($$5, int0)) {
                    this.handleResponse($$4, $$1);
                    return $$1.build();
                }
                var8 = this.retryUploadAfter($$5, int0);
            } catch (Exception var12) {
                if (!this.cancelled.get()) {
                    LOGGER.error("Caught exception while uploading: ", var12);
                }
                return $$1.build();
            } finally {
                this.cleanup($$2, $$3);
            }
            return var8;
        }
    }

    private void cleanup(HttpPost httpPost0, @Nullable CloseableHttpClient closeableHttpClient1) {
        httpPost0.releaseConnection();
        if (closeableHttpClient1 != null) {
            try {
                closeableHttpClient1.close();
            } catch (IOException var4) {
                LOGGER.error("Failed to close Realms upload client");
            }
        }
    }

    private void setupRequest(HttpPost httpPost0) throws FileNotFoundException {
        httpPost0.setHeader("Cookie", "sid=" + this.sessionId + ";token=" + this.uploadInfo.getToken() + ";user=" + this.username + ";version=" + this.clientVersion);
        FileUpload.CustomInputStreamEntity $$1 = new FileUpload.CustomInputStreamEntity(new FileInputStream(this.file), this.file.length(), this.uploadStatus);
        $$1.setContentType("application/octet-stream");
        httpPost0.setEntity($$1);
    }

    private void handleResponse(HttpResponse httpResponse0, UploadResult.Builder uploadResultBuilder1) throws IOException {
        int $$2 = httpResponse0.getStatusLine().getStatusCode();
        if ($$2 == 401) {
            LOGGER.debug("Realms server returned 401: {}", httpResponse0.getFirstHeader("WWW-Authenticate"));
        }
        uploadResultBuilder1.withStatusCode($$2);
        if (httpResponse0.getEntity() != null) {
            String $$3 = EntityUtils.toString(httpResponse0.getEntity(), "UTF-8");
            if ($$3 != null) {
                try {
                    JsonParser $$4 = new JsonParser();
                    JsonElement $$5 = $$4.parse($$3).getAsJsonObject().get("errorMsg");
                    Optional<String> $$6 = Optional.ofNullable($$5).map(JsonElement::getAsString);
                    uploadResultBuilder1.withErrorMessage((String) $$6.orElse(null));
                } catch (Exception var8) {
                }
            }
        }
    }

    private boolean shouldRetry(long long0, int int1) {
        return long0 > 0L && int1 + 1 < 5;
    }

    private UploadResult retryUploadAfter(long long0, int int1) throws InterruptedException {
        Thread.sleep(Duration.ofSeconds(long0).toMillis());
        return this.requestUpload(int1 + 1);
    }

    private long getRetryDelaySeconds(HttpResponse httpResponse0) {
        return (Long) Optional.ofNullable(httpResponse0.getFirstHeader("Retry-After")).map(NameValuePair::getValue).map(Long::valueOf).orElse(0L);
    }

    public boolean isFinished() {
        return this.uploadTask.isDone() || this.uploadTask.isCancelled();
    }

    static class CustomInputStreamEntity extends InputStreamEntity {

        private final long length;

        private final InputStream content;

        private final UploadStatus uploadStatus;

        public CustomInputStreamEntity(InputStream inputStream0, long long1, UploadStatus uploadStatus2) {
            super(inputStream0);
            this.content = inputStream0;
            this.length = long1;
            this.uploadStatus = uploadStatus2;
        }

        public void writeTo(OutputStream outputStream0) throws IOException {
            Args.notNull(outputStream0, "Output stream");
            InputStream $$1 = this.content;
            try {
                byte[] $$2 = new byte[4096];
                int $$3;
                if (this.length < 0L) {
                    while (($$3 = $$1.read($$2)) != -1) {
                        outputStream0.write($$2, 0, $$3);
                        this.uploadStatus.bytesWritten += (long) $$3;
                    }
                } else {
                    long $$4 = this.length;
                    while ($$4 > 0L) {
                        $$3 = $$1.read($$2, 0, (int) Math.min(4096L, $$4));
                        if ($$3 == -1) {
                            break;
                        }
                        outputStream0.write($$2, 0, $$3);
                        this.uploadStatus.bytesWritten += (long) $$3;
                        $$4 -= (long) $$3;
                        outputStream0.flush();
                    }
                }
            } finally {
                $$1.close();
            }
        }
    }
}