package net.liopyu.animationjs.utils;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;

public class ContextUtils {

    public static class PlayerRenderContext {

        public final PlayerRenderer renderer;

        public final AbstractClientPlayer entity;

        public final float entityYaw;

        public final float partialTicks;

        public final PoseStack poseStack;

        public final MultiBufferSource buffer;

        public final int packedLight;

        public PlayerRenderContext(PlayerRenderer renderer, AbstractClientPlayer entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
            this.renderer = renderer;
            this.entity = entity;
            this.entityYaw = entityYaw;
            this.partialTicks = partialTicks;
            this.poseStack = poseStack;
            this.buffer = buffer;
            this.packedLight = packedLight;
        }
    }
}