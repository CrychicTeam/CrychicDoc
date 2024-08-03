package info.journeymap.shaded.kotlin.kotlin.text;

import info.journeymap.shaded.kotlin.kotlin.Metadata;
import info.journeymap.shaded.kotlin.kotlin.jvm.JvmField;
import info.journeymap.shaded.kotlin.kotlin.jvm.JvmName;
import info.journeymap.shaded.kotlin.kotlin.jvm.internal.Intrinsics;
import info.journeymap.shaded.org.jetbrains.annotations.NotNull;
import info.journeymap.shaded.org.jetbrains.annotations.Nullable;
import java.nio.charset.Charset;

@Metadata(mv = { 1, 6, 0 }, k = 1, xi = 48, d1 = { "\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0010\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0010\u0010\u0003\u001a\u00020\u00048\u0006X\u0087\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u00020\u00048\u0006X\u0087\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0006\u001a\u00020\u00048\u0006X\u0087\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u00020\u00048\u0006X\u0087\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\b\u001a\u00020\u00048\u0006X\u0087\u0004¢\u0006\u0002\n\u0000R\u0011\u0010\t\u001a\u00020\u00048G¢\u0006\u0006\u001a\u0004\b\n\u0010\u000bR\u0011\u0010\f\u001a\u00020\u00048G¢\u0006\u0006\u001a\u0004\b\r\u0010\u000bR\u0011\u0010\u000e\u001a\u00020\u00048G¢\u0006\u0006\u001a\u0004\b\u000f\u0010\u000bR\u0010\u0010\u0010\u001a\u00020\u00048\u0006X\u0087\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0011\u001a\u0004\u0018\u00010\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0012\u001a\u0004\u0018\u00010\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0013\u001a\u0004\u0018\u00010\u0004X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u0014" }, d2 = { "Linfo/journeymap/shaded/kotlin/kotlin/text/Charsets;", "", "()V", "ISO_8859_1", "Ljava/nio/charset/Charset;", "US_ASCII", "UTF_16", "UTF_16BE", "UTF_16LE", "UTF_32", "UTF32", "()Ljava/nio/charset/Charset;", "UTF_32BE", "UTF32_BE", "UTF_32LE", "UTF32_LE", "UTF_8", "utf_32", "utf_32be", "utf_32le", "info.journeymap.shaded.kotlin.kotlin-stdlib" })
public final class Charsets {

    @NotNull
    public static final Charsets INSTANCE = new Charsets();

    @JvmField
    @NotNull
    public static final Charset UTF_8;

    @JvmField
    @NotNull
    public static final Charset UTF_16;

    @JvmField
    @NotNull
    public static final Charset UTF_16BE;

    @JvmField
    @NotNull
    public static final Charset UTF_16LE;

    @JvmField
    @NotNull
    public static final Charset US_ASCII;

    @JvmField
    @NotNull
    public static final Charset ISO_8859_1;

    @Nullable
    private static Charset utf_32;

    @Nullable
    private static Charset utf_32le;

    @Nullable
    private static Charset utf_32be;

    private Charsets() {
    }

    @JvmName(name = "UTF32")
    @NotNull
    public final Charset UTF32() {
        Charset var1 = utf_32;
        Charset var10000;
        if (var1 == null) {
            Charsets $this$_get_UTF_32__u24lambda_u2d0 = this;
            ???;
            Charset var5 = Charset.forName("UTF-32");
            Intrinsics.checkNotNullExpressionValue(var5, "forName(\"UTF-32\")");
            utf_32 = var5;
            var10000 = var5;
        } else {
            var10000 = var1;
        }
        return var10000;
    }

    @JvmName(name = "UTF32_LE")
    @NotNull
    public final Charset UTF32_LE() {
        Charset var1 = utf_32le;
        Charset var10000;
        if (var1 == null) {
            Charsets $this$_get_UTF_32LE__u24lambda_u2d1 = this;
            ???;
            Charset var5 = Charset.forName("UTF-32LE");
            Intrinsics.checkNotNullExpressionValue(var5, "forName(\"UTF-32LE\")");
            utf_32le = var5;
            var10000 = var5;
        } else {
            var10000 = var1;
        }
        return var10000;
    }

    @JvmName(name = "UTF32_BE")
    @NotNull
    public final Charset UTF32_BE() {
        Charset var1 = utf_32be;
        Charset var10000;
        if (var1 == null) {
            Charsets $this$_get_UTF_32BE__u24lambda_u2d2 = this;
            ???;
            Charset var5 = Charset.forName("UTF-32BE");
            Intrinsics.checkNotNullExpressionValue(var5, "forName(\"UTF-32BE\")");
            utf_32be = var5;
            var10000 = var5;
        } else {
            var10000 = var1;
        }
        return var10000;
    }

    static {
        Charset var0 = Charset.forName("UTF-8");
        Intrinsics.checkNotNullExpressionValue(var0, "forName(\"UTF-8\")");
        UTF_8 = var0;
        var0 = Charset.forName("UTF-16");
        Intrinsics.checkNotNullExpressionValue(var0, "forName(\"UTF-16\")");
        UTF_16 = var0;
        var0 = Charset.forName("UTF-16BE");
        Intrinsics.checkNotNullExpressionValue(var0, "forName(\"UTF-16BE\")");
        UTF_16BE = var0;
        var0 = Charset.forName("UTF-16LE");
        Intrinsics.checkNotNullExpressionValue(var0, "forName(\"UTF-16LE\")");
        UTF_16LE = var0;
        var0 = Charset.forName("US-ASCII");
        Intrinsics.checkNotNullExpressionValue(var0, "forName(\"US-ASCII\")");
        US_ASCII = var0;
        var0 = Charset.forName("ISO-8859-1");
        Intrinsics.checkNotNullExpressionValue(var0, "forName(\"ISO-8859-1\")");
        ISO_8859_1 = var0;
    }
}