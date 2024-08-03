package journeymap.client.service.webmap.kotlin.enums;

import info.journeymap.shaded.kotlin.kotlin.Metadata;
import info.journeymap.shaded.org.jetbrains.annotations.NotNull;

@Metadata(mv = { 1, 8, 0 }, k = 1, xi = 48, d1 = { "\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0000\n\u0002\u0010\u000e\n\u0002\b\b\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u000f\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006j\u0002\b\u0007j\u0002\b\bj\u0002\b\tj\u0002\b\n¨\u0006\u000b" }, d2 = { "Ljourneymap/client/service/webmap/kotlin/enums/WebmapStatus;", "", "status", "", "(Ljava/lang/String;ILjava/lang/String;)V", "getStatus", "()Ljava/lang/String;", "READY", "DISABLED", "NO_WORLD", "STARTING", "journeymap" })
public enum WebmapStatus {

    READY("ready"), DISABLED("disabled"), NO_WORLD("no_world"), STARTING("starting");

    @NotNull
    private final String status;

    private WebmapStatus(String status) {
        this.status = status;
    }

    @NotNull
    public final String getStatus() {
        return this.status;
    }
}