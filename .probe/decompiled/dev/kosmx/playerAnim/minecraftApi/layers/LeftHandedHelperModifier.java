package dev.kosmx.playerAnim.minecraftApi.layers;

import dev.kosmx.playerAnim.api.layered.modifier.MirrorModifier;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;

public class LeftHandedHelperModifier extends MirrorModifier {

    private final Player player;

    public LeftHandedHelperModifier(Player player) {
        this.player = player;
    }

    @Override
    public boolean isEnabled() {
        return super.isEnabled() && this.player.getMainArm() == HumanoidArm.LEFT;
    }
}