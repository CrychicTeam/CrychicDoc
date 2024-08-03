package org.violetmoon.quark.addons.oddities.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.LinkedList;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;
import org.violetmoon.quark.addons.oddities.block.be.MatrixEnchantingTableBlockEntity;
import org.violetmoon.quark.addons.oddities.inventory.EnchantmentMatrix;
import org.violetmoon.quark.addons.oddities.inventory.MatrixEnchantingMenu;
import org.violetmoon.quark.addons.oddities.module.MatrixEnchantingModule;
import org.violetmoon.quark.base.QuarkClient;
import org.violetmoon.quark.base.client.handler.ClientUtil;
import org.violetmoon.quark.base.network.message.oddities.MatrixEnchanterOperationMessage;

public class MatrixEnchantingScreen extends AbstractContainerScreen<MatrixEnchantingMenu> {

    public static final ResourceLocation BACKGROUND = new ResourceLocation("quark", "textures/misc/matrix_enchanting.png");

    protected final Inventory playerInv;

    protected final MatrixEnchantingTableBlockEntity enchanter;

    protected Button plusButton;

    protected MatrixEnchantingPieceList pieceList;

    protected EnchantmentMatrix.Piece hoveredPiece;

    protected int selectedPiece = -1;

    protected int gridHoverX;

    protected int gridHoverY;

    protected List<Integer> listPieces = null;

    public MatrixEnchantingScreen(MatrixEnchantingMenu container, Inventory inventory, Component component) {
        super(container, inventory, component);
        this.playerInv = inventory;
        this.enchanter = container.enchanter;
    }

    @Override
    public void init() {
        super.init();
        this.selectedPiece = -1;
        this.m_142416_(this.plusButton = new MatrixEnchantingPlusButton(this.f_97735_ + 86, this.f_97736_ + 63, this::add));
        this.pieceList = new MatrixEnchantingPieceList(this, 28, 64, this.f_97736_ + 11, this.f_97736_ + 75, 22);
        this.pieceList.m_93507_(this.f_97735_ + 139);
        this.m_142416_(this.pieceList);
        this.updateButtonStatus();
        this.pieceList.refresh();
        this.enchanter.updateEnchantPower();
    }

    @Override
    public void containerTick() {
        super.containerTick();
        this.updateButtonStatus();
        if (this.enchanter.matrix == null) {
            this.selectedPiece = -1;
            this.pieceList.refresh();
        }
        if (this.enchanter.clientMatrixDirty) {
            this.pieceList.refresh();
            this.enchanter.clientMatrixDirty = false;
        }
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        Minecraft mc = this.getMinecraft();
        PoseStack pose = guiGraphics.pose();
        RenderSystem.setShader(GameRenderer::m_172817_);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        int i = this.f_97735_;
        int j = this.f_97736_;
        guiGraphics.blit(BACKGROUND, i, j, 0, 0, this.f_97726_, this.f_97727_);
        if (this.enchanter.charge > 0 && MatrixEnchantingModule.chargePerLapis > 0) {
            int maxHeight = 18;
            int barHeight = (int) ((float) this.enchanter.charge / (float) MatrixEnchantingModule.chargePerLapis * (float) maxHeight);
            guiGraphics.blit(BACKGROUND, i + 7, j + 32 + maxHeight - barHeight, 50, 176 + maxHeight - barHeight, 4, barHeight);
        }
        this.pieceList.render(guiGraphics, mouseX, mouseY, partialTicks);
        boolean showCost = this.enchanter.matrix != null && this.enchanter.matrix.canGeneratePiece(this.enchanter.influences, this.enchanter.bookshelfPower, this.enchanter.enchantability) && !mc.player.m_150110_().instabuild;
        String text = this.enchanter.bookshelfPower + "";
        int x = i + 50;
        int y = j + 55;
        if (this.enchanter.bookshelfPower > 0) {
            pose.pushPose();
            guiGraphics.renderItem(new ItemStack(Items.BOOK), x, y);
            pose.translate(0.0F, 0.0F, 1000.0F);
            x -= this.f_96547_.width(text) / 2;
            this.drawBorderedText(guiGraphics, text, x + 3, y + 6, 13172623);
            pose.popPose();
        }
        if (showCost) {
            int xpCost = this.enchanter.matrix.getNewPiecePrice();
            int xpMin = this.enchanter.matrix.getMinXpLevel(this.enchanter.bookshelfPower);
            boolean has = this.enchanter.matrix.validateXp(mc.player, this.enchanter.bookshelfPower);
            x = i + 71;
            y = j + 56;
            text = String.valueOf(xpCost);
            guiGraphics.blit(BACKGROUND, x, y, 0, this.f_97727_, 10, 10);
            if (!has && mc.player.f_36078_ < xpMin) {
                text = I18n.get("quark.gui.enchanting.min", xpMin);
                x += 4;
            }
            x -= this.f_96547_.width(text) / 2;
            this.drawBorderedText(guiGraphics, text, x + 2, y + 5, has ? 13172623 : 16748431);
        }
    }

    private void drawBorderedText(GuiGraphics guiGraphics, String text, int x, int y, int color) {
        guiGraphics.drawString(this.f_96547_, text, x - 1, y, 0, false);
        guiGraphics.drawString(this.f_96547_, text, x + 1, y, 0, false);
        guiGraphics.drawString(this.f_96547_, text, x, y + 1, 0, false);
        guiGraphics.drawString(this.f_96547_, text, x, y - 1, 0, false);
        guiGraphics.drawString(this.f_96547_, text, x, y, color, false);
    }

    @Override
    protected void renderLabels(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        int color = ClientUtil.getGuiTextColor("matrix_enchanting");
        guiGraphics.drawString(this.f_96547_, this.enchanter.getDisplayName().getString(), 12, 5, color, false);
        guiGraphics.drawString(this.f_96547_, this.playerInv.m_5446_().getString(), 8, this.f_97727_ - 96 + 2, color, false);
        if (this.enchanter.matrix != null) {
            boolean needsRefresh = this.listPieces == null;
            this.listPieces = this.enchanter.matrix.benchedPieces;
            if (needsRefresh) {
                this.pieceList.refresh();
            }
            this.renderMatrixGrid(guiGraphics, this.enchanter.matrix);
        }
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.m_280273_(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        if (this.hoveredPiece != null) {
            List<Component> tooltip = new LinkedList();
            tooltip.add(Component.translatable(this.hoveredPiece.enchant.getFullname(this.hoveredPiece.level).getString().replaceAll("\\u00A7.", "")).withStyle(ChatFormatting.GOLD));
            if (this.hoveredPiece.influence > 0) {
                tooltip.add(Component.translatable("quark.gui.enchanting.influence", (int) ((double) this.hoveredPiece.influence * MatrixEnchantingModule.influencePower * 100.0)).withStyle(ChatFormatting.GRAY));
            } else if (this.hoveredPiece.influence < 0) {
                tooltip.add(Component.translatable("quark.gui.enchanting.dampen", (int) ((double) this.hoveredPiece.influence * MatrixEnchantingModule.influencePower * 100.0)).withStyle(ChatFormatting.GRAY));
            }
            int max = this.hoveredPiece.getMaxXP();
            if (max > 0) {
                tooltip.add(Component.translatable("quark.gui.enchanting.upgrade", this.hoveredPiece.xp, max).withStyle(ChatFormatting.GRAY));
            }
            if (this.gridHoverX == -1) {
                tooltip.add(Component.literal(""));
                tooltip.add(Component.translatable("quark.gui.enchanting.left_click").withStyle(ChatFormatting.GRAY));
                tooltip.add(Component.translatable("quark.gui.enchanting.right_click").withStyle(ChatFormatting.GRAY));
            } else if (this.selectedPiece != -1) {
                EnchantmentMatrix.Piece p = this.getPiece(this.selectedPiece);
                if (p != null && p.enchant == this.hoveredPiece.enchant && this.hoveredPiece.level < this.hoveredPiece.enchant.getMaxLevel()) {
                    tooltip.add(Component.literal(""));
                    tooltip.add(Component.translatable("quark.gui.enchanting.merge").withStyle(ChatFormatting.GRAY));
                }
            }
            guiGraphics.renderComponentTooltip(this.f_96547_, tooltip, mouseX, mouseY);
        } else {
            this.m_280072_(guiGraphics, mouseX, mouseY);
        }
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        int gridMouseX = (int) (mouseX - (double) this.f_97735_ - 86.0);
        int gridMouseY = (int) (mouseY - (double) this.f_97736_ - 11.0);
        this.gridHoverX = gridMouseX < 0 ? -1 : gridMouseX / 10;
        this.gridHoverY = gridMouseY < 0 ? -1 : gridMouseY / 10;
        if (this.gridHoverX < 0 || this.gridHoverX > 4 || this.gridHoverY < 0 || this.gridHoverY > 4) {
            this.gridHoverX = -1;
            this.gridHoverY = -1;
            this.hoveredPiece = null;
        } else if (this.enchanter.matrix != null) {
            int hover = this.enchanter.matrix.matrix[this.gridHoverX][this.gridHoverY];
            this.hoveredPiece = this.getPiece(hover);
        }
        this.pieceList.m_94757_((double) gridMouseX, (double) gridMouseY);
        super.m_94757_(mouseX, mouseY);
    }

    @Override
    public boolean mouseDragged(double double0, double double1, int int2, double double3, double double4) {
        this.pieceList.m_7979_(double0, double1, int2, double3, double4);
        return super.mouseDragged(double0, double1, int2, double3, double4);
    }

    @Override
    public boolean mouseReleased(double double0, double double1, int int2) {
        this.pieceList.m_6348_(double0, double1, int2);
        return super.mouseReleased(double0, double1, int2);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.pieceList.m_6375_(mouseX, mouseY, mouseButton);
        if (this.enchanter.matrix == null) {
            return true;
        } else {
            if (mouseButton == 0 && this.gridHoverX != -1) {
                int hover = this.enchanter.matrix.matrix[this.gridHoverX][this.gridHoverY];
                if (this.selectedPiece != -1) {
                    if (hover == -1) {
                        this.place(this.selectedPiece, this.gridHoverX, this.gridHoverY);
                    } else {
                        this.merge(this.selectedPiece);
                    }
                } else {
                    this.remove(hover);
                    if (!m_96638_()) {
                        this.selectedPiece = hover;
                    }
                }
            } else if (mouseButton == 1 && this.selectedPiece != -1) {
                this.rotate(this.selectedPiece);
            }
            return true;
        }
    }

    private void renderMatrixGrid(GuiGraphics guiGraphics, EnchantmentMatrix matrix) {
        PoseStack stack = guiGraphics.pose();
        RenderSystem.setShader(GameRenderer::m_172817_);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, BACKGROUND);
        stack.pushPose();
        stack.translate(86.0F, 11.0F, 0.0F);
        for (int i : matrix.placedPieces) {
            EnchantmentMatrix.Piece piece = this.getPiece(i);
            if (piece != null) {
                stack.pushPose();
                stack.translate((float) (piece.x * 10), (float) (piece.y * 10), 0.0F);
                this.renderPiece(guiGraphics, piece, 1.0F);
                stack.popPose();
            }
        }
        if (this.selectedPiece != -1 && this.gridHoverX != -1) {
            EnchantmentMatrix.Piece piece = this.getPiece(this.selectedPiece);
            if (piece != null && (this.hoveredPiece == null || piece.enchant != this.hoveredPiece.enchant || this.hoveredPiece.level >= this.hoveredPiece.enchant.getMaxLevel())) {
                stack.pushPose();
                stack.translate((float) (this.gridHoverX * 10), (float) (this.gridHoverY * 10), 0.0F);
                float a = 0.2F;
                if (matrix.canPlace(piece, this.gridHoverX, this.gridHoverY)) {
                    a = (float) ((Math.sin((double) QuarkClient.ticker.total * 0.2) + 1.0) * 0.4 + 0.4);
                }
                this.renderPiece(guiGraphics, piece, a);
                stack.popPose();
            }
        }
        if (this.hoveredPiece == null && this.gridHoverX != -1) {
            this.renderHover(guiGraphics, this.gridHoverX, this.gridHoverY);
        }
        stack.popPose();
    }

    protected void renderPiece(GuiGraphics guiGraphics, EnchantmentMatrix.Piece piece, float a) {
        float r = (float) (piece.color >> 16 & 0xFF) / 255.0F;
        float g = (float) (piece.color >> 8 & 0xFF) / 255.0F;
        float b = (float) (piece.color & 0xFF) / 255.0F;
        boolean hovered = this.hoveredPiece == piece;
        for (int[] block : piece.blocks) {
            this.renderBlock(guiGraphics, block[0], block[1], piece.type, r, g, b, a, hovered);
        }
    }

    private void renderBlock(GuiGraphics guiGraphics, int x, int y, int type, float r, float g, float b, float a, boolean hovered) {
        RenderSystem.setShaderColor(r, g, b, a);
        guiGraphics.blit(BACKGROUND, x * 10, y * 10, 11 + type * 10, this.f_97727_, 10, 10);
        if (hovered) {
            this.renderHover(guiGraphics, x, y);
        }
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private void renderHover(GuiGraphics guiGraphics, int x, int y) {
        guiGraphics.fill(x * 10, y * 10, x * 10 + 10, y * 10 + 10, 1728053247);
    }

    public void add(Button button) {
        this.send(0, 0, 0, 0);
    }

    public void place(int id, int x, int y) {
        this.send(1, id, x, y);
        this.selectedPiece = -1;
        this.click();
    }

    public void remove(int id) {
        this.send(2, id, 0, 0);
    }

    public void rotate(int id) {
        this.send(3, id, 0, 0);
    }

    public void merge(int id) {
        int hover = this.enchanter.matrix.matrix[this.gridHoverX][this.gridHoverY];
        EnchantmentMatrix.Piece p = this.getPiece(hover);
        EnchantmentMatrix.Piece p1 = this.getPiece(this.selectedPiece);
        if (p != null && p1 != null && p.enchant == p1.enchant && p.level < p.enchant.getMaxLevel()) {
            this.send(4, hover, id, 0);
            this.selectedPiece = -1;
            this.click();
        }
    }

    private void send(int operation, int arg0, int arg1, int arg2) {
        MatrixEnchanterOperationMessage message = new MatrixEnchanterOperationMessage(operation, arg0, arg1, arg2);
        QuarkClient.ZETA_CLIENT.sendToServer(message);
    }

    private void click() {
        this.getMinecraft().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }

    private void updateButtonStatus() {
        this.plusButton.f_93623_ = this.enchanter.matrix != null && (this.getMinecraft().player.m_150110_().instabuild || this.enchanter.charge > 0) && this.enchanter.matrix.validateXp(this.getMinecraft().player, this.enchanter.bookshelfPower) && this.enchanter.matrix.canGeneratePiece(this.enchanter.influences, this.enchanter.bookshelfPower, this.enchanter.enchantability);
    }

    protected EnchantmentMatrix.Piece getPiece(int id) {
        EnchantmentMatrix matrix = this.enchanter.matrix;
        return matrix != null ? (EnchantmentMatrix.Piece) matrix.pieces.get(id) : null;
    }
}