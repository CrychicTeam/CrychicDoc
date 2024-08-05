package io.github.lightman314.lightmanscurrency.common.menus;

import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyStorage;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyValue;
import io.github.lightman314.lightmanscurrency.api.money.value.builtin.CoinValue;
import io.github.lightman314.lightmanscurrency.api.network.LazyPacketData;
import io.github.lightman314.lightmanscurrency.api.traders.TradeContext;
import io.github.lightman314.lightmanscurrency.api.traders.TraderData;
import io.github.lightman314.lightmanscurrency.api.traders.menu.IMoneyCollectionMenu;
import io.github.lightman314.lightmanscurrency.common.core.ModMenus;
import io.github.lightman314.lightmanscurrency.common.menus.slots.CoinSlot;
import io.github.lightman314.lightmanscurrency.common.menus.validation.IValidatedMenu;
import io.github.lightman314.lightmanscurrency.common.menus.validation.MenuValidator;
import io.github.lightman314.lightmanscurrency.common.traders.TraderSaveData;
import io.github.lightman314.lightmanscurrency.common.traders.slot_machine.SlotMachineTraderData;
import io.github.lightman314.lightmanscurrency.util.InventoryUtil;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.items.wrapper.InvWrapper;

public class SlotMachineMenu extends LazyMessageMenu implements IValidatedMenu, IMoneyCollectionMenu {

    private final long traderID;

    private final Container coins;

    List<Slot> coinSlots = new ArrayList();

    private final List<SlotMachineMenu.RewardCache> rewards = new ArrayList();

    private final MenuValidator validator;

    @Nullable
    public final SlotMachineTraderData getTrader() {
        TraderData var2 = TraderSaveData.GetTrader(this.isClient(), this.traderID);
        return var2 instanceof SlotMachineTraderData ? (SlotMachineTraderData) var2 : null;
    }

    public final boolean hasPendingReward() {
        return !this.rewards.isEmpty();
    }

    public final SlotMachineMenu.RewardCache getNextReward() {
        return this.rewards.isEmpty() ? null : (SlotMachineMenu.RewardCache) this.rewards.get(0);
    }

    public final SlotMachineMenu.RewardCache getAndRemoveNextReward() {
        return this.rewards.isEmpty() ? null : (SlotMachineMenu.RewardCache) this.rewards.remove(0);
    }

    @Nonnull
    @Override
    public MenuValidator getValidator() {
        return this.validator;
    }

    public SlotMachineMenu(int windowID, Inventory inventory, long traderID, @Nonnull MenuValidator validator) {
        super(ModMenus.SLOT_MACHINE.get(), windowID, inventory);
        this.validator = validator;
        this.traderID = traderID;
        this.coins = new SimpleContainer(5);
        this.addValidator(this.validator);
        this.addValidator(() -> this.getTrader() != null);
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 9; x++) {
                this.m_38897_(new Slot(inventory, x + y * 9 + 9, 8 + x * 18, 140 + y * 18));
            }
        }
        for (int x = 0; x < 9; x++) {
            this.m_38897_(new Slot(inventory, x, 8 + x * 18, 198));
        }
        for (int x = 0; x < this.coins.getContainerSize(); x++) {
            this.coinSlots.add(this.m_38897_(new CoinSlot(this.coins, x, 8 + (x + 4) * 18, 108)));
        }
        SlotMachineTraderData trader = this.getTrader();
        if (trader != null) {
            trader.userOpen(this.player);
        }
    }

    @Nonnull
    @Override
    public ItemStack quickMoveStack(@Nonnull Player playerEntity, int index) {
        ItemStack clickedStack = ItemStack.EMPTY;
        Slot slot = (Slot) this.f_38839_.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            clickedStack = slotStack.copy();
            if (index < 36) {
                if (!this.m_38903_(slotStack, 36, this.f_38839_.size(), false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index < this.f_38839_.size() && !this.m_38903_(slotStack, 0, 36, false)) {
                return ItemStack.EMPTY;
            }
            if (slotStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return clickedStack;
    }

    @Override
    public void removed(@Nonnull Player player) {
        super.m_6877_(player);
        MinecraftForge.EVENT_BUS.unregister(this);
        for (SlotMachineMenu.RewardCache reward : this.rewards) {
            reward.giveToPlayer();
        }
        this.rewards.clear();
        this.m_150411_(player, this.coins);
        SlotMachineTraderData trader = this.getTrader();
        if (trader != null) {
            trader.userClose(this.player);
        }
    }

    public final void clearContainer(Container container) {
        this.m_150411_(this.player, container);
    }

    public final TradeContext getContext() {
        return this.getContext(null);
    }

    public final TradeContext getContext(@Nullable SlotMachineMenu.RewardCache rewardHolder) {
        TradeContext.Builder builder = TradeContext.create(this.getTrader(), this.player).withCoinSlots(this.coins);
        if (rewardHolder != null) {
            builder.withItemHandler(new InvWrapper(rewardHolder.itemHolder)).withStoredCoins(rewardHolder.moneyHolder);
        }
        return builder.build();
    }

    @Override
    public void CollectStoredMoney() {
        if (this.getTrader() != null) {
            TraderData trader = this.getTrader();
            trader.CollectStoredMoney(this.player);
        }
    }

    private void ExecuteTrades(int count) {
        if (this.rewards.isEmpty()) {
            SlotMachineTraderData trader = this.getTrader();
            if (trader != null) {
                boolean flag = true;
                for (int i = 0; flag && i < count; i++) {
                    SlotMachineMenu.RewardCache holder = new SlotMachineMenu.RewardCache();
                    if (trader.TryExecuteTrade(this.getContext(holder), 0).isSuccess()) {
                        if (holder.itemHolder.isEmpty() && holder.moneyHolder.isEmpty()) {
                            LightmansCurrency.LogError("Successful Slot Machine Trade executed, but no items or money were received!");
                        } else {
                            this.rewards.add(holder);
                        }
                    } else {
                        flag = false;
                    }
                }
                if (!this.rewards.isEmpty()) {
                    CompoundTag rewardData = new CompoundTag();
                    ListTag resultList = new ListTag();
                    for (SlotMachineMenu.RewardCache result : this.rewards) {
                        resultList.add(result.save());
                    }
                    rewardData.put("Rewards", resultList);
                    this.SendMessageToClient(LazyPacketData.builder().setCompound("SyncRewards", rewardData));
                }
            }
        }
    }

    public boolean GiveNextReward() {
        SlotMachineMenu.RewardCache nextReward = this.getAndRemoveNextReward();
        if (nextReward != null) {
            nextReward.giveToPlayer();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void HandleMessage(@Nonnull LazyPacketData message) {
        if (message.contains("ExecuteTrade")) {
            if (!this.rewards.isEmpty()) {
                return;
            }
            this.ExecuteTrades(message.getInt("ExecuteTrade"));
        }
        if (message.contains("GiveNextReward")) {
            this.GiveNextReward();
        }
        if (message.contains("AnimationsCompleted")) {
            while (this.GiveNextReward()) {
            }
        }
        if (message.contains("SyncRewards") && this.isClient()) {
            this.rewards.clear();
            CompoundTag rewardData = message.getNBT("SyncRewards");
            ListTag rewardList = rewardData.getList("Rewards", 10);
            for (int i = 0; i < rewardList.size(); i++) {
                this.rewards.add(this.loadReward(rewardList.getCompound(i)));
            }
        }
    }

    public final SlotMachineMenu.RewardCache loadReward(CompoundTag tag) {
        MoneyStorage storage = new MoneyStorage(() -> {
        }, -1073741824);
        storage.load(tag.getList("Money", 10));
        return new SlotMachineMenu.RewardCache(InventoryUtil.loadAllItems("Items", tag, 4), storage);
    }

    public final class RewardCache {

        public final Container itemHolder;

        public final MoneyStorage moneyHolder = new MoneyStorage(() -> {
        }, -1073741824);

        public RewardCache() {
            this.itemHolder = new SimpleContainer(4);
        }

        public RewardCache(Container itemHolder, MoneyStorage money) {
            this.itemHolder = itemHolder;
            this.moneyHolder.addValues(money.allValues());
        }

        public void giveToPlayer() {
            SlotMachineMenu.this.clearContainer(this.itemHolder);
            this.itemHolder.m_6211_();
            this.moneyHolder.GiveToPlayer(SlotMachineMenu.this.player);
        }

        public List<ItemStack> getDisplayItems() {
            if (!this.moneyHolder.isEmpty()) {
                List<ItemStack> items = new ArrayList();
                for (MoneyValue value : this.moneyHolder.allValues()) {
                    if (value instanceof CoinValue coinValue) {
                        items.addAll(coinValue.getAsSeperatedItemList());
                    }
                }
                return items;
            } else {
                List<ItemStack> items = new ArrayList();
                for (int i = 0; i < this.itemHolder.getContainerSize(); i++) {
                    ItemStack item = this.itemHolder.getItem(i);
                    if (!item.isEmpty()) {
                        items.add(item.copy());
                    }
                }
                return items;
            }
        }

        public CompoundTag save() {
            CompoundTag tag = new CompoundTag();
            InventoryUtil.saveAllItems("Items", tag, this.itemHolder);
            tag.put("Money", this.moneyHolder.save());
            return tag;
        }
    }
}