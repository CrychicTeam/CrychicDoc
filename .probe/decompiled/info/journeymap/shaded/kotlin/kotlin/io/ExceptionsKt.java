package info.journeymap.shaded.kotlin.kotlin.io;

import info.journeymap.shaded.kotlin.kotlin.Metadata;
import info.journeymap.shaded.kotlin.kotlin.jvm.internal.Intrinsics;
import java.io.File;

@Metadata(mv = { 1, 6, 0 }, k = 2, xi = 48, d1 = { "\u0000\u0010\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a$\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u00032\b\u0010\u0005\u001a\u0004\u0018\u00010\u0001H\u0002¨\u0006\u0006" }, d2 = { "constructMessage", "", "file", "Ljava/io/File;", "other", "reason", "info.journeymap.shaded.kotlin.kotlin-stdlib" })
public final class ExceptionsKt {

    private static final String constructMessage(File file, File other, String reason) {
        StringBuilder sb = new StringBuilder(file.toString());
        if (other != null) {
            sb.append(Intrinsics.stringPlus(" -> ", other));
        }
        if (reason != null) {
            sb.append(Intrinsics.stringPlus(": ", reason));
        }
        String var4 = sb.toString();
        Intrinsics.checkNotNullExpressionValue(var4, "sb.toString()");
        return var4;
    }
}