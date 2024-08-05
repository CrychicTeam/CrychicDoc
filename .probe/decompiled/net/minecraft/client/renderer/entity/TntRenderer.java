package net.minecraft.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.block.Blocks;

public class TntRenderer extends EntityRenderer<PrimedTnt> {

    private final BlockRenderDispatcher blockRenderer;

    public TntRenderer(EntityRendererProvider.Context entityRendererProviderContext0) {
        super(entityRendererProviderContext0);
        this.f_114477_ = 0.5F;
        this.blockRenderer = entityRendererProviderContext0.getBlockRenderDispatcher();
    }

    public void render(PrimedTnt primedTnt0, float float1, float float2, PoseStack poseStack3, MultiBufferSource multiBufferSource4, int int5) {
        poseStack3.pushPose();
        poseStack3.translate(0.0F, 0.5F, 0.0F);
        int $$6 = primedTnt0.getFuse();
        if ((float) $$6 - float2 + 1.0F < 10.0F) {
            float $$7 = 1.0F - ((float) $$6 - float2 + 1.0F) / 10.0F;
            $$7 = Mth.clamp($$7, 0.0F, 1.0F);
            $$7 *= $$7;
            $$7 *= $$7;
            float $$8 = 1.0F + $$7 * 0.3F;
            poseStack3.scale($$8, $$8, $$8);
        }
        poseStack3.mulPose(Axis.YP.rotationDegrees(-90.0F));
        poseStack3.translate(-0.5F, -0.5F, 0.5F);
        poseStack3.mulPose(Axis.YP.rotationDegrees(90.0F));
        TntMinecartRenderer.renderWhiteSolidBlock(this.blockRenderer, Blocks.TNT.defaultBlockState(), poseStack3, multiBufferSource4, int5, $$6 / 5 % 2 == 0);
        poseStack3.popPose();
        super.render(primedTnt0, float1, float2, poseStack3, multiBufferSource4, int5);
    }

    public ResourceLocation getTextureLocation(PrimedTnt primedTnt0) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}