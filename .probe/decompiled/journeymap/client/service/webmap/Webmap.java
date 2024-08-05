package journeymap.client.service.webmap;

import info.journeymap.shaded.kotlin.kotlin.Metadata;
import info.journeymap.shaded.kotlin.kotlin.jvm.internal.Intrinsics;
import info.journeymap.shaded.kotlin.spark.Request;
import info.journeymap.shaded.kotlin.spark.Response;
import info.journeymap.shaded.kotlin.spark.Spark;
import info.journeymap.shaded.kotlin.spark.kotlin.HttpKt;
import info.journeymap.shaded.org.jetbrains.annotations.NotNull;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import journeymap.client.Constants;
import journeymap.client.JourneymapClient;
import journeymap.client.io.FileHandler;
import journeymap.client.service.webmap.kotlin.RoutesKt;
import journeymap.common.Journeymap;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.Logger;

@Metadata(mv = { 1, 8, 0 }, k = 1, xi = 48, d1 = { "\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0005\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0012\u0010\u0013\u001a\u00020\u00142\b\b\u0002\u0010\u0015\u001a\u00020\u000eH\u0002J\b\u0010\u0016\u001a\u00020\u0014H\u0002J\u0006\u0010\u0017\u001a\u00020\u0014J\u0006\u0010\u0018\u001a\u00020\u0014R\u0011\u0010\u0003\u001a\u00020\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u001a\u0010\u0007\u001a\u00020\bX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\t\u0010\n\"\u0004\b\u000b\u0010\fR\u001a\u0010\r\u001a\u00020\u000eX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000f\u0010\u0010\"\u0004\b\u0011\u0010\u0012¨\u0006\u0019" }, d2 = { "Ljourneymap/client/service/webmap/Webmap;", "", "()V", "logger", "Lorg/apache/logging/log4j/Logger;", "getLogger", "()Lorg/apache/logging/log4j/Logger;", "port", "", "getPort", "()I", "setPort", "(I)V", "started", "", "getStarted", "()Z", "setStarted", "(Z)V", "findPort", "", "tryCurrentPort", "initialise", "start", "stop", "journeymap" })
public final class Webmap {

    @NotNull
    public static final Webmap INSTANCE = new Webmap();

    @NotNull
    private static final Logger logger;

    private static int port;

    private static boolean started;

    private Webmap() {
    }

    @NotNull
    public final Logger getLogger() {
        return logger;
    }

    public final int getPort() {
        return port;
    }

    public final void setPort(int var1) {
        ???;
    }

    public final boolean getStarted() {
        return started;
    }

    public final void setStarted(boolean var1) {
        ???;
    }

    public final void start() {
        if (!started) {
            findPort$default(this, false, 1, null);
            HttpKt.port(port);
            this.initialise();
            started = true;
            logger.info("Webmap is now listening on port " + port + ".");
        }
    }

    private final void initialise() {
        Spark.initExceptionHandler(Webmap::initialise$lambda$0);
        String assetsRootProperty = System.getProperty("journeymap.webmap.assets_root", null);
        File testFile = new File("../src/main/resources/assets/journeymap/web");
        if (assetsRootProperty != null) {
            logger.info("Detected 'journeymap.webmap.assets_root' property, serving static files from: " + assetsRootProperty);
            HttpKt.getStaticFiles().externalLocation(assetsRootProperty);
        } else if (testFile.exists()) {
            String assets = testFile.getCanonicalPath();
            logger.info("Development environment detected, serving static files from the filesystem.: " + assets);
            HttpKt.getStaticFiles().externalLocation(testFile.getCanonicalPath());
        } else {
            File dir = new File(FileHandler.getMinecraftDirectory(), Constants.WEB_DIR);
            if (!dir.exists()) {
                logger.info("Attempting to copy web content to {}", new File(Constants.JOURNEYMAP_DIR, "web"));
                FileHandler.copyResources(dir, new ResourceLocation("journeymap", "web"), "", false);
            }
            if (dir.exists()) {
                logger.info("Loading web content from: {}", dir.getPath());
                HttpKt.getStaticFiles().externalLocation(dir.getPath());
            } else {
                logger.info("Loading web content from: {}", "/assets/journeymap/web");
                HttpKt.getStaticFiles().location("/assets/journeymap/web");
            }
        }
        HttpKt.before$default(Webmap::initialise$lambda$1, null, 2, null);
        ???;
        ???;
        ???;
        ???;
        ???;
        ???;
        ???;
        Spark.init();
    }

    public final void stop() {
        if (started) {
            HttpKt.stop();
            HttpKt.getStaticFiles();
            started = false;
            logger.info("Webmap stopped.");
        }
    }

    private final void findPort(boolean tryCurrentPort) {
        if (port == 0) {
            if (JourneymapClient.getInstance() != null && JourneymapClient.getInstance().getWebMapProperties() != null) {
                Integer var10000 = JourneymapClient.getInstance().getWebMapProperties().port.getAsInteger();
                port = var10000 == null ? 0 : var10000;
            } else {
                port = 8080;
            }
        }
        if (tryCurrentPort) {
            try {
                ServerSocket socket = new ServerSocket(port);
                port = socket.getLocalPort();
                socket.close();
            } catch (IOException var3) {
                logger.warn("Configured port " + port + " could not be bound: " + var3);
                this.findPort(false);
            }
            logger.info("Configured port " + port + " is available.");
        } else {
            ServerSocket socket = new ServerSocket(0);
            port = socket.getLocalPort();
            socket.close();
            logger.info("New port " + port + " assigned by ServerSocket.");
        }
    }

    private static final void initialise$lambda$0(Webmap this$0, Exception it) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        logger.error("Failed to start server: " + it);
        this$0.stop();
    }

    private static final void initialise$lambda$1(Request var0, Response response) {
        response.header("Access-Control-Allow-Origin", "*");
        response.header("Cache-Control", "no-cache");
    }

    static {
        Logger var10000 = Journeymap.getLogger("webmap");
        Intrinsics.checkNotNullExpressionValue(var10000, "getLogger(\"webmap\")");
        logger = var10000;
    }
}