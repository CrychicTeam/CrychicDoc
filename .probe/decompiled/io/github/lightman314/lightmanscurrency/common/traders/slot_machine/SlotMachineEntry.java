package io.github.lightman314.lightmanscurrency.common.traders.slot_machine;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.api.money.coins.CoinAPI;
import io.github.lightman314.lightmanscurrency.api.money.coins.data.ChainData;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyValue;
import io.github.lightman314.lightmanscurrency.api.money.value.builtin.CoinValue;
import io.github.lightman314.lightmanscurrency.api.money.value.holder.IMoneyHolder;
import io.github.lightman314.lightmanscurrency.api.traders.TradeContext;
import io.github.lightman314.lightmanscurrency.util.FileUtil;
import io.github.lightman314.lightmanscurrency.util.InventoryUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.annotation.Nonnull;
import net.minecraft.ResourceLocationException;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;

public final class SlotMachineEntry {

    public static final int ITEM_LIMIT = 4;

    public final List<ItemStack> items;

    private int weight;

    public void TryAddItem(ItemStack item) {
        if (this.items.size() < 4 && !item.isEmpty()) {
            this.items.add(item);
        }
    }

    public int getWeight() {
        return this.weight;
    }

    public void setWeight(int newWeight) {
        this.weight = Math.max(1, newWeight);
    }

    private SlotMachineEntry(List<ItemStack> items, int weight) {
        this.items = InventoryUtil.copyList(items);
        this.setWeight(weight);
    }

    public boolean isValid() {
        return !this.items.isEmpty() && this.weight > 0;
    }

    public boolean isMoney() {
        if (this.items.isEmpty()) {
            return false;
        } else {
            ChainData chain = null;
            for (ItemStack item : this.items) {
                if (!CoinAPI.API.IsCoin(item, false)) {
                    return false;
                }
                if (chain == null) {
                    chain = CoinAPI.API.ChainDataOfCoin(item);
                } else if (chain != CoinAPI.API.ChainDataOfCoin(item)) {
                    return false;
                }
            }
            return true;
        }
    }

    public MoneyValue getMoneyValue() {
        if (!this.isMoney()) {
            return MoneyValue.empty();
        } else {
            ChainData chain = null;
            long value = 0L;
            for (ItemStack item : this.items) {
                if (CoinAPI.API.IsCoin(item, false)) {
                    if (chain == null) {
                        chain = CoinAPI.API.ChainDataOfCoin(item);
                    } else if (chain != CoinAPI.API.ChainDataOfCoin(item)) {
                        return MoneyValue.empty();
                    }
                    value += chain.getCoreValue(item) * (long) item.getCount();
                } else if (!item.isEmpty()) {
                    return MoneyValue.empty();
                }
            }
            return chain == null ? MoneyValue.empty() : CoinValue.fromNumber(chain.chain, value);
        }
    }

    public void validateItems() {
        for (int i = 0; i < this.items.size(); i++) {
            if (((ItemStack) this.items.get(i)).isEmpty()) {
                this.items.remove(i--);
            }
        }
    }

    public List<ItemStack> getDisplayItems() {
        return this.isMoney() && this.getMoneyValue() instanceof CoinValue coinValue ? coinValue.getAsSeperatedItemList() : InventoryUtil.copyList(this.items);
    }

    @Nonnull
    public static List<ItemStack> splitDisplayItems(@Nonnull List<ItemStack> displayItems) {
        if (displayItems.size() >= 4) {
            return displayItems;
        } else {
            int totalCount = 0;
            for (ItemStack s : displayItems) {
                totalCount += s.getCount();
            }
            List<ItemStack> result = InventoryUtil.copyList(displayItems);
            Random random = new Random();
            while (result.size() < 4 && result.size() < totalCount) {
                int splitIndex = random.nextInt(result.size());
                ItemStack s = (ItemStack) result.get(splitIndex);
                if (s.getCount() > 1) {
                    int splitCount = s.getCount() / 2;
                    ItemStack split = s.split(splitCount);
                    result.add(split);
                }
            }
            return result;
        }
    }

    public boolean CanGiveToCustomer(TradeContext context) {
        return this.isMoney() ? context.hasPaymentMethod() : context.canFitItems(this.items);
    }

    public boolean GiveToCustomer(SlotMachineTraderData trader, TradeContext context) {
        if (!this.hasStock(trader)) {
            return false;
        } else {
            if (this.isMoney()) {
                MoneyValue reward = this.getMoneyValue();
                if (!context.givePayment(reward)) {
                    return false;
                }
                if (!trader.isCreative()) {
                    trader.removeStoredMoney(reward, false);
                }
            } else {
                for (int i = 0; i < this.items.size(); i++) {
                    if (!context.putItem(((ItemStack) this.items.get(i)).copy())) {
                        for (int x = 0; x < i; x++) {
                            context.collectItem(((ItemStack) this.items.get(x)).copy());
                        }
                        return false;
                    }
                }
                if (!trader.isCreative()) {
                    for (ItemStack ix : this.items) {
                        trader.getStorage().removeItem(ix);
                    }
                    trader.markStorageDirty();
                }
            }
            return true;
        }
    }

    public int getStock(SlotMachineTraderData trader) {
        if (!this.isValid()) {
            return 0;
        } else if (trader.isCreative()) {
            return 1;
        } else if (this.isMoney()) {
            MoneyValue payout = this.getMoneyValue();
            if (!payout.isEmpty() && payout.getCoreValue() > 0L) {
                IMoneyHolder storedMoney = trader.getStoredMoney();
                MoneyValue totalMoney = storedMoney.getStoredMoney().valueOf(payout.getUniqueName());
                return (int) (totalMoney.getCoreValue() / payout.getCoreValue());
            } else {
                return 0;
            }
        } else {
            int minStock = Integer.MAX_VALUE;
            for (ItemStack item : InventoryUtil.combineQueryItems(this.items)) {
                int count = trader.getStorage().getItemCount(item);
                int stock = count / item.getCount();
                if (stock < minStock) {
                    minStock = stock;
                }
            }
            return minStock;
        }
    }

    public boolean hasStock(SlotMachineTraderData trader) {
        return this.getStock(trader) > 0;
    }

    public boolean isItemRelevant(ItemStack item) {
        return this.isMoney() ? false : this.items.stream().anyMatch(i -> InventoryUtil.ItemMatches(i, item));
    }

    public CompoundTag save() {
        CompoundTag compound = new CompoundTag();
        ListTag itemList = new ListTag();
        for (ItemStack item : this.items) {
            itemList.add(item.save(new CompoundTag()));
        }
        compound.put("Items", itemList);
        compound.putInt("Weight", this.weight);
        return compound;
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        JsonArray itemList = new JsonArray();
        for (ItemStack item : this.items) {
            itemList.add(FileUtil.convertItemStack(item));
        }
        json.add("Items", itemList);
        json.addProperty("Weight", this.weight);
        return json;
    }

    public static SlotMachineEntry create() {
        return new SlotMachineEntry(new ArrayList(), 1);
    }

    public static SlotMachineEntry load(CompoundTag compound) {
        List<ItemStack> items = new ArrayList();
        if (compound.contains("Items")) {
            ListTag itemList = compound.getList("Items", 10);
            for (int i = 0; i < itemList.size(); i++) {
                ItemStack stack = ItemStack.of(itemList.getCompound(i));
                if (!stack.isEmpty()) {
                    items.add(stack);
                }
            }
        }
        return compound.contains("Weight") ? new SlotMachineEntry(items, compound.getInt("Weight")) : new SlotMachineEntry(items, 1);
    }

    public static SlotMachineEntry parse(JsonObject json) throws JsonSyntaxException, ResourceLocationException {
        List<ItemStack> items = new ArrayList();
        JsonArray itemList = GsonHelper.getAsJsonArray(json, "Items");
        for (int i = 0; i < itemList.size(); i++) {
            try {
                ItemStack stack = FileUtil.parseItemStack(itemList.get(i).getAsJsonObject());
                if (stack.isEmpty()) {
                    throw new JsonSyntaxException("Cannot add an empty item to a Slot Machine Entry!");
                }
                items.add(stack);
            } catch (ResourceLocationException | JsonSyntaxException var5) {
                LightmansCurrency.LogError("Error parsing Slot Machine Entry item #" + (i + 1), var5);
            }
        }
        if (items.isEmpty()) {
            throw new JsonSyntaxException("Slot Machine Entry has no valid items!");
        } else {
            int weight = GsonHelper.getAsInt(json, "Weight", 1);
            return new SlotMachineEntry(items, weight);
        }
    }
}