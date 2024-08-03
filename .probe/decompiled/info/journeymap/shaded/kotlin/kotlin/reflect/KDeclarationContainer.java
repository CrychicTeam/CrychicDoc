package info.journeymap.shaded.kotlin.kotlin.reflect;

import info.journeymap.shaded.kotlin.kotlin.Metadata;
import info.journeymap.shaded.org.jetbrains.annotations.NotNull;
import java.util.Collection;

@Metadata(mv = { 1, 6, 0 }, k = 1, xi = 48, d1 = { "\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u001e\n\u0002\u0018\u0002\n\u0002\b\u0003\bf\u0018\u00002\u00020\u0001R\u001c\u0010\u0002\u001a\f\u0012\b\u0012\u0006\u0012\u0002\b\u00030\u00040\u0003X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006¨\u0006\u0007" }, d2 = { "Linfo/journeymap/shaded/kotlin/kotlin/reflect/KDeclarationContainer;", "", "members", "", "Linfo/journeymap/shaded/kotlin/kotlin/reflect/KCallable;", "getMembers", "()Ljava/util/Collection;", "info.journeymap.shaded.kotlin.kotlin-stdlib" })
public interface KDeclarationContainer {

    @NotNull
    Collection<KCallable<?>> getMembers();
}