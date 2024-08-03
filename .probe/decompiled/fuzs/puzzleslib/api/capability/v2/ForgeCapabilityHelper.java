package fuzs.puzzleslib.api.capability.v2;

import fuzs.puzzleslib.api.capability.v2.data.CapabilityComponent;
import fuzs.puzzleslib.api.capability.v2.data.CapabilityKey;
import fuzs.puzzleslib.impl.capability.data.ForgeCapabilityKey;
import net.minecraftforge.common.capabilities.CapabilityToken;

public final class ForgeCapabilityHelper {

    private ForgeCapabilityHelper() {
    }

    public static <C extends CapabilityComponent> void setCapabilityToken(CapabilityKey<C> key, CapabilityToken<C> token) {
        ((ForgeCapabilityKey) key).createCapability(token);
    }
}