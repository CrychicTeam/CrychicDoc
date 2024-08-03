package lio.playeranimatorapi.modifier;

import dev.kosmx.playerAnim.api.layered.modifier.MirrorModifier;
import lio.playeranimatorapi.playeranims.CustomModifierLayer;
import net.minecraft.client.Minecraft;

public class MirrorOnAltHandModifier extends MirrorModifier {

    CustomModifierLayer layer;

    public MirrorOnAltHandModifier(CustomModifierLayer layer) {
        this.layer = layer;
    }

    @Override
    public boolean isEnabled() {
        return this.layer.player == Minecraft.getInstance().player && Minecraft.getInstance().options.mainHand().get().getId() == 0;
    }
}