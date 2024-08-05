package com.rekindled.embers.blockentity;

import com.rekindled.embers.util.PipePriorityMap;
import java.util.ArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public abstract class ItemPipeBlockEntityBase extends PipeBlockEntityBase implements IItemPipePriority {

    public ItemStackHandler inventory;

    public LazyOptional<IItemHandler> holder = LazyOptional.of(() -> this.inventory);

    public ItemPipeBlockEntityBase(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
        this.initInventory();
    }

    protected void initInventory() {
        this.inventory = new ItemStackHandler(1) {

            @Override
            public int getSlotLimit(int slot) {
                return ItemPipeBlockEntityBase.this.getCapacity();
            }

            @Override
            protected void onContentsChanged(int slot) {
                ItemPipeBlockEntityBase.this.m_6596_();
            }
        };
    }

    public abstract int getCapacity();

    @Override
    public int getPriority(Direction facing) {
        return 0;
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, ItemPipeBlockEntityBase blockEntity) {
        if (!blockEntity.loaded) {
            blockEntity.initConnections();
        }
        blockEntity.ticksExisted++;
        boolean itemsMoved = false;
        ItemStack passStack = blockEntity.inventory.extractItem(0, 1, true);
        if (!passStack.isEmpty()) {
            PipePriorityMap<Integer, Direction> possibleDirections = new PipePriorityMap<>();
            IItemHandler[] itemHandlers = new IItemHandler[Direction.values().length];
            for (Direction facing : Direction.values()) {
                if (blockEntity.getConnection(facing).transfer && !blockEntity.isFrom(facing)) {
                    BlockEntity tile = level.getBlockEntity(pos.relative(facing));
                    if (tile != null) {
                        IItemHandler handler = (IItemHandler) tile.getCapability(ForgeCapabilities.ITEM_HANDLER, facing.getOpposite()).orElse(null);
                        if (handler != null) {
                            int priority = 0;
                            if (tile instanceof IItemPipePriority) {
                                priority = ((IItemPipePriority) tile).getPriority(facing.getOpposite());
                            }
                            if (blockEntity.isFrom(facing.getOpposite())) {
                                priority -= 5;
                            }
                            possibleDirections.put(priority, facing);
                            itemHandlers[facing.get3DDataValue()] = handler;
                        }
                    }
                }
            }
            for (int key : possibleDirections.keySet()) {
                ArrayList<Direction> list = possibleDirections.get(key);
                for (int i = 0; i < list.size(); i++) {
                    Direction facingx = (Direction) list.get((i + blockEntity.lastRobin) % list.size());
                    IItemHandler handler = itemHandlers[facingx.get3DDataValue()];
                    itemsMoved = blockEntity.pushStack(passStack, facingx, handler);
                    if (blockEntity.lastTransfer != facingx) {
                        blockEntity.lastTransfer = facingx;
                        blockEntity.syncTransfer = true;
                        blockEntity.m_6596_();
                    }
                    if (itemsMoved) {
                        blockEntity.lastRobin++;
                        break;
                    }
                }
                if (itemsMoved) {
                    break;
                }
            }
        }
        if (blockEntity.inventory.getStackInSlot(0).isEmpty()) {
            if (blockEntity.lastTransfer != null && !itemsMoved) {
                blockEntity.lastTransfer = null;
                blockEntity.syncTransfer = true;
                blockEntity.m_6596_();
            }
            itemsMoved = true;
            blockEntity.resetFrom();
        }
        if (blockEntity.clogged == itemsMoved) {
            blockEntity.clogged = !itemsMoved;
            blockEntity.syncCloggedFlag = true;
            blockEntity.m_6596_();
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void clientTick(Level level, BlockPos pos, BlockState state, ItemPipeBlockEntityBase blockEntity) {
        PipeBlockEntityBase.clientTick(level, pos, state, blockEntity);
    }

    private boolean pushStack(ItemStack passStack, Direction facing, IItemHandler handler) {
        int slot = -1;
        for (int j = 0; j < handler.getSlots() && slot == -1; j++) {
            if (handler.insertItem(j, passStack, true).isEmpty()) {
                slot = j;
            }
        }
        if (slot != -1) {
            ItemStack added = handler.insertItem(slot, passStack, false);
            if (added.isEmpty()) {
                this.inventory.extractItem(0, 1, false);
                return true;
            }
        }
        if (this.isFrom(facing)) {
            this.setFrom(facing, false);
        }
        return false;
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        if (nbt.contains("inventory")) {
            this.inventory.deserializeNBT(nbt.getCompound("inventory"));
        }
    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        this.writeInventory(nbt);
    }

    public void writeInventory(CompoundTag nbt) {
        nbt.put("inventory", this.inventory.serializeNBT());
    }

    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return !this.f_58859_ && cap == ForgeCapabilities.ITEM_HANDLER ? ForgeCapabilities.ITEM_HANDLER.orEmpty(cap, this.holder) : super.getCapability(cap, side);
    }

    public void invalidateCaps() {
        super.invalidateCaps();
        this.holder.invalidate();
    }
}