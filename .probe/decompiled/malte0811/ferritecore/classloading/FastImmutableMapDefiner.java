package malte0811.ferritecore.classloading;

import com.google.common.base.Preconditions;
import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableMap;
import java.io.InputStream;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.MethodHandles.Lookup;
import java.util.function.Supplier;
import malte0811.ferritecore.ducks.FastMapStateHolder;
import net.minecraft.world.level.block.state.properties.Property;
import org.apache.commons.io.IOUtils;

public class FastImmutableMapDefiner {

    public static String GOOGLE_ACCESS_PREFIX = "/googleaccess/";

    public static String GOOGLE_ACCESS_SUFFIX = ".class_manual";

    private static final Supplier<FastImmutableMapDefiner.Definer> DEFINE_CLASS = Suppliers.memoize(() -> {
        try {
            Lookup privateLookup = MethodHandles.privateLookupIn(ImmutableMap.class, MethodHandles.lookup());
            return (bytes, name) -> privateLookup.defineClass(bytes);
        } catch (IllegalAccessException var1) {
            throw new RuntimeException(var1);
        }
    });

    private static final Supplier<MethodHandle> MAKE_IMMUTABLE_FAST_MAP = Suppliers.memoize(() -> {
        try {
            defineInAppClassloader("com.google.common.collect.FerriteCoreEntrySetAccess");
            defineInAppClassloader("com.google.common.collect.FerriteCoreImmutableMapAccess");
            defineInAppClassloader("com.google.common.collect.FerriteCoreImmutableCollectionAccess");
            Class<?> map = Class.forName("malte0811.ferritecore.fastmap.immutable.FastMapEntryImmutableMap");
            Lookup lookup = MethodHandles.lookup();
            return lookup.findConstructor(map, MethodType.methodType(void.class, FastMapStateHolder.class));
        } catch (Exception var2) {
            throw new RuntimeException(var2);
        }
    });

    public static ImmutableMap<Property<?>, Comparable<?>> makeMap(FastMapStateHolder<?> state) {
        try {
            return (ImmutableMap) ((MethodHandle) MAKE_IMMUTABLE_FAST_MAP.get()).invoke(state);
        } catch (Error var2) {
            throw var2;
        } catch (Throwable var3) {
            throw new RuntimeException(var3);
        }
    }

    private static void defineInAppClassloader(String name) throws Exception {
        InputStream byteInput = FastImmutableMapDefiner.class.getResourceAsStream(GOOGLE_ACCESS_PREFIX + name.replace('.', '/') + GOOGLE_ACCESS_SUFFIX);
        byte[] classBytes;
        try {
            Preconditions.checkNotNull(byteInput, "Failed to find class bytes for " + name);
            classBytes = IOUtils.toByteArray(byteInput);
        } catch (Throwable var6) {
            if (byteInput != null) {
                try {
                    byteInput.close();
                } catch (Throwable var5) {
                    var6.addSuppressed(var5);
                }
            }
            throw var6;
        }
        if (byteInput != null) {
            byteInput.close();
        }
        Class<?> loaded = ((FastImmutableMapDefiner.Definer) DEFINE_CLASS.get()).define(classBytes, name);
        Preconditions.checkState(loaded.getClassLoader() == ImmutableMap.class.getClassLoader());
    }

    private interface Definer {

        Class<?> define(byte[] var1, String var2) throws Exception;
    }
}