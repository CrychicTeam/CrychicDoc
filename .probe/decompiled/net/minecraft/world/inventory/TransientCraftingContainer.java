package net.minecraft.world.inventory;

import java.util.List;
import net.minecraft.core.NonNullList;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.item.ItemStack;

public class TransientCraftingContainer implements CraftingContainer {

    private final NonNullList<ItemStack> items;

    private final int width;

    private final int height;

    private final AbstractContainerMenu menu;

    public TransientCraftingContainer(AbstractContainerMenu abstractContainerMenu0, int int1, int int2) {
        this(abstractContainerMenu0, int1, int2, NonNullList.withSize(int1 * int2, ItemStack.EMPTY));
    }

    public TransientCraftingContainer(AbstractContainerMenu abstractContainerMenu0, int int1, int int2, NonNullList<ItemStack> nonNullListItemStack3) {
        this.items = nonNullListItemStack3;
        this.menu = abstractContainerMenu0;
        this.width = int1;
        this.height = int2;
    }

    @Override
    public int getContainerSize() {
        return this.items.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack $$0 : this.items) {
            if (!$$0.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(int int0) {
        return int0 >= this.getContainerSize() ? ItemStack.EMPTY : this.items.get(int0);
    }

    @Override
    public ItemStack removeItemNoUpdate(int int0) {
        return ContainerHelper.takeItem(this.items, int0);
    }

    @Override
    public ItemStack removeItem(int int0, int int1) {
        ItemStack $$2 = ContainerHelper.removeItem(this.items, int0, int1);
        if (!$$2.isEmpty()) {
            this.menu.slotsChanged(this);
        }
        return $$2;
    }

    @Override
    public void setItem(int int0, ItemStack itemStack1) {
        this.items.set(int0, itemStack1);
        this.menu.slotsChanged(this);
    }

    @Override
    public void setChanged() {
    }

    @Override
    public boolean stillValid(Player player0) {
        return true;
    }

    @Override
    public void clearContent() {
        this.items.clear();
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public List<ItemStack> getItems() {
        return List.copyOf(this.items);
    }

    @Override
    public void fillStackedContents(StackedContents stackedContents0) {
        for (ItemStack $$1 : this.items) {
            stackedContents0.accountSimpleStack($$1);
        }
    }
}