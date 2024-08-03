package net.minecraft.client.gui.screens.packs;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.MultiLineLabel;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.repository.PackCompatibility;
import net.minecraft.util.FormattedCharSequence;

public class TransferableSelectionList extends ObjectSelectionList<TransferableSelectionList.PackEntry> {

    static final ResourceLocation ICON_OVERLAY_LOCATION = new ResourceLocation("textures/gui/resource_packs.png");

    static final Component INCOMPATIBLE_TITLE = Component.translatable("pack.incompatible");

    static final Component INCOMPATIBLE_CONFIRM_TITLE = Component.translatable("pack.incompatible.confirm.title");

    private final Component title;

    final PackSelectionScreen screen;

    public TransferableSelectionList(Minecraft minecraft0, PackSelectionScreen packSelectionScreen1, int int2, int int3, Component component4) {
        super(minecraft0, int2, int3, 32, int3 - 55 + 4, 36);
        this.screen = packSelectionScreen1;
        this.title = component4;
        this.f_93394_ = false;
        this.m_93473_(true, (int) (9.0F * 1.5F));
    }

    @Override
    protected void renderHeader(GuiGraphics guiGraphics0, int int1, int int2) {
        Component $$3 = Component.empty().append(this.title).withStyle(ChatFormatting.UNDERLINE, ChatFormatting.BOLD);
        guiGraphics0.drawString(this.f_93386_.font, $$3, int1 + this.f_93388_ / 2 - this.f_93386_.font.width($$3) / 2, Math.min(this.f_93390_ + 3, int2), 16777215, false);
    }

    @Override
    public int getRowWidth() {
        return this.f_93388_;
    }

    @Override
    protected int getScrollbarPosition() {
        return this.f_93392_ - 6;
    }

    @Override
    public boolean keyPressed(int int0, int int1, int int2) {
        if (this.m_93511_() != null) {
            switch(int0) {
                case 32:
                case 257:
                    ((TransferableSelectionList.PackEntry) this.m_93511_()).keyboardSelection();
                    return true;
                default:
                    if (Screen.hasShiftDown()) {
                        switch(int0) {
                            case 264:
                                ((TransferableSelectionList.PackEntry) this.m_93511_()).keyboardMoveDown();
                                return true;
                            case 265:
                                ((TransferableSelectionList.PackEntry) this.m_93511_()).keyboardMoveUp();
                                return true;
                        }
                    }
            }
        }
        return super.m_7933_(int0, int1, int2);
    }

    public static class PackEntry extends ObjectSelectionList.Entry<TransferableSelectionList.PackEntry> {

        private static final int ICON_OVERLAY_X_MOVE_RIGHT = 0;

        private static final int ICON_OVERLAY_X_MOVE_LEFT = 32;

        private static final int ICON_OVERLAY_X_MOVE_DOWN = 64;

        private static final int ICON_OVERLAY_X_MOVE_UP = 96;

        private static final int ICON_OVERLAY_Y_UNSELECTED = 0;

        private static final int ICON_OVERLAY_Y_SELECTED = 32;

        private static final int MAX_DESCRIPTION_WIDTH_PIXELS = 157;

        private static final int MAX_NAME_WIDTH_PIXELS = 157;

        private static final String TOO_LONG_NAME_SUFFIX = "...";

        private final TransferableSelectionList parent;

        protected final Minecraft minecraft;

        private final PackSelectionModel.Entry pack;

        private final FormattedCharSequence nameDisplayCache;

        private final MultiLineLabel descriptionDisplayCache;

        private final FormattedCharSequence incompatibleNameDisplayCache;

        private final MultiLineLabel incompatibleDescriptionDisplayCache;

        public PackEntry(Minecraft minecraft0, TransferableSelectionList transferableSelectionList1, PackSelectionModel.Entry packSelectionModelEntry2) {
            this.minecraft = minecraft0;
            this.pack = packSelectionModelEntry2;
            this.parent = transferableSelectionList1;
            this.nameDisplayCache = cacheName(minecraft0, packSelectionModelEntry2.getTitle());
            this.descriptionDisplayCache = cacheDescription(minecraft0, packSelectionModelEntry2.getExtendedDescription());
            this.incompatibleNameDisplayCache = cacheName(minecraft0, TransferableSelectionList.INCOMPATIBLE_TITLE);
            this.incompatibleDescriptionDisplayCache = cacheDescription(minecraft0, packSelectionModelEntry2.getCompatibility().getDescription());
        }

        private static FormattedCharSequence cacheName(Minecraft minecraft0, Component component1) {
            int $$2 = minecraft0.font.width(component1);
            if ($$2 > 157) {
                FormattedText $$3 = FormattedText.composite(minecraft0.font.substrByWidth(component1, 157 - minecraft0.font.width("...")), FormattedText.of("..."));
                return Language.getInstance().getVisualOrder($$3);
            } else {
                return component1.getVisualOrderText();
            }
        }

        private static MultiLineLabel cacheDescription(Minecraft minecraft0, Component component1) {
            return MultiLineLabel.create(minecraft0.font, component1, 157, 2);
        }

        @Override
        public Component getNarration() {
            return Component.translatable("narrator.select", this.pack.getTitle());
        }

        @Override
        public void render(GuiGraphics guiGraphics0, int int1, int int2, int int3, int int4, int int5, int int6, int int7, boolean boolean8, float float9) {
            PackCompatibility $$10 = this.pack.getCompatibility();
            if (!$$10.isCompatible()) {
                guiGraphics0.fill(int3 - 1, int2 - 1, int3 + int4 - 9, int2 + int5 + 1, -8978432);
            }
            guiGraphics0.blit(this.pack.getIconTexture(), int3, int2, 0.0F, 0.0F, 32, 32, 32, 32);
            FormattedCharSequence $$11 = this.nameDisplayCache;
            MultiLineLabel $$12 = this.descriptionDisplayCache;
            if (this.showHoverOverlay() && (this.minecraft.options.touchscreen().get() || boolean8 || this.parent.m_93511_() == this && this.parent.m_93696_())) {
                guiGraphics0.fill(int3, int2, int3 + 32, int2 + 32, -1601138544);
                int $$13 = int6 - int3;
                int $$14 = int7 - int2;
                if (!this.pack.getCompatibility().isCompatible()) {
                    $$11 = this.incompatibleNameDisplayCache;
                    $$12 = this.incompatibleDescriptionDisplayCache;
                }
                if (this.pack.canSelect()) {
                    if ($$13 < 32) {
                        guiGraphics0.blit(TransferableSelectionList.ICON_OVERLAY_LOCATION, int3, int2, 0.0F, 32.0F, 32, 32, 256, 256);
                    } else {
                        guiGraphics0.blit(TransferableSelectionList.ICON_OVERLAY_LOCATION, int3, int2, 0.0F, 0.0F, 32, 32, 256, 256);
                    }
                } else {
                    if (this.pack.canUnselect()) {
                        if ($$13 < 16) {
                            guiGraphics0.blit(TransferableSelectionList.ICON_OVERLAY_LOCATION, int3, int2, 32.0F, 32.0F, 32, 32, 256, 256);
                        } else {
                            guiGraphics0.blit(TransferableSelectionList.ICON_OVERLAY_LOCATION, int3, int2, 32.0F, 0.0F, 32, 32, 256, 256);
                        }
                    }
                    if (this.pack.canMoveUp()) {
                        if ($$13 < 32 && $$13 > 16 && $$14 < 16) {
                            guiGraphics0.blit(TransferableSelectionList.ICON_OVERLAY_LOCATION, int3, int2, 96.0F, 32.0F, 32, 32, 256, 256);
                        } else {
                            guiGraphics0.blit(TransferableSelectionList.ICON_OVERLAY_LOCATION, int3, int2, 96.0F, 0.0F, 32, 32, 256, 256);
                        }
                    }
                    if (this.pack.canMoveDown()) {
                        if ($$13 < 32 && $$13 > 16 && $$14 > 16) {
                            guiGraphics0.blit(TransferableSelectionList.ICON_OVERLAY_LOCATION, int3, int2, 64.0F, 32.0F, 32, 32, 256, 256);
                        } else {
                            guiGraphics0.blit(TransferableSelectionList.ICON_OVERLAY_LOCATION, int3, int2, 64.0F, 0.0F, 32, 32, 256, 256);
                        }
                    }
                }
            }
            guiGraphics0.drawString(this.minecraft.font, $$11, int3 + 32 + 2, int2 + 1, 16777215);
            $$12.renderLeftAligned(guiGraphics0, int3 + 32 + 2, int2 + 12, 10, 8421504);
        }

        public String getPackId() {
            return this.pack.getId();
        }

        private boolean showHoverOverlay() {
            return !this.pack.isFixedPosition() || !this.pack.isRequired();
        }

        public void keyboardSelection() {
            if (this.pack.canSelect() && this.handlePackSelection()) {
                this.parent.screen.updateFocus(this.parent);
            } else if (this.pack.canUnselect()) {
                this.pack.unselect();
                this.parent.screen.updateFocus(this.parent);
            }
        }

        void keyboardMoveUp() {
            if (this.pack.canMoveUp()) {
                this.pack.moveUp();
            }
        }

        void keyboardMoveDown() {
            if (this.pack.canMoveDown()) {
                this.pack.moveDown();
            }
        }

        private boolean handlePackSelection() {
            if (this.pack.getCompatibility().isCompatible()) {
                this.pack.select();
                return true;
            } else {
                Component $$0 = this.pack.getCompatibility().getConfirmation();
                this.minecraft.setScreen(new ConfirmScreen(p_264693_ -> {
                    this.minecraft.setScreen(this.parent.screen);
                    if (p_264693_) {
                        this.pack.select();
                    }
                }, TransferableSelectionList.INCOMPATIBLE_CONFIRM_TITLE, $$0));
                return false;
            }
        }

        @Override
        public boolean mouseClicked(double double0, double double1, int int2) {
            if (int2 != 0) {
                return false;
            } else {
                double $$3 = double0 - (double) this.parent.m_5747_();
                double $$4 = double1 - (double) this.parent.m_7610_(this.parent.m_6702_().indexOf(this));
                if (this.showHoverOverlay() && $$3 <= 32.0) {
                    this.parent.screen.clearSelected();
                    if (this.pack.canSelect()) {
                        this.handlePackSelection();
                        return true;
                    }
                    if ($$3 < 16.0 && this.pack.canUnselect()) {
                        this.pack.unselect();
                        return true;
                    }
                    if ($$3 > 16.0 && $$4 < 16.0 && this.pack.canMoveUp()) {
                        this.pack.moveUp();
                        return true;
                    }
                    if ($$3 > 16.0 && $$4 > 16.0 && this.pack.canMoveDown()) {
                        this.pack.moveDown();
                        return true;
                    }
                }
                return false;
            }
        }
    }
}