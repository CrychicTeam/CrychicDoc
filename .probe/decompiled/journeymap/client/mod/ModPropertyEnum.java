package journeymap.client.mod;

import java.lang.reflect.Method;
import java.util.Collection;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import journeymap.common.Journeymap;
import journeymap.common.log.LogFormatter;
import journeymap.common.util.ObfHelper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.apache.logging.log4j.Logger;

@ParametersAreNonnullByDefault
public class ModPropertyEnum<T> {

    private static final Logger logger = Journeymap.getLogger();

    private final boolean valid;

    private final EnumProperty propertyEnum;

    private final Method method;

    public ModPropertyEnum(EnumProperty propertyEnum, Method method, Class<T> returnType) {
        this.valid = propertyEnum != null && method != null;
        this.propertyEnum = propertyEnum;
        this.method = method;
    }

    public ModPropertyEnum(EnumProperty propertyEnum, String methodName, Class<T> returnType, Class<?>[] methodArgTypes) {
        this(propertyEnum, lookupMethod(propertyEnum, methodName, methodArgTypes), returnType);
    }

    public ModPropertyEnum(String declaringClassName, String propertyEnumStaticFieldName, String methodName, Class<T> returnType) {
        this(declaringClassName, propertyEnumStaticFieldName, methodName, returnType, new Class[0]);
    }

    public ModPropertyEnum(String declaringClassName, String propertyEnumStaticFieldName, String methodName, Class<T> returnType, Class<?>[] methodArgTypes) {
        this(lookupPropertyEnum(declaringClassName, propertyEnumStaticFieldName), methodName, returnType, methodArgTypes);
    }

    public ModPropertyEnum(String declaringClassName, String propertyEnumStaticFieldName, Method method, Class<T> returnType) {
        this(lookupPropertyEnum(declaringClassName, propertyEnumStaticFieldName), method, returnType);
    }

    public static EnumProperty lookupPropertyEnum(String declaringClassName, String propertyEnumStaticFieldName) {
        try {
            Class declaringClass = Class.forName(declaringClassName);
            return (EnumProperty) ObfHelper.findField(declaringClass, propertyEnumStaticFieldName).get(declaringClass);
        } catch (Exception var3) {
            Journeymap.getLogger().error("Error reflecting PropertyEnum on %s.%s: %s", declaringClassName, propertyEnumStaticFieldName, LogFormatter.toPartialString(var3));
            return null;
        }
    }

    public static Method lookupMethod(EnumProperty propertyEnum, String methodName, Class... methodArgTypes) {
        return propertyEnum != null ? lookupMethod(propertyEnum.m_61709_().getName(), methodName, methodArgTypes) : null;
    }

    public static Method lookupMethod(String declaringClassName, String methodName, Class... methodArgTypes) {
        try {
            Class declaringClass = Class.forName(declaringClassName);
            return ObfHelper.findMethod(declaringClass, methodName, methodArgTypes);
        } catch (Exception var4) {
            Journeymap.getLogger().error("Error reflecting method %s.%s(): %s", declaringClassName, methodName, LogFormatter.toPartialString(var4));
            return null;
        }
    }

    public EnumProperty getPropertyEnum() {
        return this.propertyEnum;
    }

    public boolean isValid() {
        return this.valid;
    }

    @Nullable
    public T getValue(BlockState blockState, @Nullable Object... args) {
        if (this.valid) {
            try {
                Comparable<?> enumValue = blockState.m_61143_(this.propertyEnum);
                if (enumValue != null) {
                    return (T) this.method.invoke(enumValue, args);
                }
            } catch (Exception var4) {
                logger.error("Error using mod PropertyEnum: " + LogFormatter.toPartialString(var4));
            }
        }
        return null;
    }

    @Nullable
    public static <T> T getFirstValue(Collection<ModPropertyEnum<T>> modPropertyEnums, BlockState blockState, @Nullable Object... args) {
        for (ModPropertyEnum<T> modPropertyEnum : modPropertyEnums) {
            T result = modPropertyEnum.getValue(blockState, args);
            if (result != null) {
                return result;
            }
        }
        return null;
    }
}