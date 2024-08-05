package com.almostreliable.summoningrituals.inventory;

import com.almostreliable.summoningrituals.platform.PlatformBlockEntity;
import com.almostreliable.summoningrituals.recipe.AltarRecipe;
import com.almostreliable.summoningrituals.recipe.AltarRecipeSerializer;
import com.almostreliable.summoningrituals.recipe.component.IngredientStack;
import com.almostreliable.summoningrituals.util.GameUtils;
import com.almostreliable.summoningrituals.util.TextUtils;
import extensions.net.minecraft.world.item.ItemStack.ItemStackExt;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class AltarInventory implements ItemHandler {

    public static final int SIZE = 64;

    private final PlatformBlockEntity parent;

    private final VanillaWrapper vanillaInv;

    private final Deque<Tuple<ItemStack, Integer>> insertOrder;

    private final NonNullList<ItemStack> items;

    private ItemStack catalyst;

    public AltarInventory(PlatformBlockEntity parent) {
        this.parent = parent;
        this.vanillaInv = new VanillaWrapper(this);
        this.items = NonNullList.withSize(64, ItemStack.EMPTY);
        this.catalyst = ItemStack.EMPTY;
        this.insertOrder = new ArrayDeque(64);
    }

    public CompoundTag serialize() {
        ListTag insertListTag = new ListTag();
        for (Tuple<ItemStack, Integer> e : this.insertOrder) {
            CompoundTag tag = new CompoundTag();
            e.getA().save(tag);
            tag.putInt("slot", e.getB());
            insertListTag.add(tag);
        }
        ListTag itemsTag = new ListTag();
        for (int slot = 0; slot < 64; slot++) {
            if (!this.items.get(slot).isEmpty()) {
                CompoundTag tag = new CompoundTag();
                tag.putInt("slot", slot);
                this.items.get(slot).save(tag);
                itemsTag.add(tag);
            }
        }
        CompoundTag tag = new CompoundTag();
        tag.put("insert_order", insertListTag);
        tag.put("items", itemsTag);
        tag.put("catalyst", ItemStackExt.serialize(this.catalyst));
        return tag;
    }

    public void deserialize(CompoundTag tag) {
        this.insertOrder.clear();
        for (Tag e : tag.getList("insert_order", 10)) {
            ItemStack stack = ItemStack.of((CompoundTag) e);
            int slot = ((CompoundTag) e).getInt("slot");
            this.insertOrder.add(new Tuple<>(stack, slot));
        }
        this.items.clear();
        for (Tag e : tag.getList("items", 10)) {
            int slot = ((CompoundTag) e).getInt("slot");
            ItemStack stack = ItemStack.of((CompoundTag) e);
            this.items.set(slot, stack);
        }
        this.catalyst = ItemStack.of(tag.getCompound("catalyst"));
    }

    @Override
    public void setStackInSlot(int slot, ItemStack stack) {
        this.validateSlot(slot);
        if (slot == AltarRecipeSerializer.MAX_INPUTS) {
            this.catalyst = stack;
        } else {
            this.items.set(slot, stack);
            this.onContentsChanged();
        }
    }

    public ItemStack handleInsertion(ItemStack stack) {
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        } else {
            ItemStack remaining = stack.copy();
            for (int i = 0; i < AltarRecipeSerializer.MAX_INPUTS; i++) {
                ItemStack original = remaining.copy();
                remaining = this.insertItem(i, remaining);
                if (remaining.isEmpty()) {
                    this.insertOrder.push(new Tuple<>(original, i));
                    return ItemStack.EMPTY;
                }
                if (remaining.getCount() != original.getCount()) {
                    original.shrink(remaining.getCount());
                    this.insertOrder.push(new Tuple<>(original, i));
                }
            }
            return remaining;
        }
    }

    public void popLastInserted() {
        Level level = this.parent.m_58904_();
        assert level != null && !level.isClientSide;
        if (!this.catalyst.isEmpty()) {
            GameUtils.dropItem(level, this.parent.m_58899_(), this.catalyst, true);
            this.catalyst = ItemStack.EMPTY;
            this.onContentsChanged();
        } else if (!this.insertOrder.isEmpty()) {
            Tuple<ItemStack, Integer> last = (Tuple<ItemStack, Integer>) this.insertOrder.pop();
            ItemStack stack = last.getA();
            int slot = last.getB();
            this.items.get(slot).shrink(stack.getCount());
            if (this.items.get(slot).isEmpty()) {
                this.items.set(slot, ItemStack.EMPTY);
            }
            this.onContentsChanged();
            GameUtils.dropItem(level, this.parent.m_58899_(), stack, true);
        }
    }

    public void dropContents() {
        Level level = this.parent.m_58904_();
        assert level != null && !level.isClientSide;
        BlockPos pos = this.parent.m_58899_();
        for (ItemStack stack : this.items) {
            if (!stack.isEmpty()) {
                GameUtils.dropItem(level, pos, stack, false);
            }
        }
        if (!this.catalyst.isEmpty()) {
            GameUtils.dropItem(level, pos, this.catalyst, false);
        }
    }

    public boolean handleRecipe(AltarRecipe recipe) {
        List<ItemStack> itemBackup = this.createItemBackup();
        int toRemove = 0;
        int actualRemoved = 0;
        for (IngredientStack input : recipe.getInputs()) {
            toRemove += input.count();
            int inputRemoved = 0;
            for (ItemStack stack : this.items) {
                if (!stack.isEmpty() && input.ingredient().test(stack)) {
                    int shrinkCount = Math.min(input.count() - inputRemoved, stack.getCount());
                    stack.shrink(shrinkCount);
                    inputRemoved += shrinkCount;
                    if (inputRemoved >= input.count()) {
                        break;
                    }
                }
            }
            actualRemoved += inputRemoved;
        }
        if (actualRemoved >= toRemove) {
            this.catalyst = ItemStack.EMPTY;
            this.rebuildInsertOrder();
            this.onContentsChanged();
            return true;
        } else {
            this.items.clear();
            for (int i = 0; i < itemBackup.size(); i++) {
                this.items.add(i, (ItemStack) itemBackup.get(i));
            }
            return false;
        }
    }

    private ItemStack insertItem(int slot, ItemStack stack) {
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        } else {
            this.validateSlot(slot);
            ItemStack currentStack = this.items.get(slot);
            if (currentStack.isEmpty()) {
                this.items.set(slot, stack);
                this.onContentsChanged();
                return ItemStack.EMPTY;
            } else if (!ItemStackExt.canStack(currentStack, stack)) {
                return stack;
            } else {
                int maxCount = this.getMaxStackSize(slot, currentStack);
                int toInsert = Math.min(maxCount - currentStack.getCount(), stack.getCount());
                if (toInsert <= 0) {
                    return stack;
                } else {
                    currentStack.grow(toInsert);
                    ItemStack remainder = stack.copyWithCount(stack.getCount() - toInsert);
                    this.onContentsChanged();
                    return remainder.isEmpty() ? ItemStack.EMPTY : remainder;
                }
            }
        }
    }

    private void rebuildInsertOrder() {
        this.insertOrder.clear();
        for (int i = 63; i >= 0; i--) {
            ItemStack stack = this.items.get(i);
            if (!stack.isEmpty()) {
                this.insertOrder.add(new Tuple<>(stack.copy(), i));
            }
        }
    }

    private void onContentsChanged() {
        this.parent.m_6596_();
        if (this.parent.m_58904_() != null && !this.parent.m_58904_().isClientSide) {
            this.parent.m_58904_().sendBlockUpdated(this.parent.m_58899_(), this.parent.m_58900_(), this.parent.m_58900_(), 3);
        }
    }

    private void validateSlot(int slot) {
        if (slot < 0 || slot >= 65) {
            throw new IllegalStateException(TextUtils.f("Slot {} is not in range [0,{})", slot, 65));
        }
    }

    private int getMaxStackSize(int slot, ItemStack stack) {
        return Math.min(this.getSlotLimit(slot), stack.getMaxStackSize());
    }

    @Override
    public int getSlots() {
        return AltarRecipeSerializer.MAX_INPUTS + 1;
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        this.validateSlot(slot);
        return slot == AltarRecipeSerializer.MAX_INPUTS ? this.catalyst : this.items.get(slot);
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        return simulate ? ItemStack.EMPTY : this.parent.handleInteraction(null, stack);
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return ItemStack.EMPTY;
    }

    @Override
    public int getSlotLimit(int slot) {
        this.validateSlot(slot);
        return slot == AltarRecipeSerializer.MAX_INPUTS ? 1 : 64;
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return true;
    }

    @Override
    public List<ItemStack> getNoneEmptyItems() {
        return (List<ItemStack>) this.items.stream().filter(stack -> !stack.isEmpty()).collect(Collectors.toList());
    }

    @Override
    public ItemStack getCatalyst() {
        return this.catalyst;
    }

    public void setCatalyst(ItemStack catalyst) {
        this.catalyst = catalyst;
        this.onContentsChanged();
    }

    public VanillaWrapper getVanillaInv() {
        return this.vanillaInv;
    }

    private List<ItemStack> createItemBackup() {
        List<ItemStack> backup = new ArrayList();
        for (ItemStack stack : this.items) {
            if (!stack.isEmpty()) {
                backup.add(stack.copy());
            }
        }
        return backup;
    }
}