package com.github.alexmodguy.alexscaves.client.render.entity;

import com.github.alexmodguy.alexscaves.server.entity.item.FallingTreeBlockEntity;
import com.github.alexmodguy.alexscaves.server.entity.util.MovingBlockData;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;

public class FallingTreeBlockRenderer extends EntityRenderer<FallingTreeBlockEntity> {

    public FallingTreeBlockRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.f_114477_ = 0.5F;
    }

    public void render(FallingTreeBlockEntity entity, float f1, float f2, PoseStack stack, MultiBufferSource source, int i) {
        stack.pushPose();
        float fallProgress = entity.getFallProgress(f2);
        this.rotateBasedOnDirection(stack, entity.getFallDirection(), fallProgress * fallProgress * 90.0F);
        stack.pushPose();
        for (MovingBlockData data : entity.getData()) {
            BlockState blockstate = data.getState();
            if (blockstate.m_60799_() != RenderShape.INVISIBLE) {
                stack.pushPose();
                stack.translate(-0.5, -0.5, -0.5);
                stack.translate((float) data.getOffset().m_123341_(), (float) data.getOffset().m_123342_(), (float) data.getOffset().m_123343_());
                if (blockstate.m_60799_() == RenderShape.ENTITYBLOCK_ANIMATED && blockstate.m_61138_(HorizontalDirectionalBlock.FACING)) {
                    float f = ((Direction) blockstate.m_61143_(HorizontalDirectionalBlock.FACING)).toYRot();
                    stack.translate(0.5, 0.5, 0.5);
                    stack.mulPose(Axis.YP.rotationDegrees(-f));
                    stack.translate(-0.5, -0.5, -0.5);
                }
                Minecraft.getInstance().getBlockRenderer().renderSingleBlock(blockstate, stack, source, i, OverlayTexture.NO_OVERLAY);
                stack.popPose();
            }
        }
        stack.popPose();
        stack.popPose();
        super.render(entity, f1, f2, stack, source, i);
    }

    private void rotateBasedOnDirection(PoseStack poseStack, Direction fallDirection, float f) {
        switch(fallDirection) {
            case NORTH:
                poseStack.mulPose(Axis.XN.rotationDegrees(f));
                break;
            case SOUTH:
                poseStack.mulPose(Axis.XP.rotationDegrees(f));
                break;
            case EAST:
                poseStack.mulPose(Axis.ZN.rotationDegrees(f));
                break;
            case WEST:
                poseStack.mulPose(Axis.ZP.rotationDegrees(f));
        }
    }

    public ResourceLocation getTextureLocation(FallingTreeBlockEntity entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}