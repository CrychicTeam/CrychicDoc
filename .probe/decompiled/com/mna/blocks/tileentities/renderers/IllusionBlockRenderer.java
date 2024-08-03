package com.mna.blocks.tileentities.renderers;

import com.mna.ManaAndArtifice;
import com.mna.api.tools.RLoc;
import com.mna.blocks.BlockInit;
import com.mna.blocks.tileentities.IllusionBlockTile;
import com.mna.effects.EffectInit;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;

public class IllusionBlockRenderer implements BlockEntityRenderer<IllusionBlockTile> {

    private final ResourceLocation baseModel = RLoc.create("block/illusion_block");

    public IllusionBlockRenderer(BlockEntityRendererProvider.Context context) {
    }

    public void render(IllusionBlockTile tile, float partialTick, PoseStack stack, MultiBufferSource irtb, int packedLight, int packedOverlay) {
        Pair<BlockPos, BlockState> mimicData = tile.getMimicBlock();
        boolean hasSight = ManaAndArtifice.instance.proxy.getClientPlayer().m_21023_(EffectInit.ELDRIN_SIGHT.get());
        if (!hasSight && mimicData.getSecond() != null && ((BlockState) mimicData.getSecond()).m_60734_() != BlockInit.ILLUSION_BLOCK.get() && ((BlockState) mimicData.getSecond()).m_60734_() != Blocks.AIR) {
            Minecraft m = Minecraft.getInstance();
            m.getBlockRenderer().renderSingleBlock((BlockState) mimicData.getSecond(), stack, irtb, packedLight, packedOverlay, ModelData.EMPTY, null);
        } else {
            Minecraft mc = Minecraft.getInstance();
            BlockRenderDispatcher brd = mc.getBlockRenderer();
            BlockState state = tile.m_58900_();
            BakedModel ibakedmodel = Minecraft.getInstance().getModelManager().getModel(this.baseModel);
            int i = -1;
            float f = (float) (i >> 16 & 0xFF) / 255.0F;
            float f1 = (float) (i >> 8 & 0xFF) / 255.0F;
            float f2 = (float) (i & 0xFF) / 255.0F;
            brd.getModelRenderer().renderModel(stack.last(), irtb.getBuffer(ItemBlockRenderTypes.getRenderType(state, false)), state, ibakedmodel, f, f1, f2, packedLight, OverlayTexture.NO_OVERLAY, ModelData.EMPTY, null);
        }
    }
}