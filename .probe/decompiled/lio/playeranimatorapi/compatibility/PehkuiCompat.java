package lio.playeranimatorapi.compatibility;

import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.phys.Vec2;
import virtuoel.pehkui.api.ScaleTypes;

public class PehkuiCompat {

    public static Vec2 getScale(AbstractClientPlayer player, float tickDelta) {
        return new Vec2(ScaleTypes.MODEL_WIDTH.getScaleData(player).getScale(tickDelta), ScaleTypes.MODEL_HEIGHT.getScaleData(player).getScale(tickDelta));
    }
}