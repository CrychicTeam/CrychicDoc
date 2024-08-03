package journeymap.common.util;

import info.journeymap.shaded.org.jetbrains.annotations.NotNull;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

public class ObfHelper {

    @NotNull
    public static <T> Field findField(@NotNull Class<? super T> declaringClass, @NotNull String propertyEnumStaticFieldName) {
        return ObfuscationReflectionHelper.findField(declaringClass, propertyEnumStaticFieldName);
    }

    @NotNull
    public static Method findMethod(@NotNull Class<?> declaringClass, @NotNull String methodName, @NotNull Class<?>... methodArgTypes) {
        return ObfuscationReflectionHelper.findMethod(declaringClass, methodName, methodArgTypes);
    }
}