package dev.ftb.mods.ftbquests.block.forge;

import dev.ftb.mods.ftbquests.block.TaskScreenBlock;
import dev.ftb.mods.ftbquests.block.entity.TaskScreenBlockEntity;
import dev.ftb.mods.ftbquests.integration.item_filtering.ItemMatchingSystem;
import dev.ftb.mods.ftbquests.quest.TeamData;
import dev.ftb.mods.ftbquests.quest.task.FluidTask;
import dev.ftb.mods.ftbquests.quest.task.ItemTask;
import dev.ftb.mods.ftbquests.quest.task.forge.ForgeEnergyTask;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ForgeTaskScreenBlockEntity extends TaskScreenBlockEntity {

    private AABB cachedRenderAABB = null;

    private final LazyOptional<IItemHandler> itemCap = LazyOptional.of(() -> new ForgeTaskScreenBlockEntity.TaskItemHandler());

    private final LazyOptional<IFluidHandler> fluidCap = LazyOptional.of(() -> new ForgeTaskScreenBlockEntity.TaskFluidHandler());

    private final LazyOptional<IEnergyStorage> energyCap = LazyOptional.of(() -> new ForgeTaskScreenBlockEntity.TaskForgeEnergyHandler());

    public ForgeTaskScreenBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(blockPos, blockState);
    }

    public AABB getRenderBoundingBox() {
        if (this.cachedRenderAABB == null) {
            AABB box = super.getRenderBoundingBox();
            if (this.m_58900_().m_60734_() instanceof TaskScreenBlock tsb && tsb.getSize() != 1) {
                this.cachedRenderAABB = box.inflate((double) tsb.getSize());
                return this.cachedRenderAABB;
            }
            this.cachedRenderAABB = box;
        }
        return this.cachedRenderAABB;
    }

    public void invalidateCaps() {
        super.invalidateCaps();
        this.itemCap.invalidate();
        this.fluidCap.invalidate();
        this.energyCap.invalidate();
    }

    @NotNull
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return this.itemCap.cast();
        } else if (cap == ForgeCapabilities.FLUID_HANDLER) {
            return this.fluidCap.cast();
        } else {
            return cap == ForgeCapabilities.ENERGY ? this.energyCap.cast() : super.getCapability(cap, side);
        }
    }

    private class TaskFluidHandler implements IFluidHandler {

        @Override
        public int getTanks() {
            return 1;
        }

        @NotNull
        @Override
        public FluidStack getFluidInTank(int tank) {
            return tank == 0 && ForgeTaskScreenBlockEntity.this.getTask() instanceof FluidTask task ? new FluidStack(task.getFluid(), (int) ForgeTaskScreenBlockEntity.this.getCachedTeamData().getProgress(task)) : FluidStack.EMPTY;
        }

        @Override
        public int getTankCapacity(int tank) {
            return tank == 0 && ForgeTaskScreenBlockEntity.this.getTask() instanceof FluidTask task ? (int) task.getMaxProgress() : 0;
        }

        @Override
        public boolean isFluidValid(int tank, @NotNull FluidStack fluidStack) {
            return tank == 0 && ForgeTaskScreenBlockEntity.this.getTask() instanceof FluidTask task && task.getFluid() == fluidStack.getFluid() && (task.getFluidNBT() == null || task.getFluidNBT().equals(fluidStack.getTag()));
        }

        @Override
        public int fill(FluidStack fluidStack, IFluidHandler.FluidAction fluidAction) {
            if (ForgeTaskScreenBlockEntity.this.getTask() instanceof FluidTask task && this.isFluidValid(0, fluidStack)) {
                TeamData data = ForgeTaskScreenBlockEntity.this.getCachedTeamData();
                if (data != null && data.canStartTasks(task.getQuest()) && !data.isCompleted(task)) {
                    long space = task.getMaxProgress() - data.getProgress(task);
                    long toAdd = Math.min((long) fluidStack.getAmount(), space);
                    if (fluidAction.execute()) {
                        data.addProgress(task, Math.min((long) fluidStack.getAmount(), toAdd));
                    }
                    return (int) toAdd;
                }
            }
            return 0;
        }

        @NotNull
        @Override
        public FluidStack drain(FluidStack fluidStack, IFluidHandler.FluidAction fluidAction) {
            if (ForgeTaskScreenBlockEntity.this.getTask() instanceof FluidTask task && fluidStack.getFluid() == task.getFluid()) {
                return this.drain(fluidStack.getAmount(), fluidAction);
            }
            return FluidStack.EMPTY;
        }

        @NotNull
        @Override
        public FluidStack drain(int maxDrain, IFluidHandler.FluidAction fluidAction) {
            if (ForgeTaskScreenBlockEntity.this.getTask() instanceof FluidTask task) {
                TeamData data = ForgeTaskScreenBlockEntity.this.getCachedTeamData();
                if (data != null && data.canStartTasks(task.getQuest()) && !data.isCompleted(task)) {
                    long toTake = Math.min((long) maxDrain, data.getProgress(task));
                    if (fluidAction.execute()) {
                        data.addProgress(task, -toTake);
                    }
                    FluidStack result = new FluidStack(task.getFluid(), (int) toTake);
                    if (task.getFluidNBT() != null) {
                        result.setTag(task.getFluidNBT().copy());
                    }
                    return result;
                }
            }
            return FluidStack.EMPTY;
        }
    }

    private class TaskForgeEnergyHandler implements IEnergyStorage {

        @Override
        public int receiveEnergy(int amount, boolean simulate) {
            if (ForgeTaskScreenBlockEntity.this.getTask() instanceof ForgeEnergyTask task) {
                TeamData data = ForgeTaskScreenBlockEntity.this.getCachedTeamData();
                if (data != null && data.canStartTasks(task.getQuest()) && !data.isCompleted(task)) {
                    long space = task.getMaxProgress() - data.getProgress(task);
                    long toAdd = Math.min(task.getMaxInput(), Math.min((long) amount, space));
                    if (!simulate) {
                        data.addProgress(task, toAdd);
                    }
                    return (int) toAdd;
                }
            }
            return 0;
        }

        @Override
        public int extractEnergy(int amount, boolean simulate) {
            return 0;
        }

        @Override
        public int getEnergyStored() {
            return ForgeTaskScreenBlockEntity.this.getTask() instanceof ForgeEnergyTask task ? (int) ForgeTaskScreenBlockEntity.this.getCachedTeamData().getProgress(task) : 0;
        }

        @Override
        public int getMaxEnergyStored() {
            return ForgeTaskScreenBlockEntity.this.getTask() instanceof ForgeEnergyTask task ? (int) task.getValue() : 0;
        }

        @Override
        public boolean canExtract() {
            return false;
        }

        @Override
        public boolean canReceive() {
            return true;
        }
    }

    private class TaskItemHandler implements IItemHandler {

        @Override
        public int getSlots() {
            return 2;
        }

        @NotNull
        @Override
        public ItemStack getStackInSlot(int slot) {
            if (ForgeTaskScreenBlockEntity.this.getTask() instanceof ItemTask t && slot == 0) {
                return ItemHandlerHelper.copyStackWithSize(t.getItemStack(), (int) Math.min(ForgeTaskScreenBlockEntity.this.getCachedTeamData().getProgress(t), (long) t.getItemStack().getMaxStackSize()));
            }
            return ItemStack.EMPTY;
        }

        @NotNull
        @Override
        public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            TeamData data = ForgeTaskScreenBlockEntity.this.getCachedTeamData();
            if (ForgeTaskScreenBlockEntity.this.getTask() instanceof ItemTask task && data.canStartTasks(task.getQuest())) {
                return task.insert(data, stack, simulate);
            }
            return stack;
        }

        @NotNull
        @Override
        public ItemStack extractItem(int slot, int count, boolean simulate) {
            if (!ForgeTaskScreenBlockEntity.this.isInputOnly() && ForgeTaskScreenBlockEntity.this.getTask() instanceof ItemTask task && !ItemMatchingSystem.INSTANCE.isItemFilter(task.getItemStack())) {
                TeamData data = ForgeTaskScreenBlockEntity.this.getCachedTeamData();
                if (data != null && data.canStartTasks(task.getQuest()) && !data.isCompleted(task)) {
                    int itemsRemoved = (int) Math.min(data.getProgress(task), (long) count);
                    if (!simulate) {
                        data.addProgress(task, (long) (-itemsRemoved));
                    }
                    return ItemHandlerHelper.copyStackWithSize(task.getItemStack(), itemsRemoved);
                }
            }
            return ItemStack.EMPTY;
        }

        @Override
        public int getSlotLimit(int slot) {
            return ForgeTaskScreenBlockEntity.this.getTask() instanceof ItemTask itemTask ? itemTask.getItemStack().getMaxStackSize() : 0;
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            if (ForgeTaskScreenBlockEntity.this.getTask() instanceof ItemTask t && t.test(stack)) {
                return true;
            }
            return false;
        }
    }
}