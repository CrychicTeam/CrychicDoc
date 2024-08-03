package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.entity.EntityGhostSword;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class RenderGhostSword extends EntityRenderer<EntityGhostSword> {

    public RenderGhostSword(EntityRendererProvider.Context context) {
        super(context);
    }

    @NotNull
    public ResourceLocation getTextureLocation(@NotNull EntityGhostSword entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }

    public void render(EntityGhostSword entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, @NotNull MultiBufferSource bufferIn, int packedLightIn) {
        matrixStackIn.pushPose();
        matrixStackIn.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, entityIn.f_19859_, entityIn.m_146908_()) - 90.0F));
        matrixStackIn.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTicks, entityIn.f_19860_, entityIn.m_146909_())));
        matrixStackIn.translate(0.0F, 0.5F, 0.0F);
        matrixStackIn.scale(2.0F, 2.0F, 2.0F);
        matrixStackIn.mulPose(Axis.YP.rotationDegrees(0.0F));
        matrixStackIn.mulPose(Axis.ZN.rotationDegrees(((float) entityIn.f_19797_ + partialTicks) * 30.0F));
        matrixStackIn.translate(0.0F, -0.15F, 0.0F);
        Minecraft.getInstance().getItemRenderer().renderStatic(new ItemStack(IafItemRegistry.GHOST_SWORD.get()), ItemDisplayContext.GROUND, 240, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn, Minecraft.getInstance().level, 0);
        matrixStackIn.popPose();
    }
}