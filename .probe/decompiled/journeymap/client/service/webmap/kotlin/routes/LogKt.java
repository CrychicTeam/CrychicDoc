package journeymap.client.service.webmap.kotlin.routes;

import info.journeymap.shaded.kotlin.kotlin.Metadata;
import info.journeymap.shaded.kotlin.kotlin.io.FilesKt;
import info.journeymap.shaded.kotlin.kotlin.jvm.internal.Intrinsics;
import info.journeymap.shaded.kotlin.spark.kotlin.RouteHandler;
import info.journeymap.shaded.org.jetbrains.annotations.NotNull;
import java.io.File;
import journeymap.client.log.JMLogger;
import journeymap.common.Journeymap;
import org.apache.logging.log4j.Logger;

@Metadata(mv = { 1, 8, 0 }, k = 2, xi = 48, d1 = { "\u0000\u0014\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0000\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0006" }, d2 = { "logger", "Lorg/apache/logging/log4j/Logger;", "logGet", "", "handler", "Linfo/journeymap/shaded/kotlin/spark/kotlin/RouteHandler;", "journeymap" })
public final class LogKt {

    @NotNull
    private static final Logger logger;

    @NotNull
    public static final Object logGet(@NotNull RouteHandler handler) {
        Intrinsics.checkNotNullParameter(handler, "handler");
        File var10000 = JMLogger.getLogFile();
        Intrinsics.checkNotNullExpressionValue(var10000, "getLogFile()");
        File logFile = var10000;
        if (logFile.exists()) {
            handler.getResponse().raw().addHeader("Content-Disposition", "inline; filename=\"journeymap.log\"");
            handler.getResponse().raw().getOutputStream().write(FilesKt.readBytes(logFile));
            handler.getResponse().raw().getOutputStream().flush();
            var10000 = handler.getResponse();
        } else {
            logger.warn("Unable to find JourneyMap logfile");
            handler.status(404);
            var10000 = "Not found: " + logFile.getPath();
        }
        return var10000;
    }

    static {
        Logger var10000 = Journeymap.getLogger("webmap/routes/log");
        Intrinsics.checkNotNullExpressionValue(var10000, "getLogger(\"webmap/routes/log\")");
        logger = var10000;
    }
}