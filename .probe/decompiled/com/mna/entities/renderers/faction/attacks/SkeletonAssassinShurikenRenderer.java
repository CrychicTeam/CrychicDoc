package com.mna.entities.renderers.faction.attacks;

import com.mna.entities.projectile.SkeletonAssassinShuriken;
import com.mna.items.ItemInit;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class SkeletonAssassinShurikenRenderer extends EntityRenderer<SkeletonAssassinShuriken> {

    private static final ResourceLocation shurikenTexture = new ResourceLocation("mna", "textures/entity/shuriken.png");

    private static ItemStack shurikenStack;

    private static ItemRenderer itemRenderer;

    private final Minecraft mc = Minecraft.getInstance();

    public SkeletonAssassinShurikenRenderer(EntityRendererProvider.Context context) {
        super(context);
        if (shurikenStack == null) {
            shurikenStack = new ItemStack(ItemInit.SKELETON_SHURIKEN.get());
        }
        if (itemRenderer == null) {
            itemRenderer = this.mc.getItemRenderer();
        }
    }

    public void render(SkeletonAssassinShuriken entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        matrixStackIn.pushPose();
        float ticks = ((float) entityIn.f_19797_ + partialTicks) * 50.0F;
        matrixStackIn.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, entityIn.f_19859_, entityIn.m_146908_()) - 90.0F));
        matrixStackIn.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTicks, entityIn.f_19860_, entityIn.m_146909_())));
        matrixStackIn.mulPose(Axis.XP.rotationDegrees(90.0F));
        if (!entityIn.isInGround()) {
            matrixStackIn.mulPose(Axis.ZP.rotationDegrees(ticks));
        }
        matrixStackIn.scale(0.5F, 0.5F, 0.25F);
        itemRenderer.renderStatic(shurikenStack, ItemDisplayContext.FIXED, 15728640, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn, this.mc.level, 0);
        matrixStackIn.popPose();
    }

    public ResourceLocation getTextureLocation(SkeletonAssassinShuriken entity) {
        return shurikenTexture;
    }
}