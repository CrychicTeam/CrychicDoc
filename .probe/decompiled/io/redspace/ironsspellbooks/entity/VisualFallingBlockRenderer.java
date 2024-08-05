package io.redspace.ironsspellbooks.entity;

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
public class VisualFallingBlockRenderer extends EntityRenderer<VisualFallingBlockEntity> {

    private final BlockRenderDispatcher dispatcher;

    public VisualFallingBlockRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
        this.f_114477_ = 0.5F;
        this.dispatcher = pContext.getBlockRenderDispatcher();
    }

    public void render(VisualFallingBlockEntity entity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
        BlockState blockstate = entity.m_31980_();
        if (blockstate.m_60799_() == RenderShape.MODEL) {
            Level level = entity.f_19853_;
            pMatrixStack.pushPose();
            BlockPos blockpos = BlockPos.containing(entity.m_20185_(), entity.m_20191_().maxY, entity.m_20189_());
            pMatrixStack.translate(-0.5, 0.0, -0.5);
            BakedModel model = this.dispatcher.getBlockModel(blockstate);
            for (RenderType renderType : model.getRenderTypes(blockstate, RandomSource.create(blockstate.m_60726_(entity.m_31978_())), ModelData.EMPTY)) {
                this.dispatcher.getModelRenderer().tesselateBlock(level, model, blockstate, blockpos, pMatrixStack, pBuffer.getBuffer(renderType), false, RandomSource.create(), blockstate.m_60726_(entity.m_31978_()), OverlayTexture.NO_OVERLAY, ModelData.EMPTY, renderType);
            }
            pMatrixStack.popPose();
            super.render(entity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
        }
    }

    public ResourceLocation getTextureLocation(VisualFallingBlockEntity pEntity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}