package net.mehvahdjukaar.supplementaries.client.screens;

import com.mojang.blaze3d.vertex.PoseStack;
import net.mehvahdjukaar.moonlight.api.client.util.LOD;
import net.mehvahdjukaar.supplementaries.client.renderers.tiles.NoticeBoardBlockTileRenderer;
import net.mehvahdjukaar.supplementaries.common.block.tiles.NoticeBoardBlockTile;
import net.mehvahdjukaar.supplementaries.common.inventories.NoticeBoardContainerMenu;
import net.mehvahdjukaar.supplementaries.integration.CompatHandler;
import net.mehvahdjukaar.supplementaries.integration.ImmediatelyFastCompat;
import net.mehvahdjukaar.supplementaries.reg.ModTextures;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.MapRenderer;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CyclingSlotBackground;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ComplexItem;
import net.minecraft.world.item.ItemStack;

public class NoticeBoardScreen extends AbstractContainerScreen<NoticeBoardContainerMenu> {

    private final NoticeBoardBlockTile tile;

    private final CyclingSlotBackground slotBG = new CyclingSlotBackground(0);

    public NoticeBoardScreen(NoticeBoardContainerMenu container, Inventory inventory, Component text) {
        super(container, inventory, text);
        this.f_97726_ = 176;
        this.f_97727_ = 166;
        this.tile = container.getContainer();
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int x, int y) {
        if (CompatHandler.IMMEDIATELY_FAST) {
            ImmediatelyFastCompat.startBatching();
        }
        int k = (this.f_96543_ - this.f_97726_) / 2;
        int l = (this.f_96544_ - this.f_97727_) / 2;
        graphics.blit(ModTextures.NOTICE_BOARD_GUI_TEXTURE, k, l, 0, 0, this.f_97726_, this.f_97727_);
        this.slotBG.render(this.f_97732_, graphics, partialTicks, this.f_97735_, this.f_97736_);
        ItemStack stack = this.tile.getDisplayedItem();
        if (!stack.isEmpty()) {
            graphics.blit(ModTextures.NOTICE_BOARD_GUI_TEXTURE, k + 88, l + 13, this.f_97726_, 0, 48, 56);
            PoseStack poseStack = graphics.pose();
            poseStack.pushPose();
            poseStack.translate((float) (this.f_97735_ + 112), (float) (this.f_97736_ + 41), 1.0F);
            poseStack.scale(64.0F, -64.0F, -1.0F);
            if (stack.getItem() instanceof ComplexItem) {
                poseStack.scale(0.9375F, 0.9375F, 1.0F);
            }
            MapRenderer mr = this.f_96541_.gameRenderer.getMapRenderer();
            MultiBufferSource.BufferSource buffer = graphics.bufferSource();
            NoticeBoardBlockTileRenderer.renderNoticeBoardContent(mr, this.f_96547_, this.f_96541_.getItemRenderer(), this.tile, graphics.pose(), buffer, 15728880, OverlayTexture.NO_OVERLAY, stack, Direction.UP, LOD.MAX);
            poseStack.popPose();
        }
        if (CompatHandler.IMMEDIATELY_FAST) {
            ImmediatelyFastCompat.endBatching();
        }
    }

    @Override
    public void render(GuiGraphics matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.m_280273_(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.m_280072_(matrixStack, mouseX, mouseY);
    }

    @Override
    public boolean keyPressed(int key, int b, int c) {
        if (key == 256) {
            this.f_96541_.player.closeContainer();
            return true;
        } else {
            return super.keyPressed(key, b, c);
        }
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        this.slotBG.tick(ModTextures.NOTICE_BOARD_SLOT_ICONS);
    }
}