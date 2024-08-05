package journeymap.client.service.webmap.kotlin;

import info.journeymap.shaded.kotlin.kotlin.Metadata;
import info.journeymap.shaded.kotlin.kotlin.jvm.functions.Function1;
import info.journeymap.shaded.kotlin.kotlin.jvm.internal.Intrinsics;
import info.journeymap.shaded.kotlin.spark.kotlin.RouteHandler;
import info.journeymap.shaded.org.jetbrains.annotations.NotNull;
import journeymap.common.Journeymap;
import journeymap.common.log.LogFormatter;
import org.apache.logging.log4j.Logger;

@Metadata(mv = { 1, 8, 0 }, k = 2, xi = 48, d1 = { "\u0000\u0018\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\u001a(\u0010\u0002\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00050\u00032\u0012\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00050\u0003H\u0000\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0007" }, d2 = { "logger", "Lorg/apache/logging/log4j/Logger;", "wrapForError", "Linfo/journeymap/shaded/kotlin/kotlin/Function1;", "Linfo/journeymap/shaded/kotlin/spark/kotlin/RouteHandler;", "", "function", "journeymap" })
public final class RoutesKt {

    @NotNull
    private static final Logger logger;

    @NotNull
    public static final Function1<RouteHandler, Object> wrapForError(@NotNull Function1<? super RouteHandler, ? extends Object> function) {
        Intrinsics.checkNotNullParameter(function, "function");
        return (Function1<RouteHandler, Object>) (new Function1<RouteHandler, Object>(function) {

            {
                this.$function = $function;
            }

            @NotNull
            public final Object invoke(@NotNull RouteHandler p0) {
                Intrinsics.checkNotNullParameter(p0, "p0");
                return RoutesKt.wrapForError$wrapper(this.$function, p0);
            }
        });
    }

    private static final Object wrapForError$wrapper(Function1<? super RouteHandler, ? extends Object> $function, RouteHandler handler) {
        Object var2;
        try {
            var2 = $function.invoke(handler);
        } catch (Throwable var4) {
            logger.error(LogFormatter.toString(var4));
            handler.getResponse().status(500);
            String var10000 = var4.getLocalizedMessage();
            Intrinsics.checkNotNullExpressionValue(var10000, "{\n            logger.err…ocalizedMessage\n        }");
            var2 = var10000;
        }
        return var2;
    }

    static {
        Logger var10000 = Journeymap.getLogger("webmap/routes");
        Intrinsics.checkNotNullExpressionValue(var10000, "getLogger(\"webmap/routes\")");
        logger = var10000;
    }
}