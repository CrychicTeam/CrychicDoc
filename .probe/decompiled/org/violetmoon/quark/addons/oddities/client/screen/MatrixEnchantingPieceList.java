package org.violetmoon.quark.addons.oddities.client.screen;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.violetmoon.quark.addons.oddities.inventory.EnchantmentMatrix;

public class MatrixEnchantingPieceList extends ObjectSelectionList<MatrixEnchantingPieceList.PieceEntry> {

    private final MatrixEnchantingScreen parent;

    private final int listWidth;

    public MatrixEnchantingPieceList(MatrixEnchantingScreen parent, int listWidth, int listHeight, int top, int bottom, int entryHeight) {
        super(parent.getMinecraft(), listWidth, listHeight, top, bottom, entryHeight);
        this.listWidth = listWidth;
        this.parent = parent;
    }

    @Override
    protected int getScrollbarPosition() {
        return this.getLeft() + this.listWidth - 5;
    }

    @Override
    public int getRowWidth() {
        return this.listWidth;
    }

    public void refresh() {
        this.m_93516_();
        if (this.parent.listPieces != null) {
            for (int i : this.parent.listPieces) {
                EnchantmentMatrix.Piece piece = this.parent.getPiece(i);
                if (piece != null) {
                    this.m_7085_(new MatrixEnchantingPieceList.PieceEntry(piece, i));
                }
            }
        }
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        int i = this.getScrollbarPosition();
        int j = i + 6;
        guiGraphics.fill(this.getLeft(), this.getTop(), this.getLeft() + this.getWidth() + 1, this.getTop() + this.getHeight(), -13948117);
        Window main = this.parent.getMinecraft().getWindow();
        int res = (int) main.getGuiScale();
        RenderSystem.enableScissor(this.getLeft() * res, (main.getGuiScaledHeight() - this.getBottom()) * res, this.getWidth() * res, this.getHeight() * res);
        this.m_239227_(guiGraphics, mouseX, mouseY, partialTicks);
        RenderSystem.disableScissor();
        this.renderScroll(guiGraphics, i, j);
    }

    protected int getMaxScroll2() {
        return Math.max(0, this.m_5775_() - (this.f_93391_ - this.f_93390_ - 4));
    }

    private void renderScroll(GuiGraphics guiGraphics, int i, int j) {
        int j1 = this.getMaxScroll2();
        if (j1 > 0) {
            int k1 = (int) ((float) ((this.f_93391_ - this.f_93390_) * (this.f_93391_ - this.f_93390_)) / (float) this.m_5775_());
            k1 = Mth.clamp(k1, 32, this.f_93391_ - this.f_93390_ - 8);
            int l1 = (int) this.m_93517_() * (this.f_93391_ - this.f_93390_ - k1) / j1 + this.f_93390_;
            if (l1 < this.f_93390_) {
                l1 = this.f_93390_;
            }
            guiGraphics.fill(i, this.f_93391_, j, this.f_93390_, -16777216);
            guiGraphics.fill(i, l1 + k1, j, l1, -8289919);
            guiGraphics.fill(i, l1 + k1 - 1, j - 1, l1, -4144960);
        }
    }

    @Override
    protected void renderBackground(@NotNull GuiGraphics guiGraphics) {
    }

    protected class PieceEntry extends ObjectSelectionList.Entry<MatrixEnchantingPieceList.PieceEntry> {

        private final EnchantmentMatrix.Piece piece;

        private final int index;

        PieceEntry(EnchantmentMatrix.Piece piece, int index) {
            this.piece = piece;
            this.index = index;
        }

        @Override
        public void render(@NotNull GuiGraphics guiGraphics, int entryIdx, int top, int left, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hover, float partialTicks) {
            PoseStack stack = guiGraphics.pose();
            if (mouseX > left && mouseY > top && mouseX <= left + entryWidth && mouseY <= top + entryHeight) {
                MatrixEnchantingPieceList.this.parent.hoveredPiece = this.piece;
            }
            RenderSystem.setShader(GameRenderer::m_172817_);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.setShaderTexture(0, MatrixEnchantingScreen.BACKGROUND);
            stack.pushPose();
            stack.translate((float) left + (float) (MatrixEnchantingPieceList.this.listWidth - 7) / 2.0F, (float) top + (float) entryHeight / 2.0F, 0.0F);
            stack.scale(0.5F, 0.5F, 0.5F);
            stack.translate(-8.0F, -8.0F, 0.0F);
            MatrixEnchantingPieceList.this.parent.renderPiece(guiGraphics, this.piece, 1.0F);
            stack.popPose();
        }

        @Override
        public boolean mouseClicked(double x, double y, int button) {
            MatrixEnchantingPieceList.this.parent.selectedPiece = this.index;
            MatrixEnchantingPieceList.this.m_6987_(this);
            return false;
        }

        @NotNull
        @Override
        public Component getNarration() {
            return Component.literal("");
        }
    }
}