package io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.traderstorage.auction;

import io.github.lightman314.lightmanscurrency.LCConfig;
import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.api.misc.EasyText;
import io.github.lightman314.lightmanscurrency.api.misc.client.rendering.EasyGuiGraphics;
import io.github.lightman314.lightmanscurrency.api.money.input.MoneyValueWidget;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyValue;
import io.github.lightman314.lightmanscurrency.api.network.LazyPacketData;
import io.github.lightman314.lightmanscurrency.api.traders.menu.storage.TraderStorageClientTab;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.TraderScreen;
import io.github.lightman314.lightmanscurrency.client.gui.widget.TimeInputWidget;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.icon.IconButton;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.icon.IconData;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.trade.TradeButton;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyAddonHelper;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyButton;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyTextButton;
import io.github.lightman314.lightmanscurrency.client.util.IconAndButtonUtil;
import io.github.lightman314.lightmanscurrency.client.util.ScreenArea;
import io.github.lightman314.lightmanscurrency.client.util.TextRenderUtil;
import io.github.lightman314.lightmanscurrency.common.menus.slots.SimpleSlot;
import io.github.lightman314.lightmanscurrency.common.menus.traderstorage.auction.AuctionCreateTab;
import io.github.lightman314.lightmanscurrency.common.player.LCAdminMode;
import io.github.lightman314.lightmanscurrency.common.traders.auction.tradedata.AuctionTradeData;
import io.github.lightman314.lightmanscurrency.network.message.persistentdata.CPacketCreatePersistentAuction;
import io.github.lightman314.lightmanscurrency.util.TimeUtil;
import javax.annotation.Nonnull;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class AuctionCreateClientTab extends TraderStorageClientTab<AuctionCreateTab> {

    public static final long CLOSE_DELAY = 5000L;

    AuctionTradeData pendingAuction;

    TradeButton tradeDisplay;

    MoneyValueWidget priceSelect;

    EasyButton buttonTogglePriceMode;

    boolean startingBidMode = true;

    EasyButton buttonSubmitAuction;

    boolean locked = false;

    long successTime = 0L;

    EasyButton buttonSubmitPersistentAuction;

    EditBox persistentAuctionIDInput;

    TimeInputWidget timeInput;

    public AuctionCreateClientTab(Object screen, AuctionCreateTab commonTab) {
        super(screen, commonTab);
    }

    @Nonnull
    @Override
    public IconData getIcon() {
        return IconAndButtonUtil.ICON_PLUS;
    }

    public MutableComponent getTooltip() {
        return LCText.TOOLTIP_TRADER_AUCTION_CREATE.get();
    }

    @Override
    public boolean blockInventoryClosing() {
        return LCAdminMode.isAdminPlayer(this.screen.getMenu().getPlayer());
    }

    @Override
    public void initialize(ScreenArea screenArea, boolean firstOpen) {
        if (firstOpen) {
            this.pendingAuction = new AuctionTradeData(this.menu.getPlayer());
            this.locked = false;
            this.successTime = 0L;
            this.startingBidMode = true;
            this.commonTab.getAuctionItems().addListener(c -> this.UpdateAuctionItems());
        }
        this.tradeDisplay = this.addChild(new TradeButton(this.menu::getContext, () -> this.pendingAuction, b -> {
        }));
        this.tradeDisplay.setPosition(screenArea.pos.offset(15, 5));
        this.priceSelect = this.addChild(new MoneyValueWidget(screenArea.pos.offset(screenArea.width / 2 - 88, 34), firstOpen ? null : this.priceSelect, MoneyValue.empty(), this::onPriceChanged));
        this.priceSelect.drawBG = this.priceSelect.allowFreeInput = false;
        this.buttonTogglePriceMode = this.addChild(new EasyTextButton(screenArea.pos.offset(114, 5), screenArea.width - 119, 20, this::getBidModeText, b -> this.TogglePriceTarget()));
        this.timeInput = this.addChild(new TimeInputWidget(screenArea.pos.offset(80, 112), 10, TimeUtil.TimeUnit.DAY, TimeUtil.TimeUnit.HOUR, this::updateDuration));
        this.timeInput.minDuration = Math.max((long) LCConfig.SERVER.auctionHouseDurationMin.get().intValue() * 86400000L, 3600000L);
        this.timeInput.maxDuration = (long) Math.max(LCConfig.SERVER.auctionHouseDurationMax.get(), LCConfig.SERVER.auctionHouseDurationMin.get()) * 86400000L;
        this.timeInput.setTime(this.timeInput.minDuration);
        this.buttonSubmitAuction = this.addChild(new EasyTextButton(screenArea.pos.offset(40, -20), screenArea.width - 80, 20, LCText.BUTTON_TRADER_AUCTION_CREATE.get(), b -> this.submitAuction()));
        this.buttonSubmitAuction.f_93623_ = false;
        this.buttonSubmitPersistentAuction = this.addChild(new IconButton(screenArea.pos.offset(screenArea.width - 20, -20), this::submitPersistentAuction, IconAndButtonUtil.ICON_PERSISTENT_DATA).withAddons(EasyAddonHelper.tooltip(LCText.TOOLTIP_PERSISTENT_CREATE_AUCTION)));
        this.buttonSubmitPersistentAuction.f_93624_ = LCAdminMode.isAdminPlayer(this.screen.getPlayer());
        this.buttonSubmitPersistentAuction.f_93623_ = false;
        int idWidth = this.getFont().width(LCText.GUI_PERSISTENT_ID.get());
        this.persistentAuctionIDInput = this.addChild(new EditBox(this.getFont(), screenArea.x + idWidth + 2, screenArea.y - 40, screenArea.width - idWidth - 2, 18, EasyText.empty()));
        this.persistentAuctionIDInput.f_93624_ = LCAdminMode.isAdminPlayer(this.screen.getPlayer());
    }

    @Override
    public void closeAction() {
        this.commonTab.getAuctionItems().removeListener(c -> this.UpdateAuctionItems());
    }

    @Override
    public void renderBG(@Nonnull EasyGuiGraphics gui) {
        gui.resetColor();
        for (SimpleSlot slot : this.commonTab.getSlots()) {
            gui.blit(TraderScreen.GUI_TEXTURE, slot.f_40220_ - 1, slot.f_40221_ - 1, 206, 0, 18, 18);
        }
        gui.drawString(LCText.GUI_TRADER_AUCTION_ITEMS.get(), 22, 112, 4210752);
        if (this.locked && this.successTime != 0L) {
            TextRenderUtil.drawCenteredText(gui, LCText.GUI_TRADER_AUCTION_CREATE_SUCCESS.getWithStyle(ChatFormatting.BOLD), this.screen.getXSize() / 2, 34, 4210752);
        }
        if (LCAdminMode.isAdminPlayer(this.screen.getPlayer())) {
            gui.drawString(LCText.GUI_PERSISTENT_ID.get(), 0, -35, 16777215);
        }
    }

    @Override
    public void tick() {
        if (this.locked && !this.priceSelect.isLocked()) {
            this.priceSelect.lock();
        }
        if (this.locked && this.successTime != 0L && TimeUtil.compareTime(5000L, this.successTime)) {
            this.screen.changeTab(0);
        } else {
            if (this.locked) {
                this.buttonTogglePriceMode.f_93623_ = this.buttonSubmitAuction.f_93623_ = false;
            } else {
                this.buttonTogglePriceMode.f_93623_ = true;
                this.buttonSubmitAuction.f_93623_ = this.pendingAuction.isValid();
            }
            if (LCAdminMode.isAdminPlayer(this.screen.getPlayer())) {
                this.buttonSubmitPersistentAuction.f_93624_ = this.persistentAuctionIDInput.f_93624_ = !this.locked;
                this.buttonSubmitPersistentAuction.f_93623_ = this.pendingAuction.isValid();
                this.persistentAuctionIDInput.tick();
            } else {
                this.buttonSubmitPersistentAuction.f_93624_ = this.persistentAuctionIDInput.f_93624_ = false;
            }
        }
    }

    private void UpdateAuctionItems() {
        this.pendingAuction.setAuctionItems(this.commonTab.getAuctionItems());
    }

    private void onPriceChanged(MoneyValue newPrice) {
        if (this.startingBidMode) {
            this.pendingAuction.setStartingBid(newPrice);
        } else {
            this.pendingAuction.setMinBidDifferent(newPrice);
        }
    }

    private void TogglePriceTarget() {
        this.startingBidMode = !this.startingBidMode;
        if (this.startingBidMode) {
            this.priceSelect.changeValue(this.pendingAuction.getLastBidAmount());
        } else {
            this.priceSelect.changeValue(this.pendingAuction.getMinBidDifference());
        }
    }

    private Component getBidModeText() {
        return this.startingBidMode ? LCText.BUTTON_TRADER_AUCTION_PRICE_MODE_STARTING_BID.get() : LCText.BUTTON_TRADER_AUCTION_PRICE_MODE_MIN_BID_SIZE.get();
    }

    private void updateDuration(TimeUtil.TimeData newTime) {
        this.pendingAuction.setDuration(newTime.miliseconds);
    }

    private void submitAuction() {
        this.commonTab.createAuction(this.pendingAuction);
        this.locked = true;
        for (SimpleSlot slot : this.commonTab.getSlots()) {
            slot.locked = true;
        }
    }

    private void submitPersistentAuction(EasyButton button) {
        new CPacketCreatePersistentAuction(this.pendingAuction.getAsNBT(), this.persistentAuctionIDInput.getValue()).send();
    }

    @Override
    public void receiveServerMessage(LazyPacketData message) {
        if (message.contains("AuctionCreated")) {
            if (message.getBoolean("AuctionCreated")) {
                this.successTime = TimeUtil.getCurrentTime();
            } else {
                this.locked = false;
                for (SimpleSlot slot : this.commonTab.getSlots()) {
                    slot.locked = false;
                }
            }
        }
    }
}