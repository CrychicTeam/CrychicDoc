package com.mna.entities.renderers;

import com.mna.entities.utility.RisingBlock;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.ModelData;

@OnlyIn(Dist.CLIENT)
public class RisingBlockRenderer extends EntityRenderer<RisingBlock> {

    private final BlockRenderDispatcher dispatcher;

    public RisingBlockRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
        this.f_114477_ = 0.5F;
        this.dispatcher = pContext.getBlockRenderDispatcher();
    }

    public void render(RisingBlock pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
        BlockState blockstate = pEntity.getBlockState();
        if (blockstate.m_60799_() == RenderShape.MODEL) {
            Level level = pEntity.m_9236_();
            if (blockstate != level.getBlockState(pEntity.m_20183_()) && blockstate.m_60799_() != RenderShape.INVISIBLE) {
                pMatrixStack.pushPose();
                BlockPos blockpos = BlockPos.containing(pEntity.m_20185_(), pEntity.m_20191_().maxY, pEntity.m_20189_());
                pMatrixStack.translate(-0.5, 0.0, -0.5);
                BakedModel model = this.dispatcher.getBlockModel(blockstate);
                for (RenderType renderType : model.getRenderTypes(blockstate, RandomSource.create(blockstate.m_60726_(pEntity.getStartPos())), ModelData.EMPTY)) {
                    this.dispatcher.getModelRenderer().tesselateBlock(level, model, blockstate, blockpos, pMatrixStack, pBuffer.getBuffer(renderType), false, RandomSource.create(), blockstate.m_60726_(pEntity.getStartPos()), OverlayTexture.NO_OVERLAY, ModelData.EMPTY, renderType);
                }
                pMatrixStack.popPose();
                super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
            }
        }
    }

    public ResourceLocation getTextureLocation(RisingBlock risingBlock0) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}