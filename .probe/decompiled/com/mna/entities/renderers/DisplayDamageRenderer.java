package com.mna.entities.renderers;

import com.mna.ManaAndArtifice;
import com.mna.effects.EffectInit;
import com.mna.entities.utility.DisplayDamage;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;

public class DisplayDamageRenderer extends EntityRenderer<DisplayDamage> {

    public DisplayDamageRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    public void render(DisplayDamage pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
        if (ManaAndArtifice.instance.proxy.getClientPlayer().m_21023_(EffectInit.INSIGHT.get())) {
            float f = pEntity.m_20206_() + 0.5F;
            pMatrixStack.pushPose();
            pMatrixStack.translate(0.0, (double) f, 0.0);
            pMatrixStack.mulPose(this.f_114476_.cameraOrientation());
            pMatrixStack.scale(-0.025F, -0.025F, 0.025F);
            Matrix4f matrix4f = pMatrixStack.last().pose();
            Font font = this.m_114481_();
            Component amount = Component.literal(String.format("%.2f", pEntity.getAmount()));
            float f2 = (float) (-font.width(amount) / 2);
            font.drawInBatch(amount, f2, 0.0F, pEntity.getColor(), true, matrix4f, pBuffer, Font.DisplayMode.NORMAL, 0, 15728880);
            pMatrixStack.popPose();
        }
    }

    public ResourceLocation getTextureLocation(DisplayDamage pEntity) {
        return null;
    }

    protected boolean shouldShowName(DisplayDamage pEntity) {
        return true;
    }

    protected void renderNameTag(DisplayDamage pEntity, Component pDisplayName, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
    }
}