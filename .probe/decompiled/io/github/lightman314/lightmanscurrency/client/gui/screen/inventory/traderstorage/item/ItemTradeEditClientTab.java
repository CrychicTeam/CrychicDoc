package io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.traderstorage.item;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.api.misc.EasyText;
import io.github.lightman314.lightmanscurrency.api.misc.client.rendering.EasyGuiGraphics;
import io.github.lightman314.lightmanscurrency.api.money.input.MoneyValueWidget;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyValue;
import io.github.lightman314.lightmanscurrency.api.network.LazyPacketData;
import io.github.lightman314.lightmanscurrency.api.traders.TraderData;
import io.github.lightman314.lightmanscurrency.api.traders.menu.storage.TraderStorageClientTab;
import io.github.lightman314.lightmanscurrency.api.traders.trade.TradeData;
import io.github.lightman314.lightmanscurrency.client.gui.easy.interfaces.IMouseListener;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.TraderScreen;
import io.github.lightman314.lightmanscurrency.client.gui.widget.ItemEditWidget;
import io.github.lightman314.lightmanscurrency.client.gui.widget.TradeButtonArea;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.PlainButton;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.icon.IconData;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.trade.TradeButton;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyButton;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyTextButton;
import io.github.lightman314.lightmanscurrency.client.gui.widget.scroll.ScrollBarWidget;
import io.github.lightman314.lightmanscurrency.client.util.IconAndButtonUtil;
import io.github.lightman314.lightmanscurrency.client.util.ScreenArea;
import io.github.lightman314.lightmanscurrency.common.menus.traderstorage.item.ItemTradeEditTab;
import io.github.lightman314.lightmanscurrency.common.traders.item.tradedata.ItemTradeData;
import javax.annotation.Nonnull;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;

public class ItemTradeEditClientTab extends TraderStorageClientTab<ItemTradeEditTab> implements TradeButtonArea.InteractionConsumer, ItemEditWidget.IItemEditListener, IMouseListener {

    private static final int X_OFFSET = 13;

    private static final int Y_OFFSET = 71;

    private static final int COLUMNS = 10;

    private static final int ROWS = 2;

    TradeButton tradeDisplay;

    MoneyValueWidget priceSelection;

    EditBox customNameInput;

    ItemEditWidget itemEdit = null;

    ScrollBarWidget itemEditScroll;

    EasyButton buttonToggleTradeType;

    PlainButton buttonToggleNBTEnforcement;

    private int selection = -1;

    public ItemTradeEditClientTab(Object screen, ItemTradeEditTab commonTab) {
        super(screen, commonTab);
    }

    @Nonnull
    @Override
    public IconData getIcon() {
        return IconData.BLANK;
    }

    public MutableComponent getTooltip() {
        return EasyText.empty();
    }

    @Override
    public boolean tabButtonVisible() {
        return false;
    }

    @Override
    public boolean blockInventoryClosing() {
        return true;
    }

    @Override
    public int getTradeRuleTradeIndex() {
        return this.commonTab.getTradeIndex();
    }

    @Override
    public void initialize(ScreenArea screenArea, boolean firstOpen) {
        this.addChild(this);
        ItemTradeData trade = this.getTrade();
        this.tradeDisplay = this.addChild(new TradeButton(this.menu::getContext, this.commonTab::getTrade, button -> {
        }));
        this.tradeDisplay.setPosition(screenArea.pos.offset(10, 18));
        this.priceSelection = this.addChild(new MoneyValueWidget(screenArea.pos.offset(15, 40), firstOpen ? null : this.priceSelection, trade == null ? MoneyValue.empty() : trade.getCost(), this::onValueChanged));
        this.priceSelection.drawBG = false;
        this.itemEdit = this.addChild(new ItemEditWidget(screenArea.pos.offset(13, 71), 10, 2, this.itemEdit, this));
        this.itemEditScroll = this.addChild(new ScrollBarWidget(screenArea.pos.offset(193, 71), 36, this.itemEdit));
        this.itemEditScroll.smallKnob = true;
        int labelWidth = this.getFont().width(LCText.GUI_NAME.get());
        this.customNameInput = this.addChild(new EditBox(this.getFont(), screenArea.x + 15 + labelWidth, screenArea.y + 38, screenArea.width - 28 - labelWidth, 18, EasyText.empty()));
        if (this.selection >= 0 && this.selection < 2 && trade != null) {
            this.customNameInput.setValue(trade.getCustomName(this.selection));
        }
        this.buttonToggleTradeType = this.addChild(new EasyTextButton(screenArea.pos.offset(113, 15), 80, 20, EasyText.empty(), this::ToggleTradeType));
        this.buttonToggleNBTEnforcement = this.addChild(IconAndButtonUtil.checkmarkButton(screenArea.pos.offset(113, 4), this::ToggleNBTEnforcement, this::getEnforceNBTState));
    }

    private boolean getEnforceNBTState() {
        ItemTradeData trade = this.getTrade();
        return trade != null ? trade.getEnforceNBT(this.selection) : true;
    }

    @Override
    public void closeAction() {
        this.selection = -1;
    }

    @Override
    public void renderBG(@Nonnull EasyGuiGraphics gui) {
        if (this.getTrade() != null) {
            this.validateRenderables();
            RenderSystem.setShaderTexture(0, TraderScreen.GUI_TEXTURE);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            gui.resetColor();
            gui.blit(TraderScreen.GUI_TEXTURE, this.getArrowPosition(), 10, 214, 18, 8, 6);
            if (this.customNameInput.f_93624_) {
                gui.drawString(LCText.GUI_NAME.get(), 13, 42, 4210752);
            }
            if (this.isNBTButtonVisible()) {
                gui.drawString(LCText.GUI_TRADER_ITEM_ENFORCE_NBT.get(), 124, 5, 4210752);
            }
        }
    }

    private int getArrowPosition() {
        ItemTradeData trade = this.getTrade();
        if (this.selection == -1) {
            if (trade.isSale()) {
                return 25;
            } else {
                return trade.isPurchase() ? 81 : -1000;
            }
        } else if (this.selection >= 2 && !trade.isBarter()) {
            return -1000;
        } else {
            int horizSlot = this.selection;
            if (trade.isSale() || trade.isBarter()) {
                horizSlot += 2;
            }
            int spacing = horizSlot % 4 >= 2 ? 20 : 0;
            return 16 + 18 * (horizSlot % 4) + spacing;
        }
    }

    private void validateRenderables() {
        this.priceSelection.f_93624_ = this.selection < 0 && !this.getTrade().isBarter();
        this.itemEdit.f_93624_ = this.itemEditScroll.f_93624_ = this.getTrade().isBarter() && this.selection >= 2 || this.getTrade().isPurchase() && this.selection >= 0;
        this.customNameInput.f_93624_ = this.selection >= 0 && this.selection < 2 && !this.getTrade().isPurchase();
        if (this.customNameInput.f_93624_ && !this.customNameInput.getValue().contentEquals(this.getTrade().getCustomName(this.selection))) {
            this.commonTab.setCustomName(this.selection, this.customNameInput.getValue());
        }
        this.buttonToggleTradeType.m_93666_(LCText.GUI_TRADE_DIRECTION.get(this.getTrade().getTradeDirection()).get());
    }

    @Override
    public void tick() {
        if (this.customNameInput.f_93624_) {
            this.customNameInput.tick();
        }
        this.buttonToggleNBTEnforcement.f_93624_ = this.isNBTButtonVisible();
    }

    private boolean isNBTButtonVisible() {
        ItemTradeData trade = this.getTrade();
        return trade == null ? false : this.selection >= 0 && !trade.alwaysEnforcesNBT(this.selection);
    }

    @Override
    public void receiveSelfMessage(LazyPacketData message) {
        if (message.contains("TradeIndex")) {
            this.commonTab.setTradeIndex(message.getInt("TradeIndex"));
        }
        if (message.contains("StartingSlot")) {
            this.selection = message.getInt("StartingSlot");
        }
    }

    @Override
    public void onTradeButtonInputInteraction(TraderData trader, TradeData trade, int index, int mouseButton) {
        if (trade instanceof ItemTradeData t) {
            ItemStack heldItem = this.menu.getHeldItem();
            if (t.isSale()) {
                this.changeSelection(-1);
            } else if (t.isPurchase()) {
                if (this.selection != index && heldItem.isEmpty()) {
                    this.changeSelection(index);
                } else {
                    this.commonTab.defaultInteraction(index, heldItem, mouseButton);
                }
            } else if (t.isBarter()) {
                if (this.selection != index + 2 && heldItem.isEmpty()) {
                    this.changeSelection(index + 2);
                } else {
                    this.commonTab.defaultInteraction(index + 2, heldItem, mouseButton);
                }
            }
        }
    }

    @Override
    public void onTradeButtonOutputInteraction(TraderData trader, TradeData trade, int index, int mouseButton) {
        if (trade instanceof ItemTradeData t) {
            ItemStack heldItem = this.menu.getHeldItem();
            if (!t.isSale() && !t.isBarter()) {
                if (t.isPurchase()) {
                    this.changeSelection(-1);
                }
            } else if (this.selection != index && heldItem.isEmpty()) {
                this.changeSelection(index);
            } else {
                this.commonTab.defaultInteraction(index, heldItem, mouseButton);
            }
        }
    }

    private void changeSelection(int newSelection) {
        this.selection = newSelection;
        if (this.selection == -1) {
            this.priceSelection.changeValue(this.getTrade().getCost());
        }
        if (this.selection >= 0 && this.selection < 2) {
            this.itemEdit.refreshSearch();
            this.customNameInput.setValue(this.commonTab.getTrade().getCustomName(this.selection));
        }
        if (this.selection >= 2) {
            this.itemEdit.refreshSearch();
        }
    }

    @Override
    public void onTradeButtonInteraction(TraderData trader, TradeData trade, int localMouseX, int localMouseY, int mouseButton) {
    }

    @Override
    public boolean onMouseClicked(double mouseX, double mouseY, int button) {
        this.tradeDisplay.onInteractionClick((int) mouseX, (int) mouseY, button, this);
        return false;
    }

    public void onValueChanged(MoneyValue value) {
        this.commonTab.setPrice(value);
    }

    @Override
    public ItemTradeData getTrade() {
        return this.commonTab.getTrade();
    }

    @Override
    public boolean restrictItemEditItems() {
        return this.selection < 2;
    }

    @Override
    public void onItemClicked(ItemStack item) {
        this.commonTab.setSelectedItem(this.selection, item);
    }

    private void ToggleTradeType(EasyButton button) {
        if (this.getTrade() != null) {
            this.commonTab.setType(ItemTradeData.getNextInCycle(this.getTrade().getTradeDirection()));
            this.itemEdit.refreshSearch();
        }
    }

    private void ToggleNBTEnforcement(EasyButton button) {
        if (this.getTrade() != null) {
            this.commonTab.setNBTEnforced(this.selection, !this.getTrade().getEnforceNBT(this.selection));
        }
    }
}