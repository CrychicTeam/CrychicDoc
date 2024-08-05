package fuzs.puzzleslib.impl.client.screen;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import fuzs.puzzleslib.api.client.screen.v2.KeyMappingActivationHelper;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.IKeyConflictContext;
import net.minecraftforge.client.settings.KeyConflictContext;

public final class ForgeKeyMappingActivationHelper implements KeyMappingActivationHelper {

    public static final BiMap<KeyMappingActivationHelper.KeyActivationContext, IKeyConflictContext> KEY_CONTEXTS = ImmutableBiMap.of(KeyMappingActivationHelper.KeyActivationContext.UNIVERSAL, KeyConflictContext.UNIVERSAL, KeyMappingActivationHelper.KeyActivationContext.GAME, KeyConflictContext.IN_GAME, KeyMappingActivationHelper.KeyActivationContext.SCREEN, KeyConflictContext.GUI);

    @Override
    public KeyMappingActivationHelper.KeyActivationContext getKeyActivationContext(KeyMapping keyMapping) {
        return (KeyMappingActivationHelper.KeyActivationContext) KEY_CONTEXTS.inverse().getOrDefault(keyMapping.getKeyConflictContext(), KeyMappingActivationHelper.KeyActivationContext.UNIVERSAL);
    }
}