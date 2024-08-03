package net.mehvahdjukaar.supplementaries.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import net.mehvahdjukaar.supplementaries.common.inventories.RedMerchantMenu;
import net.mehvahdjukaar.supplementaries.common.network.ModNetwork;
import net.mehvahdjukaar.supplementaries.common.network.ServerBoundSelectMerchantTradePacket;
import net.mehvahdjukaar.supplementaries.reg.ModTextures;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;

public class RedMerchantScreen extends AbstractContainerScreen<RedMerchantMenu> {

    private static final ResourceLocation TEXTURE = ModTextures.RED_MERCHANT_GUI_TEXTURE;

    private static final MutableComponent TRADE_OFFER = Component.translatable("gui.supplementaries.orange_trader.trade");

    private static final MutableComponent I_RECEIVE = Component.translatable("gui.supplementaries.orange_trader.get");

    private static final MutableComponent YOU_RECEIVE = Component.translatable("gui.supplementaries.orange_trader.receive");

    private static final Component TRADES_LABEL = Component.translatable("merchant.trades");

    private static final Component LEVEL_SEPARATOR = Component.literal(" - ");

    private static final Component DEPRECATED_TOOLTIP = Component.translatable("merchant.deprecated");

    private int shopItem;

    private final RedMerchantScreen.TradeOfferButton[] tradeOfferButtons = new RedMerchantScreen.TradeOfferButton[7];

    int scrollOff;

    private boolean isDragging;

    public RedMerchantScreen(RedMerchantMenu merchantMenu, Inventory inventory, Component component) {
        super(merchantMenu, inventory, component);
        this.f_97726_ = 276;
        this.f_97730_ = 107;
    }

    private void postButtonClick() {
        ((RedMerchantMenu) this.f_97732_).setSelectionHint(this.shopItem);
        ((RedMerchantMenu) this.f_97732_).tryMoveItems(this.shopItem);
        ModNetwork.CHANNEL.sendToServer(new ServerBoundSelectMerchantTradePacket(this.shopItem));
    }

    @Override
    protected void init() {
        super.init();
        int i = (this.f_96543_ - this.f_97726_) / 2;
        int j = (this.f_96544_ - this.f_97727_) / 2;
        int k = j + 16 + 2;
        for (int l = 0; l < 7; l++) {
            this.tradeOfferButtons[l] = (RedMerchantScreen.TradeOfferButton) this.m_142416_(new RedMerchantScreen.TradeOfferButton(i + 5, k, l, button -> {
                if (button instanceof RedMerchantScreen.TradeOfferButton b) {
                    this.shopItem = b.getIndex() + this.scrollOff;
                    this.postButtonClick();
                }
            }));
            k += 20;
        }
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float f, int i, int j) {
        int k = (this.f_96543_ - this.f_97726_) / 2;
        int l = (this.f_96544_ - this.f_97727_) / 2;
        guiGraphics.blit(TEXTURE, k, l, 0, 0.0F, 0.0F, this.f_97726_, this.f_97727_, 512, 256);
        MerchantOffers merchantOffers = ((RedMerchantMenu) this.f_97732_).getOffers();
        if (!merchantOffers.isEmpty()) {
            int m = this.shopItem;
            if (m < 0 || m >= merchantOffers.size()) {
                return;
            }
            MerchantOffer merchantOffer = (MerchantOffer) merchantOffers.get(m);
            if (merchantOffer.isOutOfStock()) {
                guiGraphics.blit(TEXTURE, this.f_97735_ + 83 + 99, this.f_97736_ + 35, 0, 311.0F, 0.0F, 28, 21, 512, 256);
            }
        }
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        int i = ((RedMerchantMenu) this.f_97732_).getTraderLevel();
        if (i > 0 && i <= 5 && ((RedMerchantMenu) this.f_97732_).showProgressBar()) {
            Component component = this.f_96539_.copy().append(LEVEL_SEPARATOR).append(Component.translatable("merchant.level." + i));
            int j = this.f_96547_.width(component);
            int k = 49 + this.f_97726_ / 2 - j / 2;
            graphics.drawString(this.f_96547_, component, k, 6, 4210752);
        }
        graphics.drawString(this.f_96547_, this.f_169604_, this.f_97730_, this.f_97731_, 4210752, false);
        int l = this.f_96547_.width(TRADES_LABEL);
        graphics.drawString(this.f_96547_, TRADES_LABEL, (int) (5.0F - (float) l / 2.0F + 48.0F), 6, 4210752, false);
        MutableComponent tradeOffer = TRADE_OFFER.withStyle(ChatFormatting.WHITE).withStyle(ChatFormatting.BOLD);
        graphics.drawString(this.f_96547_, tradeOffer, (int) (49.0F + (float) this.f_97726_ / 2.0F - (float) this.f_96547_.width(tradeOffer) / 2.0F), 10, 4210752);
        Component iReceive = I_RECEIVE.withStyle(ChatFormatting.WHITE);
        graphics.drawString(this.f_96547_, iReceive, (int) (20.0F + (float) this.f_97726_ / 2.0F - (float) this.f_96547_.width(iReceive) / 2.0F), 24, 4210752);
        Component uReceive = YOU_RECEIVE.withStyle(ChatFormatting.WHITE);
        graphics.drawString(this.f_96547_, uReceive, (int) (91.0F + (float) this.f_97726_ / 2.0F - (float) this.f_96547_.width(uReceive) / 2.0F), 24, 4210752);
    }

    private void renderProgressBar(GuiGraphics guiGraphics, int i, int j, MerchantOffer merchantOffer) {
        int k = ((RedMerchantMenu) this.f_97732_).getTraderLevel();
        int l = ((RedMerchantMenu) this.f_97732_).getTraderXp();
        if (k < 5) {
            guiGraphics.blit(TEXTURE, i + 136, j + 16, 0, 0.0F, 186.0F, 102, 5, 512, 256);
            int m = VillagerData.getMinXpPerLevel(k);
            if (l >= m && VillagerData.canLevelUp(k)) {
                float f = 100.0F / (float) (VillagerData.getMaxXpPerLevel(k) - m);
                int o = Math.min(Mth.floor(f * (float) (l - m)), 100);
                guiGraphics.blit(TEXTURE, i + 136, j + 16, 0, 0.0F, 191.0F, o + 1, 5, 512, 256);
                int p = ((RedMerchantMenu) this.f_97732_).getFutureTraderXp();
                if (p > 0) {
                    int q = Math.min(Mth.floor((float) p * f), 100 - o);
                    guiGraphics.blit(TEXTURE, i + 136 + o + 1, j + 16 + 1, 0, 2.0F, 182.0F, q, 3, 512, 256);
                }
            }
        }
    }

    private void renderScroller(GuiGraphics guiGraphics, int i, int j, MerchantOffers merchantOffers) {
        int k = merchantOffers.size() + 1 - 7;
        if (k > 1) {
            int l = 139 - (27 + (k - 1) * 139 / k);
            int m = 1 + l / k + 139 / k;
            int o = Math.min(113, this.scrollOff * m);
            if (this.scrollOff == k - 1) {
                o = 113;
            }
            guiGraphics.blit(TEXTURE, i + 94, j + 18 + o, 0, 0.0F, 199.0F, 6, 27, 512, 256);
        } else {
            guiGraphics.blit(TEXTURE, i + 94, j + 18, 0, 6.0F, 199.0F, 6, 27, 512, 256);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.m_280273_(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        MerchantOffers merchantOffers = ((RedMerchantMenu) this.f_97732_).getOffers();
        if (!merchantOffers.isEmpty()) {
            int k = (this.f_96543_ - this.f_97726_) / 2;
            int l = (this.f_96544_ - this.f_97727_) / 2;
            int m = l + 16 + 1;
            int n = k + 5 + 5;
            this.renderScroller(guiGraphics, k, l, merchantOffers);
            int o = 0;
            for (MerchantOffer merchantOffer : merchantOffers) {
                if (!this.canScroll(merchantOffers.size()) || o >= this.scrollOff && o < 7 + this.scrollOff) {
                    ItemStack itemStack = merchantOffer.getBaseCostA();
                    ItemStack itemStack2 = merchantOffer.getCostA();
                    ItemStack itemStack3 = merchantOffer.getCostB();
                    ItemStack itemStack4 = merchantOffer.getResult();
                    guiGraphics.pose().pushPose();
                    guiGraphics.pose().translate(0.0F, 0.0F, 100.0F);
                    int p = m + 2;
                    this.renderAndDecorateCostA(guiGraphics, itemStack2, itemStack, n, p);
                    if (!itemStack3.isEmpty()) {
                        guiGraphics.renderFakeItem(itemStack3, k + 5 + 35, p);
                        guiGraphics.renderItemDecorations(this.f_96547_, itemStack3, k + 5 + 35, p);
                    }
                    this.renderButtonArrows(guiGraphics, merchantOffer, k, p);
                    guiGraphics.renderFakeItem(itemStack4, k + 5 + 68, p);
                    guiGraphics.renderItemDecorations(this.f_96547_, itemStack4, k + 5 + 68, p);
                    guiGraphics.pose().popPose();
                    m += 20;
                }
                o++;
            }
            int q = this.shopItem;
            MerchantOffer merchantOffer = (MerchantOffer) merchantOffers.get(q);
            if (((RedMerchantMenu) this.f_97732_).showProgressBar()) {
                this.renderProgressBar(guiGraphics, k, l, merchantOffer);
            }
            if (merchantOffer.isOutOfStock() && this.m_6774_(186, 35, 22, 21, (double) mouseX, (double) mouseY) && ((RedMerchantMenu) this.f_97732_).canRestock()) {
                guiGraphics.renderTooltip(this.f_96547_, DEPRECATED_TOOLTIP, mouseX, mouseY);
            }
            for (RedMerchantScreen.TradeOfferButton tradeOfferButton : this.tradeOfferButtons) {
                if (tradeOfferButton.m_198029_()) {
                    tradeOfferButton.renderToolTip(guiGraphics, mouseX, mouseY);
                }
                tradeOfferButton.f_93624_ = tradeOfferButton.index < ((RedMerchantMenu) this.f_97732_).getOffers().size();
            }
            RenderSystem.enableDepthTest();
        }
        this.m_280072_(guiGraphics, mouseX, mouseY);
    }

    private void renderButtonArrows(GuiGraphics guiGraphics, MerchantOffer merchantOffer, int i, int j) {
        RenderSystem.enableBlend();
        if (merchantOffer.isOutOfStock()) {
            guiGraphics.blit(TEXTURE, i + 5 + 35 + 20, j + 3, 0, 25.0F, 171.0F, 10, 9, 512, 256);
        } else {
            guiGraphics.blit(TEXTURE, i + 5 + 35 + 20, j + 3, 0, 15.0F, 171.0F, 10, 9, 512, 256);
        }
    }

    private void renderAndDecorateCostA(GuiGraphics guiGraphics, ItemStack itemStack, ItemStack itemStack2, int i, int j) {
        guiGraphics.renderFakeItem(itemStack, i, j);
        if (itemStack2.getCount() == itemStack.getCount()) {
            guiGraphics.renderItemDecorations(this.f_96547_, itemStack, i, j);
        } else {
            guiGraphics.renderItemDecorations(this.f_96547_, itemStack2, i, j, itemStack2.getCount() == 1 ? "1" : null);
            guiGraphics.renderItemDecorations(this.f_96547_, itemStack, i + 14, j, itemStack.getCount() == 1 ? "1" : null);
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(0.0F, 0.0F, 300.0F);
            guiGraphics.blit(TEXTURE, i + 7, j + 12, 0, 0.0F, 176.0F, 9, 2, 512, 256);
            guiGraphics.pose().popPose();
        }
    }

    private boolean canScroll(int numOffers) {
        return numOffers > 7;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        int i = ((RedMerchantMenu) this.f_97732_).getOffers().size();
        if (this.canScroll(i)) {
            int j = i - 7;
            this.scrollOff = Mth.clamp((int) ((double) this.scrollOff - delta), 0, j);
        }
        return true;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        int i = ((RedMerchantMenu) this.f_97732_).getOffers().size();
        if (this.isDragging) {
            int j = this.f_97736_ + 18;
            int k = j + 139;
            int l = i - 7;
            float f = ((float) mouseY - (float) j - 13.5F) / ((float) (k - j) - 27.0F);
            f = f * (float) l + 0.5F;
            this.scrollOff = Mth.clamp((int) f, 0, l);
            return true;
        } else {
            return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.isDragging = false;
        int i = (this.f_96543_ - this.f_97726_) / 2;
        int j = (this.f_96544_ - this.f_97727_) / 2;
        if (this.canScroll(((RedMerchantMenu) this.f_97732_).getOffers().size()) && mouseX > (double) (i + 94) && mouseX < (double) (i + 94 + 6) && mouseY > (double) (j + 18) && mouseY <= (double) (j + 18 + 139 + 1)) {
            this.isDragging = true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    class TradeOfferButton extends Button {

        final int index;

        public TradeOfferButton(int i, int j, int k, Button.OnPress onPress) {
            super(i, j, 88, 20, CommonComponents.EMPTY, onPress, f_252438_);
            this.index = k;
            this.f_93624_ = false;
        }

        public int getIndex() {
            return this.index;
        }

        public void renderToolTip(GuiGraphics graphics, int mouseX, int mouseY) {
            if (this.f_93622_ && ((RedMerchantMenu) RedMerchantScreen.this.f_97732_).getOffers().size() > this.index + RedMerchantScreen.this.scrollOff) {
                if (mouseX < this.m_252754_() + 20) {
                    ItemStack itemStack = ((MerchantOffer) ((RedMerchantMenu) RedMerchantScreen.this.f_97732_).getOffers().get(this.index + RedMerchantScreen.this.scrollOff)).getCostA();
                    graphics.renderTooltip(RedMerchantScreen.this.f_96547_, itemStack, mouseX, mouseY);
                } else if (mouseX < this.m_252754_() + 50 && mouseX > this.m_252754_() + 30) {
                    ItemStack itemStack = ((MerchantOffer) ((RedMerchantMenu) RedMerchantScreen.this.f_97732_).getOffers().get(this.index + RedMerchantScreen.this.scrollOff)).getCostB();
                    if (!itemStack.isEmpty()) {
                        graphics.renderTooltip(RedMerchantScreen.this.f_96547_, itemStack, mouseX, mouseY);
                    }
                } else if (mouseX > this.m_252754_() + 65) {
                    ItemStack itemStack = ((MerchantOffer) ((RedMerchantMenu) RedMerchantScreen.this.f_97732_).getOffers().get(this.index + RedMerchantScreen.this.scrollOff)).getResult();
                    graphics.renderTooltip(RedMerchantScreen.this.f_96547_, itemStack, mouseX, mouseY);
                }
            }
        }
    }
}