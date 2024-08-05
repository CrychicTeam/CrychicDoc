package org.violetmoon.zetaimplforge.client.event.play;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.RenderPlayerEvent;
import org.violetmoon.zeta.client.event.play.ZRenderPlayer;

public abstract class ForgeZRenderPlayer implements ZRenderPlayer {

    private final RenderPlayerEvent e;

    public ForgeZRenderPlayer(RenderPlayerEvent e) {
        this.e = e;
    }

    @Override
    public PlayerRenderer getRenderer() {
        return this.e.getRenderer();
    }

    @Override
    public float getPartialTick() {
        return this.e.getPartialTick();
    }

    @Override
    public PoseStack getPoseStack() {
        return this.e.getPoseStack();
    }

    @Override
    public MultiBufferSource getMultiBufferSource() {
        return this.e.getMultiBufferSource();
    }

    @Override
    public int getPackedLight() {
        return this.e.getPackedLight();
    }

    @Override
    public Player getEntity() {
        return this.e.getEntity();
    }

    public static class Post extends ForgeZRenderPlayer implements ZRenderPlayer.Post {

        public Post(RenderPlayerEvent.Post e) {
            super(e);
        }
    }

    public static class Pre extends ForgeZRenderPlayer implements ZRenderPlayer.Pre {

        public Pre(RenderPlayerEvent.Pre e) {
            super(e);
        }
    }
}