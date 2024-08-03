package fuzs.puzzleslib.api.core.v1;

import java.util.Optional;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public interface ObjectShareAccess {

    default <T> Optional<T> getOptional(ResourceLocation key) {
        return this.getOptional(key.toString());
    }

    default <T> Optional<T> getOptional(String key) {
        return Optional.ofNullable(this.get(key));
    }

    @Nullable
    default Object get(ResourceLocation key) {
        return this.get(key.toString());
    }

    @Nullable
    Object get(String var1);

    @Nullable
    default Object put(ResourceLocation key, Object value) {
        return this.put(key.toString(), value);
    }

    @Nullable
    Object put(String var1, Object var2);

    @Nullable
    default Object putIfAbsent(ResourceLocation key, Object value) {
        return this.putIfAbsent(key.toString(), value);
    }

    @Nullable
    Object putIfAbsent(String var1, Object var2);

    @Nullable
    default Object remove(ResourceLocation key) {
        return this.remove(key.toString());
    }

    @Nullable
    Object remove(String var1);
}