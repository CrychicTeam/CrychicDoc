package com.mna.blocks.tileentities.renderers;

import com.mna.api.tools.RLoc;
import com.mna.blocks.artifice.LodestarBlock;
import com.mna.blocks.tileentities.LodestarTile;
import com.mna.blocks.tileentities.models.ModelLodestar;
import com.mna.tools.render.ModelUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoBlockRenderer;
import software.bernie.geckolib.util.RenderUtils;

public class LodestarRenderer extends GeoBlockRenderer<LodestarTile> {

    public static final ResourceLocation delegation_station = RLoc.create("block/delegation_station");

    public static final ResourceLocation base = RLoc.create("block/lodestar_base");

    public static final ResourceLocation small_crystal = RLoc.create("block/lodestar_crystalsmall");

    public static final ResourceLocation big_crystal = RLoc.create("block/lodestar_crystalbig");

    public static final ResourceLocation small_gear = RLoc.create("block/lodestar_gearsmall");

    public static final ResourceLocation stabilizer = RLoc.create("block/lodestar_stabilizer");

    protected Minecraft mc = Minecraft.getInstance();

    protected boolean low_tier;

    public LodestarRenderer(BlockEntityRendererProvider.Context context) {
        super(new ModelLodestar());
    }

    public RenderType getRenderType(LodestarTile animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entitySmoothCutout(texture);
    }

    public void renderRecursively(PoseStack poseStack, LodestarTile animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if (this.mc != null) {
            poseStack.pushPose();
            RenderUtils.translateMatrixToBone(poseStack, bone);
            RenderUtils.translateToPivotPoint(poseStack, bone);
            RenderUtils.rotateMatrixAroundBone(poseStack, bone);
            RenderUtils.scaleMatrixForBone(poseStack, bone);
            BlockState state = animatable.m_58900_();
            BlockPos pos = animatable.m_58899_();
            if (!bone.isHidden()) {
                boolean low_tier = (Boolean) state.m_61143_(LodestarBlock.LOW_TIER);
                poseStack.pushPose();
                String var18 = bone.getName();
                switch(var18) {
                    case "ROOT":
                        if (!low_tier) {
                            ModelUtils.renderModel(bufferSource, this.mc.level, pos, state, base, poseStack, packedLight, packedOverlay);
                        } else {
                            ModelUtils.renderModel(bufferSource, this.mc.level, pos, state, delegation_station, poseStack, packedLight, packedOverlay);
                        }
                        break;
                    case "CRYSTAL_BIG":
                        if (!low_tier) {
                            ModelUtils.renderModel(bufferSource, this.mc.level, pos, state, big_crystal, poseStack, packedLight, packedOverlay);
                        }
                        break;
                    case "STABILIZER":
                        if (!low_tier) {
                            ModelUtils.renderModel(bufferSource, this.mc.level, pos, state, stabilizer, poseStack, packedLight, packedOverlay);
                        }
                        break;
                    case "GEAR_ROTATION":
                        if (!low_tier) {
                            ModelUtils.renderModel(bufferSource, this.mc.level, pos, state, small_gear, poseStack, packedLight, packedOverlay);
                        }
                        break;
                    case "CRYSTAL_SMALL":
                        if (!low_tier) {
                            ModelUtils.renderModel(bufferSource, this.mc.level, pos, state, small_crystal, poseStack, packedLight, packedOverlay);
                        }
                }
                poseStack.popPose();
                for (GeoBone childBone : bone.getChildBones()) {
                    this.renderRecursively(poseStack, animatable, childBone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
                }
            }
            poseStack.popPose();
        }
    }
}