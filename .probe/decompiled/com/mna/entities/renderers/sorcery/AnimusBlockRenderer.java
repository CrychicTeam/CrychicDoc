package com.mna.entities.renderers.sorcery;

import com.mna.entities.summon.AnimusBlock;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.SlimeModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;

public class AnimusBlockRenderer extends MobRenderer<AnimusBlock, SlimeModel<AnimusBlock>> {

    private final BlockRenderDispatcher dispatcher;

    public AnimusBlockRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new SlimeModel<>(pContext.bakeLayer(ModelLayers.SLIME)), 0.25F);
        this.dispatcher = pContext.getBlockRenderDispatcher();
    }

    public void render(AnimusBlock pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
        BlockState blockstate = pEntity.getFeetBlockState();
        if (blockstate.m_60799_() == RenderShape.MODEL) {
            Level level = pEntity.m_9236_();
            if (blockstate != level.getBlockState(pEntity.m_20183_()) && blockstate.m_60799_() != RenderShape.INVISIBLE) {
                pMatrixStack.pushPose();
                BlockPos blockpos = BlockPos.containing(pEntity.m_20185_(), pEntity.m_20191_().maxY, pEntity.m_20189_());
                float f = Mth.rotLerp(pPartialTicks, pEntity.f_20884_, pEntity.f_20883_);
                pMatrixStack.mulPose(Axis.YP.rotationDegrees(-f));
                this.scale(pEntity, pMatrixStack, pPartialTicks);
                pMatrixStack.translate(-0.5, 0.0, -0.5);
                BakedModel model = this.dispatcher.getBlockModel(blockstate);
                for (RenderType renderType : model.getRenderTypes(blockstate, RandomSource.create(432L), ModelData.EMPTY)) {
                    this.dispatcher.getModelRenderer().tesselateBlock(level, model, blockstate, blockpos, pMatrixStack, pBuffer.getBuffer(renderType), false, RandomSource.create(), 432L, OverlayTexture.NO_OVERLAY, ModelData.EMPTY, renderType);
                }
                pMatrixStack.popPose();
            }
        }
    }

    protected void scale(AnimusBlock entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
        matrixStackIn.scale(0.999F, 0.999F, 0.999F);
        matrixStackIn.translate(0.0, 0.001F, 0.0);
        float f1 = 1.0F;
        float f2 = Mth.lerp(partialTickTime, entitylivingbaseIn.prevSquishFactor, entitylivingbaseIn.squishFactor) / (f1 * 0.5F + 1.0F);
        float f3 = 1.0F / (f2 + 1.0F);
        matrixStackIn.scale(f3 * f1, 1.0F / f3 * f1, f3 * f1);
    }

    public ResourceLocation getTextureLocation(AnimusBlock entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}