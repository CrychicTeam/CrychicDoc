package info.journeymap.shaded.kotlin.kotlin.io;

import info.journeymap.shaded.kotlin.kotlin.Metadata;
import info.journeymap.shaded.kotlin.kotlin.collections.CollectionsKt;
import info.journeymap.shaded.kotlin.kotlin.jvm.internal.Intrinsics;
import info.journeymap.shaded.kotlin.kotlin.text.StringsKt;
import info.journeymap.shaded.org.jetbrains.annotations.NotNull;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Metadata(mv = { 1, 6, 0 }, k = 5, xi = 49, d1 = { "\u0000$\n\u0000\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\u001a\u0011\u0010\u000b\u001a\u00020\f*\u00020\bH\u0002¢\u0006\u0002\b\r\u001a\u001c\u0010\u000e\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u000f\u001a\u00020\f2\u0006\u0010\u0010\u001a\u00020\fH\u0000\u001a\f\u0010\u0011\u001a\u00020\u0012*\u00020\u0002H\u0000\"\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00028F¢\u0006\u0006\u001a\u0004\b\u0000\u0010\u0003\"\u0018\u0010\u0004\u001a\u00020\u0002*\u00020\u00028@X\u0080\u0004¢\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006\"\u0018\u0010\u0007\u001a\u00020\b*\u00020\u00028@X\u0080\u0004¢\u0006\u0006\u001a\u0004\b\t\u0010\n¨\u0006\u0013" }, d2 = { "isRooted", "", "Ljava/io/File;", "(Ljava/io/File;)Z", "root", "getRoot", "(Ljava/io/File;)Ljava/io/File;", "rootName", "", "getRootName", "(Ljava/io/File;)Ljava/lang/String;", "getRootLength", "", "getRootLength$FilesKt__FilePathComponentsKt", "subPath", "beginIndex", "endIndex", "toComponents", "Linfo/journeymap/shaded/kotlin/kotlin/io/FilePathComponents;", "info.journeymap.shaded.kotlin.kotlin-stdlib" }, xs = "info/journeymap/shaded/kotlin/kotlin/io/FilesKt")
class FilesKt__FilePathComponentsKt {

    private static final int getRootLength$FilesKt__FilePathComponentsKt(String $this$getRootLength) {
        int first = StringsKt.indexOf$default((CharSequence) $this$getRootLength, File.separatorChar, 0, false, 4, null);
        if (first == 0) {
            if ($this$getRootLength.length() > 1 && $this$getRootLength.charAt(1) == File.separatorChar) {
                first = StringsKt.indexOf$default((CharSequence) $this$getRootLength, File.separatorChar, 2, false, 4, null);
                if (first >= 0) {
                    first = StringsKt.indexOf$default((CharSequence) $this$getRootLength, File.separatorChar, first + 1, false, 4, null);
                    if (first >= 0) {
                        return first + 1;
                    }
                    return $this$getRootLength.length();
                }
            }
            return 1;
        } else if (first > 0 && $this$getRootLength.charAt(first - 1) == ':') {
            return first + 1;
        } else {
            return first == -1 && StringsKt.endsWith$default((CharSequence) $this$getRootLength, (char) 58, false, 2, null) ? $this$getRootLength.length() : 0;
        }
    }

    @NotNull
    public static final String getRootName(@NotNull File $this$rootName) {
        Intrinsics.checkNotNullParameter($this$rootName, "<this>");
        String var1 = $this$rootName.getPath();
        Intrinsics.checkNotNullExpressionValue(var1, "path");
        byte var2 = 0;
        String var3 = $this$rootName.getPath();
        Intrinsics.checkNotNullExpressionValue(var3, "path");
        int var5 = getRootLength$FilesKt__FilePathComponentsKt(var3);
        String var4 = var1.substring(var2, var5);
        Intrinsics.checkNotNullExpressionValue(var4, "this as java.lang.String…ing(startIndex, endIndex)");
        return var4;
    }

    @NotNull
    public static final File getRoot(@NotNull File $this$root) {
        Intrinsics.checkNotNullParameter($this$root, "<this>");
        return new File(FilesKt.getRootName($this$root));
    }

    public static final boolean isRooted(@NotNull File $this$isRooted) {
        Intrinsics.checkNotNullParameter($this$isRooted, "<this>");
        String var1 = $this$isRooted.getPath();
        Intrinsics.checkNotNullExpressionValue(var1, "path");
        return getRootLength$FilesKt__FilePathComponentsKt(var1) > 0;
    }

    @NotNull
    public static final FilePathComponents toComponents(@NotNull File $this$toComponents) {
        Intrinsics.checkNotNullParameter($this$toComponents, "<this>");
        String path = $this$toComponents.getPath();
        Intrinsics.checkNotNullExpressionValue(path, "path");
        int rootLength = getRootLength$FilesKt__FilePathComponentsKt(path);
        byte list = 0;
        String $this$map$iv = path.substring(list, rootLength);
        Intrinsics.checkNotNullExpressionValue($this$map$iv, "this as java.lang.String…ing(startIndex, endIndex)");
        $this$map$iv = path.substring(rootLength);
        Intrinsics.checkNotNullExpressionValue($this$map$iv, "this as java.lang.String).substring(startIndex)");
        List var10000;
        if (((CharSequence) $this$map$iv).length() == 0) {
            var10000 = CollectionsKt.emptyList();
        } else {
            CharSequence var21 = (CharSequence) $this$map$iv;
            char[] var19 = new char[] { File.separatorChar };
            Iterable $this$map$ivx = (Iterable) StringsKt.split$default(var21, var19, false, 0, 6, null);
            int $i$f$map = 0;
            Collection destination$iv$iv = (Collection) (new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$ivx, 10)));
            int $i$f$mapTo = 0;
            for (Object item$iv$iv : $this$map$ivx) {
                String p0 = (String) item$iv$iv;
                ???;
                File var16 = new File(p0);
                destination$iv$iv.add(var16);
            }
            var10000 = (List) destination$iv$iv;
        }
        List listx = var10000;
        return new FilePathComponents(new File($this$map$iv), listx);
    }

    @NotNull
    public static final File subPath(@NotNull File $this$subPath, int beginIndex, int endIndex) {
        Intrinsics.checkNotNullParameter($this$subPath, "<this>");
        return FilesKt.toComponents($this$subPath).subPath(beginIndex, endIndex);
    }

    public FilesKt__FilePathComponentsKt() {
    }
}