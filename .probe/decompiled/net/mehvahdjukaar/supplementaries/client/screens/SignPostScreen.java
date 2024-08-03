package net.mehvahdjukaar.supplementaries.client.screens;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;
import net.mehvahdjukaar.moonlight.api.client.util.RotHlpr;
import net.mehvahdjukaar.moonlight.api.client.util.TextUtil;
import net.mehvahdjukaar.supplementaries.client.renderers.tiles.SignPostBlockTileRenderer;
import net.mehvahdjukaar.supplementaries.common.block.tiles.SignPostBlockTile;
import net.mehvahdjukaar.supplementaries.integration.CompatHandler;
import net.mehvahdjukaar.supplementaries.integration.FramedBlocksCompat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;

public class SignPostScreen extends TextHolderEditScreen<SignPostBlockTile> {

    private SignPostScreen(SignPostBlockTile tile) {
        super(tile, Component.translatable("sign.edit"));
        this.textHolderIndex = !this.tile.getSignUp().active() ? 1 : 0;
    }

    public static void open(SignPostBlockTile teSign) {
        Minecraft.getInstance().setScreen(new SignPostScreen(teSign));
    }

    @Override
    protected boolean canScroll() {
        return this.tile.getSignUp().active() && this.tile.getSignDown().active();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        Lighting.setupForFlatItems();
        this.m_280273_(graphics);
        super.m_88315_(graphics, mouseX, mouseY, partialTicks);
        graphics.drawCenteredString(this.f_96547_, this.f_96539_, this.f_96543_ / 2, 40, 16777215);
        PoseStack poseStack = graphics.pose();
        MultiBufferSource.BufferSource bufferSource = graphics.bufferSource();
        poseStack.pushPose();
        poseStack.translate((double) this.f_96543_ / 2.0, 0.0, 50.0);
        poseStack.scale(93.75F, -93.75F, 93.75F);
        poseStack.translate(0.0, -1.3125, 0.0);
        poseStack.pushPose();
        BlockRenderDispatcher blockRenderer = Minecraft.getInstance().getBlockRenderer();
        SignPostBlockTile.Sign signUp = this.tile.getSignUp();
        SignPostBlockTile.Sign signDown = this.tile.getSignDown();
        boolean leftUp = signUp.left();
        boolean leftDown = signDown.left();
        int[] o = new int[] { leftUp ? 1 : -1, leftDown ? 1 : -1 };
        boolean blink = this.updateCounter / 6 % 2 == 0;
        ModelBlockRenderer modelRenderer = blockRenderer.getModelRenderer();
        poseStack.pushPose();
        this.renderSign(poseStack, modelRenderer, bufferSource, signUp, leftUp);
        poseStack.translate(0.0, -0.5, 0.0);
        this.renderSign(poseStack, modelRenderer, bufferSource, signDown, leftDown);
        poseStack.popPose();
        poseStack.translate(-0.5, -0.5, -0.5);
        BlockState fence = this.tile.getHeldBlock();
        if (CompatHandler.FRAMEDBLOCKS && this.tile.isFramed()) {
            fence = FramedBlocksCompat.getFramedFence();
        }
        if (fence != null) {
            blockRenderer.renderSingleBlock(fence, poseStack, bufferSource, 15728880, OverlayTexture.NO_OVERLAY);
        }
        poseStack.popPose();
        if (signUp.active() || signDown.active()) {
            poseStack.translate((double) (-0.03125F * (float) o[0]), 0.21875, 0.1925);
            poseStack.scale(0.010416667F, -0.010416667F, 0.010416667F);
            int cursorPos = this.textInputUtil.getCursorPos();
            int selectionPos = this.textInputUtil.getSelectionPos();
            if (signUp.active()) {
                TextUtil.RenderProperties properties = this.tile.getTextHolder(0).getGUIRenderTextProperties();
                TextUtil.renderGuiLine(properties, this.messages[0][0], this.f_96547_, graphics, bufferSource, cursorPos, selectionPos, this.textHolderIndex == 0, blink, -10);
            }
            if (signDown.active()) {
                poseStack.translate((float) (-3 * o[1]), 0.0F, 0.0F);
                TextUtil.RenderProperties properties = this.tile.getTextHolder(1).getGUIRenderTextProperties();
                TextUtil.renderGuiLine(properties, this.messages[1][0], this.f_96547_, graphics, bufferSource, cursorPos, selectionPos, this.textHolderIndex == 1, blink, 38);
            }
        }
        poseStack.popPose();
        Lighting.setupFor3DItems();
    }

    private void renderSign(PoseStack poseStack, ModelBlockRenderer renderer, MultiBufferSource bufferSource, SignPostBlockTile.Sign sign, boolean leftDown) {
        if (sign.active()) {
            poseStack.pushPose();
            if (!leftDown) {
                poseStack.mulPose(RotHlpr.YN180);
                poseStack.translate(0.0, 0.0, -0.55);
            } else {
                poseStack.translate(0.0, 0.0, -0.3);
            }
            poseStack.translate(-0.5, 0.0, 0.0);
            renderer.renderModel(poseStack.last(), bufferSource.getBuffer(RenderType.cutout()), null, (BakedModel) SignPostBlockTileRenderer.MODELS.get(sign.woodType()), 1.0F, 1.0F, 1.0F, 15728880, OverlayTexture.NO_OVERLAY);
            poseStack.popPose();
        }
    }
}