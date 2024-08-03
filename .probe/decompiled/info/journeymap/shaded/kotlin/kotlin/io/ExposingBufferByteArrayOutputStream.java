package info.journeymap.shaded.kotlin.kotlin.io;

import info.journeymap.shaded.kotlin.kotlin.Metadata;
import info.journeymap.shaded.kotlin.kotlin.jvm.internal.Intrinsics;
import info.journeymap.shaded.org.jetbrains.annotations.NotNull;
import java.io.ByteArrayOutputStream;

@Metadata(mv = { 1, 6, 0 }, k = 1, xi = 48, d1 = { "\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0002\b\u0003\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004R\u0011\u0010\u0005\u001a\u00020\u00068F¢\u0006\u0006\u001a\u0004\b\u0007\u0010\b¨\u0006\t" }, d2 = { "Linfo/journeymap/shaded/kotlin/kotlin/io/ExposingBufferByteArrayOutputStream;", "Ljava/io/ByteArrayOutputStream;", "size", "", "(I)V", "buffer", "", "getBuffer", "()[B", "info.journeymap.shaded.kotlin.kotlin-stdlib" })
final class ExposingBufferByteArrayOutputStream extends ByteArrayOutputStream {

    public ExposingBufferByteArrayOutputStream(int size) {
        super(size);
    }

    @NotNull
    public final byte[] getBuffer() {
        byte[] var1 = this.buf;
        Intrinsics.checkNotNullExpressionValue(var1, "buf");
        return var1;
    }
}