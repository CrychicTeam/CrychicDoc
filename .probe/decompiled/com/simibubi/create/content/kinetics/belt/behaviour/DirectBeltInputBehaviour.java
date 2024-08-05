package com.simibubi.create.content.kinetics.belt.behaviour;

import com.simibubi.create.content.kinetics.belt.transport.TransportedItemStack;
import com.simibubi.create.content.logistics.funnel.BeltFunnelBlock;
import com.simibubi.create.content.logistics.funnel.FunnelBlock;
import com.simibubi.create.content.logistics.funnel.FunnelBlockEntity;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

public class DirectBeltInputBehaviour extends BlockEntityBehaviour {

    public static final BehaviourType<DirectBeltInputBehaviour> TYPE = new BehaviourType<>();

    private DirectBeltInputBehaviour.InsertionCallback tryInsert = this::defaultInsertionCallback;

    private DirectBeltInputBehaviour.OccupiedPredicate isOccupied;

    private DirectBeltInputBehaviour.AvailabilityPredicate canInsert = d -> true;

    private Supplier<Boolean> supportsBeltFunnels;

    public DirectBeltInputBehaviour(SmartBlockEntity be) {
        super(be);
        this.isOccupied = d -> false;
        this.supportsBeltFunnels = () -> false;
    }

    public DirectBeltInputBehaviour allowingBeltFunnelsWhen(Supplier<Boolean> pred) {
        this.supportsBeltFunnels = pred;
        return this;
    }

    public DirectBeltInputBehaviour allowingBeltFunnels() {
        this.supportsBeltFunnels = () -> true;
        return this;
    }

    public DirectBeltInputBehaviour onlyInsertWhen(DirectBeltInputBehaviour.AvailabilityPredicate pred) {
        this.canInsert = pred;
        return this;
    }

    public DirectBeltInputBehaviour considerOccupiedWhen(DirectBeltInputBehaviour.OccupiedPredicate pred) {
        this.isOccupied = pred;
        return this;
    }

    public DirectBeltInputBehaviour setInsertionHandler(DirectBeltInputBehaviour.InsertionCallback callback) {
        this.tryInsert = callback;
        return this;
    }

    private ItemStack defaultInsertionCallback(TransportedItemStack inserted, Direction side, boolean simulate) {
        LazyOptional<IItemHandler> lazy = this.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER, side);
        return !lazy.isPresent() ? inserted.stack : ItemHandlerHelper.insertItemStacked(lazy.orElse(null), inserted.stack.copy(), simulate);
    }

    public boolean canInsertFromSide(Direction side) {
        return this.canInsert.test(side);
    }

    public boolean isOccupied(Direction side) {
        return this.isOccupied.test(side);
    }

    public ItemStack handleInsertion(ItemStack stack, Direction side, boolean simulate) {
        return this.handleInsertion(new TransportedItemStack(stack), side, simulate);
    }

    public ItemStack handleInsertion(TransportedItemStack stack, Direction side, boolean simulate) {
        return this.tryInsert.apply(stack, side, simulate);
    }

    @Override
    public BehaviourType<?> getType() {
        return TYPE;
    }

    @Nullable
    public ItemStack tryExportingToBeltFunnel(ItemStack stack, @Nullable Direction side, boolean simulate) {
        BlockPos funnelPos = this.blockEntity.m_58899_().above();
        Level world = this.getWorld();
        BlockState funnelState = world.getBlockState(funnelPos);
        if (!(funnelState.m_60734_() instanceof BeltFunnelBlock)) {
            return null;
        } else if (funnelState.m_61143_(BeltFunnelBlock.SHAPE) != BeltFunnelBlock.Shape.PULLING) {
            return null;
        } else if (side != null && FunnelBlock.getFunnelFacing(funnelState) != side) {
            return null;
        } else {
            BlockEntity be = world.getBlockEntity(funnelPos);
            if (!(be instanceof FunnelBlockEntity)) {
                return null;
            } else if ((Boolean) funnelState.m_61143_(BeltFunnelBlock.POWERED)) {
                return stack;
            } else {
                ItemStack insert = FunnelBlock.tryInsert(world, funnelPos, stack, simulate);
                if (insert.getCount() != stack.getCount() && !simulate) {
                    ((FunnelBlockEntity) be).flap(true);
                }
                return insert;
            }
        }
    }

    public boolean canSupportBeltFunnels() {
        return (Boolean) this.supportsBeltFunnels.get();
    }

    @FunctionalInterface
    public interface AvailabilityPredicate {

        boolean test(Direction var1);
    }

    @FunctionalInterface
    public interface InsertionCallback {

        ItemStack apply(TransportedItemStack var1, Direction var2, boolean var3);
    }

    @FunctionalInterface
    public interface OccupiedPredicate {

        boolean test(Direction var1);
    }
}