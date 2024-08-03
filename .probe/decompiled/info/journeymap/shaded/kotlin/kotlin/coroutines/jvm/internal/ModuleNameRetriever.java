package info.journeymap.shaded.kotlin.kotlin.coroutines.jvm.internal;

import info.journeymap.shaded.kotlin.kotlin.Metadata;
import info.journeymap.shaded.kotlin.kotlin.jvm.JvmField;
import info.journeymap.shaded.kotlin.kotlin.jvm.internal.Intrinsics;
import info.journeymap.shaded.org.jetbrains.annotations.NotNull;
import info.journeymap.shaded.org.jetbrains.annotations.Nullable;
import java.lang.reflect.Method;

@Metadata(mv = { 1, 6, 0 }, k = 1, xi = 48, d1 = { "\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\bÂ\u0002\u0018\u00002\u00020\u0001:\u0001\u000bB\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0006\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\bH\u0002J\u0010\u0010\t\u001a\u0004\u0018\u00010\n2\u0006\u0010\u0007\u001a\u00020\bR\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\f" }, d2 = { "Linfo/journeymap/shaded/kotlin/kotlin/coroutines/jvm/internal/ModuleNameRetriever;", "", "()V", "cache", "Linfo/journeymap/shaded/kotlin/kotlin/coroutines/jvm/internal/ModuleNameRetriever$Cache;", "notOnJava9", "buildCache", "continuation", "Linfo/journeymap/shaded/kotlin/kotlin/coroutines/jvm/internal/BaseContinuationImpl;", "getModuleName", "", "Cache", "info.journeymap.shaded.kotlin.kotlin-stdlib" })
final class ModuleNameRetriever {

    @NotNull
    public static final ModuleNameRetriever INSTANCE = new ModuleNameRetriever();

    @NotNull
    private static final ModuleNameRetriever.Cache notOnJava9 = new ModuleNameRetriever.Cache(null, null, null);

    @Nullable
    private static ModuleNameRetriever.Cache cache;

    private ModuleNameRetriever() {
    }

    @Nullable
    public final String getModuleName(@NotNull BaseContinuationImpl continuation) {
        Intrinsics.checkNotNullParameter(continuation, "continuation");
        ModuleNameRetriever.Cache module = ModuleNameRetriever.cache;
        ModuleNameRetriever.Cache cache = module == null ? this.buildCache(continuation) : module;
        if (cache == notOnJava9) {
            return null;
        } else {
            Method var5 = cache.getModuleMethod;
            Object descriptor = var5 == null ? null : var5.invoke(continuation.getClass());
            if (descriptor == null) {
                return null;
            } else {
                Method var6 = cache.getDescriptorMethod;
                Object var7 = var6 == null ? null : var6.invoke(descriptor);
                if (var7 == null) {
                    return null;
                } else {
                    var6 = cache.nameMethod;
                    var7 = var6 == null ? null : var6.invoke(var7);
                    return var7 instanceof String ? (String) var7 : null;
                }
            }
        }
    }

    private final ModuleNameRetriever.Cache buildCache(BaseContinuationImpl continuation) {
        try {
            Method getModuleMethod = Class.class.getDeclaredMethod("getModule");
            Class methodClass = continuation.getClass().getClassLoader().loadClass("java.lang.Module");
            Method getDescriptorMethod = methodClass.getDeclaredMethod("getDescriptor");
            Class moduleDescriptorClass = continuation.getClass().getClassLoader().loadClass("java.lang.module.ModuleDescriptor");
            Method nameMethod = moduleDescriptorClass.getDeclaredMethod("name");
            ModuleNameRetriever.Cache var7 = new ModuleNameRetriever.Cache(getModuleMethod, getDescriptorMethod, nameMethod);
            ???;
            cache = var7;
            return var7;
        } catch (Exception var10) {
            ModuleNameRetriever.Cache methodClassx = notOnJava9;
            ???;
            cache = methodClassx;
            return methodClassx;
        }
    }

    @Metadata(mv = { 1, 6, 0 }, k = 1, xi = 48, d1 = { "\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0002\u0018\u00002\u00020\u0001B#\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0003\u0012\b\u0010\u0005\u001a\u0004\u0018\u00010\u0003¢\u0006\u0002\u0010\u0006R\u0012\u0010\u0004\u001a\u0004\u0018\u00010\u00038\u0006X\u0087\u0004¢\u0006\u0002\n\u0000R\u0012\u0010\u0002\u001a\u0004\u0018\u00010\u00038\u0006X\u0087\u0004¢\u0006\u0002\n\u0000R\u0012\u0010\u0005\u001a\u0004\u0018\u00010\u00038\u0006X\u0087\u0004¢\u0006\u0002\n\u0000¨\u0006\u0007" }, d2 = { "Linfo/journeymap/shaded/kotlin/kotlin/coroutines/jvm/internal/ModuleNameRetriever$Cache;", "", "getModuleMethod", "Ljava/lang/reflect/Method;", "getDescriptorMethod", "nameMethod", "(Ljava/lang/reflect/Method;Ljava/lang/reflect/Method;Ljava/lang/reflect/Method;)V", "info.journeymap.shaded.kotlin.kotlin-stdlib" })
    private static final class Cache {

        @JvmField
        @Nullable
        public final Method getModuleMethod;

        @JvmField
        @Nullable
        public final Method getDescriptorMethod;

        @JvmField
        @Nullable
        public final Method nameMethod;

        public Cache(@Nullable Method getModuleMethod, @Nullable Method getDescriptorMethod, @Nullable Method nameMethod) {
            this.getModuleMethod = getModuleMethod;
            this.getDescriptorMethod = getDescriptorMethod;
            this.nameMethod = nameMethod;
        }
    }
}