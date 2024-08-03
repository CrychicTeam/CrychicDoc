package net.minecraft.world.inventory;

import com.mojang.datafixers.util.Pair;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class Slot {

    private final int slot;

    public final Container container;

    public int index;

    public final int x;

    public final int y;

    public Slot(Container container0, int int1, int int2, int int3) {
        this.container = container0;
        this.slot = int1;
        this.x = int2;
        this.y = int3;
    }

    public void onQuickCraft(ItemStack itemStack0, ItemStack itemStack1) {
        int $$2 = itemStack1.getCount() - itemStack0.getCount();
        if ($$2 > 0) {
            this.onQuickCraft(itemStack1, $$2);
        }
    }

    protected void onQuickCraft(ItemStack itemStack0, int int1) {
    }

    protected void onSwapCraft(int int0) {
    }

    protected void checkTakeAchievements(ItemStack itemStack0) {
    }

    public void onTake(Player player0, ItemStack itemStack1) {
        this.setChanged();
    }

    public boolean mayPlace(ItemStack itemStack0) {
        return true;
    }

    public ItemStack getItem() {
        return this.container.getItem(this.slot);
    }

    public boolean hasItem() {
        return !this.getItem().isEmpty();
    }

    public void setByPlayer(ItemStack itemStack0) {
        this.set(itemStack0);
    }

    public void set(ItemStack itemStack0) {
        this.container.setItem(this.slot, itemStack0);
        this.setChanged();
    }

    public void setChanged() {
        this.container.setChanged();
    }

    public int getMaxStackSize() {
        return this.container.getMaxStackSize();
    }

    public int getMaxStackSize(ItemStack itemStack0) {
        return Math.min(this.getMaxStackSize(), itemStack0.getMaxStackSize());
    }

    @Nullable
    public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
        return null;
    }

    public ItemStack remove(int int0) {
        return this.container.removeItem(this.slot, int0);
    }

    public boolean mayPickup(Player player0) {
        return true;
    }

    public boolean isActive() {
        return true;
    }

    public Optional<ItemStack> tryRemove(int int0, int int1, Player player2) {
        if (!this.mayPickup(player2)) {
            return Optional.empty();
        } else if (!this.allowModification(player2) && int1 < this.getItem().getCount()) {
            return Optional.empty();
        } else {
            int0 = Math.min(int0, int1);
            ItemStack $$3 = this.remove(int0);
            if ($$3.isEmpty()) {
                return Optional.empty();
            } else {
                if (this.getItem().isEmpty()) {
                    this.setByPlayer(ItemStack.EMPTY);
                }
                return Optional.of($$3);
            }
        }
    }

    public ItemStack safeTake(int int0, int int1, Player player2) {
        Optional<ItemStack> $$3 = this.tryRemove(int0, int1, player2);
        $$3.ifPresent(p_150655_ -> this.onTake(player2, p_150655_));
        return (ItemStack) $$3.orElse(ItemStack.EMPTY);
    }

    public ItemStack safeInsert(ItemStack itemStack0) {
        return this.safeInsert(itemStack0, itemStack0.getCount());
    }

    public ItemStack safeInsert(ItemStack itemStack0, int int1) {
        if (!itemStack0.isEmpty() && this.mayPlace(itemStack0)) {
            ItemStack $$2 = this.getItem();
            int $$3 = Math.min(Math.min(int1, itemStack0.getCount()), this.getMaxStackSize(itemStack0) - $$2.getCount());
            if ($$2.isEmpty()) {
                this.setByPlayer(itemStack0.split($$3));
            } else if (ItemStack.isSameItemSameTags($$2, itemStack0)) {
                itemStack0.shrink($$3);
                $$2.grow($$3);
                this.setByPlayer($$2);
            }
            return itemStack0;
        } else {
            return itemStack0;
        }
    }

    public boolean allowModification(Player player0) {
        return this.mayPickup(player0) && this.mayPlace(this.getItem());
    }

    public int getContainerSlot() {
        return this.slot;
    }

    public boolean isHighlightable() {
        return true;
    }
}