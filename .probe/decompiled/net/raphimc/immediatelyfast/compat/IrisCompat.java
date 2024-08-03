package net.raphimc.immediatelyfast.compat;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.VertexFormat;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import java.lang.invoke.CallSite;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.MethodHandles.Lookup;
import java.util.function.BooleanSupplier;
import net.lenni0451.reflect.accessor.FieldAccessor;
import net.raphimc.immediatelyfast.ImmediatelyFast;

public class IrisCompat {

    public static boolean IRIS_LOADED = false;

    public static BooleanSupplier isRenderingLevel;

    public static BooleanConsumer renderWithExtendedVertexFormat;

    public static TriConsumer<BufferBuilder, VertexFormat.Mode, VertexFormat> iris$beginWithoutExtending;

    private static String[] PACKAGE_NAMES = new String[] { "net.coderbot.iris.vertices", "net.irisshaders.iris.vertices" };

    public static void init() {
        IRIS_LOADED = true;
        Throwable throwable = null;
        for (int i = 0; i < PACKAGE_NAMES.length; i++) {
            String packageName = PACKAGE_NAMES[i];
            try {
                Lookup lookup = MethodHandles.lookup();
                Class<?> immediateStateClass = Class.forName(packageName + ".ImmediateState");
                Class<?> extendingBufferBuilderClass = Class.forName(packageName + ".ExtendingBufferBuilder");
                isRenderingLevel = (BooleanSupplier) FieldAccessor.makeGetter(BooleanSupplier.class, null, immediateStateClass.getDeclaredField("isRenderingLevel"));
                renderWithExtendedVertexFormat = (BooleanConsumer) FieldAccessor.makeSetter(BooleanConsumer.class, null, immediateStateClass.getDeclaredField("renderWithExtendedVertexFormat"));
                MethodHandle iris$beginWithoutExtendingMH = lookup.findVirtual(extendingBufferBuilderClass, "iris$beginWithoutExtending", MethodType.methodType(void.class, VertexFormat.Mode.class, VertexFormat.class));
                CallSite iris$beginWithoutExtendingCallSite = LambdaMetafactory.metafactory(lookup, "accept", MethodType.methodType(TriConsumer.class), MethodType.methodType(void.class, Object.class, Object.class, Object.class), iris$beginWithoutExtendingMH, iris$beginWithoutExtendingMH.type());
                iris$beginWithoutExtending = (TriConsumer) iris$beginWithoutExtendingCallSite.getTarget().invoke();
                return;
            } catch (Throwable var8) {
                throwable = var8;
            }
        }
        ImmediatelyFast.LOGGER.error("Failed to initialize Iris compatibility. Try updating Iris and ImmediatelyFast before reporting this on GitHub", throwable);
        System.exit(-1);
    }
}