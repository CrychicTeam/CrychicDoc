package com.mna.blocks.tileentities.renderers.wizard_lab;

import com.mna.api.tools.RLoc;
import com.mna.blocks.sorcery.InscriptionTableBlock;
import com.mna.blocks.tileentities.models.ModelInscriptionTable;
import com.mna.blocks.tileentities.wizard_lab.InscriptionTableTile;
import com.mna.tools.render.ModelUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class InscriptionTableRenderer extends GeoBlockRenderer<InscriptionTableTile> {

    public static final ResourceLocation ink = RLoc.create("block/wizard_lab/inscription_table_ink");

    public static final ResourceLocation ash = RLoc.create("block/wizard_lab/inscription_table_ash");

    public static final ResourceLocation vellum = RLoc.create("block/wizard_lab/inscription_table_vellum");

    public InscriptionTableRenderer(BlockEntityRendererProvider.Context rendererDispatcherIn) {
        super(new ModelInscriptionTable());
    }

    public RenderType getRenderType(InscriptionTableTile animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entitySmoothCutout(texture);
    }

    public void postRender(PoseStack poseStack, InscriptionTableTile animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        Level world = animatable.m_58904_();
        BlockPos pos = animatable.m_58899_();
        BlockState state = animatable.m_58900_();
        poseStack.pushPose();
        Direction dir = (Direction) state.m_61143_(InscriptionTableBlock.f_54117_);
        switch(dir) {
            case EAST:
                poseStack.translate(1.0F, 0.0F, 0.0F);
                poseStack.mulPose(Axis.YP.rotationDegrees(dir.toYRot()));
                break;
            case WEST:
                poseStack.translate(0.0F, 0.0F, 1.0F);
                poseStack.mulPose(Axis.YP.rotationDegrees(dir.toYRot()));
            case NORTH:
                break;
            case SOUTH:
            default:
                poseStack.translate(1.0F, 0.0F, 1.0F);
                poseStack.mulPose(Axis.YP.rotationDegrees(dir.getOpposite().toYRot()));
        }
        if (((Integer) state.m_61143_(InscriptionTableBlock.CONTAINED_RESOURCES) & 1) != 0) {
            ModelUtils.renderModel(bufferSource.getBuffer(RenderType.cutout()), world, pos, state, ink, poseStack, packedLight, packedOverlay);
        }
        if (((Integer) state.m_61143_(InscriptionTableBlock.CONTAINED_RESOURCES) & 2) != 0) {
            ModelUtils.renderModel(bufferSource.getBuffer(RenderType.cutout()), world, pos, state, vellum, poseStack, packedLight, packedOverlay);
        }
        if (((Integer) state.m_61143_(InscriptionTableBlock.CONTAINED_RESOURCES) & 4) != 0) {
            ModelUtils.renderModel(bufferSource.getBuffer(RenderType.cutout()), world, pos, state, ash, poseStack, packedLight, packedOverlay);
        }
        poseStack.popPose();
        super.postRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    public void actuallyRender(PoseStack poseStack, InscriptionTableTile animatable, BakedGeoModel model, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(90.0F));
        poseStack.translate(-1.0F, 0.0F, 0.0F);
        super.actuallyRender(poseStack, animatable, model, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
        poseStack.popPose();
    }
}