package com.mna.entities.renderers.sorcery;

import com.mna.api.affinity.Affinity;
import com.mna.entities.sorcery.targeting.Smite;
import com.mna.tools.render.ModelUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;

public class SmiteProjectileRenderer extends EntityRenderer<Smite> {

    final Minecraft mc = Minecraft.getInstance();

    public SmiteProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    public void render(Smite entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        if (!Minecraft.getInstance().isPaused()) {
            entityIn.spawnParticles(10, partialTicks);
        }
        ArrayList<Affinity> affs = entityIn.getAffinity();
        Affinity aff = Affinity.UNKNOWN;
        if (affs != null) {
            aff = (Affinity) affs.get(0);
        }
        int[] clr = aff.getColor();
        int[] sec = aff.getSecondaryColor();
        VertexConsumer builder = bufferIn.getBuffer(RenderType.translucent());
        int override = entityIn.getOverrideColor();
        if (override != -1) {
            clr = new int[] { FastColor.ARGB32.red(override), FastColor.ARGB32.green(override), FastColor.ARGB32.blue(override) };
            sec = clr;
        }
        switch(aff) {
            case ARCANE:
                this.renderWaterProjectile(entityIn, builder, matrixStackIn, packedLightIn, sec);
                matrixStackIn.translate(0.0, 0.0, 0.01);
                this.renderWaterProjectile(entityIn, builder, matrixStackIn, packedLightIn, clr);
            case LIGHTNING:
                break;
            default:
                this.renderWaterProjectile(entityIn, builder, matrixStackIn, packedLightIn, clr);
        }
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    private void renderWaterProjectile(Smite entityIn, VertexConsumer builder, PoseStack matrixStackIn, int packedLightIn, int[] color) {
        matrixStackIn.pushPose();
        matrixStackIn.mulPose(Axis.XP.rotationDegrees(90.0F));
        ModelUtils.renderEntityModel(builder, this.mc.level, SpellProjectileRenderer.model_water, matrixStackIn, packedLightIn, OverlayTexture.NO_OVERLAY, new float[] { (float) color[0] / 255.0F, (float) color[1] / 255.0F, (float) color[2] / 255.0F }, 0.75F);
        ModelUtils.renderEntityModel(builder, this.mc.level, SpellProjectileRenderer.model_water2, matrixStackIn, packedLightIn, OverlayTexture.NO_OVERLAY, new float[] { 1.0F, 1.0F, 1.0F }, 0.5F);
        matrixStackIn.popPose();
    }

    public ResourceLocation getTextureLocation(Smite entity) {
        return null;
    }
}