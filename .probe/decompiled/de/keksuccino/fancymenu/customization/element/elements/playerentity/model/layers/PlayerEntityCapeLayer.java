package de.keksuccino.fancymenu.customization.element.elements.playerentity.model.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import de.keksuccino.fancymenu.customization.element.elements.playerentity.model.PlayerEntityElementRenderer;
import de.keksuccino.fancymenu.customization.element.elements.playerentity.model.PlayerEntityProperties;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.PlayerModelPart;
import org.jetbrains.annotations.Nullable;

public class PlayerEntityCapeLayer extends PlayerEntityRenderLayer {

    public final PlayerEntityProperties properties;

    public final PlayerEntityElementRenderer renderer;

    public PlayerEntityCapeLayer(PlayerEntityElementRenderer renderer, PlayerEntityProperties properties) {
        super(renderer);
        this.properties = properties;
        this.renderer = renderer;
    }

    @Override
    public void render(PoseStack matrix, MultiBufferSource multiBufferSource0, int int1, @Nullable Entity entity, float float2, float float3, float float4, float float5, float float6, float float7) {
        if (!this.properties.invisible && this.properties.isModelPartShown(PlayerModelPart.CAPE) && this.properties.getCapeTextureLocation() != null) {
            matrix.pushPose();
            matrix.translate(0.0F, 0.0F, 0.125F);
            double d0 = Mth.lerp((double) float4, this.properties.xCloakO, this.properties.xCloak) - Mth.lerp((double) float4, this.properties.xo, 0.0);
            double d1 = Mth.lerp((double) float4, this.properties.yCloakO, this.properties.yCloak) - Mth.lerp((double) float4, this.properties.yo, 0.0);
            double d2 = Mth.lerp((double) float4, this.properties.zCloakO, this.properties.zCloak) - Mth.lerp((double) float4, this.properties.zo, 0.0);
            float f = this.properties.yBodyRotO + (this.properties.yBodyRot - this.properties.yBodyRotO);
            double d3 = (double) Mth.sin(f * (float) (Math.PI / 180.0));
            double d4 = (double) (-Mth.cos(f * (float) (Math.PI / 180.0)));
            float f1 = (float) d1 * 10.0F;
            f1 = Mth.clamp(f1, -6.0F, 32.0F);
            float f2 = (float) (d0 * d3 + d2 * d4) * 100.0F;
            f2 = Mth.clamp(f2, 0.0F, 150.0F);
            float f3 = (float) (d0 * d4 - d2 * d3) * 100.0F;
            f3 = Mth.clamp(f3, -20.0F, 20.0F);
            if (f2 < 0.0F) {
                f2 = 0.0F;
            }
            float f4 = Mth.lerp(float4, this.properties.oBob, this.properties.bob);
            f1 += Mth.sin(Mth.lerp(float4, 0.0F, 0.0F) * 6.0F) * 32.0F * f4;
            if (this.properties.isCrouching()) {
                f1 += 25.0F;
            }
            matrix.mulPose(Axis.XP.rotationDegrees(6.0F + f2 / 2.0F + f1));
            matrix.mulPose(Axis.ZP.rotationDegrees(f3 / 2.0F));
            matrix.mulPose(Axis.YP.rotationDegrees(180.0F - f3 / 2.0F));
            VertexConsumer vertexconsumer = multiBufferSource0.getBuffer(RenderType.entitySolid(this.properties.getCapeTextureLocation()));
            ((PlayerModel) this.renderer.m_7200_()).renderCloak(matrix, vertexconsumer, int1, OverlayTexture.NO_OVERLAY);
            matrix.popPose();
        }
    }
}