package io.github.lightman314.lightmanscurrency.common.traders.item.tradedata.client;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.api.misc.EasyText;
import io.github.lightman314.lightmanscurrency.api.traders.TradeContext;
import io.github.lightman314.lightmanscurrency.api.traders.trade.client.TradeRenderManager;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.trade.AlertData;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.trade.DisplayData;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.trade.DisplayEntry;
import io.github.lightman314.lightmanscurrency.client.util.ScreenPosition;
import io.github.lightman314.lightmanscurrency.common.menus.slots.easy.EasySlot;
import io.github.lightman314.lightmanscurrency.common.traders.item.ItemTraderData;
import io.github.lightman314.lightmanscurrency.common.traders.item.tradedata.ItemTradeData;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.LazyOptional;

@OnlyIn(Dist.CLIENT)
public class ItemTradeButtonRenderer extends TradeRenderManager<ItemTradeData> {

    public static final ResourceLocation NBT_SLOT = new ResourceLocation("lightmanscurrency", "item/empty_nbt_highlight");

    public static final Pair<ResourceLocation, ResourceLocation> NBT_BACKGROUND = Pair.of(InventoryMenu.BLOCK_ATLAS, NBT_SLOT);

    public ItemTradeButtonRenderer(ItemTradeData trade) {
        super(trade);
    }

    @Override
    public int tradeButtonWidth(TradeContext context) {
        return 94;
    }

    @Override
    public LazyOptional<ScreenPosition> arrowPosition(TradeContext context) {
        return ScreenPosition.ofOptional(36, 1);
    }

    @Override
    public DisplayData inputDisplayArea(TradeContext context) {
        return new DisplayData(1, 1, 34, 16);
    }

    @Override
    public List<DisplayEntry> getInputDisplays(TradeContext context) {
        if (this.trade.isSale()) {
            return Lists.newArrayList(new DisplayEntry[] { DisplayEntry.of(this.trade.getCost(context), context.isStorageMode ? LCText.TOOLTIP_TRADE_EDIT_PRICE.getAsList() : null) });
        } else if (this.trade.isPurchase()) {
            return this.getSaleItemEntries(context);
        } else {
            return (List<DisplayEntry>) (this.trade.isBarter() ? this.getBarterItemEntries(context) : new ArrayList());
        }
    }

    @Override
    public DisplayData outputDisplayArea(TradeContext context) {
        return new DisplayData(59, 1, 34, 16);
    }

    @Override
    public List<DisplayEntry> getOutputDisplays(TradeContext context) {
        if (this.trade.isSale() || this.trade.isBarter()) {
            return this.getSaleItemEntries(context);
        } else {
            return this.trade.isPurchase() ? Lists.newArrayList(new DisplayEntry[] { DisplayEntry.of(this.trade.getCost(context), context.isStorageMode ? LCText.TOOLTIP_TRADE_EDIT_PRICE.getAsList() : null) }) : new ArrayList();
        }
    }

    private List<DisplayEntry> getSaleItemEntries(TradeContext context) {
        List<DisplayEntry> entries = new ArrayList();
        for (int i = 0; i < 2; i++) {
            ItemStack item = this.trade.getSellItem(i);
            if (!item.isEmpty()) {
                entries.add(DisplayEntry.of(item, item.getCount(), this.getSaleItemTooltip(item, this.trade.getCustomName(i), this.trade.getEnforceNBT(i), context), this.getNBTHightlight(this.trade.getEnforceNBT(i))));
            } else if (context.isStorageMode) {
                entries.add(DisplayEntry.of(this.trade.getRestriction().getEmptySlotBG(), LCText.TOOLTIP_TRADE_ITEM_EDIT_ITEM.getAsList()));
            }
        }
        return entries;
    }

    private Consumer<List<Component>> getSaleItemTooltip(ItemStack stack, String customName, boolean enforceNBT, TradeContext context) {
        return tooltips -> {
            Component originalName = null;
            if (!customName.isEmpty() && (this.trade.isSale() || this.trade.isBarter())) {
                originalName = stack.getHoverName();
                tooltips.set(0, EasyText.literal(customName).withStyle(ChatFormatting.GOLD));
            }
            this.addNBTWarning(tooltips, this.trade.isPurchase(), enforceNBT);
            if (!context.isStorageMode || originalName != null) {
                tooltips.add(LCText.TOOLTIP_TRADE_INFO_TITLE.getWithStyle(ChatFormatting.GOLD));
                if (originalName != null) {
                    tooltips.add(LCText.TOOLTIP_TRADE_INFO_ORIGINAL_NAME.get(originalName).withStyle(ChatFormatting.GOLD));
                }
                if (context.hasTrader() && context.hasPlayerReference() && context.getTrader() instanceof ItemTraderData trader) {
                    tooltips.add(this.getStockTooltip(trader.isCreative(), this.trade.getStock(context)));
                }
            }
        };
    }

    private List<DisplayEntry> getBarterItemEntries(TradeContext context) {
        List<DisplayEntry> entries = new ArrayList();
        for (int i = 0; i < 2; i++) {
            ItemStack item = this.trade.getBarterItem(i);
            if (!item.isEmpty()) {
                entries.add(DisplayEntry.of(item, item.getCount(), this.getBarterTooltips(this.trade.getEnforceNBT(i + 2)), this.getNBTHightlight(this.trade.getEnforceNBT(i + 2))));
            } else if (context.isStorageMode) {
                entries.add(DisplayEntry.of(EasySlot.BACKGROUND, LCText.TOOLTIP_TRADE_ITEM_EDIT_ITEM.getAsList()));
            }
        }
        return entries;
    }

    private Pair<ResourceLocation, ResourceLocation> getNBTHightlight(boolean enforceNBT) {
        return enforceNBT ? null : NBT_BACKGROUND;
    }

    private void addNBTWarning(@Nonnull List<Component> tooltips, boolean purchase, boolean enforceNBT) {
        if (!enforceNBT) {
            tooltips.add(0, (purchase ? LCText.TOOLTIP_TRADE_ITEM_NBT_WARNING_PURCHASE.get() : LCText.TOOLTIP_TRADE_ITEM_NBT_WARNING_SALE.get()).withStyle(ChatFormatting.DARK_PURPLE, ChatFormatting.BOLD));
        }
    }

    private Consumer<List<Component>> getBarterTooltips(boolean enforceNBT) {
        return tooltips -> this.addNBTWarning(tooltips, true, enforceNBT);
    }

    @Override
    protected void getAdditionalAlertData(TradeContext context, List<AlertData> alerts) {
        if (context.hasTrader() && context.getTrader() instanceof ItemTraderData trader) {
            if (!trader.isCreative()) {
                if (this.trade.outOfStock(context)) {
                    alerts.add(AlertData.warn(LCText.TOOLTIP_OUT_OF_STOCK));
                }
                if (this.trade.isPurchase() && !this.trade.hasSpace(trader, context.getCollectableItems(this.trade.getItemRequirement(0), this.trade.getItemRequirement(1)))) {
                    alerts.add(AlertData.warn(LCText.TOOLTIP_OUT_OF_SPACE));
                }
                if (this.trade.isBarter() && !this.trade.hasSpace(trader, context.getCollectableItems(this.trade.getItemRequirement(2), this.trade.getItemRequirement(3)))) {
                    alerts.add(AlertData.warn(LCText.TOOLTIP_OUT_OF_SPACE));
                }
            }
            if (!this.trade.canAfford(context)) {
                alerts.add(AlertData.warn(LCText.TOOLTIP_CANNOT_AFFORD));
            }
        }
    }
}