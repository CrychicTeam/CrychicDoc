package info.journeymap.shaded.kotlin.kotlin.text;

import info.journeymap.shaded.kotlin.kotlin.Metadata;
import info.journeymap.shaded.kotlin.kotlin.jvm.internal.Intrinsics;
import info.journeymap.shaded.org.jetbrains.annotations.NotNull;
import java.util.Locale;

@Metadata(mv = { 1, 6, 0 }, k = 2, xi = 48, d1 = { "\u0000\f\n\u0000\n\u0002\u0010\u000e\n\u0002\u0010\f\n\u0000\u001a\f\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u0000¨\u0006\u0003" }, d2 = { "titlecaseImpl", "", "", "info.journeymap.shaded.kotlin.kotlin-stdlib" })
public final class _OneToManyTitlecaseMappingsKt {

    @NotNull
    public static final String titlecaseImpl(char $this$titlecaseImpl) {
        String var3 = String.valueOf($this$titlecaseImpl).toUpperCase(Locale.ROOT);
        Intrinsics.checkNotNullExpressionValue(var3, "this as java.lang.String).toUpperCase(Locale.ROOT)");
        if (var3.length() > 1) {
            String var10000;
            if ($this$titlecaseImpl == 329) {
                var10000 = var3;
            } else {
                char var2 = var3.charAt(0);
                byte var4 = 1;
                String var5 = var3.substring(var4);
                Intrinsics.checkNotNullExpressionValue(var5, "this as java.lang.String).substring(startIndex)");
                String var6 = var5.toLowerCase(Locale.ROOT);
                Intrinsics.checkNotNullExpressionValue(var6, "this as java.lang.String).toLowerCase(Locale.ROOT)");
                var10000 = var2 + var6;
            }
            return var10000;
        } else {
            return String.valueOf(Character.toTitleCase($this$titlecaseImpl));
        }
    }
}