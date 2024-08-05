package net.minecraft.client.gui.screens.inventory;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundSelectTradePacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MerchantMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;

public class MerchantScreen extends AbstractContainerScreen<MerchantMenu> {

    private static final ResourceLocation VILLAGER_LOCATION = new ResourceLocation("textures/gui/container/villager2.png");

    private static final int TEXTURE_WIDTH = 512;

    private static final int TEXTURE_HEIGHT = 256;

    private static final int MERCHANT_MENU_PART_X = 99;

    private static final int PROGRESS_BAR_X = 136;

    private static final int PROGRESS_BAR_Y = 16;

    private static final int SELL_ITEM_1_X = 5;

    private static final int SELL_ITEM_2_X = 35;

    private static final int BUY_ITEM_X = 68;

    private static final int LABEL_Y = 6;

    private static final int NUMBER_OF_OFFER_BUTTONS = 7;

    private static final int TRADE_BUTTON_X = 5;

    private static final int TRADE_BUTTON_HEIGHT = 20;

    private static final int TRADE_BUTTON_WIDTH = 88;

    private static final int SCROLLER_HEIGHT = 27;

    private static final int SCROLLER_WIDTH = 6;

    private static final int SCROLL_BAR_HEIGHT = 139;

    private static final int SCROLL_BAR_TOP_POS_Y = 18;

    private static final int SCROLL_BAR_START_X = 94;

    private static final Component TRADES_LABEL = Component.translatable("merchant.trades");

    private static final Component LEVEL_SEPARATOR = Component.literal(" - ");

    private static final Component DEPRECATED_TOOLTIP = Component.translatable("merchant.deprecated");

    private int shopItem;

    private final MerchantScreen.TradeOfferButton[] tradeOfferButtons = new MerchantScreen.TradeOfferButton[7];

    int scrollOff;

    private boolean isDragging;

    public MerchantScreen(MerchantMenu merchantMenu0, Inventory inventory1, Component component2) {
        super(merchantMenu0, inventory1, component2);
        this.f_97726_ = 276;
        this.f_97730_ = 107;
    }

    private void postButtonClick() {
        ((MerchantMenu) this.f_97732_).setSelectionHint(this.shopItem);
        ((MerchantMenu) this.f_97732_).tryMoveItems(this.shopItem);
        this.f_96541_.getConnection().send(new ServerboundSelectTradePacket(this.shopItem));
    }

    @Override
    protected void init() {
        super.init();
        int $$0 = (this.f_96543_ - this.f_97726_) / 2;
        int $$1 = (this.f_96544_ - this.f_97727_) / 2;
        int $$2 = $$1 + 16 + 2;
        for (int $$3 = 0; $$3 < 7; $$3++) {
            this.tradeOfferButtons[$$3] = (MerchantScreen.TradeOfferButton) this.m_142416_(new MerchantScreen.TradeOfferButton($$0 + 5, $$2, $$3, p_99174_ -> {
                if (p_99174_ instanceof MerchantScreen.TradeOfferButton) {
                    this.shopItem = ((MerchantScreen.TradeOfferButton) p_99174_).getIndex() + this.scrollOff;
                    this.postButtonClick();
                }
            }));
            $$2 += 20;
        }
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics0, int int1, int int2) {
        int $$3 = ((MerchantMenu) this.f_97732_).getTraderLevel();
        if ($$3 > 0 && $$3 <= 5 && ((MerchantMenu) this.f_97732_).showProgressBar()) {
            Component $$4 = this.f_96539_.copy().append(LEVEL_SEPARATOR).append(Component.translatable("merchant.level." + $$3));
            int $$5 = this.f_96547_.width($$4);
            int $$6 = 49 + this.f_97726_ / 2 - $$5 / 2;
            guiGraphics0.drawString(this.f_96547_, $$4, $$6, 6, 4210752, false);
        } else {
            guiGraphics0.drawString(this.f_96547_, this.f_96539_, 49 + this.f_97726_ / 2 - this.f_96547_.width(this.f_96539_) / 2, 6, 4210752, false);
        }
        guiGraphics0.drawString(this.f_96547_, this.f_169604_, this.f_97730_, this.f_97731_, 4210752, false);
        int $$7 = this.f_96547_.width(TRADES_LABEL);
        guiGraphics0.drawString(this.f_96547_, TRADES_LABEL, 5 - $$7 / 2 + 48, 6, 4210752, false);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics0, float float1, int int2, int int3) {
        int $$4 = (this.f_96543_ - this.f_97726_) / 2;
        int $$5 = (this.f_96544_ - this.f_97727_) / 2;
        guiGraphics0.blit(VILLAGER_LOCATION, $$4, $$5, 0, 0.0F, 0.0F, this.f_97726_, this.f_97727_, 512, 256);
        MerchantOffers $$6 = ((MerchantMenu) this.f_97732_).getOffers();
        if (!$$6.isEmpty()) {
            int $$7 = this.shopItem;
            if ($$7 < 0 || $$7 >= $$6.size()) {
                return;
            }
            MerchantOffer $$8 = (MerchantOffer) $$6.get($$7);
            if ($$8.isOutOfStock()) {
                guiGraphics0.blit(VILLAGER_LOCATION, this.f_97735_ + 83 + 99, this.f_97736_ + 35, 0, 311.0F, 0.0F, 28, 21, 512, 256);
            }
        }
    }

    private void renderProgressBar(GuiGraphics guiGraphics0, int int1, int int2, MerchantOffer merchantOffer3) {
        int $$4 = ((MerchantMenu) this.f_97732_).getTraderLevel();
        int $$5 = ((MerchantMenu) this.f_97732_).getTraderXp();
        if ($$4 < 5) {
            guiGraphics0.blit(VILLAGER_LOCATION, int1 + 136, int2 + 16, 0, 0.0F, 186.0F, 102, 5, 512, 256);
            int $$6 = VillagerData.getMinXpPerLevel($$4);
            if ($$5 >= $$6 && VillagerData.canLevelUp($$4)) {
                int $$7 = 100;
                float $$8 = 100.0F / (float) (VillagerData.getMaxXpPerLevel($$4) - $$6);
                int $$9 = Math.min(Mth.floor($$8 * (float) ($$5 - $$6)), 100);
                guiGraphics0.blit(VILLAGER_LOCATION, int1 + 136, int2 + 16, 0, 0.0F, 191.0F, $$9 + 1, 5, 512, 256);
                int $$10 = ((MerchantMenu) this.f_97732_).getFutureTraderXp();
                if ($$10 > 0) {
                    int $$11 = Math.min(Mth.floor((float) $$10 * $$8), 100 - $$9);
                    guiGraphics0.blit(VILLAGER_LOCATION, int1 + 136 + $$9 + 1, int2 + 16 + 1, 0, 2.0F, 182.0F, $$11, 3, 512, 256);
                }
            }
        }
    }

    private void renderScroller(GuiGraphics guiGraphics0, int int1, int int2, MerchantOffers merchantOffers3) {
        int $$4 = merchantOffers3.size() + 1 - 7;
        if ($$4 > 1) {
            int $$5 = 139 - (27 + ($$4 - 1) * 139 / $$4);
            int $$6 = 1 + $$5 / $$4 + 139 / $$4;
            int $$7 = 113;
            int $$8 = Math.min(113, this.scrollOff * $$6);
            if (this.scrollOff == $$4 - 1) {
                $$8 = 113;
            }
            guiGraphics0.blit(VILLAGER_LOCATION, int1 + 94, int2 + 18 + $$8, 0, 0.0F, 199.0F, 6, 27, 512, 256);
        } else {
            guiGraphics0.blit(VILLAGER_LOCATION, int1 + 94, int2 + 18, 0, 6.0F, 199.0F, 6, 27, 512, 256);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        this.m_280273_(guiGraphics0);
        super.render(guiGraphics0, int1, int2, float3);
        MerchantOffers $$4 = ((MerchantMenu) this.f_97732_).getOffers();
        if (!$$4.isEmpty()) {
            int $$5 = (this.f_96543_ - this.f_97726_) / 2;
            int $$6 = (this.f_96544_ - this.f_97727_) / 2;
            int $$7 = $$6 + 16 + 1;
            int $$8 = $$5 + 5 + 5;
            this.renderScroller(guiGraphics0, $$5, $$6, $$4);
            int $$9 = 0;
            for (MerchantOffer $$10 : $$4) {
                if (!this.canScroll($$4.size()) || $$9 >= this.scrollOff && $$9 < 7 + this.scrollOff) {
                    ItemStack $$11 = $$10.getBaseCostA();
                    ItemStack $$12 = $$10.getCostA();
                    ItemStack $$13 = $$10.getCostB();
                    ItemStack $$14 = $$10.getResult();
                    guiGraphics0.pose().pushPose();
                    guiGraphics0.pose().translate(0.0F, 0.0F, 100.0F);
                    int $$15 = $$7 + 2;
                    this.renderAndDecorateCostA(guiGraphics0, $$12, $$11, $$8, $$15);
                    if (!$$13.isEmpty()) {
                        guiGraphics0.renderFakeItem($$13, $$5 + 5 + 35, $$15);
                        guiGraphics0.renderItemDecorations(this.f_96547_, $$13, $$5 + 5 + 35, $$15);
                    }
                    this.renderButtonArrows(guiGraphics0, $$10, $$5, $$15);
                    guiGraphics0.renderFakeItem($$14, $$5 + 5 + 68, $$15);
                    guiGraphics0.renderItemDecorations(this.f_96547_, $$14, $$5 + 5 + 68, $$15);
                    guiGraphics0.pose().popPose();
                    $$7 += 20;
                    $$9++;
                } else {
                    $$9++;
                }
            }
            int $$16 = this.shopItem;
            MerchantOffer $$17 = (MerchantOffer) $$4.get($$16);
            if (((MerchantMenu) this.f_97732_).showProgressBar()) {
                this.renderProgressBar(guiGraphics0, $$5, $$6, $$17);
            }
            if ($$17.isOutOfStock() && this.m_6774_(186, 35, 22, 21, (double) int1, (double) int2) && ((MerchantMenu) this.f_97732_).canRestock()) {
                guiGraphics0.renderTooltip(this.f_96547_, DEPRECATED_TOOLTIP, int1, int2);
            }
            for (MerchantScreen.TradeOfferButton $$18 : this.tradeOfferButtons) {
                if ($$18.m_198029_()) {
                    $$18.renderToolTip(guiGraphics0, int1, int2);
                }
                $$18.f_93624_ = $$18.index < ((MerchantMenu) this.f_97732_).getOffers().size();
            }
            RenderSystem.enableDepthTest();
        }
        this.m_280072_(guiGraphics0, int1, int2);
    }

    private void renderButtonArrows(GuiGraphics guiGraphics0, MerchantOffer merchantOffer1, int int2, int int3) {
        RenderSystem.enableBlend();
        if (merchantOffer1.isOutOfStock()) {
            guiGraphics0.blit(VILLAGER_LOCATION, int2 + 5 + 35 + 20, int3 + 3, 0, 25.0F, 171.0F, 10, 9, 512, 256);
        } else {
            guiGraphics0.blit(VILLAGER_LOCATION, int2 + 5 + 35 + 20, int3 + 3, 0, 15.0F, 171.0F, 10, 9, 512, 256);
        }
    }

    private void renderAndDecorateCostA(GuiGraphics guiGraphics0, ItemStack itemStack1, ItemStack itemStack2, int int3, int int4) {
        guiGraphics0.renderFakeItem(itemStack1, int3, int4);
        if (itemStack2.getCount() == itemStack1.getCount()) {
            guiGraphics0.renderItemDecorations(this.f_96547_, itemStack1, int3, int4);
        } else {
            guiGraphics0.renderItemDecorations(this.f_96547_, itemStack2, int3, int4, itemStack2.getCount() == 1 ? "1" : null);
            guiGraphics0.renderItemDecorations(this.f_96547_, itemStack1, int3 + 14, int4, itemStack1.getCount() == 1 ? "1" : null);
            guiGraphics0.pose().pushPose();
            guiGraphics0.pose().translate(0.0F, 0.0F, 300.0F);
            guiGraphics0.blit(VILLAGER_LOCATION, int3 + 7, int4 + 12, 0, 0.0F, 176.0F, 9, 2, 512, 256);
            guiGraphics0.pose().popPose();
        }
    }

    private boolean canScroll(int int0) {
        return int0 > 7;
    }

    @Override
    public boolean mouseScrolled(double double0, double double1, double double2) {
        int $$3 = ((MerchantMenu) this.f_97732_).getOffers().size();
        if (this.canScroll($$3)) {
            int $$4 = $$3 - 7;
            this.scrollOff = Mth.clamp((int) ((double) this.scrollOff - double2), 0, $$4);
        }
        return true;
    }

    @Override
    public boolean mouseDragged(double double0, double double1, int int2, double double3, double double4) {
        int $$5 = ((MerchantMenu) this.f_97732_).getOffers().size();
        if (this.isDragging) {
            int $$6 = this.f_97736_ + 18;
            int $$7 = $$6 + 139;
            int $$8 = $$5 - 7;
            float $$9 = ((float) double1 - (float) $$6 - 13.5F) / ((float) ($$7 - $$6) - 27.0F);
            $$9 = $$9 * (float) $$8 + 0.5F;
            this.scrollOff = Mth.clamp((int) $$9, 0, $$8);
            return true;
        } else {
            return super.mouseDragged(double0, double1, int2, double3, double4);
        }
    }

    @Override
    public boolean mouseClicked(double double0, double double1, int int2) {
        this.isDragging = false;
        int $$3 = (this.f_96543_ - this.f_97726_) / 2;
        int $$4 = (this.f_96544_ - this.f_97727_) / 2;
        if (this.canScroll(((MerchantMenu) this.f_97732_).getOffers().size()) && double0 > (double) ($$3 + 94) && double0 < (double) ($$3 + 94 + 6) && double1 > (double) ($$4 + 18) && double1 <= (double) ($$4 + 18 + 139 + 1)) {
            this.isDragging = true;
        }
        return super.mouseClicked(double0, double1, int2);
    }

    class TradeOfferButton extends Button {

        final int index;

        public TradeOfferButton(int int0, int int1, int int2, Button.OnPress buttonOnPress3) {
            super(int0, int1, 88, 20, CommonComponents.EMPTY, buttonOnPress3, f_252438_);
            this.index = int2;
            this.f_93624_ = false;
        }

        public int getIndex() {
            return this.index;
        }

        public void renderToolTip(GuiGraphics guiGraphics0, int int1, int int2) {
            if (this.f_93622_ && ((MerchantMenu) MerchantScreen.this.f_97732_).getOffers().size() > this.index + MerchantScreen.this.scrollOff) {
                if (int1 < this.m_252754_() + 20) {
                    ItemStack $$3 = ((MerchantOffer) ((MerchantMenu) MerchantScreen.this.f_97732_).getOffers().get(this.index + MerchantScreen.this.scrollOff)).getCostA();
                    guiGraphics0.renderTooltip(MerchantScreen.this.f_96547_, $$3, int1, int2);
                } else if (int1 < this.m_252754_() + 50 && int1 > this.m_252754_() + 30) {
                    ItemStack $$4 = ((MerchantOffer) ((MerchantMenu) MerchantScreen.this.f_97732_).getOffers().get(this.index + MerchantScreen.this.scrollOff)).getCostB();
                    if (!$$4.isEmpty()) {
                        guiGraphics0.renderTooltip(MerchantScreen.this.f_96547_, $$4, int1, int2);
                    }
                } else if (int1 > this.m_252754_() + 65) {
                    ItemStack $$5 = ((MerchantOffer) ((MerchantMenu) MerchantScreen.this.f_97732_).getOffers().get(this.index + MerchantScreen.this.scrollOff)).getResult();
                    guiGraphics0.renderTooltip(MerchantScreen.this.f_96547_, $$5, int1, int2);
                }
            }
        }
    }
}