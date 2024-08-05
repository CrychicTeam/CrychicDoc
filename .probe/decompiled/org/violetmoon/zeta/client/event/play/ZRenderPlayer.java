package org.violetmoon.zeta.client.event.play;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.entity.player.Player;
import org.violetmoon.zeta.event.bus.IZetaPlayEvent;

public interface ZRenderPlayer extends IZetaPlayEvent {

    PlayerRenderer getRenderer();

    float getPartialTick();

    PoseStack getPoseStack();

    MultiBufferSource getMultiBufferSource();

    int getPackedLight();

    Player getEntity();

    public interface Post extends ZRenderPlayer {
    }

    public interface Pre extends ZRenderPlayer {
    }
}