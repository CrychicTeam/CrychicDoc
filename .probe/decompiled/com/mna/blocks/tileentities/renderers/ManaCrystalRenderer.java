package com.mna.blocks.tileentities.renderers;

import com.mna.ManaAndArtifice;
import com.mna.api.tools.RLoc;
import com.mna.blocks.sorcery.ManaCrystalBlock;
import com.mna.blocks.tileentities.ManaCrystalTile;
import com.mna.blocks.tileentities.models.ModelManaCrystal;
import com.mna.tools.render.ModelUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoBlockRenderer;
import software.bernie.geckolib.util.RenderUtils;

public class ManaCrystalRenderer extends GeoBlockRenderer<ManaCrystalTile> {

    public static final ResourceLocation crystal = RLoc.create("block/mana_crystal_gem");

    public static final ResourceLocation runes = RLoc.create("block/mana_crystal_runes");

    protected Minecraft mc = Minecraft.getInstance();

    public ManaCrystalRenderer(BlockEntityRendererProvider.Context context) {
        super(new ModelManaCrystal());
    }

    public RenderType getRenderType(ManaCrystalTile animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entitySmoothCutout(texture);
    }

    public void preRender(PoseStack poseStack, ManaCrystalTile animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if ((Boolean) animatable.m_58900_().m_61143_(ManaCrystalBlock.HANGING)) {
            poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
            poseStack.translate(0.0F, -1.0F, -1.0F);
        }
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    public void renderRecursively(PoseStack stack, ManaCrystalTile animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if (this.mc != null) {
            stack.pushPose();
            RenderUtils.translateMatrixToBone(stack, bone);
            RenderUtils.translateToPivotPoint(stack, bone);
            RenderUtils.rotateMatrixAroundBone(stack, bone);
            RenderUtils.scaleMatrixForBone(stack, bone);
            RenderUtils.translateAwayFromPivotPoint(stack, bone);
            BlockPos pos = animatable.m_58899_();
            BlockState state = animatable.m_58900_();
            if (!bone.isHidden()) {
                stack.pushPose();
                String var17 = bone.getName();
                switch(var17) {
                    case "BINDING_RING":
                        stack.mulPose(Axis.YP.rotationDegrees((partialTick + (float) ManaAndArtifice.instance.proxy.getGameTicks()) / 8.0F));
                        ModelUtils.renderModel(bufferSource, this.mc.level, pos, state, runes, stack, packedLight, packedOverlay);
                        break;
                    case "CRYSTAL":
                        ModelUtils.renderModel(bufferSource, this.mc.level, pos, state, crystal, stack, packedLight, packedOverlay);
                }
                stack.popPose();
                for (GeoBone childBone : bone.getChildBones()) {
                    this.renderRecursively(stack, animatable, childBone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
                }
            }
            stack.popPose();
        }
    }
}