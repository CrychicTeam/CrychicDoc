package info.journeymap.shaded.kotlin.kotlin.reflect;

import info.journeymap.shaded.kotlin.kotlin.Metadata;
import info.journeymap.shaded.org.jetbrains.annotations.NotNull;
import info.journeymap.shaded.org.jetbrains.annotations.Nullable;
import java.util.List;
import java.util.Map;

@Metadata(mv = { 1, 6, 0 }, k = 1, xi = 48, d1 = { "\u0000T\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\n\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0011\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010$\n\u0002\b\u0002\bf\u0018\u0000*\u0006\b\u0000\u0010\u0001 \u00012\u00020\u0002J%\u0010$\u001a\u00028\u00002\u0016\u0010%\u001a\f\u0012\b\b\u0001\u0012\u0004\u0018\u00010'0&\"\u0004\u0018\u00010'H&¢\u0006\u0002\u0010(J#\u0010)\u001a\u00028\u00002\u0014\u0010%\u001a\u0010\u0012\u0004\u0012\u00020\u0014\u0012\u0006\u0012\u0004\u0018\u00010'0*H&¢\u0006\u0002\u0010+R\u001a\u0010\u0003\u001a\u00020\u00048&X§\u0004¢\u0006\f\u0012\u0004\b\u0005\u0010\u0006\u001a\u0004\b\u0003\u0010\u0007R\u001a\u0010\b\u001a\u00020\u00048&X§\u0004¢\u0006\f\u0012\u0004\b\t\u0010\u0006\u001a\u0004\b\b\u0010\u0007R\u001a\u0010\n\u001a\u00020\u00048&X§\u0004¢\u0006\f\u0012\u0004\b\u000b\u0010\u0006\u001a\u0004\b\n\u0010\u0007R\u001a\u0010\f\u001a\u00020\u00048&X§\u0004¢\u0006\f\u0012\u0004\b\r\u0010\u0006\u001a\u0004\b\f\u0010\u0007R\u0012\u0010\u000e\u001a\u00020\u000fX¦\u0004¢\u0006\u0006\u001a\u0004\b\u0010\u0010\u0011R\u0018\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00140\u0013X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0015\u0010\u0016R\u0012\u0010\u0017\u001a\u00020\u0018X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0019\u0010\u001aR \u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u001c0\u00138&X§\u0004¢\u0006\f\u0012\u0004\b\u001d\u0010\u0006\u001a\u0004\b\u001e\u0010\u0016R\u001c\u0010\u001f\u001a\u0004\u0018\u00010 8&X§\u0004¢\u0006\f\u0012\u0004\b!\u0010\u0006\u001a\u0004\b\"\u0010#¨\u0006," }, d2 = { "Linfo/journeymap/shaded/kotlin/kotlin/reflect/KCallable;", "R", "Linfo/journeymap/shaded/kotlin/kotlin/reflect/KAnnotatedElement;", "isAbstract", "", "isAbstract$annotations", "()V", "()Z", "isFinal", "isFinal$annotations", "isOpen", "isOpen$annotations", "isSuspend", "isSuspend$annotations", "name", "", "getName", "()Ljava/lang/String;", "parameters", "", "Linfo/journeymap/shaded/kotlin/kotlin/reflect/KParameter;", "getParameters", "()Ljava/util/List;", "returnType", "Linfo/journeymap/shaded/kotlin/kotlin/reflect/KType;", "getReturnType", "()Lkotlin/reflect/KType;", "typeParameters", "Linfo/journeymap/shaded/kotlin/kotlin/reflect/KTypeParameter;", "getTypeParameters$annotations", "getTypeParameters", "visibility", "Linfo/journeymap/shaded/kotlin/kotlin/reflect/KVisibility;", "getVisibility$annotations", "getVisibility", "()Lkotlin/reflect/KVisibility;", "call", "args", "", "", "([Ljava/lang/Object;)Ljava/lang/Object;", "callBy", "", "(Ljava/util/Map;)Ljava/lang/Object;", "info.journeymap.shaded.kotlin.kotlin-stdlib" })
public interface KCallable<R> extends KAnnotatedElement {

    @NotNull
    String getName();

    @NotNull
    List<KParameter> getParameters();

    @NotNull
    KType getReturnType();

    @NotNull
    List<KTypeParameter> getTypeParameters();

    R call(@NotNull Object... var1);

    R callBy(@NotNull Map<KParameter, ? extends Object> var1);

    @Nullable
    KVisibility getVisibility();

    boolean isFinal();

    boolean isOpen();

    boolean isAbstract();

    boolean isSuspend();

    @Metadata(mv = { 1, 6, 0 }, k = 3, xi = 48)
    public static final class DefaultImpls {
    }
}