package info.journeymap.shaded.kotlin.kotlin.reflect;

import info.journeymap.shaded.kotlin.kotlin.Metadata;
import info.journeymap.shaded.org.jetbrains.annotations.NotNull;
import info.journeymap.shaded.org.jetbrains.annotations.Nullable;
import java.util.List;

@Metadata(mv = { 1, 6, 0 }, k = 1, xi = 48, d1 = { "\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001R \u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00038&X§\u0004¢\u0006\f\u0012\u0004\b\u0005\u0010\u0006\u001a\u0004\b\u0007\u0010\bR\u001c\u0010\t\u001a\u0004\u0018\u00010\n8&X§\u0004¢\u0006\f\u0012\u0004\b\u000b\u0010\u0006\u001a\u0004\b\f\u0010\rR\u0012\u0010\u000e\u001a\u00020\u000fX¦\u0004¢\u0006\u0006\u001a\u0004\b\u000e\u0010\u0010¨\u0006\u0011" }, d2 = { "Linfo/journeymap/shaded/kotlin/kotlin/reflect/KType;", "Linfo/journeymap/shaded/kotlin/kotlin/reflect/KAnnotatedElement;", "arguments", "", "Linfo/journeymap/shaded/kotlin/kotlin/reflect/KTypeProjection;", "getArguments$annotations", "()V", "getArguments", "()Ljava/util/List;", "classifier", "Linfo/journeymap/shaded/kotlin/kotlin/reflect/KClassifier;", "getClassifier$annotations", "getClassifier", "()Lkotlin/reflect/KClassifier;", "isMarkedNullable", "", "()Z", "info.journeymap.shaded.kotlin.kotlin-stdlib" })
public interface KType extends KAnnotatedElement {

    @Nullable
    KClassifier getClassifier();

    @NotNull
    List<KTypeProjection> getArguments();

    boolean isMarkedNullable();

    @Metadata(mv = { 1, 6, 0 }, k = 3, xi = 48)
    public static final class DefaultImpls {
    }
}