package com.simibubi.create.foundation.blockEntity.behaviour.inventory;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringBehaviour;
import com.simibubi.create.foundation.item.ItemHelper;
import com.simibubi.create.foundation.utility.BlockFace;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

public abstract class CapManipulationBehaviourBase<T, S extends CapManipulationBehaviourBase<?, ?>> extends BlockEntityBehaviour {

    protected CapManipulationBehaviourBase.InterfaceProvider target;

    protected LazyOptional<T> targetCapability;

    protected boolean simulateNext;

    protected boolean bypassSided;

    private boolean findNewNextTick;

    public CapManipulationBehaviourBase(SmartBlockEntity be, CapManipulationBehaviourBase.InterfaceProvider target) {
        super(be);
        this.setLazyTickRate(5);
        this.target = target;
        this.targetCapability = LazyOptional.empty();
        this.simulateNext = false;
        this.bypassSided = false;
    }

    protected abstract Capability<T> capability();

    @Override
    public void initialize() {
        super.initialize();
        this.findNewNextTick = true;
    }

    @Override
    public void onNeighborChanged(BlockPos neighborPos) {
        BlockFace targetBlockFace = this.target.getTarget(this.getWorld(), this.blockEntity.m_58899_(), this.blockEntity.m_58900_());
        if (targetBlockFace.getConnectedPos().equals(neighborPos)) {
            this.onHandlerInvalidated(this.targetCapability);
        }
    }

    public S bypassSidedness() {
        this.bypassSided = true;
        return (S) this;
    }

    public S simulate() {
        this.simulateNext = true;
        return (S) this;
    }

    public boolean hasInventory() {
        return this.targetCapability.isPresent();
    }

    @Nullable
    public T getInventory() {
        return this.targetCapability.orElse(null);
    }

    protected void onHandlerInvalidated(LazyOptional<T> handler) {
        this.findNewNextTick = true;
        this.targetCapability = LazyOptional.empty();
    }

    @Override
    public void lazyTick() {
        super.lazyTick();
        if (!this.targetCapability.isPresent()) {
            this.findNewCapability();
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.findNewNextTick || this.getWorld().getGameTime() % 64L == 0L) {
            this.findNewNextTick = false;
            this.findNewCapability();
        }
    }

    public int getAmountFromFilter() {
        int amount = -1;
        FilteringBehaviour filter = this.blockEntity.getBehaviour(FilteringBehaviour.TYPE);
        if (filter != null && !filter.anyAmount()) {
            amount = filter.getAmount();
        }
        return amount;
    }

    public ItemHelper.ExtractionCountMode getModeFromFilter() {
        ItemHelper.ExtractionCountMode mode = ItemHelper.ExtractionCountMode.UPTO;
        FilteringBehaviour filter = this.blockEntity.getBehaviour(FilteringBehaviour.TYPE);
        if (filter != null && !filter.upTo) {
            mode = ItemHelper.ExtractionCountMode.EXACTLY;
        }
        return mode;
    }

    public void findNewCapability() {
        Level world = this.getWorld();
        BlockFace targetBlockFace = this.target.getTarget(world, this.blockEntity.m_58899_(), this.blockEntity.m_58900_()).getOpposite();
        BlockPos pos = targetBlockFace.getPos();
        this.targetCapability = LazyOptional.empty();
        if (world.isLoaded(pos)) {
            BlockEntity invBE = world.getBlockEntity(pos);
            if (invBE != null) {
                Capability<T> capability = this.capability();
                this.targetCapability = this.bypassSided ? invBE.getCapability(capability) : invBE.getCapability(capability, targetBlockFace.getFace());
                if (this.targetCapability.isPresent()) {
                    this.targetCapability.addListener(this::onHandlerInvalidated);
                }
            }
        }
    }

    @FunctionalInterface
    public interface InterfaceProvider {

        static CapManipulationBehaviourBase.InterfaceProvider towardBlockFacing() {
            return (w, p, s) -> new BlockFace(p, s.m_61138_(BlockStateProperties.FACING) ? (Direction) s.m_61143_(BlockStateProperties.FACING) : (Direction) s.m_61143_(BlockStateProperties.HORIZONTAL_FACING));
        }

        static CapManipulationBehaviourBase.InterfaceProvider oppositeOfBlockFacing() {
            return (w, p, s) -> new BlockFace(p, (s.m_61138_(BlockStateProperties.FACING) ? (Direction) s.m_61143_(BlockStateProperties.FACING) : (Direction) s.m_61143_(BlockStateProperties.HORIZONTAL_FACING)).getOpposite());
        }

        BlockFace getTarget(Level var1, BlockPos var2, BlockState var3);
    }
}