package info.journeymap.shaded.kotlin.kotlin.io;

import info.journeymap.shaded.kotlin.kotlin.Metadata;
import info.journeymap.shaded.kotlin.kotlin.PublishedApi;
import info.journeymap.shaded.kotlin.kotlin.SinceKotlin;
import info.journeymap.shaded.kotlin.kotlin.internal.InlineOnly;
import info.journeymap.shaded.kotlin.kotlin.internal.PlatformImplementationsKt;
import info.journeymap.shaded.kotlin.kotlin.jvm.JvmName;
import info.journeymap.shaded.kotlin.kotlin.jvm.functions.Function1;
import info.journeymap.shaded.kotlin.kotlin.jvm.internal.InlineMarker;
import info.journeymap.shaded.kotlin.kotlin.jvm.internal.Intrinsics;
import info.journeymap.shaded.org.jetbrains.annotations.Nullable;
import java.io.Closeable;

@Metadata(mv = { 1, 6, 0 }, k = 2, xi = 48, d1 = { "\u0000\u001c\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0003\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a\u0018\u0010\u0000\u001a\u00020\u0001*\u0004\u0018\u00010\u00022\b\u0010\u0003\u001a\u0004\u0018\u00010\u0004H\u0001\u001aK\u0010\u0005\u001a\u0002H\u0006\"\n\b\u0000\u0010\u0007*\u0004\u0018\u00010\u0002\"\u0004\b\u0001\u0010\u0006*\u0002H\u00072\u0012\u0010\b\u001a\u000e\u0012\u0004\u0012\u0002H\u0007\u0012\u0004\u0012\u0002H\u00060\tH\u0087\bø\u0001\u0000ø\u0001\u0001\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0001 \u0001¢\u0006\u0002\u0010\u000b\u0082\u0002\u000f\n\u0006\b\u0011(\n0\u0001\n\u0005\b\u009920\u0001¨\u0006\f" }, d2 = { "closeFinally", "", "Ljava/io/Closeable;", "cause", "", "use", "R", "T", "block", "Linfo/journeymap/shaded/kotlin/kotlin/Function1;", "Requires newer compiler version to be inlined correctly.", "(Ljava/io/Closeable;Lkotlin/jvm/functions/Function1;)Ljava/lang/Object;", "info.journeymap.shaded.kotlin.kotlin-stdlib" })
@JvmName(name = "CloseableKt")
public final class CloseableKt {

    // $VF: Could not verify finally blocks. A semaphore variable has been added to preserve control flow.
    // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
    @InlineOnly
    private static final <T extends Closeable, R> R use(T $this$use, Function1<? super T, ? extends R> block) {
        Intrinsics.checkNotNullParameter(block, "block");
        Throwable exception = null;
        boolean var8 = false;
        Object e;
        try {
            var8 = true;
            e = block.invoke($this$use);
            var8 = false;
        } catch (Throwable var10) {
            exception = var10;
            throw var10;
        } finally {
            if (var8) {
                InlineMarker.finallyStart(1);
                if (PlatformImplementationsKt.apiVersionIsAtLeast(1, 1, 0)) {
                    closeFinally($this$use, exception);
                } else if ($this$use != null) {
                    if (exception == null) {
                        $this$use.close();
                    } else {
                        try {
                            $this$use.close();
                        } catch (Throwable var9) {
                        }
                    }
                }
                InlineMarker.finallyEnd(1);
            }
        }
        InlineMarker.finallyStart(1);
        if (PlatformImplementationsKt.apiVersionIsAtLeast(1, 1, 0)) {
            closeFinally($this$use, exception);
        } else if ($this$use != null) {
            $this$use.close();
        }
        InlineMarker.finallyEnd(1);
        return (R) e;
    }

    @SinceKotlin(version = "1.1")
    @PublishedApi
    public static final void closeFinally(@Nullable Closeable $this$closeFinally, @Nullable Throwable cause) {
        if ($this$closeFinally != null) {
            if (cause == null) {
                $this$closeFinally.close();
            } else {
                try {
                    $this$closeFinally.close();
                } catch (Throwable var3) {
                    info.journeymap.shaded.kotlin.kotlin.ExceptionsKt.addSuppressed(cause, var3);
                }
            }
        }
    }
}