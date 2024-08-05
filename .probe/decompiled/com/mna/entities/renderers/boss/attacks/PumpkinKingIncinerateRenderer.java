package com.mna.entities.renderers.boss.attacks;

import com.mna.entities.boss.attacks.PumpkinKingIncinerate;
import com.mna.events.ClientEventHandler;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class PumpkinKingIncinerateRenderer extends EntityRenderer<PumpkinKingIncinerate> {

    public PumpkinKingIncinerateRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    public void render(PumpkinKingIncinerate p_225623_1_, float p_225623_2_, float p_225623_3_, PoseStack p_225623_4_, MultiBufferSource p_225623_5_, int p_225623_6_) {
        ClientEventHandler.setFogAmount(Math.min(ClientEventHandler.getFogAmount() + 0.025F, 0.85F));
        ClientEventHandler.fogColor[0] = 0.314F;
        ClientEventHandler.fogColor[1] = 0.14F;
        ClientEventHandler.fogColor[2] = 0.06F;
        super.render(p_225623_1_, p_225623_2_, p_225623_3_, p_225623_4_, p_225623_5_, p_225623_6_);
    }

    public ResourceLocation getTextureLocation(PumpkinKingIncinerate p_110775_1_) {
        return null;
    }
}