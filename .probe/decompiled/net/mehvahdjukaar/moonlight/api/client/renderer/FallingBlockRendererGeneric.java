package net.mehvahdjukaar.moonlight.api.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.mehvahdjukaar.moonlight.api.client.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;

public class FallingBlockRendererGeneric<T extends FallingBlockEntity> extends EntityRenderer<T> {

    public FallingBlockRendererGeneric(EntityRendererProvider.Context context) {
        super(context);
        this.f_114477_ = 0.5F;
    }

    public ResourceLocation getTextureLocation(T pEntity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }

    public void render(T entity, float pEntityYaw, float pPartialTicks, PoseStack poseStack, MultiBufferSource buffer, int pPackedLight) {
        BlockState state = entity.getBlockState();
        if (state.m_60799_() == RenderShape.MODEL) {
            Level level = entity.m_9236_();
            BlockPos pos = entity.m_20183_();
            boolean isJustSpawned = Math.abs(entity.m_20186_() - (double) pos.m_123342_()) < 0.02 && entity.f_19797_ < 0 && state != level.getBlockState(pos);
            if (!isJustSpawned && state.m_60799_() != RenderShape.INVISIBLE) {
                poseStack.pushPose();
                BlockPos blockPos = BlockPos.containing(entity.m_20185_(), entity.m_20191_().maxY, entity.m_20189_());
                poseStack.translate(-0.5, 0.0, -0.5);
                BlockRenderDispatcher dispatcher = Minecraft.getInstance().getBlockRenderer();
                RenderUtil.renderBlock(state.m_60726_(entity.getStartPos()), poseStack, buffer, state, level, blockPos, dispatcher);
                poseStack.popPose();
                super.render(entity, pEntityYaw, pPartialTicks, poseStack, buffer, pPackedLight);
            }
        }
    }
}