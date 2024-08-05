package io.github.lightman314.lightmanscurrency.common.traders.auction;

import io.github.lightman314.lightmanscurrency.api.misc.player.PlayerReference;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyStorage;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyValue;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;

public class AuctionPlayerStorage {

    PlayerReference owner;

    private final MoneyStorage storedCoins = new MoneyStorage(() -> {
    });

    private final List<ItemStack> storedItems = new ArrayList();

    public PlayerReference getOwner() {
        return this.owner;
    }

    public MoneyStorage getStoredCoins() {
        return this.storedCoins;
    }

    public List<ItemStack> getStoredItems() {
        return this.storedItems;
    }

    public AuctionPlayerStorage(PlayerReference player) {
        this.owner = player;
    }

    public AuctionPlayerStorage(CompoundTag compound) {
        this.load(compound);
    }

    public CompoundTag save(CompoundTag compound) {
        compound.put("Owner", this.owner.save());
        compound.put("StoredMoney", this.storedCoins.save());
        ListTag itemList = new ListTag();
        for (ItemStack storedItem : this.storedItems) {
            itemList.add(storedItem.save(new CompoundTag()));
        }
        compound.put("StoredItems", itemList);
        return compound;
    }

    protected void load(CompoundTag compound) {
        this.owner = PlayerReference.load(compound.getCompound("Owner"));
        this.storedCoins.safeLoad(compound, "StoredMoney");
        this.storedItems.clear();
        ListTag itemList = compound.getList("StoredItems", 10);
        for (int i = 0; i < itemList.size(); i++) {
            ItemStack stack = ItemStack.of(itemList.getCompound(i));
            if (!stack.isEmpty()) {
                this.storedItems.add(stack);
            }
        }
    }

    public void giveMoney(@Nonnull MoneyValue amount) {
        this.storedCoins.addValue(amount);
    }

    public void collectedMoney(Player player) {
        this.storedCoins.GiveToPlayer(player);
    }

    public void giveItem(ItemStack item) {
        if (!item.isEmpty()) {
            this.storedItems.add(item);
        }
    }

    public void removePartial(int itemSlot, int count) {
        if (this.storedItems.size() < itemSlot && itemSlot >= 0) {
            ((ItemStack) this.storedItems.get(itemSlot)).shrink(count);
            if (((ItemStack) this.storedItems.get(itemSlot)).isEmpty()) {
                this.storedItems.remove(itemSlot);
            }
        }
    }

    public void collectItems(Player player) {
        for (ItemStack stack : this.storedItems) {
            ItemHandlerHelper.giveItemToPlayer(player, stack);
        }
        this.storedItems.clear();
    }
}