package icyllis.modernui.mc;

import java.lang.reflect.Method;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public final class IrisApiIntegration {

    private static Object irisApiInstance;

    private static Method isShaderPackInUse;

    private IrisApiIntegration() {
    }

    public static boolean isShaderPackInUse() {
        if (isShaderPackInUse != null) {
            try {
                return (Boolean) isShaderPackInUse.invoke(irisApiInstance);
            } catch (Exception var1) {
                var1.printStackTrace();
            }
        }
        return false;
    }

    static {
        try {
            Class<?> clazz = Class.forName("net.irisshaders.iris.api.v0.IrisApi");
            irisApiInstance = clazz.getMethod("getInstance").invoke(null);
            isShaderPackInUse = clazz.getMethod("isShaderPackInUse");
        } catch (Exception var1) {
            var1.printStackTrace();
        }
    }
}