package fuzs.puzzleslib.api.capability.v2.data;

import java.util.Optional;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public interface CapabilityKey<C extends CapabilityComponent> {

    ResourceLocation getId();

    Class<C> getComponentClass();

    @Nullable
    <V> C get(@Nullable V var1);

    <V> Optional<C> maybeGet(@Nullable V var1);

    default <V> C orThrow(@Nullable V provider) {
        return (C) this.maybeGet(provider).orElseThrow(IllegalStateException::new);
    }

    default <V> boolean isProvidedBy(@Nullable V provider) {
        return this.get(provider) != null;
    }
}