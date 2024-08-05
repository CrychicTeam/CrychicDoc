package com.simibubi.create.content.equipment.clipboard;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllPackets;
import com.simibubi.create.foundation.gui.AbstractSimiScreen;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.gui.widget.IconButton;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Lang;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.SharedConstants;
import net.minecraft.Util;
import net.minecraft.client.StringSplitter;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.font.TextFieldHelper;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.PageButton;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableInt;

public class ClipboardScreen extends AbstractSimiScreen {

    public ItemStack item;

    public BlockPos targetedBlock;

    List<List<ClipboardEntry>> pages;

    List<ClipboardEntry> currentEntries;

    int editingIndex;

    int frameTick;

    PageButton forward;

    PageButton backward;

    int currentPage;

    long lastClickTime;

    int lastIndex = -1;

    int hoveredEntry;

    boolean hoveredCheck;

    boolean readonly;

    ClipboardScreen.DisplayCache displayCache = ClipboardScreen.DisplayCache.EMPTY;

    TextFieldHelper editContext;

    IconButton closeBtn;

    IconButton clearBtn;

    private int targetSlot;

    public ClipboardScreen(int targetSlot, ItemStack item, @Nullable BlockPos pos) {
        this.targetSlot = targetSlot;
        this.targetedBlock = pos;
        this.reopenWith(item);
    }

    public void reopenWith(ItemStack clipboard) {
        this.item = clipboard;
        this.pages = ClipboardEntry.readAll(this.item);
        if (this.pages.isEmpty()) {
            this.pages.add(new ArrayList());
        }
        if (this.clearBtn == null) {
            this.currentPage = this.item.getTag() == null ? 0 : this.item.getTag().getInt("PreviouslyOpenedPage");
            this.currentPage = Mth.clamp(this.currentPage, 0, this.pages.size() - 1);
        }
        this.currentEntries = (List<ClipboardEntry>) this.pages.get(this.currentPage);
        boolean startEmpty = this.currentEntries.isEmpty();
        if (startEmpty) {
            this.currentEntries.add(new ClipboardEntry(false, Components.empty()));
        }
        this.editingIndex = 0;
        this.editContext = new TextFieldHelper(this::getCurrentEntryText, this::setCurrentEntryText, this::getClipboard, this::setClipboard, this::validateTextForEntry);
        this.editingIndex = startEmpty ? 0 : -1;
        this.readonly = this.item.getTag() != null && this.item.getTag().getBoolean("Readonly");
        if (this.readonly) {
            this.editingIndex = -1;
        }
        if (this.clearBtn != null) {
            this.init();
        }
    }

    @Override
    protected void init() {
        this.setWindowSize(256, 256);
        super.init();
        this.clearDisplayCache();
        int x = this.guiLeft;
        int y = this.guiTop - 8;
        this.m_169413_();
        this.clearBtn = new IconButton(x + 234, y + 153, AllIcons.I_CLEAR_CHECKED).withCallback(() -> {
            this.editingIndex = -1;
            this.currentEntries.removeIf(ce -> ce.checked);
            if (this.currentEntries.isEmpty()) {
                this.currentEntries.add(new ClipboardEntry(false, Components.empty()));
            }
            this.sendIfEditingBlock();
        });
        this.clearBtn.setToolTip(Lang.translateDirect("gui.clipboard.erase_checked"));
        this.closeBtn = new IconButton(x + 234, y + 175, AllIcons.I_PRIORITY_VERY_LOW).withCallback(() -> this.f_96541_.setScreen(null));
        this.closeBtn.setToolTip(Lang.translateDirect("station.close"));
        this.m_142416_(this.closeBtn);
        this.m_142416_(this.clearBtn);
        this.forward = new PageButton(x + 176, y + 229, true, $ -> this.changePage(true), true);
        this.backward = new PageButton(x + 53, y + 229, false, $ -> this.changePage(false), true);
        this.m_142416_(this.forward);
        this.m_142416_(this.backward);
        this.forward.f_93624_ = this.currentPage < 50 && (!this.readonly || this.currentPage + 1 < this.pages.size());
        this.backward.f_93624_ = this.currentPage > 0;
    }

    private int getNumPages() {
        return this.pages.size();
    }

    @Override
    public void tick() {
        super.tick();
        this.frameTick++;
        if (this.targetedBlock != null) {
            if (!this.f_96541_.player.m_20183_().m_123314_(this.targetedBlock, 10.0)) {
                this.removed();
                return;
            }
            if (!AllBlocks.CLIPBOARD.has(this.f_96541_.level.m_8055_(this.targetedBlock))) {
                this.removed();
                return;
            }
        }
        int mx = (int) (this.f_96541_.mouseHandler.xpos() * (double) this.f_96541_.getWindow().getGuiScaledWidth() / (double) this.f_96541_.getWindow().getScreenWidth());
        int my = (int) (this.f_96541_.mouseHandler.ypos() * (double) this.f_96541_.getWindow().getGuiScaledHeight() / (double) this.f_96541_.getWindow().getScreenHeight());
        mx -= this.guiLeft + 35;
        my -= this.guiTop + 41;
        this.hoveredCheck = false;
        this.hoveredEntry = -1;
        if (mx > 0 && mx < 183 && my > 0 && my < 190) {
            this.hoveredCheck = mx < 20;
            int totalHeight = 0;
            for (int i = 0; i < this.currentEntries.size(); i++) {
                ClipboardEntry clipboardEntry = (ClipboardEntry) this.currentEntries.get(i);
                String text = clipboardEntry.text.getString();
                totalHeight += Math.max(12, this.f_96547_.split(Components.literal(text), clipboardEntry.icon.isEmpty() ? 150 : 130).size() * 9 + 3);
                if (totalHeight > my) {
                    this.hoveredEntry = i;
                    return;
                }
            }
            this.hoveredEntry = this.currentEntries.size();
        }
    }

    private String getCurrentEntryText() {
        return ((ClipboardEntry) this.currentEntries.get(this.editingIndex)).text.getString();
    }

    private void setCurrentEntryText(String text) {
        ((ClipboardEntry) this.currentEntries.get(this.editingIndex)).text = Components.literal(text);
        this.sendIfEditingBlock();
    }

    private void setClipboard(String string0) {
        if (this.f_96541_ != null) {
            TextFieldHelper.setClipboardContents(this.f_96541_, string0);
        }
    }

    private String getClipboard() {
        return this.f_96541_ != null ? TextFieldHelper.getClipboardContents(this.f_96541_) : "";
    }

    private boolean validateTextForEntry(String newText) {
        int totalHeight = 0;
        for (int i = 0; i < this.currentEntries.size(); i++) {
            ClipboardEntry clipboardEntry = (ClipboardEntry) this.currentEntries.get(i);
            String text = i == this.editingIndex ? newText : clipboardEntry.text.getString();
            totalHeight += Math.max(12, this.f_96547_.split(Components.literal(text), 150).size() * 9 + 3);
        }
        return totalHeight < 185;
    }

    private int yOffsetOfEditingEntry() {
        int totalHeight = 0;
        for (int i = 0; i < this.currentEntries.size() && i != this.editingIndex; i++) {
            ClipboardEntry clipboardEntry = (ClipboardEntry) this.currentEntries.get(i);
            totalHeight += Math.max(12, this.f_96547_.split(clipboardEntry.text, 150).size() * 9 + 3);
        }
        return totalHeight;
    }

    private void changePage(boolean next) {
        int previously = this.currentPage;
        this.currentPage = Mth.clamp(this.currentPage + (next ? 1 : -1), 0, 50);
        if (this.currentPage != previously) {
            this.editingIndex = -1;
            if (this.pages.size() <= this.currentPage) {
                if (this.readonly) {
                    this.currentPage = previously;
                    return;
                }
                this.pages.add(new ArrayList());
            }
            this.currentEntries = (List<ClipboardEntry>) this.pages.get(this.currentPage);
            if (this.currentEntries.isEmpty()) {
                this.currentEntries.add(new ClipboardEntry(false, Components.empty()));
                if (!this.readonly) {
                    this.editingIndex = 0;
                    this.editContext.setCursorToEnd();
                    this.clearDisplayCacheAfterChange();
                }
            }
            this.forward.f_93624_ = this.currentPage < 50 && (!this.readonly || this.currentPage + 1 < this.pages.size());
            this.backward.f_93624_ = this.currentPage > 0;
            if (!next) {
                if (((List) this.pages.get(this.currentPage + 1)).stream().allMatch(ce -> ce.text.getString().isBlank())) {
                    this.pages.remove(this.currentPage + 1);
                }
            }
        }
    }

    @Override
    protected void renderWindow(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        int x = this.guiLeft;
        int y = this.guiTop - 8;
        AllGuiTextures.CLIPBOARD.render(graphics, x, y);
        graphics.drawString(this.f_96547_, Components.translatable("book.pageIndicator", this.currentPage + 1, this.getNumPages()), x + 150, y + 9, 1140850687, false);
        for (int i = 0; i < this.currentEntries.size(); i++) {
            ClipboardEntry clipboardEntry = (ClipboardEntry) this.currentEntries.get(i);
            boolean checked = clipboardEntry.checked;
            int iconOffset = clipboardEntry.icon.isEmpty() ? 0 : 16;
            graphics.drawString(this.f_96547_, "□", x + 45, y + 51, checked ? 1720549227 : -7504021, false);
            if (checked) {
                graphics.drawString(this.f_96547_, "✔", x + 45, y + 50, 3256925, false);
            }
            List<FormattedCharSequence> split = this.f_96547_.split(clipboardEntry.text, 150 - iconOffset);
            if (split.isEmpty()) {
                y += 12;
            } else {
                if (!clipboardEntry.icon.isEmpty()) {
                    graphics.renderItem(clipboardEntry.icon, x + 54, y + 50);
                }
                for (FormattedCharSequence sequence : split) {
                    if (i != this.editingIndex) {
                        graphics.drawString(this.f_96547_, sequence, x + 58 + iconOffset, y + 50, checked ? 3256925 : 3217920, false);
                    }
                    y += 9;
                }
                y += 3;
            }
        }
        if (this.editingIndex != -1) {
            boolean checkedx = ((ClipboardEntry) this.currentEntries.get(this.editingIndex)).checked;
            this.m_7522_(null);
            ClipboardScreen.DisplayCache cache = this.getDisplayCache();
            for (ClipboardScreen.LineInfo line : cache.lines) {
                graphics.drawString(this.f_96547_, line.asComponent, line.x, line.y, checkedx ? 3256925 : 3217920, false);
            }
            this.renderHighlight(cache.selection);
            this.renderCursor(graphics, cache.cursor, cache.cursorAtEnd);
        }
    }

    @Override
    public void removed() {
        this.pages.forEach(list -> list.removeIf(ce -> ce.text.getString().isBlank()));
        this.pages.removeIf(List::isEmpty);
        for (int i = 0; i < this.pages.size(); i++) {
            if (this.pages.get(i) == this.currentEntries) {
                this.item.getOrCreateTag().putInt("PreviouslyOpenedPage", i);
            }
        }
        this.send();
        super.m_7861_();
    }

    private void sendIfEditingBlock() {
        ClientPacketListener handler = this.f_96541_.player.connection;
        if (handler.getOnlinePlayers().size() > 1 && this.targetedBlock != null) {
            this.send();
        }
    }

    private void send() {
        ClipboardEntry.saveAll(this.pages, this.item);
        ClipboardOverrides.switchTo(ClipboardOverrides.ClipboardType.WRITTEN, this.item);
        if (this.pages.isEmpty()) {
            this.item.setTag(new CompoundTag());
        }
        AllPackets.getChannel().sendToServer(new ClipboardEditPacket(this.targetSlot, this.item.getOrCreateTag(), this.targetedBlock));
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean mouseScrolled(double pMouseX, double pMouseY, double pDelta) {
        this.changePage(pDelta < 0.0);
        return true;
    }

    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        if (pKeyCode == 266) {
            this.backward.m_5691_();
            return true;
        } else if (pKeyCode == 267) {
            this.forward.m_5691_();
            return true;
        } else if (this.editingIndex != -1 && pKeyCode != 256) {
            this.keyPressedWhileEditing(pKeyCode, pScanCode, pModifiers);
            this.clearDisplayCache();
            return true;
        } else {
            return super.keyPressed(pKeyCode, pScanCode, pModifiers) ? true : true;
        }
    }

    @Override
    public boolean charTyped(char pCodePoint, int pModifiers) {
        if (super.m_5534_(pCodePoint, pModifiers)) {
            return true;
        } else if (!SharedConstants.isAllowedChatCharacter(pCodePoint)) {
            return false;
        } else if (this.editingIndex == -1) {
            return false;
        } else {
            this.editContext.insertText(Character.toString(pCodePoint));
            this.clearDisplayCache();
            return true;
        }
    }

    private boolean keyPressedWhileEditing(int pKeyCode, int pScanCode, int pModifiers) {
        if (Screen.isSelectAll(pKeyCode)) {
            this.editContext.selectAll();
            return true;
        } else if (Screen.isCopy(pKeyCode)) {
            this.editContext.copy();
            return true;
        } else if (Screen.isPaste(pKeyCode)) {
            this.editContext.paste();
            return true;
        } else if (Screen.isCut(pKeyCode)) {
            this.editContext.cut();
            return true;
        } else {
            switch(pKeyCode) {
                case 257:
                case 335:
                    if (m_96638_()) {
                        this.editContext.insertText("\n");
                        return true;
                    } else if (m_96637_()) {
                        this.editingIndex = -1;
                        return true;
                    } else {
                        if (this.currentEntries.size() <= this.editingIndex + 1 || !((ClipboardEntry) this.currentEntries.get(this.editingIndex + 1)).text.getString().isEmpty()) {
                            this.currentEntries.add(this.editingIndex + 1, new ClipboardEntry(false, Components.empty()));
                        }
                        this.editingIndex++;
                        this.editContext.setCursorToEnd();
                        if (this.validateTextForEntry(" ")) {
                            return true;
                        } else {
                            this.currentEntries.remove(this.editingIndex);
                            this.editingIndex--;
                            this.editContext.setCursorToEnd();
                            return true;
                        }
                    }
                case 259:
                    if (((ClipboardEntry) this.currentEntries.get(this.editingIndex)).text.getString().isEmpty() && this.currentEntries.size() > 1) {
                        this.currentEntries.remove(this.editingIndex);
                        this.editingIndex = Math.max(0, this.editingIndex - 1);
                        this.editContext.setCursorToEnd();
                        return true;
                    } else {
                        if (m_96637_()) {
                            int prevPos = this.editContext.getCursorPos();
                            this.editContext.moveByWords(-1);
                            if (prevPos != this.editContext.getCursorPos()) {
                                this.editContext.removeCharsFromCursor(prevPos - this.editContext.getCursorPos());
                            }
                            return true;
                        }
                        this.editContext.removeCharsFromCursor(-1);
                        return true;
                    }
                case 261:
                    if (m_96637_()) {
                        int prevPos = this.editContext.getCursorPos();
                        this.editContext.moveByWords(1);
                        if (prevPos != this.editContext.getCursorPos()) {
                            this.editContext.removeCharsFromCursor(prevPos - this.editContext.getCursorPos());
                        }
                        return true;
                    }
                    this.editContext.removeCharsFromCursor(1);
                    return true;
                case 262:
                    if (m_96637_()) {
                        this.editContext.moveByWords(1, Screen.hasShiftDown());
                        return true;
                    }
                    this.editContext.moveByChars(1, Screen.hasShiftDown());
                    return true;
                case 263:
                    if (m_96637_()) {
                        this.editContext.moveByWords(-1, Screen.hasShiftDown());
                        return true;
                    }
                    this.editContext.moveByChars(-1, Screen.hasShiftDown());
                    return true;
                case 264:
                    this.keyDown();
                    return true;
                case 265:
                    this.keyUp();
                    return true;
                case 268:
                    this.keyHome();
                    return true;
                case 269:
                    this.keyEnd();
                    return true;
                default:
                    return false;
            }
        }
    }

    private void keyUp() {
        this.changeLine(-1);
    }

    private void keyDown() {
        this.changeLine(1);
    }

    private void changeLine(int pYChange) {
        int i = this.editContext.getCursorPos();
        int j = this.getDisplayCache().changeLine(i, pYChange);
        this.editContext.setCursorPos(j, Screen.hasShiftDown());
    }

    private void keyHome() {
        int i = this.editContext.getCursorPos();
        int j = this.getDisplayCache().findLineStart(i);
        this.editContext.setCursorPos(j, Screen.hasShiftDown());
    }

    private void keyEnd() {
        ClipboardScreen.DisplayCache cache = this.getDisplayCache();
        int i = this.editContext.getCursorPos();
        int j = cache.findLineEnd(i);
        this.editContext.setCursorPos(j, Screen.hasShiftDown());
    }

    private void renderCursor(GuiGraphics graphics, ClipboardScreen.Pos2i pCursorPos, boolean pIsEndOfText) {
        if (this.frameTick / 6 % 2 == 0) {
            pCursorPos = this.convertLocalToScreen(pCursorPos);
            if (!pIsEndOfText) {
                graphics.fill(pCursorPos.x, pCursorPos.y - 1, pCursorPos.x + 1, pCursorPos.y + 9, -16777216);
            } else {
                graphics.drawString(this.f_96547_, "_", (float) pCursorPos.x, (float) pCursorPos.y, 0, false);
            }
        }
    }

    private void renderHighlight(Rect2i[] pSelected) {
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tesselator.getBuilder();
        RenderSystem.setShader(GameRenderer::m_172808_);
        RenderSystem.setShaderColor(0.0F, 0.0F, 255.0F, 255.0F);
        RenderSystem.enableColorLogicOp();
        RenderSystem.logicOp(GlStateManager.LogicOp.OR_REVERSE);
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);
        for (Rect2i rect2i : pSelected) {
            int i = rect2i.getX();
            int j = rect2i.getY();
            int k = i + rect2i.getWidth();
            int l = j + rect2i.getHeight();
            bufferbuilder.m_5483_((double) i, (double) l, 0.0).endVertex();
            bufferbuilder.m_5483_((double) k, (double) l, 0.0).endVertex();
            bufferbuilder.m_5483_((double) k, (double) j, 0.0).endVertex();
            bufferbuilder.m_5483_((double) i, (double) j, 0.0).endVertex();
        }
        tesselator.end();
        RenderSystem.disableColorLogicOp();
    }

    private ClipboardScreen.Pos2i convertScreenToLocal(ClipboardScreen.Pos2i pScreenPos) {
        return new ClipboardScreen.Pos2i(pScreenPos.x - (this.f_96543_ - 192) / 2 - 36 + 10, pScreenPos.y - 32 - 24 - this.yOffsetOfEditingEntry() - this.guiTop + 14);
    }

    private ClipboardScreen.Pos2i convertLocalToScreen(ClipboardScreen.Pos2i pLocalScreenPos) {
        return new ClipboardScreen.Pos2i(pLocalScreenPos.x + (this.f_96543_ - 192) / 2 + 36 - 10, pLocalScreenPos.y + 32 + 24 + this.yOffsetOfEditingEntry() + this.guiTop - 14);
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if (super.mouseClicked(pMouseX, pMouseY, pButton)) {
            return true;
        } else if (pButton != 0) {
            return true;
        } else {
            if (this.hoveredEntry != -1) {
                if (this.hoveredCheck) {
                    this.editingIndex = -1;
                    if (this.hoveredEntry < this.currentEntries.size()) {
                        ((ClipboardEntry) this.currentEntries.get(this.hoveredEntry)).checked ^= true;
                    }
                    this.sendIfEditingBlock();
                    return true;
                }
                if (this.hoveredEntry != this.editingIndex && !this.readonly) {
                    this.editingIndex = this.hoveredEntry;
                    if (this.hoveredEntry >= this.currentEntries.size()) {
                        this.currentEntries.add(new ClipboardEntry(false, Components.empty()));
                        if (!this.validateTextForEntry(" ")) {
                            this.currentEntries.remove(this.hoveredEntry);
                            this.editingIndex = -1;
                            return true;
                        }
                    }
                    this.clearDisplayCacheAfterChange();
                }
            }
            if (this.editingIndex == -1) {
                return false;
            } else if (!(pMouseX < (double) (this.guiLeft + 50)) && !(pMouseX > (double) (this.guiLeft + 220)) && !(pMouseY < (double) (this.guiTop + 30)) && !(pMouseY > (double) (this.guiTop + 230))) {
                long i = Util.getMillis();
                ClipboardScreen.DisplayCache cache = this.getDisplayCache();
                int j = cache.getIndexAtPosition(this.f_96547_, this.convertScreenToLocal(new ClipboardScreen.Pos2i((int) pMouseX, (int) pMouseY)));
                if (j >= 0) {
                    if (j != this.lastIndex || i - this.lastClickTime >= 250L) {
                        this.editContext.setCursorPos(j, Screen.hasShiftDown());
                    } else if (!this.editContext.isSelecting()) {
                        this.selectWord(j);
                    } else {
                        this.editContext.selectAll();
                    }
                    this.clearDisplayCache();
                }
                this.lastIndex = j;
                this.lastClickTime = i;
                return true;
            } else {
                this.m_7522_(null);
                this.clearDisplayCache();
                this.editingIndex = -1;
                return false;
            }
        }
    }

    private void selectWord(int pIndex) {
        String s = this.getCurrentEntryText();
        this.editContext.setSelectionRange(StringSplitter.getWordPosition(s, -1, pIndex, false), StringSplitter.getWordPosition(s, 1, pIndex, false));
    }

    @Override
    public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
        if (super.m_7979_(pMouseX, pMouseY, pButton, pDragX, pDragY)) {
            return true;
        } else if (pButton != 0) {
            return true;
        } else if (this.editingIndex == -1) {
            return false;
        } else {
            ClipboardScreen.DisplayCache cache = this.getDisplayCache();
            int i = cache.getIndexAtPosition(this.f_96547_, this.convertScreenToLocal(new ClipboardScreen.Pos2i((int) pMouseX, (int) pMouseY)));
            this.editContext.setCursorPos(i, true);
            this.clearDisplayCache();
            return true;
        }
    }

    private ClipboardScreen.DisplayCache getDisplayCache() {
        if (this.displayCache == null) {
            this.displayCache = this.rebuildDisplayCache();
        }
        return this.displayCache;
    }

    private void clearDisplayCache() {
        this.displayCache = null;
    }

    private void clearDisplayCacheAfterChange() {
        this.editContext.setCursorToEnd();
        this.clearDisplayCache();
    }

    private ClipboardScreen.DisplayCache rebuildDisplayCache() {
        String s = this.getCurrentEntryText();
        if (s.isEmpty()) {
            return ClipboardScreen.DisplayCache.EMPTY;
        } else {
            int i = this.editContext.getCursorPos();
            int j = this.editContext.getSelectionPos();
            IntList intlist = new IntArrayList();
            List<ClipboardScreen.LineInfo> list = Lists.newArrayList();
            MutableInt mutableint = new MutableInt();
            MutableBoolean mutableboolean = new MutableBoolean();
            StringSplitter stringsplitter = this.f_96547_.getSplitter();
            stringsplitter.splitLines(s, 150, Style.EMPTY, true, (p_98132_, p_98133_, p_98134_) -> {
                int k3 = mutableint.getAndIncrement();
                String s2 = s.substring(p_98133_, p_98134_);
                mutableboolean.setValue(s2.endsWith("\n"));
                String s3 = StringUtils.stripEnd(s2, " \n");
                int l3 = k3 * 9;
                ClipboardScreen.Pos2i pos1 = this.convertLocalToScreen(new ClipboardScreen.Pos2i(0, l3));
                intlist.add(p_98133_);
                list.add(new ClipboardScreen.LineInfo(p_98132_, s3, pos1.x, pos1.y));
            });
            int[] aint = intlist.toIntArray();
            boolean flag = i == s.length();
            ClipboardScreen.Pos2i pos;
            if (flag && mutableboolean.isTrue()) {
                pos = new ClipboardScreen.Pos2i(0, list.size() * 9);
            } else {
                int k = findLineFromPos(aint, i);
                int l = this.f_96547_.width(s.substring(aint[k], i));
                pos = new ClipboardScreen.Pos2i(l, k * 9);
            }
            List<Rect2i> list1 = Lists.newArrayList();
            if (i != j) {
                int l2 = Math.min(i, j);
                int i1 = Math.max(i, j);
                int j1 = findLineFromPos(aint, l2);
                int k1 = findLineFromPos(aint, i1);
                if (j1 == k1) {
                    int l1 = j1 * 9;
                    int i2 = aint[j1];
                    list1.add(this.createPartialLineSelection(s, stringsplitter, l2, i1, l1, i2));
                } else {
                    int i3 = j1 + 1 > aint.length ? s.length() : aint[j1 + 1];
                    list1.add(this.createPartialLineSelection(s, stringsplitter, l2, i3, j1 * 9, aint[j1]));
                    for (int j3 = j1 + 1; j3 < k1; j3++) {
                        int j2 = j3 * 9;
                        String s1 = s.substring(aint[j3], aint[j3 + 1]);
                        int k2 = (int) stringsplitter.stringWidth(s1);
                        list1.add(this.createSelection(new ClipboardScreen.Pos2i(0, j2), new ClipboardScreen.Pos2i(k2, j2 + 9)));
                    }
                    list1.add(this.createPartialLineSelection(s, stringsplitter, aint[k1], i1, k1 * 9, aint[k1]));
                }
            }
            return new ClipboardScreen.DisplayCache(s, pos, flag, aint, (ClipboardScreen.LineInfo[]) list.toArray(new ClipboardScreen.LineInfo[0]), (Rect2i[]) list1.toArray(new Rect2i[0]));
        }
    }

    static int findLineFromPos(int[] pLineStarts, int pFind) {
        int i = Arrays.binarySearch(pLineStarts, pFind);
        return i < 0 ? -(i + 2) : i;
    }

    private Rect2i createPartialLineSelection(String pInput, StringSplitter pSplitter, int int0, int int1, int int2, int int3) {
        String s = pInput.substring(int3, int0);
        String s1 = pInput.substring(int3, int1);
        ClipboardScreen.Pos2i firstPos = new ClipboardScreen.Pos2i((int) pSplitter.stringWidth(s), int2);
        ClipboardScreen.Pos2i secondPos = new ClipboardScreen.Pos2i((int) pSplitter.stringWidth(s1), int2 + 9);
        return this.createSelection(firstPos, secondPos);
    }

    private Rect2i createSelection(ClipboardScreen.Pos2i pCorner1, ClipboardScreen.Pos2i pCorner2) {
        ClipboardScreen.Pos2i firstPos = this.convertLocalToScreen(pCorner1);
        ClipboardScreen.Pos2i secondPos = this.convertLocalToScreen(pCorner2);
        int i = Math.min(firstPos.x, secondPos.x);
        int j = Math.max(firstPos.x, secondPos.x);
        int k = Math.min(firstPos.y, secondPos.y);
        int l = Math.max(firstPos.y, secondPos.y);
        return new Rect2i(i, k, j - i, l - k);
    }

    @OnlyIn(Dist.CLIENT)
    static class DisplayCache {

        static final ClipboardScreen.DisplayCache EMPTY = new ClipboardScreen.DisplayCache("", new ClipboardScreen.Pos2i(0, 0), true, new int[] { 0 }, new ClipboardScreen.LineInfo[] { new ClipboardScreen.LineInfo(Style.EMPTY, "", 0, 0) }, new Rect2i[0]);

        private final String fullText;

        final ClipboardScreen.Pos2i cursor;

        final boolean cursorAtEnd;

        private final int[] lineStarts;

        final ClipboardScreen.LineInfo[] lines;

        final Rect2i[] selection;

        public DisplayCache(String pFullText, ClipboardScreen.Pos2i pCursor, boolean pCursorAtEnd, int[] pLineStarts, ClipboardScreen.LineInfo[] pLines, Rect2i[] pSelection) {
            this.fullText = pFullText;
            this.cursor = pCursor;
            this.cursorAtEnd = pCursorAtEnd;
            this.lineStarts = pLineStarts;
            this.lines = pLines;
            this.selection = pSelection;
        }

        public int getIndexAtPosition(Font pFont, ClipboardScreen.Pos2i pCursorPosition) {
            int i = pCursorPosition.y / 9;
            if (i < 0) {
                return 0;
            } else if (i >= this.lines.length) {
                return this.fullText.length();
            } else {
                ClipboardScreen.LineInfo line = this.lines[i];
                return this.lineStarts[i] + pFont.getSplitter().plainIndexAtWidth(line.contents, pCursorPosition.x, line.style);
            }
        }

        public int changeLine(int pXChange, int pYChange) {
            int i = ClipboardScreen.findLineFromPos(this.lineStarts, pXChange);
            int j = i + pYChange;
            int k;
            if (0 <= j && j < this.lineStarts.length) {
                int l = pXChange - this.lineStarts[i];
                int i1 = this.lines[j].contents.length();
                k = this.lineStarts[j] + Math.min(l, i1);
            } else {
                k = pXChange;
            }
            return k;
        }

        public int findLineStart(int pLine) {
            int i = ClipboardScreen.findLineFromPos(this.lineStarts, pLine);
            return this.lineStarts[i];
        }

        public int findLineEnd(int pLine) {
            int i = ClipboardScreen.findLineFromPos(this.lineStarts, pLine);
            return this.lineStarts[i] + this.lines[i].contents.length();
        }
    }

    @OnlyIn(Dist.CLIENT)
    static class LineInfo {

        final Style style;

        final String contents;

        final Component asComponent;

        final int x;

        final int y;

        public LineInfo(Style pStyle, String pContents, int pX, int pY) {
            this.style = pStyle;
            this.contents = pContents;
            this.x = pX;
            this.y = pY;
            this.asComponent = Components.literal(pContents).setStyle(pStyle);
        }
    }

    @OnlyIn(Dist.CLIENT)
    static class Pos2i {

        public final int x;

        public final int y;

        Pos2i(int pX, int pY) {
            this.x = pX;
            this.y = pY;
        }
    }
}