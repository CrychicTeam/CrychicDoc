package com.rekindled.embers.blockentity;

import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.api.filter.FilterItem;
import com.rekindled.embers.api.filter.IFilter;
import com.rekindled.embers.api.item.IFilterItem;
import com.rekindled.embers.particle.VaporParticleOptions;
import com.rekindled.embers.util.FilterUtil;
import com.rekindled.embers.util.Misc;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.joml.Vector3f;

public class ItemTransferBlockEntity extends ItemPipeBlockEntityBase {

    public static final int PRIORITY_TRANSFER = -10;

    public ItemStack filterItem = ItemStack.EMPTY;

    Random random = new Random();

    public boolean syncFilter = true;

    IItemHandler outputSide;

    public LazyOptional<IItemHandler> outputHolder = LazyOptional.of(() -> this.outputSide);

    IFilter filter = FilterUtil.FILTER_ANY;

    public ItemTransferBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(RegistryManager.ITEM_TRANSFER_ENTITY.get(), pPos, pBlockState);
        this.syncConnections = false;
        this.saveConnections = false;
    }

    @Override
    protected void initInventory() {
        this.inventory = new ItemStackHandler(1) {

            @Override
            public int getSlotLimit(int slot) {
                return ItemTransferBlockEntity.this.getCapacity();
            }

            @Override
            protected void onContentsChanged(int slot) {
                ItemTransferBlockEntity.this.m_6596_();
            }

            @Override
            public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
                return ItemTransferBlockEntity.this.acceptsItem(stack) ? super.insertItem(slot, stack, simulate) : stack;
            }

            @Override
            public boolean isItemValid(int slot, ItemStack stack) {
                return ItemTransferBlockEntity.this.acceptsItem(stack);
            }
        };
        this.outputSide = Misc.makeRestrictedItemHandler(this.inventory, false, true);
    }

    public boolean acceptsItem(ItemStack stack) {
        return this.filter.acceptsItem(stack);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        if (nbt.contains("filter")) {
            this.filterItem = ItemStack.of(nbt.getCompound("filter"));
        }
        this.setupFilter();
    }

    @Override
    protected boolean requiresSync() {
        return this.syncFilter || super.requiresSync();
    }

    @Override
    protected void resetSync() {
        super.resetSync();
        this.syncFilter = false;
    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        this.writeFilter(nbt);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag nbt = super.m_5995_();
        if (this.syncFilter) {
            this.writeFilter(nbt);
        }
        return nbt;
    }

    private void writeFilter(CompoundTag nbt) {
        nbt.put("filter", this.filterItem.serializeNBT());
    }

    public void setupFilter() {
        Item item = this.filterItem.getItem();
        if (item instanceof IFilterItem) {
            this.filter = ((IFilterItem) item).getFilter(this.filterItem);
        } else if (!this.filterItem.isEmpty()) {
            this.filter = new FilterItem(this.filterItem);
        } else {
            this.filter = FilterUtil.FILTER_ANY;
        }
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, ItemTransferBlockEntity blockEntity) {
        if (level instanceof ServerLevel && blockEntity.clogged && blockEntity.isAnySideUnclogged()) {
            Random posRand = new Random(pos.asLong());
            double angleA = posRand.nextDouble() * Math.PI * 2.0;
            double angleB = posRand.nextDouble() * Math.PI * 2.0;
            float xOffset = (float) (Math.cos(angleA) * Math.cos(angleB));
            float yOffset = (float) (Math.sin(angleA) * Math.cos(angleB));
            float zOffset = (float) Math.sin(angleB);
            float speed = 0.1F;
            float vx = xOffset * speed + posRand.nextFloat() * speed * 0.3F;
            float vy = yOffset * speed + posRand.nextFloat() * speed * 0.3F;
            float vz = zOffset * speed + posRand.nextFloat() * speed * 0.3F;
            ((ServerLevel) level).sendParticles(new VaporParticleOptions(new Vector3f(0.2509804F, 0.2509804F, 0.2509804F), new Vec3((double) vx, (double) vy, (double) vz), 1.0F), (double) pos.m_123341_() + 0.5, (double) pos.m_123342_() + 0.5, (double) pos.m_123343_() + 0.5, 4, 0.0, 0.0, 0.0, 1.0);
        }
        ItemPipeBlockEntityBase.serverTick(level, pos, state, blockEntity);
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (!this.f_58859_ && cap == ForgeCapabilities.ITEM_HANDLER) {
            if (side == null) {
                return ForgeCapabilities.ITEM_HANDLER.orEmpty(cap, this.holder);
            }
            if (this.f_58857_.getBlockState(this.m_58899_()).m_61138_(BlockStateProperties.FACING)) {
                Direction facing = (Direction) this.f_58857_.getBlockState(this.m_58899_()).m_61143_(BlockStateProperties.FACING);
                if (side.getOpposite() == facing) {
                    return ForgeCapabilities.ITEM_HANDLER.orEmpty(cap, this.outputHolder);
                }
                if (side.getAxis() == facing.getAxis()) {
                    return ForgeCapabilities.ITEM_HANDLER.orEmpty(cap, this.holder);
                }
            }
        }
        return LazyOptional.empty();
    }

    @Override
    public int getCapacity() {
        return 4;
    }

    @Override
    public int getPriority(Direction facing) {
        return -10;
    }

    @Override
    public PipeBlockEntityBase.PipeConnection getConnection(Direction facing) {
        BlockState state = this.f_58857_.getBlockState(this.m_58899_());
        return state.m_61138_(BlockStateProperties.FACING) && ((Direction) state.m_61143_(BlockStateProperties.FACING)).getAxis() == facing.getAxis() ? PipeBlockEntityBase.PipeConnection.PIPE : PipeBlockEntityBase.PipeConnection.NONE;
    }

    @Override
    protected boolean isFrom(Direction facing) {
        return this.f_58857_.getBlockState(this.m_58899_()).m_61143_(BlockStateProperties.FACING) == facing;
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        this.outputHolder.invalidate();
    }
}