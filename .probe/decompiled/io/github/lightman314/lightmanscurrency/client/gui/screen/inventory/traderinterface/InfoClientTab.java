package io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.traderinterface;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.api.misc.client.rendering.EasyGuiGraphics;
import io.github.lightman314.lightmanscurrency.api.money.bank.IBankAccount;
import io.github.lightman314.lightmanscurrency.api.trader_interface.blockentity.TraderInterfaceBlockEntity;
import io.github.lightman314.lightmanscurrency.api.trader_interface.menu.TraderInterfaceClientTab;
import io.github.lightman314.lightmanscurrency.api.traders.TradeResult;
import io.github.lightman314.lightmanscurrency.api.traders.TraderData;
import io.github.lightman314.lightmanscurrency.api.traders.trade.TradeData;
import io.github.lightman314.lightmanscurrency.api.traders.trade.comparison.TradeComparisonResult;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.TraderInterfaceScreen;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.icon.IconButton;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.icon.IconData;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.trade.TradeButton;
import io.github.lightman314.lightmanscurrency.client.gui.widget.dropdown.DropdownWidget;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyAddonHelper;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyButton;
import io.github.lightman314.lightmanscurrency.client.util.IconAndButtonUtil;
import io.github.lightman314.lightmanscurrency.client.util.ScreenArea;
import io.github.lightman314.lightmanscurrency.client.util.TextRenderUtil;
import io.github.lightman314.lightmanscurrency.common.menus.TraderInterfaceMenu;
import io.github.lightman314.lightmanscurrency.common.menus.traderinterface.base.InfoTab;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

public class InfoClientTab extends TraderInterfaceClientTab<InfoTab> {

    TradeButton tradeDisplay;

    TradeButton newTradeDisplay;

    DropdownWidget interactionDropdown;

    EasyButton acceptChangesButton;

    private final ScreenArea WARNING_AREA = ScreenArea.of(45, 69, 16, 16);

    public InfoClientTab(TraderInterfaceScreen screen, InfoTab tab) {
        super(screen, tab);
    }

    @Nonnull
    @NotNull
    @Override
    public IconData getIcon() {
        return IconData.of(Items.PAPER);
    }

    public MutableComponent getTooltip() {
        return LCText.TOOLTIP_INTERFACE_INFO.get();
    }

    @Override
    public void initialize(ScreenArea screenArea, boolean firstOpen) {
        this.tradeDisplay = this.addChild(new TradeButton(this.menu::getTradeContext, ((TraderInterfaceMenu) this.screen.m_6262_()).getBE()::getReferencedTrade, TradeButton.NULL_PRESS));
        this.tradeDisplay.setPosition(screenArea.pos.offset(6, 47));
        this.tradeDisplay.displayOnly = true;
        this.newTradeDisplay = this.addChild(new TradeButton(this.menu::getTradeContext, ((TraderInterfaceMenu) this.screen.m_6262_()).getBE()::getTrueTrade, TradeButton.NULL_PRESS));
        this.newTradeDisplay.setPosition(screenArea.pos.offset(6, 91));
        this.newTradeDisplay.f_93624_ = false;
        this.newTradeDisplay.displayOnly = true;
        this.interactionDropdown = this.addChild(IconAndButtonUtil.interactionTypeDropdown(screenArea.pos.offset(104, 25), 97, ((TraderInterfaceMenu) this.screen.m_6262_()).getBE().getInteractionType(), this::onInteractionSelect, this.menu.getBE().getBlacklistedInteractions()));
        this.acceptChangesButton = this.addChild(new IconButton(screenArea.pos.offset(181, 90), this::AcceptTradeChanges, IconAndButtonUtil.ICON_CHECKMARK).withAddons(EasyAddonHelper.tooltip(LCText.TOOLTIP_INTERFACE_INFO_ACCEPT_CHANGES.get())));
        this.acceptChangesButton.f_93624_ = false;
    }

    private List<Component> getWarningMessages() {
        if (this.menu.getBE() == null) {
            return new ArrayList();
        } else {
            List<Component> list = new ArrayList();
            TradeResult result = this.menu.getBE().mostRecentTradeResult();
            Component message = result.getMessage();
            if (message != null) {
                list.add(message);
            }
            if (this.menu.getBE().getInteractionType().trades) {
                TradeData referencedTrade = this.menu.getBE().getReferencedTrade();
                TradeData trueTrade = this.menu.getBE().getTrueTrade();
                if (referencedTrade == null) {
                    return new ArrayList();
                } else if (trueTrade == null) {
                    list.add(LCText.GUI_TRADE_DIFFERENCE_MISSING.getWithStyle(ChatFormatting.RED));
                    return list;
                } else {
                    TradeComparisonResult differences = trueTrade.compare(referencedTrade);
                    if (!differences.TypeMatches()) {
                        list.add(LCText.GUI_TRADE_DIFFERENCE_TYPE.getWithStyle(ChatFormatting.RED));
                        return list;
                    } else {
                        list.addAll(trueTrade.GetDifferenceWarnings(differences));
                        return list;
                    }
                }
            } else {
                if (this.menu.getBE().getInteractionType().requiresPermissions) {
                    TraderData trader = this.menu.getBE().getTrader();
                    if (trader != null && !trader.hasPermission(this.menu.getBE().getReferencedPlayer(), "interactionLink")) {
                        list.add(LCText.GUI_INTERFACE_INFO_MISSING_PERMISSIONS.getWithStyle(ChatFormatting.RED));
                    }
                }
                return list;
            }
        }
    }

    @Override
    public void renderBG(@Nonnull EasyGuiGraphics gui) {
        if (this.menu.getBE() != null) {
            gui.drawString(this.menu.getBE().m_58900_().m_60734_().getName(), 8, 6, 4210752);
            TraderData trader = this.menu.getBE().getTrader();
            Component infoText;
            if (trader != null) {
                infoText = trader.getTitle();
            } else if (this.menu.getBE().hasTrader()) {
                infoText = LCText.GUI_INTERFACE_INFO_TRADER_REMOVED.getWithStyle(ChatFormatting.RED);
            } else {
                infoText = LCText.GUI_INTERFACE_INFO_TRADER_NULL.get();
            }
            gui.drawString(TextRenderUtil.fitString(infoText, this.screen.getXSize() - 16), 8, 16, 4210752);
            this.tradeDisplay.f_93624_ = this.menu.getBE().getInteractionType().trades;
            this.newTradeDisplay.f_93624_ = this.tradeDisplay.f_93624_ && this.changeInTrades();
            this.acceptChangesButton.f_93624_ = this.newTradeDisplay.f_93624_;
            if (this.tradeDisplay.f_93624_ && this.menu.getBE().getReferencedTrade() == null) {
                gui.drawString(LCText.GUI_INTERFACE_INFO_TRADE_NOT_DEFINED.get(), 6, 40, 4210752);
            }
            if (this.newTradeDisplay.f_93624_) {
                gui.resetColor();
                gui.blit(TraderInterfaceScreen.GUI_TEXTURE, this.tradeDisplay.m_5711_() / 2 - 2, 67, 206, 18, 16, 22);
                if (this.menu.getBE().getTrueTrade() == null) {
                    gui.drawString(LCText.GUI_INTERFACE_INFO_TRADE_MISSING.getWithStyle(ChatFormatting.RED), 6, 109 - 9, 4210752);
                }
            }
            IBankAccount account = this.menu.getBE().getBankAccount();
            if (account != null && this.menu.getBE().getInteractionType().trades) {
                Component accountName = TextRenderUtil.fitString(account.getName(), 160);
                gui.drawString(accountName, 103 - gui.font.width(accountName) / 2, 120, 4210752);
                Component balanceText = account.getBalanceText();
                gui.drawString(balanceText, 103 - gui.font.width(balanceText) / 2, 130, 4210752);
            }
            if (!this.getWarningMessages().isEmpty()) {
                RenderSystem.setShaderTexture(0, TraderInterfaceScreen.GUI_TEXTURE);
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                gui.resetColor();
                gui.blit(TraderInterfaceScreen.GUI_TEXTURE, this.WARNING_AREA.x, this.WARNING_AREA.y, 206, 40, 16, 16);
            }
        }
    }

    public boolean changeInTrades() {
        TradeData referencedTrade = this.menu.getBE().getReferencedTrade();
        TradeData trueTrade = this.menu.getBE().getTrueTrade();
        if (referencedTrade == null) {
            return false;
        } else {
            return trueTrade == null ? true : !referencedTrade.compare(trueTrade).Identical();
        }
    }

    @Override
    public void renderAfterWidgets(@Nonnull EasyGuiGraphics gui) {
        if (this.menu.getBE() != null) {
            if (this.WARNING_AREA.atPosition(this.WARNING_AREA.pos.offset(this.screen)).isMouseInArea(gui.mousePos)) {
                List<Component> warnings = this.getWarningMessages();
                if (!warnings.isEmpty()) {
                    gui.renderComponentTooltip(warnings);
                }
            }
        }
    }

    private void onInteractionSelect(int newTypeIndex) {
        TraderInterfaceBlockEntity.InteractionType newType = TraderInterfaceBlockEntity.InteractionType.fromIndex(newTypeIndex);
        this.commonTab.changeInteractionType(newType);
    }

    private void AcceptTradeChanges(EasyButton button) {
        this.commonTab.acceptTradeChanges();
    }
}