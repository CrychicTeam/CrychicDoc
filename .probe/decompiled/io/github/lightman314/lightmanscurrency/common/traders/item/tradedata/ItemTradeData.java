package io.github.lightman314.lightmanscurrency.common.traders.item.tradedata;

import com.google.common.collect.Lists;
import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.api.network.LazyPacketData;
import io.github.lightman314.lightmanscurrency.api.traders.TradeContext;
import io.github.lightman314.lightmanscurrency.api.traders.trade.TradeData;
import io.github.lightman314.lightmanscurrency.api.traders.trade.TradeDirection;
import io.github.lightman314.lightmanscurrency.api.traders.trade.client.TradeRenderManager;
import io.github.lightman314.lightmanscurrency.api.traders.trade.comparison.ProductComparisonResult;
import io.github.lightman314.lightmanscurrency.api.traders.trade.comparison.TradeComparisonResult;
import io.github.lightman314.lightmanscurrency.common.menus.traderstorage.trades_basic.BasicTradeEditTab;
import io.github.lightman314.lightmanscurrency.common.traders.item.ItemTraderData;
import io.github.lightman314.lightmanscurrency.common.traders.item.TraderItemStorage;
import io.github.lightman314.lightmanscurrency.common.traders.item.tradedata.client.ItemTradeButtonRenderer;
import io.github.lightman314.lightmanscurrency.common.traders.item.tradedata.restrictions.ItemTradeRestriction;
import io.github.lightman314.lightmanscurrency.util.InventoryUtil;
import io.github.lightman314.lightmanscurrency.util.ItemRequirement;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ItemTradeData extends TradeData {

    ItemTradeRestriction restriction = ItemTradeRestriction.NONE;

    SimpleContainer items = new SimpleContainer(4);

    final List<Boolean> enforceNBT = Lists.newArrayList(new Boolean[] { true, true, true, true });

    TradeDirection tradeType = TradeDirection.SALE;

    String customName1 = "";

    String customName2 = "";

    @Nonnull
    public static TradeDirection getNextInCycle(@Nonnull TradeDirection direction) {
        int index = direction.index + 1;
        if (index > TradeDirection.BARTER.index) {
            index = 0;
        }
        return TradeDirection.fromIndex(index);
    }

    public ItemTradeData(boolean validateRules) {
        super(validateRules);
        this.resetNBTList();
    }

    private void resetNBTList() {
        for (int i = 0; i < 4; i++) {
            this.enforceNBT.set(i, true);
        }
    }

    public ItemStack getSellItem(int index) {
        return index >= 0 && index < 2 ? this.restriction.modifySellItem(this.items.getItem(index).copy(), this.getCustomName(index), this) : ItemStack.EMPTY;
    }

    public List<ItemStack> getRandomSellItems(ItemTraderData trader) {
        return this.restriction.getRandomSellItems(trader, this);
    }

    public ItemStack getBarterItem(int index) {
        return index >= 0 && index < 2 ? this.items.getItem(index + 2).copy() : ItemStack.EMPTY;
    }

    public ItemStack getItem(int index) {
        if (index >= 0 && index < 2) {
            return this.getSellItem(index);
        } else {
            return index >= 2 && index < 4 ? this.getBarterItem(index - 2) : ItemStack.EMPTY;
        }
    }

    public void setItem(ItemStack itemStack, int index) {
        if (index < 0 || index >= 4) {
            LightmansCurrency.LogError("Cannot define the item trades item at index " + index + ". Must be between 0-3!");
        } else if (index < 2) {
            if (this.restriction.allowSellItem(itemStack) || itemStack.isEmpty()) {
                this.items.setItem(index, this.restriction.filterSellItem(itemStack).copy());
            }
        } else {
            this.items.setItem(index, itemStack.copy());
        }
    }

    public boolean alwaysEnforcesNBT(int slot) {
        return this.restriction.alwaysEnforceNBT(slot);
    }

    public boolean getEnforceNBT(int slot) {
        return slot >= 0 && slot < 4 ? (Boolean) this.enforceNBT.get(slot) || this.alwaysEnforcesNBT(slot) : true;
    }

    public void setEnforceNBT(int slot, boolean newValue) {
        if (slot >= 0 && slot < 4) {
            this.enforceNBT.set(slot, newValue || this.alwaysEnforcesNBT(slot));
        }
    }

    public ItemRequirement getItemRequirement(int slot) {
        if (slot < 0 || slot >= 4) {
            return ItemRequirement.NULL;
        } else {
            return this.getEnforceNBT(slot) ? ItemRequirement.of(this.getItem(slot)) : ItemRequirement.ofItemNoNBT(this.getItem(slot));
        }
    }

    public boolean allowItemInStorage(ItemStack item) {
        for (int i = 0; i < (this.isBarter() ? 4 : 2); i++) {
            if (this.getItemRequirement(i).test(item)) {
                return true;
            }
        }
        return this.restriction.allowExtraItemInStorage(item);
    }

    public boolean shouldStorageItemBeSaved(ItemStack item) {
        if ((this.isSale() || this.isBarter()) && this.isValid()) {
            for (int i = 0; i < 2; i++) {
                if (!this.getEnforceNBT(i) && this.getItemRequirement(i).test(item)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean hasCustomName(int index) {
        return !this.getCustomName(index).isEmpty();
    }

    public String getCustomName(int index) {
        return switch(index) {
            case 0 ->
                this.customName1;
            case 1 ->
                this.customName2;
            default ->
                "";
        };
    }

    public void setCustomName(int index, String customName) {
        switch(index) {
            case 0:
                this.customName1 = customName;
                break;
            case 1:
                this.customName2 = customName;
        }
    }

    @Override
    public TradeDirection getTradeDirection() {
        return this.tradeType;
    }

    public boolean isSale() {
        return this.tradeType == TradeDirection.SALE;
    }

    public boolean isPurchase() {
        return this.tradeType == TradeDirection.PURCHASE;
    }

    public boolean isBarter() {
        return this.tradeType == TradeDirection.BARTER;
    }

    public void setTradeType(TradeDirection tradeDirection) {
        this.tradeType = tradeDirection;
        this.validateRuleStates();
    }

    @Nonnull
    public ItemTradeRestriction getRestriction() {
        return this.restriction;
    }

    public void setRestriction(ItemTradeRestriction restriction) {
        this.restriction = restriction;
    }

    @Override
    public boolean isValid() {
        return this.tradeType == TradeDirection.BARTER ? this.sellItemsDefined() && this.barterItemsDefined() : super.isValid() && this.sellItemsDefined();
    }

    public boolean sellItemsDefined() {
        return !this.getSellItem(0).isEmpty() || !this.getSellItem(1).isEmpty();
    }

    public boolean barterItemsDefined() {
        return !this.getBarterItem(0).isEmpty() || !this.getBarterItem(1).isEmpty();
    }

    public boolean hasStock(ItemTraderData trader) {
        return !this.sellItemsDefined() ? false : this.stockCount(trader) > 0;
    }

    public boolean hasSpace(ItemTraderData trader, List<ItemStack> collectableItems) {
        return switch(this.tradeType) {
            case PURCHASE, BARTER ->
                trader.getStorage().canFitItems(collectableItems);
            default ->
                true;
        };
    }

    public int stockCount(ItemTraderData trader) {
        if (!this.sellItemsDefined()) {
            return 0;
        } else if (this.tradeType == TradeDirection.PURCHASE) {
            return this.stockCountOfCost(trader);
        } else {
            return this.tradeType != TradeDirection.SALE && this.tradeType != TradeDirection.BARTER ? 0 : this.restriction.getSaleStock(trader.getStorage(), this);
        }
    }

    @Override
    public int getStock(@Nonnull TradeContext context) {
        if (!this.sellItemsDefined()) {
            return 0;
        } else if (!context.hasTrader() || !(context.getTrader() instanceof ItemTraderData trader)) {
            return 0;
        } else if (trader.isCreative()) {
            return 1;
        } else if (this.tradeType == TradeDirection.PURCHASE) {
            return this.stockCountOfCost(context);
        } else {
            return this.tradeType != TradeDirection.SALE && this.tradeType != TradeDirection.BARTER ? 0 : this.restriction.getSaleStock(trader.getStorage(), this);
        }
    }

    public boolean canAfford(TradeContext context) {
        if (this.isSale()) {
            return context.hasFunds(this.getCost(context));
        } else if (this.isPurchase()) {
            return context.hasItems(this.getItemRequirement(0), this.getItemRequirement(1));
        } else {
            return this.isBarter() ? context.hasItems(this.getItemRequirement(2), this.getItemRequirement(3)) : false;
        }
    }

    public void RemoveItemsFromStorage(TraderItemStorage storage, List<ItemStack> soldItems) {
        this.restriction.removeItemsFromStorage(storage, soldItems);
    }

    @Override
    public CompoundTag getAsNBT() {
        CompoundTag tradeNBT = super.getAsNBT();
        InventoryUtil.saveAllItems("Items", tradeNBT, this.items);
        tradeNBT.putString("TradeDirection", this.tradeType.name());
        tradeNBT.putString("CustomName1", this.customName1);
        tradeNBT.putString("CustomName2", this.customName2);
        List<Integer> ignoreNBTSlots = new ArrayList();
        for (int i = 0; i < 4; i++) {
            if (!this.getEnforceNBT(i)) {
                ignoreNBTSlots.add(i);
            }
        }
        if (!ignoreNBTSlots.isEmpty()) {
            tradeNBT.putIntArray("IgnoreNBT", ignoreNBTSlots);
        }
        return tradeNBT;
    }

    public static void saveAllData(CompoundTag nbt, List<ItemTradeData> data) {
        saveAllData(nbt, data, "Trades");
    }

    public static void saveAllData(CompoundTag nbt, List<ItemTradeData> data, String key) {
        ListTag listNBT = new ListTag();
        for (ItemTradeData datum : data) {
            listNBT.add(datum.getAsNBT());
        }
        nbt.put(key, listNBT);
    }

    public static ItemTradeData loadData(CompoundTag compound, boolean validateRules) {
        ItemTradeData trade = new ItemTradeData(validateRules);
        trade.loadFromNBT(compound);
        return trade;
    }

    public static List<ItemTradeData> loadAllData(CompoundTag nbt, boolean validateRules) {
        return loadAllData("Trades", nbt, validateRules);
    }

    public static List<ItemTradeData> loadAllData(String key, CompoundTag compound, boolean validateRules) {
        List<ItemTradeData> data = new ArrayList();
        ListTag listNBT = compound.getList(key, 10);
        for (int i = 0; i < listNBT.size(); i++) {
            data.add(loadData(listNBT.getCompound(i), validateRules));
        }
        return data;
    }

    @Override
    public void loadFromNBT(CompoundTag nbt) {
        super.loadFromNBT(nbt);
        if (nbt.contains("Items", 9)) {
            this.items = InventoryUtil.loadAllItems("Items", nbt, 4);
        } else {
            this.items = new SimpleContainer(4);
            if (nbt.contains("SellItem", 10)) {
                this.items.setItem(0, ItemStack.of(nbt.getCompound("SellItem")));
            } else {
                this.items.setItem(0, ItemStack.of(nbt));
            }
            if (nbt.contains("BarterItem", 10)) {
                this.items.setItem(2, ItemStack.of(nbt.getCompound("BarterItem")));
            } else {
                this.items.setItem(2, ItemStack.EMPTY);
            }
        }
        if (nbt.contains("TradeDirection", 8)) {
            this.tradeType = loadTradeType(nbt.getString("TradeDirection"));
        } else {
            this.tradeType = TradeDirection.SALE;
        }
        if (nbt.contains("CustomName1")) {
            this.customName1 = nbt.getString("CustomName1");
        } else if (nbt.contains("CustomName")) {
            this.customName1 = nbt.getString("CustomName");
        } else {
            this.customName1 = "";
        }
        if (nbt.contains("CustomName2")) {
            this.customName2 = nbt.getString("CustomName2");
        } else {
            this.customName2 = "";
        }
        this.resetNBTList();
        if (nbt.contains("IgnoreNBT")) {
            for (int i : nbt.getIntArray("IgnoreNBT")) {
                if (i >= 0 && i < this.enforceNBT.size()) {
                    this.enforceNBT.set(i, false);
                }
            }
        }
    }

    public static TradeDirection loadTradeType(String name) {
        TradeDirection value = TradeDirection.SALE;
        try {
            value = TradeDirection.valueOf(name);
        } catch (IllegalArgumentException var3) {
            LightmansCurrency.LogError("Could not load '" + name + "' as a TradeDirection.");
        }
        return value;
    }

    public static List<ItemTradeData> listOfSize(int tradeCount, boolean validateRules) {
        List<ItemTradeData> data = Lists.newArrayList();
        while (data.size() < tradeCount) {
            data.add(new ItemTradeData(validateRules));
        }
        return data;
    }

    @Override
    public TradeComparisonResult compare(TradeData otherTrade) {
        TradeComparisonResult result = new TradeComparisonResult();
        if (otherTrade instanceof ItemTradeData otherItemTrade) {
            result.setCompatible();
            result.addProductResults(ProductComparisonResult.CompareTwoItems(this.getSellItem(0), this.getSellItem(1), otherItemTrade.getSellItem(0), otherItemTrade.getSellItem(1), this.compareNBT(otherItemTrade, 0)));
            if (this.isBarter()) {
                result.addProductResults(ProductComparisonResult.CompareTwoItems(this.getBarterItem(0), this.getBarterItem(1), otherItemTrade.getBarterItem(0), otherItemTrade.getBarterItem(1), this.compareNBT(otherItemTrade, 2)));
            }
            if (!this.isBarter()) {
                result.comparePrices(this.getCost(), otherTrade.getCost());
            }
            result.setTypeResult(this.tradeType == otherItemTrade.tradeType);
        }
        return result;
    }

    private boolean compareNBT(@Nonnull ItemTradeData otherItemTrade, int startingSlot) {
        for (int i = startingSlot; i < startingSlot + 2; i++) {
            ItemStack true1 = this.getItem(i);
            ItemStack expected1 = otherItemTrade.getItem(i);
            if ((!true1.isEmpty() || !expected1.isEmpty()) && ((Boolean) this.enforceNBT.get(i) || (Boolean) otherItemTrade.enforceNBT.get(i))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean AcceptableDifferences(TradeComparisonResult result) {
        if (result.TypeMatches() && result.isCompatible()) {
            if (result.getProductResultCount() < 2) {
                return false;
            } else {
                for (int i = 0; i < 2; i++) {
                    ProductComparisonResult sellResult = result.getProductResult(i);
                    if (!sellResult.SameProductType() || !sellResult.SameProductNBT()) {
                        return false;
                    }
                    if (!this.isSale() && !this.isBarter()) {
                        if (this.isPurchase() && sellResult.ProductQuantityDifference() > 0) {
                            return false;
                        }
                    } else if (sellResult.ProductQuantityDifference() < 0) {
                        return false;
                    }
                }
                if (this.isBarter()) {
                    if (result.getProductResultCount() < 4) {
                        return false;
                    }
                    for (int i = 0; i < 2; i++) {
                        ProductComparisonResult barterResult = result.getProductResult(i + 2);
                        if (!barterResult.SameProductType() || !barterResult.SameProductNBT()) {
                            return false;
                        }
                        if (barterResult.ProductQuantityDifference() > 0) {
                            return false;
                        }
                    }
                }
                return this.isSale() && result.isPriceExpensive() ? false : !this.isPurchase() || !result.isPriceCheaper();
            }
        } else {
            return false;
        }
    }

    @Override
    public List<Component> GetDifferenceWarnings(TradeComparisonResult differences) {
        List<Component> list = new ArrayList();
        if (!differences.PriceMatches()) {
            if (differences.PriceIncompatible()) {
                list.add(LCText.GUI_TRADE_DIFFERENCE_MONEY_TYPE.getWithStyle(ChatFormatting.RED));
            }
            if (this.isPurchase()) {
                if (differences.isPriceCheaper()) {
                    list.add(LCText.GUI_TRADE_DIFFERENCE_PURCHASE_CHEAPER.get(differences.priceDifference().getText()).withStyle(ChatFormatting.RED));
                } else {
                    list.add(LCText.GUI_TRADE_DIFFERENCE_PURCHASE_EXPENSIVE.get(differences.priceDifference().getText()).withStyle(ChatFormatting.GOLD));
                }
            } else if (differences.isPriceExpensive()) {
                list.add(LCText.GUI_TRADE_DIFFERENCE_EXPENSIVE.get(differences.priceDifference().getText()).withStyle(ChatFormatting.RED));
            } else {
                list.add(LCText.GUI_TRADE_DIFFERENCE_CHEAPER.get(differences.priceDifference().getText()).withStyle(ChatFormatting.GOLD));
            }
        }
        for (int i = 0; i < differences.getProductResultCount(); i++) {
            int slot = this.isPurchase() ? i + 2 : i;
            ChatFormatting moreColor = slot >= 2 ? ChatFormatting.RED : ChatFormatting.GOLD;
            ChatFormatting lessColor = slot >= 2 ? ChatFormatting.GOLD : ChatFormatting.RED;
            Component slotName = slot >= 2 ? LCText.GUI_TRADE_DIFFERENCE_ITEM_PURCHASING.get() : LCText.GUI_TRADE_DIFFERENCE_ITEM_SELLING.get();
            ProductComparisonResult productCheck = differences.getProductResult(i);
            if (!productCheck.SameProductType()) {
                list.add(LCText.GUI_TRADE_DIFFERENCE_ITEM_TYPE.get(slotName).withStyle(ChatFormatting.RED));
            } else if (!productCheck.SameProductNBT()) {
                list.add(LCText.GUI_TRADE_DIFFERENCE_ITEM_NBT.get(slotName).withStyle(ChatFormatting.RED));
            } else if (!productCheck.SameProductQuantity()) {
                int quantityDifference = productCheck.ProductQuantityDifference();
                if (quantityDifference > 0) {
                    list.add(LCText.GUI_TRADE_DIFFERENCE_ITEM_QUANTITY_MORE.get(slotName, quantityDifference).withStyle(moreColor));
                } else {
                    list.add(LCText.GUI_TRADE_DIFFERENCE_ITEM_QUANTITY_LESS.get(slotName, -quantityDifference).withStyle(lessColor));
                }
            }
        }
        return list;
    }

    @Nonnull
    @OnlyIn(Dist.CLIENT)
    @Override
    public TradeRenderManager<?> getButtonRenderer() {
        return new ItemTradeButtonRenderer(this);
    }

    @Override
    public void OnInputDisplayInteraction(@Nonnull BasicTradeEditTab tab, Consumer<LazyPacketData.Builder> clientHandler, int index, int button, @Nonnull ItemStack heldItem) {
        if (tab.menu.getTrader() instanceof ItemTraderData it) {
            int tradeIndex = it.indexOfTrade(this);
            if (tradeIndex < 0) {
                return;
            }
            if (this.isSale()) {
                tab.sendOpenTabMessage(2, LazyPacketData.builder().setInt("TradeIndex", tradeIndex).setInt("StartingSlot", -1));
            }
            if (this.isPurchase() && index >= 0 && index < 2) {
                ItemStack sellItem = this.getSellItem(index);
                if (sellItem.isEmpty() && heldItem.isEmpty()) {
                    tab.sendOpenTabMessage(2, LazyPacketData.builder().setInt("TradeIndex", tradeIndex).setInt("StartingSlot", index));
                } else if (InventoryUtil.ItemMatches(sellItem, heldItem) && button == 1) {
                    sellItem.setCount(Math.min(sellItem.getCount() + 1, sellItem.getMaxStackSize()));
                    this.setItem(sellItem, index);
                } else {
                    ItemStack setItem = heldItem.copy();
                    if (button == 1) {
                        setItem.setCount(1);
                    }
                    this.setItem(setItem, index);
                }
                if (tab.menu.isClient()) {
                    tab.sendInputInteractionMessage(tradeIndex, index, button, heldItem);
                }
            } else if (this.isBarter() && index >= 0 && index < 2) {
                ItemStack barterItem = this.getBarterItem(index);
                if (barterItem.isEmpty() && heldItem.isEmpty()) {
                    tab.sendOpenTabMessage(2, LazyPacketData.builder().setInt("TradeIndex", tradeIndex).setInt("StartingSlot", index + 2));
                }
                if (InventoryUtil.ItemMatches(barterItem, heldItem) && button == 1) {
                    barterItem.setCount(Math.min(barterItem.getCount() + 1, barterItem.getMaxStackSize()));
                    this.setItem(barterItem, index + 2);
                } else {
                    ItemStack setItem = heldItem.copy();
                    if (button == 1) {
                        setItem.setCount(1);
                    }
                    this.setItem(setItem, index + 2);
                }
                if (tab.menu.isClient()) {
                    tab.sendInputInteractionMessage(tradeIndex, index, button, heldItem);
                }
            }
        }
    }

    public void onSlotInteraction(int index, ItemStack heldItem, int button) {
        if (index < 2) {
            ItemStack sellItem = this.getSellItem(index);
            if (sellItem.isEmpty() && heldItem.isEmpty()) {
                return;
            }
            if (InventoryUtil.ItemMatches(sellItem, heldItem) && button == 1) {
                sellItem.setCount(Math.min(sellItem.getCount() + 1, sellItem.getMaxStackSize()));
                this.setItem(sellItem, index);
            } else {
                ItemStack setItem = heldItem.copy();
                if (button == 1) {
                    setItem.setCount(1);
                }
                this.setItem(setItem, index);
            }
        }
        if (this.isBarter() && index >= 2 && index < 4) {
            ItemStack barterItem = this.getItem(index);
            if (barterItem.isEmpty() && heldItem.isEmpty()) {
                return;
            }
            if (InventoryUtil.ItemMatches(barterItem, heldItem) && button == 1) {
                barterItem.setCount(Math.min(barterItem.getCount() + 1, barterItem.getMaxStackSize()));
                this.setItem(barterItem, index);
            } else {
                ItemStack setItem = heldItem.copy();
                if (button == 1) {
                    setItem.setCount(1);
                }
                this.setItem(setItem, index);
            }
        }
    }

    @Override
    public void OnOutputDisplayInteraction(@Nonnull BasicTradeEditTab tab, Consumer<LazyPacketData.Builder> clientHandler, int index, int button, @Nonnull ItemStack heldItem) {
        if (tab.menu.getTrader() instanceof ItemTraderData it) {
            int tradeIndex = it.indexOfTrade(this);
            if (tradeIndex < 0) {
                return;
            }
            if ((this.isSale() || this.isBarter()) && index >= 0 && index < 2) {
                ItemStack sellItem = this.getSellItem(index);
                if (sellItem.isEmpty() && heldItem.isEmpty()) {
                    tab.sendOpenTabMessage(2, LazyPacketData.builder().setInt("TradeIndex", tradeIndex).setInt("StartingSlot", index));
                }
                if (InventoryUtil.ItemMatches(sellItem, heldItem) && button == 1) {
                    sellItem.setCount(Math.min(sellItem.getCount() + 1, sellItem.getMaxStackSize()));
                    this.setItem(sellItem, index);
                } else {
                    ItemStack setItem = heldItem.copy();
                    if (button == 1) {
                        setItem.setCount(1);
                    }
                    this.setItem(setItem, index);
                }
                if (tab.menu.isClient()) {
                    tab.sendOutputInteractionMessage(tradeIndex, index, button, heldItem);
                }
            } else if (this.isPurchase()) {
                tab.sendOpenTabMessage(2, LazyPacketData.builder().setInt("TradeIndex", tradeIndex).setInt("StartingSlot", -1));
            }
        }
    }

    @Override
    public void OnInteraction(@Nonnull BasicTradeEditTab tab, Consumer<LazyPacketData.Builder> clientHandler, int mouseX, int mouseY, int button, @Nonnull ItemStack heldItem) {
        if (tab.menu.getTrader() instanceof ItemTraderData it) {
            int tradeIndex = it.indexOfTrade(this);
            if (tradeIndex < 0) {
                return;
            }
            tab.sendOpenTabMessage(2, LazyPacketData.simpleInt("TradeIndex", tradeIndex));
        }
    }

    @Override
    protected void collectRelevantInventorySlots(TradeContext context, List<Slot> slots, List<Integer> results) {
        if (this.isPurchase()) {
            context.hightlightItems(Lists.newArrayList(new ItemRequirement[] { this.getItemRequirement(0), this.getItemRequirement(1) }), slots, results);
        } else if (this.isBarter()) {
            context.hightlightItems(Lists.newArrayList(new ItemRequirement[] { this.getItemRequirement(2), this.getItemRequirement(3) }), slots, results);
        }
    }

    @Override
    public boolean isMoneyRelevant() {
        return !this.isBarter();
    }
}