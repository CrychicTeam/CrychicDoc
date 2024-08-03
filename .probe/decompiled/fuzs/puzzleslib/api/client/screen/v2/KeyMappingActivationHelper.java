package fuzs.puzzleslib.api.client.screen.v2;

import fuzs.puzzleslib.impl.client.core.ClientFactories;
import net.minecraft.client.KeyMapping;

public interface KeyMappingActivationHelper {

    KeyMappingActivationHelper INSTANCE = ClientFactories.INSTANCE.getKeyMappingActivationHelper();

    KeyMappingActivationHelper.KeyActivationContext getKeyActivationContext(KeyMapping var1);

    default boolean hasConflictWith(KeyMapping keyMapping, KeyMapping other) {
        return this.getKeyActivationContext(keyMapping).hasConflictWith(this.getKeyActivationContext(other));
    }

    public static enum KeyActivationContext {

        UNIVERSAL, GAME, SCREEN;

        public boolean hasConflictWith(KeyMappingActivationHelper.KeyActivationContext other) {
            return this == UNIVERSAL || other == UNIVERSAL || this == other;
        }
    }
}