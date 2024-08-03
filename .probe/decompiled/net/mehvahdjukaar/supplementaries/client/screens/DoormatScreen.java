package net.mehvahdjukaar.supplementaries.client.screens;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;
import net.mehvahdjukaar.moonlight.api.client.util.RotHlpr;
import net.mehvahdjukaar.moonlight.api.client.util.TextUtil;
import net.mehvahdjukaar.supplementaries.common.block.blocks.DoormatBlock;
import net.mehvahdjukaar.supplementaries.common.block.tiles.DoormatBlockTile;
import net.mehvahdjukaar.supplementaries.integration.CompatHandler;
import net.mehvahdjukaar.supplementaries.integration.ImmediatelyFastCompat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;

public class DoormatScreen extends TextHolderEditScreen<DoormatBlockTile> {

    private final BlockState state = (BlockState) this.tile.m_58900_().m_60734_().defaultBlockState().m_61124_(DoormatBlock.FACING, Direction.EAST);

    private DoormatScreen(DoormatBlockTile tile) {
        super(tile, Component.translatable("gui.supplementaries.doormat.edit"));
    }

    public static void open(DoormatBlockTile tile) {
        Minecraft.getInstance().setScreen(new DoormatScreen(tile));
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        if (CompatHandler.IMMEDIATELY_FAST) {
            ImmediatelyFastCompat.startBatching();
        }
        this.m_280273_(graphics);
        super.m_88315_(graphics, mouseX, mouseY, partialTicks);
        Lighting.setupForFlatItems();
        graphics.drawCenteredString(this.f_96547_, this.f_96539_, this.f_96543_ / 2, 40, 16777215);
        MultiBufferSource.BufferSource bufferSource = this.f_96541_.renderBuffers().bufferSource();
        PoseStack poseStack = graphics.pose();
        poseStack.pushPose();
        poseStack.translate((double) this.f_96543_ / 2.0, 0.0, 50.0);
        poseStack.scale(93.75F, -93.75F, 93.75F);
        poseStack.translate(0.0, -1.25, 0.0);
        poseStack.pushPose();
        poseStack.mulPose(RotHlpr.Y90);
        poseStack.translate(0.0, -0.5, -0.5);
        poseStack.mulPose(RotHlpr.Z90);
        BlockRenderDispatcher blockRenderer = Minecraft.getInstance().getBlockRenderer();
        blockRenderer.renderSingleBlock(this.state, graphics.pose(), bufferSource, 15728880, OverlayTexture.NO_OVERLAY);
        poseStack.popPose();
        boolean blink = this.updateCounter / 6 % 2 == 0;
        poseStack.translate(0.0, 0.04166666604578495, 0.0675);
        poseStack.scale(0.010416667F, -0.010416667F, 0.010416667F);
        TextUtil.renderGuiText(this.tile.textHolder.getGUIRenderTextProperties(), this.messages[this.textHolderIndex], this.f_96547_, graphics, bufferSource, this.textInputUtil.getCursorPos(), this.textInputUtil.getSelectionPos(), this.lineIndex, blink, 15);
        poseStack.popPose();
        Lighting.setupFor3DItems();
        if (CompatHandler.IMMEDIATELY_FAST) {
            ImmediatelyFastCompat.endBatching();
        }
    }
}