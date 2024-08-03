package com.mna.blocks.tileentities.renderers;

import com.mna.blocks.FacingBlock;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class SimpleGeoBlockRenderer<V extends BlockEntity & GeoAnimatable> extends GeoBlockRenderer<V> {

    public SimpleGeoBlockRenderer(BlockEntityRendererProvider.Context rendererProvider, GeoModel<V> model) {
        super(model);
    }

    @Override
    public void render(BlockEntity tile, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        if (tile.getBlockState().m_60734_() instanceof FacingBlock) {
            BlockState state = tile.getBlockState();
            Direction dir = (Direction) state.m_61143_(FacingBlock.FACING);
            int face = (Integer) state.m_61143_(FacingBlock.SURFACE_TYPE);
            switch(face) {
                case 2:
                    poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
                    poseStack.translate(0.0F, -1.0F, -1.0F);
                    break;
                case 3:
                    poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
                    switch(dir) {
                        case EAST:
                            poseStack.mulPose(Axis.ZP.rotationDegrees(270.0F));
                            poseStack.translate(-1.0F, 0.0F, -1.0F);
                            break;
                        case WEST:
                            poseStack.mulPose(Axis.ZP.rotationDegrees(90.0F));
                            poseStack.translate(0.0F, -1.0F, -1.0F);
                            break;
                        case NORTH:
                            poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
                            poseStack.translate(-1.0F, -1.0F, -1.0F);
                            break;
                        case SOUTH:
                        default:
                            poseStack.translate(0.0F, 0.0F, -1.0F);
                    }
            }
        }
        super.render(tile, partialTicks, poseStack, bufferSource, packedLight, packedOverlay);
    }
}