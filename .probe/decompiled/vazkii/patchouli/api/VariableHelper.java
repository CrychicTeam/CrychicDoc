package vazkii.patchouli.api;

import com.google.common.base.Suppliers;
import com.google.gson.JsonElement;
import java.util.function.Supplier;
import org.jetbrains.annotations.Nullable;

public interface VariableHelper {

    Supplier<VariableHelper> INSTANCE = Suppliers.memoize(() -> {
        try {
            return (VariableHelper) Class.forName("vazkii.patchouli.client.book.template.variable.VariableHelperImpl").newInstance();
        } catch (ReflectiveOperationException var1) {
            return new VariableHelper() {
            };
        }
    });

    static VariableHelper instance() {
        return (VariableHelper) INSTANCE.get();
    }

    default <T> IVariable createFromObject(T object) {
        return null;
    }

    default IVariable createFromJson(JsonElement elem) {
        return null;
    }

    @Nullable
    default <T> IVariableSerializer<T> serializerForClass(Class<T> clazz) {
        return null;
    }

    default <T> VariableHelper registerSerializer(IVariableSerializer<T> serializer, Class<T> clazz) {
        return instance();
    }
}