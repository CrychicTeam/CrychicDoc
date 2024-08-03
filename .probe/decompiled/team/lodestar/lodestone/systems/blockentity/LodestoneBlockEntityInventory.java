package team.lodestar.lodestone.systems.blockentity;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import team.lodestar.lodestone.helpers.BlockHelper;

public class LodestoneBlockEntityInventory extends ItemStackHandler {

    public final int slotCount;

    public final int allowedItemSize;

    public Predicate<ItemStack> inputPredicate;

    public Predicate<ItemStack> outputPredicate;

    public final LazyOptional<IItemHandler> inventoryOptional = LazyOptional.of(() -> this);

    public ArrayList<ItemStack> nonEmptyItemStacks = new ArrayList();

    public int emptyItemAmount;

    public int nonEmptyItemAmount;

    public int firstEmptyItemIndex;

    public LodestoneBlockEntityInventory(int slotCount, int allowedItemSize, Predicate<ItemStack> inputPredicate, Predicate<ItemStack> outputPredicate) {
        this(slotCount, allowedItemSize, inputPredicate);
        this.outputPredicate = outputPredicate;
    }

    public LodestoneBlockEntityInventory(int slotCount, int allowedItemSize, Predicate<ItemStack> inputPredicate) {
        this(slotCount, allowedItemSize);
        this.inputPredicate = inputPredicate;
    }

    public LodestoneBlockEntityInventory(int slotCount, int allowedItemSize) {
        super(slotCount);
        this.slotCount = slotCount;
        this.allowedItemSize = allowedItemSize;
        this.updateData();
    }

    @Override
    public void onContentsChanged(int slot) {
        this.updateData();
    }

    @Override
    public int getSlots() {
        return this.slotCount;
    }

    @Override
    public int getSlotLimit(int slot) {
        return this.allowedItemSize;
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return this.inputPredicate != null && !this.inputPredicate.test(stack) ? false : super.isItemValid(slot, stack);
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return this.outputPredicate != null && !this.outputPredicate.test(super.extractItem(slot, amount, true)) ? ItemStack.EMPTY : super.extractItem(slot, amount, simulate);
    }

    public void updateData() {
        NonNullList<ItemStack> stacks = this.getStacks();
        this.nonEmptyItemStacks = (ArrayList<ItemStack>) stacks.stream().filter(s -> !s.isEmpty()).collect(Collectors.toCollection(ArrayList::new));
        this.nonEmptyItemAmount = this.nonEmptyItemStacks.size();
        this.emptyItemAmount = (int) stacks.stream().filter(ItemStack::m_41619_).count();
        for (int i = 0; i < stacks.size(); i++) {
            ItemStack stack = stacks.get(i);
            if (stack.isEmpty()) {
                this.firstEmptyItemIndex = i;
                return;
            }
        }
        this.firstEmptyItemIndex = -1;
    }

    public void load(CompoundTag compound) {
        this.load(compound, "inventory");
    }

    public void load(CompoundTag compound, String name) {
        this.deserializeNBT(compound.getCompound(name));
        if (this.stacks.size() != this.slotCount) {
            int missing = this.slotCount - this.stacks.size();
            for (int i = 0; i < missing; i++) {
                this.stacks.add(ItemStack.EMPTY);
            }
        }
        this.updateData();
    }

    public void save(CompoundTag compound) {
        this.save(compound, "inventory");
    }

    public void save(CompoundTag compound, String name) {
        compound.put(name, this.serializeNBT());
    }

    public NonNullList<ItemStack> getStacks() {
        return this.stacks;
    }

    public boolean isEmpty() {
        return this.nonEmptyItemAmount == 0;
    }

    public void clear() {
        for (int i = 0; i < this.slotCount; i++) {
            this.setStackInSlot(i, ItemStack.EMPTY);
        }
    }

    public void dumpItems(Level level, BlockPos pos) {
        this.dumpItems(level, BlockHelper.fromBlockPos(pos).add(0.5, 0.5, 0.5));
    }

    public void dumpItems(Level level, Vec3 pos) {
        for (int i = 0; i < this.slotCount; i++) {
            if (!this.getStackInSlot(i).isEmpty()) {
                level.m_7967_(new ItemEntity(level, pos.x(), pos.y(), pos.z(), this.getStackInSlot(i)));
            }
            this.setStackInSlot(i, ItemStack.EMPTY);
        }
    }

    public ItemStack interact(Level level, Player player, InteractionHand handIn) {
        if (level.isClientSide) {
            return ItemStack.EMPTY;
        } else {
            ItemStack held = player.m_21120_(handIn);
            player.m_21011_(handIn, true);
            int size = this.nonEmptyItemStacks.size() - 1;
            if ((held.isEmpty() || this.firstEmptyItemIndex == -1) && size != -1) {
                ItemStack takeOutStack = (ItemStack) this.nonEmptyItemStacks.get(size);
                if (takeOutStack.getItem().equals(held.getItem())) {
                    return this.insertItem(player, held);
                } else {
                    ItemStack extractedStack = this.extractItem(level, held, player);
                    boolean success = !extractedStack.isEmpty();
                    if (success) {
                        this.insertItem(player, held);
                    }
                    return extractedStack;
                }
            } else {
                return this.insertItem(player, held);
            }
        }
    }

    public ItemStack extractItem(Level level, ItemStack heldStack, Player player) {
        if (!level.isClientSide) {
            List<ItemStack> nonEmptyStacks = this.nonEmptyItemStacks;
            if (nonEmptyStacks.isEmpty()) {
                return heldStack;
            } else {
                ItemStack takeOutStack = (ItemStack) nonEmptyStacks.get(nonEmptyStacks.size() - 1);
                int slot = this.stacks.indexOf(takeOutStack);
                if (this.extractItem(slot, takeOutStack.getCount(), true).equals(ItemStack.EMPTY)) {
                    return heldStack;
                } else {
                    this.extractItem(player, takeOutStack, slot);
                    return takeOutStack;
                }
            }
        } else {
            return ItemStack.EMPTY;
        }
    }

    public void extractItem(Player playerEntity, ItemStack stack, int slot) {
        ItemHandlerHelper.giveItemToPlayer(playerEntity, stack);
        this.setStackInSlot(slot, ItemStack.EMPTY);
    }

    public ItemStack insertItem(Player playerEntity, ItemStack stack) {
        return this.insertItem(stack);
    }

    public ItemStack insertItem(ItemStack stack) {
        if (!stack.isEmpty()) {
            ItemStack simulate = this.insertItem(stack, true);
            if (simulate.equals(stack)) {
                return ItemStack.EMPTY;
            } else {
                int count = stack.getCount() - simulate.getCount();
                if (count > this.allowedItemSize) {
                    count = this.allowedItemSize;
                }
                ItemStack input = stack.split(count);
                this.insertItem(input, false);
                return input;
            }
        } else {
            return ItemStack.EMPTY;
        }
    }

    public ItemStack insertItem(ItemStack stack, boolean simulate) {
        return ItemHandlerHelper.insertItem(this, stack, simulate);
    }
}