package com.mna.entities.renderers.sorcery;

import com.mna.entities.summon.InsectSwarm;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class InsectSwarmRenderer extends EntityRenderer<InsectSwarm> {

    public InsectSwarmRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    public void render(InsectSwarm pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
        pEntity.spawnParticle();
    }

    public ResourceLocation getTextureLocation(InsectSwarm entity) {
        return null;
    }
}