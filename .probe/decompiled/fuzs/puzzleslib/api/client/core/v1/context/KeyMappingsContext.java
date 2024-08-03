package fuzs.puzzleslib.api.client.core.v1.context;

import com.google.common.base.Preconditions;
import fuzs.puzzleslib.api.client.screen.v2.KeyMappingActivationHelper;
import java.util.Objects;
import net.minecraft.client.KeyMapping;

@FunctionalInterface
public interface KeyMappingsContext {

    @Deprecated(forRemoval = true)
    default void registerKeyMapping(KeyMapping... keyMappings) {
        Objects.requireNonNull(keyMappings, "key mappings is null");
        Preconditions.checkPositionIndex(1, keyMappings.length, "key mappings is empty");
        for (KeyMapping keyMapping : keyMappings) {
            Objects.requireNonNull(keyMapping, "key mapping is null");
            this.registerKeyMapping(keyMapping, KeyMappingActivationHelper.KeyActivationContext.UNIVERSAL);
        }
    }

    void registerKeyMapping(KeyMapping var1, KeyMappingActivationHelper.KeyActivationContext var2);
}