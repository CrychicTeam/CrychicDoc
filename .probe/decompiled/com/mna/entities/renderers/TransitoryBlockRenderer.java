package com.mna.entities.renderers;

import com.mna.entities.utility.StationaryBlock;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
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
public class TransitoryBlockRenderer extends EntityRenderer<StationaryBlock> {

    public TransitoryBlockRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.f_114477_ = 0.5F;
    }

    public void render(StationaryBlock entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        BlockState blockstate = entityIn.m_31980_();
        if (blockstate.m_60799_() == RenderShape.MODEL) {
            Level world = entityIn.m_9236_();
            if (blockstate != world.getBlockState(entityIn.m_20183_()) && blockstate.m_60799_() != RenderShape.INVISIBLE) {
                matrixStackIn.pushPose();
                BlockPos blockpos = BlockPos.containing(entityIn.m_20185_(), entityIn.m_20191_().maxY, entityIn.m_20189_());
                matrixStackIn.translate(-0.5, 0.0, -0.5);
                BlockRenderDispatcher blockrendererdispatcher = Minecraft.getInstance().getBlockRenderer();
                RenderType type = RenderType.translucent();
                blockrendererdispatcher.getModelRenderer().tesselateBlock(world, blockrendererdispatcher.getBlockModel(blockstate), blockstate, blockpos, matrixStackIn, bufferIn.getBuffer(type), false, RandomSource.create(), blockstate.m_60726_(entityIn.m_31978_()), OverlayTexture.NO_OVERLAY, ModelData.EMPTY, type);
                matrixStackIn.popPose();
                super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
            }
        }
    }

    public ResourceLocation getTextureLocation(StationaryBlock pEntity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}