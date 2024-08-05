package io.github.lightman314.lightmanscurrency.common.traders.slot_machine;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyValue;
import io.github.lightman314.lightmanscurrency.api.stats.StatKeys;
import io.github.lightman314.lightmanscurrency.api.traders.TradeContext;
import io.github.lightman314.lightmanscurrency.api.traders.TradeResult;
import io.github.lightman314.lightmanscurrency.api.traders.TraderData;
import io.github.lightman314.lightmanscurrency.api.traders.TraderType;
import io.github.lightman314.lightmanscurrency.api.traders.menu.storage.ITraderStorageMenu;
import io.github.lightman314.lightmanscurrency.api.traders.permissions.PermissionOption;
import io.github.lightman314.lightmanscurrency.api.upgrades.UpgradeType;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.icon.IconData;
import io.github.lightman314.lightmanscurrency.client.util.IconAndButtonUtil;
import io.github.lightman314.lightmanscurrency.common.items.UpgradeItem;
import io.github.lightman314.lightmanscurrency.common.menus.SlotMachineMenu;
import io.github.lightman314.lightmanscurrency.common.menus.providers.EasyMenuProvider;
import io.github.lightman314.lightmanscurrency.common.menus.traderstorage.slot_machine.SlotMachineEntryTab;
import io.github.lightman314.lightmanscurrency.common.menus.traderstorage.slot_machine.SlotMachinePriceTab;
import io.github.lightman314.lightmanscurrency.common.menus.traderstorage.slot_machine.SlotMachineStorageTab;
import io.github.lightman314.lightmanscurrency.common.menus.validation.MenuValidator;
import io.github.lightman314.lightmanscurrency.common.notifications.types.trader.OutOfStockNotification;
import io.github.lightman314.lightmanscurrency.common.notifications.types.trader.SlotMachineTradeNotification;
import io.github.lightman314.lightmanscurrency.common.traders.item.TraderItemStorage;
import io.github.lightman314.lightmanscurrency.common.traders.slot_machine.trade_data.SlotMachineTrade;
import io.github.lightman314.lightmanscurrency.common.upgrades.Upgrades;
import io.github.lightman314.lightmanscurrency.common.upgrades.types.capacity.CapacityUpgrade;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.ResourceLocationException;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SlotMachineTraderData extends TraderData implements TraderItemStorage.ITraderItemFilter {

    public static final TraderType<SlotMachineTraderData> TYPE = new TraderType<>(new ResourceLocation("lightmanscurrency", "slot_machine_trader"), SlotMachineTraderData::new);

    private MoneyValue price = MoneyValue.empty();

    private List<ItemStack> lastReward = new ArrayList();

    private final List<SlotMachineEntry> entries = Lists.newArrayList(new SlotMachineEntry[] { SlotMachineEntry.create() });

    private boolean entriesChanged = false;

    private final TraderItemStorage storage = new TraderItemStorage(this);

    private final ImmutableList<SlotMachineTrade> trade = ImmutableList.of(new SlotMachineTrade(this));

    public final MoneyValue getPrice() {
        return this.price;
    }

    public void setPrice(MoneyValue newValue) {
        this.price = newValue;
        this.markPriceDirty();
    }

    public final boolean isPriceValid() {
        return this.price.isFree() || !this.price.isEmpty();
    }

    public List<ItemStack> getLastRewards() {
        return ImmutableList.copyOf(this.lastReward);
    }

    public final List<SlotMachineEntry> getAllEntries() {
        return new ArrayList(this.entries);
    }

    public final List<SlotMachineEntry> getValidEntries() {
        return this.entries.stream().filter(SlotMachineEntry::isValid).toList();
    }

    public boolean areEntriesChanged() {
        return this.entriesChanged;
    }

    public void clearEntriesChangedCache() {
        this.entriesChanged = false;
    }

    public void addEntry() {
        if (this.entries.size() < 100) {
            this.entries.add(SlotMachineEntry.create());
            this.markEntriesDirty();
        }
    }

    public void removeEntry(int entryIndex) {
        if (entryIndex >= 0 && entryIndex < this.entries.size()) {
            this.entries.remove(entryIndex);
            this.markEntriesDirty();
        }
    }

    public final int getTotalWeight() {
        int weight = 0;
        for (SlotMachineEntry entry : this.getValidEntries()) {
            weight += entry.getWeight();
        }
        return weight;
    }

    @Nullable
    public final SlotMachineEntry getRandomizedEntry(TradeContext context) {
        Level level;
        if (context.hasPlayer()) {
            level = context.getPlayer().m_9236_();
        } else {
            try {
                level = LightmansCurrency.PROXY.safeGetDummyLevel();
            } catch (Throwable var4) {
                LightmansCurrency.LogError("Could not get a valid level from the trade's context or the proxy. Will have to use Java randomizer");
                return this.getRandomizedEntry(new Random().nextInt(this.getTotalWeight()) + 1);
            }
        }
        return this.getRandomizedEntry(level.random.nextInt(this.getTotalWeight()) + 1);
    }

    private SlotMachineEntry getRandomizedEntry(int rand) {
        for (SlotMachineEntry entry : this.getValidEntries()) {
            rand -= entry.getWeight();
            if (rand <= 0) {
                return entry;
            }
        }
        return null;
    }

    @Nonnull
    public final List<Component> getSlotMachineInfo() {
        List<Component> tooltips = new ArrayList();
        if (!this.hasValidTrade()) {
            tooltips.add(LCText.TOOLTIP_SLOT_MACHINE_UNDEFINED.get().withStyle(ChatFormatting.RED));
            return tooltips;
        } else {
            if (!this.hasStock()) {
                tooltips.add(LCText.TOOLTIP_OUT_OF_STOCK.get().withStyle(ChatFormatting.RED));
            }
            return tooltips;
        }
    }

    public String getOdds(int weight) {
        DecimalFormat df = new DecimalFormat();
        double odds = (double) weight / (double) this.getTotalWeight() * 100.0;
        df.setMaximumFractionDigits(odds < 1.0 ? 2 : 0);
        return df.format(odds);
    }

    public final TraderItemStorage getStorage() {
        return this.storage;
    }

    private SlotMachineTraderData() {
        super(TYPE);
    }

    public SlotMachineTraderData(@Nonnull Level level, @Nonnull BlockPos pos) {
        super(TYPE, level, pos);
    }

    @Override
    public IconData getIcon() {
        return IconAndButtonUtil.ICON_TRADER_ALT;
    }

    @Override
    protected boolean allowAdditionalUpgradeType(UpgradeType type) {
        return type == Upgrades.ITEM_CAPACITY;
    }

    @Override
    public int getTradeCount() {
        return 1;
    }

    @Override
    public int getTradeStock(int tradeIndex) {
        if (!this.hasValidTrade()) {
            return 0;
        } else if (this.isCreative()) {
            return 1;
        } else {
            int minStock = Integer.MAX_VALUE;
            for (SlotMachineEntry entry : this.entries) {
                int stock = entry.getStock(this);
                if (stock < minStock) {
                    minStock = stock;
                }
            }
            return minStock;
        }
    }

    public boolean hasStock() {
        return this.getTradeStock(0) > 0;
    }

    @Override
    public boolean hasValidTrade() {
        return this.entries.stream().anyMatch(SlotMachineEntry::isValid) && this.isPriceValid();
    }

    @Override
    protected void saveTrades(CompoundTag compound) {
    }

    @Override
    protected MenuProvider getTraderMenuProvider(@Nonnull MenuValidator validator) {
        return new SlotMachineTraderData.SlotMachineMenuProvider(this.getID(), validator);
    }

    public final void markStorageDirty() {
        this.markDirty(new Consumer[] { this::saveStorage });
    }

    public final void markLastRewardDirty() {
        this.markDirty(new Consumer[] { this::saveLastRewards });
    }

    public final void markEntriesDirty() {
        this.markDirty(new Consumer[] { this::saveEntries });
    }

    public final void markPriceDirty() {
        this.markDirty(new Consumer[] { this::savePrice });
    }

    @Override
    protected void saveAdditional(CompoundTag compound) {
        this.saveStorage(compound);
        this.saveLastRewards(compound);
        this.saveEntries(compound);
        this.savePrice(compound);
    }

    protected final void saveStorage(CompoundTag compound) {
        this.storage.save(compound, "Storage");
    }

    protected final void saveLastRewards(CompoundTag compound) {
        ListTag itemList = new ListTag();
        for (ItemStack reward : this.lastReward) {
            if (!reward.isEmpty()) {
                itemList.add(reward.save(new CompoundTag()));
            }
        }
        compound.put("LastReward", itemList);
    }

    protected final void saveEntries(CompoundTag compound) {
        ListTag list = new ListTag();
        for (SlotMachineEntry entry : this.entries) {
            list.add(entry.save());
        }
        compound.put("Entries", list);
    }

    protected final void savePrice(CompoundTag compound) {
        compound.put("Price", this.price.save());
    }

    @Override
    protected void loadAdditional(CompoundTag compound) {
        if (compound.contains("Storage")) {
            this.storage.load(compound, "Storage");
        }
        if (compound.contains("LastReward")) {
            this.lastReward.clear();
            ListTag itemList = compound.getList("LastReward", 10);
            for (int i = 0; i < itemList.size(); i++) {
                ItemStack stack = ItemStack.of(itemList.getCompound(i));
                if (!stack.isEmpty()) {
                    this.lastReward.add(stack);
                }
            }
        }
        if (compound.contains("Entries")) {
            this.entries.clear();
            ListTag list = compound.getList("Entries", 10);
            for (int ix = 0; ix < list.size(); ix++) {
                this.entries.add(SlotMachineEntry.load(list.getCompound(ix)));
            }
            this.entriesChanged = true;
        }
        if (compound.contains("Price")) {
            this.price = MoneyValue.safeLoad(compound, "Price");
        }
    }

    @Override
    protected void saveAdditionalToJson(JsonObject json) {
        json.add("Price", this.price.toJson());
        JsonArray entryList = new JsonArray();
        for (SlotMachineEntry entry : this.entries) {
            if (entry.isValid()) {
                entryList.add(entry.toJson());
            }
        }
        json.add("Entries", entryList);
    }

    @Override
    protected void loadAdditionalFromJson(JsonObject json) throws JsonSyntaxException, ResourceLocationException {
        if (!json.has("Price")) {
            throw new JsonSyntaxException("Expected a 'Price' entry!");
        } else {
            this.price = MoneyValue.loadFromJson(json.get("Price"));
            this.entries.clear();
            JsonArray entryList = GsonHelper.getAsJsonArray(json, "Entries");
            for (int i = 0; i < entryList.size(); i++) {
                try {
                    this.entries.add(SlotMachineEntry.parse(GsonHelper.convertToJsonObject(entryList.get(i), "Entries[" + i + "]")));
                } catch (ResourceLocationException | JsonSyntaxException var5) {
                    LightmansCurrency.LogError("Error parsing Slot Machine Trader Entry #" + (i + 1), var5);
                }
            }
            if (this.entries.isEmpty()) {
                throw new JsonSyntaxException("Slot Machine Trader had no valid Entries!");
            }
        }
    }

    @Override
    protected void saveAdditionalPersistentData(CompoundTag compound) {
        this.saveLastRewards(compound);
    }

    @Override
    protected void loadAdditionalPersistentData(CompoundTag compound) {
        if (compound.contains("LastReward")) {
            this.lastReward = new ArrayList();
            ListTag itemList = compound.getList("LastReward", 10);
            for (int i = 0; i < itemList.size(); i++) {
                ItemStack stack = ItemStack.of(itemList.getCompound(i));
                if (!stack.isEmpty()) {
                    this.lastReward.add(stack);
                }
            }
        }
    }

    @Override
    protected void getAdditionalContents(List<ItemStack> results) {
        results.addAll(this.storage.getSplitContents());
    }

    @Nonnull
    @Override
    public List<SlotMachineTrade> getTradeData() {
        return this.trade;
    }

    @Nullable
    public SlotMachineTrade getTrade(int tradeIndex) {
        return (SlotMachineTrade) this.trade.get(0);
    }

    @Override
    public void addTrade(Player requestor) {
    }

    @Override
    public void removeTrade(Player requestor) {
    }

    @Override
    public TradeResult ExecuteTrade(TradeContext context, int tradeIndex) {
        if (!this.hasValidTrade()) {
            return TradeResult.FAIL_INVALID_TRADE;
        } else {
            SlotMachineTrade trade = (SlotMachineTrade) this.trade.get(0);
            if (trade == null) {
                LightmansCurrency.LogError("Slot Machine somehow doesn't have a valid trade!");
                return TradeResult.FAIL_INVALID_TRADE;
            } else if (!context.hasPlayerReference()) {
                return TradeResult.FAIL_NULL;
            } else if (!this.hasStock()) {
                return TradeResult.FAIL_OUT_OF_STOCK;
            } else if (this.runPreTradeEvent(trade, context).isCanceled()) {
                return TradeResult.FAIL_TRADE_RULE_DENIAL;
            } else {
                MoneyValue price = this.runTradeCostEvent(trade, context).getCostResult();
                SlotMachineEntry loot = this.getRandomizedEntry(context);
                if (loot == null) {
                    LightmansCurrency.LogError("Slot Machine encountered an error randomizing the loot.");
                    return TradeResult.FAIL_NULL;
                } else if (!loot.CanGiveToCustomer(context)) {
                    return TradeResult.FAIL_NO_OUTPUT_SPACE;
                } else if (context.getPayment(price)) {
                    if (!loot.GiveToCustomer(this, context)) {
                        context.givePayment(price);
                        return TradeResult.FAIL_NO_OUTPUT_SPACE;
                    } else {
                        this.lastReward = loot.getDisplayItems();
                        this.markLastRewardDirty();
                        MoneyValue taxesPaid = MoneyValue.empty();
                        if (!this.isCreative()) {
                            taxesPaid = this.addStoredMoney(price, true);
                            if (!this.hasStock()) {
                                this.pushNotification(OutOfStockNotification.create(this.getNotificationCategory(), -1));
                            }
                        }
                        this.incrementStat(StatKeys.Traders.MONEY_EARNED, price);
                        if (!taxesPaid.isEmpty()) {
                            this.incrementStat(StatKeys.Taxables.TAXES_PAID, taxesPaid);
                        }
                        if (loot.isMoney()) {
                            this.incrementStat(StatKeys.Traders.MONEY_PAID, loot.getMoneyValue());
                        }
                        this.pushNotification(SlotMachineTradeNotification.create(loot, price, context.getPlayerReference(), this.getNotificationCategory(), taxesPaid));
                        this.runPostTradeEvent(trade, context, price, taxesPaid);
                        return TradeResult.SUCCESS;
                    }
                } else {
                    return TradeResult.FAIL_CANNOT_AFFORD;
                }
            }
        }
    }

    @Override
    public boolean canMakePersistent() {
        return true;
    }

    @Override
    public void initStorageTabs(@Nonnull ITraderStorageMenu menu) {
        menu.setTab(0, new SlotMachineEntryTab(menu));
        menu.setTab(1, new SlotMachinePriceTab(menu));
        menu.setTab(2, new SlotMachineStorageTab(menu));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    protected void addPermissionOptions(List<PermissionOption> options) {
    }

    @Override
    public boolean isItemRelevant(ItemStack item) {
        for (SlotMachineEntry entry : this.entries) {
            if (entry.isItemRelevant(item)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getStorageStackLimit() {
        int limit = 576;
        for (int i = 0; i < this.getUpgrades().getContainerSize(); i++) {
            ItemStack stack = this.getUpgrades().getItem(i);
            Item var5 = stack.getItem();
            if (var5 instanceof UpgradeItem) {
                UpgradeItem upgradeItem = (UpgradeItem) var5;
                if (this.allowUpgrade(upgradeItem) && upgradeItem.getUpgradeType() instanceof CapacityUpgrade) {
                    limit += UpgradeItem.getUpgradeData(stack).getIntValue(CapacityUpgrade.CAPACITY);
                }
            }
        }
        return limit;
    }

    private static record SlotMachineMenuProvider(long traderID, @Nonnull MenuValidator validator) implements EasyMenuProvider {

        @Override
        public AbstractContainerMenu createMenu(int windowID, @Nonnull Inventory inventory, @Nonnull Player player) {
            return new SlotMachineMenu(windowID, inventory, this.traderID, this.validator);
        }
    }
}