package com.mna.blocks.tileentities.renderers;

import com.mna.api.tools.RLoc;
import com.mna.blocks.BlockInit;
import com.mna.blocks.tileentities.SpectralTile;
import com.mna.blocks.tileentities.models.SpectralModel;
import com.mna.tools.render.ModelUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoBlockRenderer;
import software.bernie.geckolib.util.RenderUtils;

public class SpectralRenderer extends GeoBlockRenderer<SpectralTile> {

    public static final ResourceLocation floating_runes = RLoc.create("block/spectral/conjured_runes");

    public static final ResourceLocation floating_top = RLoc.create("block/spectral/conjured_floaty_top");

    public static final ResourceLocation floating_mid = RLoc.create("block/spectral/conjured_floaty_mid");

    public static final ResourceLocation floating_bot = RLoc.create("block/spectral/conjured_floaty_bot");

    public static final ResourceLocation workbench_top = RLoc.create("block/spectral/conjured_craftingtable");

    public static final ResourceLocation anvil_top = RLoc.create("block/spectral/conjured_anvil");

    public static final ResourceLocation stonecutter_top = RLoc.create("block/spectral/conjured_stonecutter");

    public static final ResourceLocation stonecutter_blade = RLoc.create("block/spectral/conjured_stonecutter_blade");

    protected Minecraft mc = Minecraft.getInstance();

    public SpectralRenderer(BlockEntityRendererProvider.Context context) {
        super(new SpectralModel());
    }

    public RenderType getRenderType(SpectralTile animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entitySmoothCutout(texture);
    }

    public void renderRecursively(PoseStack stack, SpectralTile animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        stack.pushPose();
        RenderUtils.translateMatrixToBone(stack, bone);
        RenderUtils.translateToPivotPoint(stack, bone);
        RenderUtils.rotateMatrixAroundBone(stack, bone);
        RenderUtils.scaleMatrixForBone(stack, bone);
        BlockState state = animatable.m_58900_();
        BlockPos pos = animatable.m_58899_();
        if (!bone.isHidden()) {
            stack.pushPose();
            String var17 = bone.getName();
            switch(var17) {
                case "GRID":
                    ModelUtils.renderModel(bufferSource, this.mc.level, pos, state, getTabletopModel(state), stack, 15728880, packedOverlay);
                    break;
                case "BUZZSAW":
                    ResourceLocation spinLoc = getSpinnyBoiModel(state);
                    if (spinLoc != null) {
                        ModelUtils.renderModel(bufferSource, this.mc.level, pos, state, spinLoc, stack, 15728880, packedOverlay);
                    }
                    break;
                case "RUNES":
                    ModelUtils.renderModel(bufferSource, this.mc.level, pos, state, floating_runes, stack, 15728880, packedOverlay);
                    break;
                case "SPINNY_TOP":
                    ModelUtils.renderModel(bufferSource, this.mc.level, pos, state, floating_top, stack, 15728880, packedOverlay);
                    break;
                case "SPINNY_MID":
                    ModelUtils.renderModel(bufferSource, this.mc.level, pos, state, floating_mid, stack, 15728880, packedOverlay);
                    break;
                case "SPINNY_BOT":
                    ModelUtils.renderModel(bufferSource, this.mc.level, pos, state, floating_bot, stack, 15728880, packedOverlay);
            }
            stack.popPose();
            for (GeoBone childBone : bone.getChildBones()) {
                this.renderRecursively(stack, animatable, childBone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
            }
        }
        stack.popPose();
    }

    private static ResourceLocation getTabletopModel(BlockState state) {
        Block block = state.m_60734_();
        if (block == BlockInit.SPECTRAL_ANVIL.get()) {
            return anvil_top;
        } else {
            return block == BlockInit.SPECTRAL_STONECUTTER.get() ? stonecutter_top : workbench_top;
        }
    }

    private static ResourceLocation getSpinnyBoiModel(BlockState state) {
        Block block = state.m_60734_();
        return block == BlockInit.SPECTRAL_STONECUTTER.get() ? stonecutter_blade : null;
    }
}