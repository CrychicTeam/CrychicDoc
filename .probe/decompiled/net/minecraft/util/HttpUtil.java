package net.minecraft.util;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.mojang.logging.LogUtils;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.ServerSocket;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import javax.annotation.Nullable;
import net.minecraft.DefaultUncaughtExceptionHandler;
import net.minecraft.network.chat.Component;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;

public class HttpUtil {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static final ListeningExecutorService DOWNLOAD_EXECUTOR = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool(new ThreadFactoryBuilder().setDaemon(true).setUncaughtExceptionHandler(new DefaultUncaughtExceptionHandler(LOGGER)).setNameFormat("Downloader %d").build()));

    private HttpUtil() {
    }

    public static CompletableFuture<?> downloadTo(File file0, URL uRL1, Map<String, String> mapStringString2, int int3, @Nullable ProgressListener progressListener4, Proxy proxy5) {
        return CompletableFuture.supplyAsync(() -> {
            HttpURLConnection $$6 = null;
            InputStream $$7 = null;
            OutputStream $$8 = null;
            if (progressListener4 != null) {
                progressListener4.progressStart(Component.translatable("resourcepack.downloading"));
                progressListener4.progressStage(Component.translatable("resourcepack.requesting"));
            }
            try {
                byte[] $$9 = new byte[4096];
                $$6 = (HttpURLConnection) uRL1.openConnection(proxy5);
                $$6.setInstanceFollowRedirects(true);
                float $$10 = 0.0F;
                float $$11 = (float) mapStringString2.entrySet().size();
                for (Entry<String, String> $$12 : mapStringString2.entrySet()) {
                    $$6.setRequestProperty((String) $$12.getKey(), (String) $$12.getValue());
                    if (progressListener4 != null) {
                        progressListener4.progressStagePercentage((int) (++$$10 / $$11 * 100.0F));
                    }
                }
                $$7 = $$6.getInputStream();
                $$11 = (float) $$6.getContentLength();
                int $$13 = $$6.getContentLength();
                if (progressListener4 != null) {
                    progressListener4.progressStage(Component.translatable("resourcepack.progress", String.format(Locale.ROOT, "%.2f", $$11 / 1000.0F / 1000.0F)));
                }
                if (file0.exists()) {
                    long $$14 = file0.length();
                    if ($$14 == (long) $$13) {
                        if (progressListener4 != null) {
                            progressListener4.stop();
                        }
                        return null;
                    }
                    LOGGER.warn("Deleting {} as it does not match what we currently have ({} vs our {}).", new Object[] { file0, $$13, $$14 });
                    FileUtils.deleteQuietly(file0);
                } else if (file0.getParentFile() != null) {
                    file0.getParentFile().mkdirs();
                }
                $$8 = new DataOutputStream(new FileOutputStream(file0));
                if (int3 > 0 && $$11 > (float) int3) {
                    if (progressListener4 != null) {
                        progressListener4.stop();
                    }
                    throw new IOException("Filesize is bigger than maximum allowed (file is " + $$10 + ", limit is " + int3 + ")");
                } else {
                    int $$15;
                    while (($$15 = $$7.read($$9)) >= 0) {
                        $$10 += (float) $$15;
                        if (progressListener4 != null) {
                            progressListener4.progressStagePercentage((int) ($$10 / $$11 * 100.0F));
                        }
                        if (int3 > 0 && $$10 > (float) int3) {
                            if (progressListener4 != null) {
                                progressListener4.stop();
                            }
                            throw new IOException("Filesize was bigger than maximum allowed (got >= " + $$10 + ", limit was " + int3 + ")");
                        }
                        if (Thread.interrupted()) {
                            LOGGER.error("INTERRUPTED");
                            if (progressListener4 != null) {
                                progressListener4.stop();
                            }
                            return null;
                        }
                        $$8.write($$9, 0, $$15);
                    }
                    if (progressListener4 != null) {
                        progressListener4.stop();
                    }
                    return null;
                }
            } catch (Throwable var21) {
                LOGGER.error("Failed to download file", var21);
                if ($$6 != null) {
                    InputStream $$17 = $$6.getErrorStream();
                    try {
                        LOGGER.error("HTTP response error: {}", IOUtils.toString($$17, StandardCharsets.UTF_8));
                    } catch (IOException var20) {
                        LOGGER.error("Failed to read response from server");
                    }
                }
                if (progressListener4 != null) {
                    progressListener4.stop();
                }
                return null;
            } finally {
                IOUtils.closeQuietly($$7);
                IOUtils.closeQuietly($$8);
            }
        }, DOWNLOAD_EXECUTOR);
    }

    public static int getAvailablePort() {
        try {
            ServerSocket $$0 = new ServerSocket(0);
            int var1;
            try {
                var1 = $$0.getLocalPort();
            } catch (Throwable var4) {
                try {
                    $$0.close();
                } catch (Throwable var3) {
                    var4.addSuppressed(var3);
                }
                throw var4;
            }
            $$0.close();
            return var1;
        } catch (IOException var5) {
            return 25564;
        }
    }

    public static boolean isPortAvailable(int int0) {
        if (int0 >= 0 && int0 <= 65535) {
            try {
                ServerSocket $$1 = new ServerSocket(int0);
                boolean var2;
                try {
                    var2 = $$1.getLocalPort() == int0;
                } catch (Throwable var5) {
                    try {
                        $$1.close();
                    } catch (Throwable var4) {
                        var5.addSuppressed(var4);
                    }
                    throw var5;
                }
                $$1.close();
                return var2;
            } catch (IOException var6) {
                return false;
            }
        } else {
            return false;
        }
    }
}