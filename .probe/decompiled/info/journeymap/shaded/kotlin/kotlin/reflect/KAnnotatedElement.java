package info.journeymap.shaded.kotlin.kotlin.reflect;

import info.journeymap.shaded.kotlin.kotlin.Metadata;
import info.journeymap.shaded.org.jetbrains.annotations.NotNull;
import java.lang.annotation.Annotation;
import java.util.List;

@Metadata(mv = { 1, 6, 0 }, k = 1, xi = 48, d1 = { "\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0010\u001b\n\u0002\b\u0003\bf\u0018\u00002\u00020\u0001R\u0018\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006¨\u0006\u0007" }, d2 = { "Linfo/journeymap/shaded/kotlin/kotlin/reflect/KAnnotatedElement;", "", "annotations", "", "", "getAnnotations", "()Ljava/util/List;", "info.journeymap.shaded.kotlin.kotlin-stdlib" })
public interface KAnnotatedElement {

    @NotNull
    List<Annotation> getAnnotations();
}