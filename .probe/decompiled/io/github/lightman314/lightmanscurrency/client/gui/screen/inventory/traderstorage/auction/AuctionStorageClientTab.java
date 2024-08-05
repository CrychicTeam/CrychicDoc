package io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.traderstorage.auction;

import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.api.misc.client.rendering.EasyGuiGraphics;
import io.github.lightman314.lightmanscurrency.api.traders.menu.storage.TraderStorageClientTab;
import io.github.lightman314.lightmanscurrency.client.gui.easy.EasyScreenHelper;
import io.github.lightman314.lightmanscurrency.client.gui.easy.interfaces.IMouseListener;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.TraderScreen;
import io.github.lightman314.lightmanscurrency.client.gui.widget.ScrollListener;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.icon.IconButton;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.icon.IconData;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyButton;
import io.github.lightman314.lightmanscurrency.client.gui.widget.scroll.IScrollable;
import io.github.lightman314.lightmanscurrency.client.gui.widget.scroll.ScrollBarWidget;
import io.github.lightman314.lightmanscurrency.client.util.IconAndButtonUtil;
import io.github.lightman314.lightmanscurrency.client.util.ScreenArea;
import io.github.lightman314.lightmanscurrency.client.util.ScreenPosition;
import io.github.lightman314.lightmanscurrency.client.util.TextRenderUtil;
import io.github.lightman314.lightmanscurrency.common.menus.traderstorage.auction.AuctionStorageTab;
import io.github.lightman314.lightmanscurrency.common.traders.auction.AuctionHouseTrader;
import io.github.lightman314.lightmanscurrency.common.traders.auction.AuctionPlayerStorage;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;

public class AuctionStorageClientTab extends TraderStorageClientTab<AuctionStorageTab> implements IScrollable, IMouseListener {

    private static final int X_OFFSET = 13;

    private static final int Y_OFFSET = 17;

    private static final int COLUMNS = 10;

    private static final int ROWS = 4;

    int scroll = 0;

    ScrollBarWidget scrollBar;

    EasyButton buttonCollectItems;

    IconButton buttonCollectMoney;

    public AuctionStorageClientTab(Object screen, AuctionStorageTab commonTab) {
        super(screen, commonTab);
    }

    @Nonnull
    @Override
    public IconData getIcon() {
        return IconAndButtonUtil.ICON_STORAGE;
    }

    public MutableComponent getTooltip() {
        return LCText.TOOLTIP_TRADER_AUCTION_STORAGE.get();
    }

    @Override
    public void initialize(ScreenArea screenArea, boolean firstOpen) {
        this.scrollBar = this.addChild(new ScrollBarWidget(screenArea.pos.offset(193, 17), 72, this));
        this.buttonCollectItems = this.addChild(IconAndButtonUtil.quickExtractButton(screenArea.pos.offset(11, 97), b -> this.commonTab.quickTransfer()));
        this.buttonCollectMoney = this.addChild(new IconButton(screenArea.pos.offset(25, 118), b -> this.commonTab.collectCoins(), IconAndButtonUtil.ICON_COLLECT_COINS));
        this.addChild(new ScrollListener(screenArea.pos, screenArea.width, 118, this));
    }

    @Override
    public void renderBG(@Nonnull EasyGuiGraphics gui) {
        gui.drawString(LCText.TOOLTIP_TRADER_AUCTION_STORAGE.get(), 8, 6, 4210752);
        if (this.menu.getTrader() instanceof AuctionHouseTrader trader) {
            this.validateScroll();
            int index = this.scroll * 10;
            AuctionPlayerStorage storage = trader.getStorage(this.screen.getPlayer());
            if (storage != null) {
                List<ItemStack> storedItems = storage.getStoredItems();
                int hoverSlot = this.isMouseOverSlot(gui.mousePos) + this.scroll * 10;
                for (int y = 0; y < 4 && index < storedItems.size(); y++) {
                    int yPos = 17 + y * 18;
                    for (int x = 0; x < 10 && index < storedItems.size(); x++) {
                        int xPos = 13 + x * 18;
                        gui.resetColor();
                        gui.blit(TraderScreen.GUI_TEXTURE, xPos, yPos, 206, 0, 18, 18);
                        if (index < storedItems.size()) {
                            gui.renderItem((ItemStack) storedItems.get(index), xPos + 1, yPos + 1);
                        }
                        if (index == hoverSlot) {
                            gui.renderSlotHighlight(xPos + 1, yPos + 1);
                        }
                        index++;
                    }
                }
                if (storedItems.isEmpty()) {
                    TextRenderUtil.drawCenteredMultilineText(gui, LCText.GUI_TRADER_AUCTION_STORAGE_ITEMS_NONE.get(), 10, this.screen.getXSize() - 20, 49, 4210752);
                }
                this.buttonCollectItems.f_93623_ = !storedItems.isEmpty();
                if (!storage.getStoredCoins().isEmpty()) {
                    this.buttonCollectMoney.f_93623_ = true;
                    gui.drawString(LCText.GUI_TRADER_AUCTION_STORAGE_MONEY.get(storage.getStoredCoins().getRandomValueText()), 50, 118, 4210752);
                } else {
                    this.buttonCollectMoney.f_93623_ = false;
                    gui.drawString(LCText.GUI_TRADER_AUCTION_STORAGE_MONEY_NONE.get(), 50, 118, 4210752);
                }
            }
        }
    }

    @Override
    public void renderAfterWidgets(@Nonnull EasyGuiGraphics gui) {
        if (this.menu.getTrader() instanceof AuctionHouseTrader ah && this.screen.getMenu().getHeldItem().isEmpty()) {
            int hoveredSlot = this.isMouseOverSlot(gui.mousePos);
            if (hoveredSlot >= 0) {
                hoveredSlot += this.scroll * 10;
                AuctionPlayerStorage storage = ah.getStorage(this.screen.getPlayer());
                if (hoveredSlot < storage.getStoredItems().size()) {
                    ItemStack stack = (ItemStack) storage.getStoredItems().get(hoveredSlot);
                    gui.renderComponentTooltip(EasyScreenHelper.getTooltipFromItem(stack));
                }
            }
        }
    }

    private int isMouseOverSlot(ScreenPosition mousePos) {
        int foundColumn = -1;
        int foundRow = -1;
        int leftEdge = this.screen.getGuiLeft() + 13;
        int topEdge = this.screen.getGuiTop() + 17;
        for (int x = 0; x < 10 && foundColumn < 0; x++) {
            if (mousePos.x >= leftEdge + x * 18 && mousePos.x < leftEdge + x * 18 + 18) {
                foundColumn = x;
            }
        }
        for (int y = 0; y < 4 && foundRow < 0; y++) {
            if (mousePos.y >= topEdge + y * 18 && mousePos.y < topEdge + y * 18 + 18) {
                foundRow = y;
            }
        }
        return foundColumn >= 0 && foundRow >= 0 ? foundRow * 10 + foundColumn : -1;
    }

    private int totalStorageSlots() {
        return this.menu.getTrader() instanceof AuctionHouseTrader ah ? ah.getStorage(this.screen.getPlayer()).getStoredItems().size() : 0;
    }

    @Override
    public boolean onMouseClicked(double mouseX, double mouseY, int button) {
        if (this.menu.getTrader() instanceof AuctionHouseTrader) {
            int hoveredSlot = this.isMouseOverSlot(ScreenPosition.of(mouseX, mouseY));
            if (hoveredSlot >= 0) {
                hoveredSlot += this.scroll * 10;
                this.commonTab.clickedOnSlot(hoveredSlot, Screen.hasShiftDown());
                return true;
            }
        }
        return false;
    }

    @Override
    public int currentScroll() {
        return this.scroll;
    }

    @Override
    public void setScroll(int newScroll) {
        this.scroll = newScroll;
        this.validateScroll();
    }

    @Override
    public int getMaxScroll() {
        return Math.max((this.totalStorageSlots() - 1) / 10 - 4 + 1, 0);
    }
}