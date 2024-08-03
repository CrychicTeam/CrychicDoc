package com.simibubi.create.content.logistics.depot;

import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.kinetics.belt.BeltHelper;
import com.simibubi.create.content.kinetics.belt.behaviour.BeltProcessingBehaviour;
import com.simibubi.create.content.kinetics.belt.behaviour.DirectBeltInputBehaviour;
import com.simibubi.create.content.kinetics.belt.behaviour.TransportedItemStackHandlerBehaviour;
import com.simibubi.create.content.kinetics.belt.transport.TransportedItemStack;
import com.simibubi.create.content.logistics.funnel.AbstractFunnelBlock;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.item.ItemHelper;
import com.simibubi.create.foundation.utility.NBTHelper;
import com.simibubi.create.foundation.utility.VecHelper;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

public class DepotBehaviour extends BlockEntityBehaviour {

    public static final BehaviourType<DepotBehaviour> TYPE = new BehaviourType<>();

    TransportedItemStack heldItem;

    List<TransportedItemStack> incoming;

    ItemStackHandler processingOutputBuffer;

    DepotItemHandler itemHandler;

    LazyOptional<DepotItemHandler> lazyItemHandler;

    TransportedItemStackHandlerBehaviour transportedHandler;

    Supplier<Integer> maxStackSize = () -> 64;

    Supplier<Boolean> canAcceptItems = () -> true;

    Predicate<Direction> canFunnelsPullFrom = $ -> true;

    Consumer<ItemStack> onHeldInserted;

    Predicate<ItemStack> acceptedItems = $ -> true;

    boolean allowMerge;

    public DepotBehaviour(final SmartBlockEntity be) {
        super(be);
        this.onHeldInserted = $ -> {
        };
        this.incoming = new ArrayList();
        this.itemHandler = new DepotItemHandler(this);
        this.lazyItemHandler = LazyOptional.of(() -> this.itemHandler);
        this.processingOutputBuffer = new ItemStackHandler(8) {

            @Override
            protected void onContentsChanged(int slot) {
                be.notifyUpdate();
            }
        };
    }

    public void enableMerging() {
        this.allowMerge = true;
    }

    public DepotBehaviour withCallback(Consumer<ItemStack> changeListener) {
        this.onHeldInserted = changeListener;
        return this;
    }

    public DepotBehaviour onlyAccepts(Predicate<ItemStack> filter) {
        this.acceptedItems = filter;
        return this;
    }

    @Override
    public void tick() {
        super.tick();
        Level world = this.blockEntity.m_58904_();
        Iterator<TransportedItemStack> iterator = this.incoming.iterator();
        while (iterator.hasNext()) {
            TransportedItemStack ts = (TransportedItemStack) iterator.next();
            if (this.tick(ts) && (!world.isClientSide || this.blockEntity.isVirtual())) {
                if (this.heldItem == null) {
                    this.heldItem = ts;
                } else if (!ItemHelper.canItemStackAmountsStack(this.heldItem.stack, ts.stack)) {
                    Vec3 vec = VecHelper.getCenterOf(this.blockEntity.m_58899_());
                    Containers.dropItemStack(this.blockEntity.m_58904_(), vec.x, vec.y + 0.5, vec.z, ts.stack);
                } else {
                    this.heldItem.stack.grow(ts.stack.getCount());
                }
                iterator.remove();
                this.blockEntity.notifyUpdate();
            }
        }
        if (this.heldItem != null) {
            if (this.tick(this.heldItem)) {
                BlockPos pos = this.blockEntity.m_58899_();
                if (!world.isClientSide) {
                    if (!this.handleBeltFunnelOutput()) {
                        BeltProcessingBehaviour processingBehaviour = BlockEntityBehaviour.get(world, pos.above(2), BeltProcessingBehaviour.TYPE);
                        if (processingBehaviour != null) {
                            if (this.heldItem.locked || !BeltProcessingBehaviour.isBlocked(world, pos)) {
                                ItemStack previousItem = this.heldItem.stack;
                                boolean wasLocked = this.heldItem.locked;
                                BeltProcessingBehaviour.ProcessingResult result = wasLocked ? processingBehaviour.handleHeldItem(this.heldItem, this.transportedHandler) : processingBehaviour.handleReceivedItem(this.heldItem, this.transportedHandler);
                                if (result == BeltProcessingBehaviour.ProcessingResult.REMOVE) {
                                    this.heldItem = null;
                                    this.blockEntity.sendData();
                                } else {
                                    this.heldItem.locked = result == BeltProcessingBehaviour.ProcessingResult.HOLD;
                                    if (this.heldItem.locked != wasLocked || !previousItem.equals(this.heldItem.stack, false)) {
                                        this.blockEntity.sendData();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    protected boolean tick(TransportedItemStack heldItem) {
        heldItem.prevBeltPosition = heldItem.beltPosition;
        heldItem.prevSideOffset = heldItem.sideOffset;
        float diff = 0.5F - heldItem.beltPosition;
        if (diff > 0.001953125F) {
            if (diff > 0.03125F && !BeltHelper.isItemUpright(heldItem.stack)) {
                heldItem.angle++;
            }
            heldItem.beltPosition += diff / 4.0F;
        }
        return diff < 0.0625F;
    }

    private boolean handleBeltFunnelOutput() {
        BlockState funnel = this.getWorld().getBlockState(this.getPos().above());
        Direction funnelFacing = AbstractFunnelBlock.getFunnelFacing(funnel);
        if (funnelFacing != null && this.canFunnelsPullFrom.test(funnelFacing.getOpposite())) {
            for (int slot = 0; slot < this.processingOutputBuffer.getSlots(); slot++) {
                ItemStack previousItem = this.processingOutputBuffer.getStackInSlot(slot);
                if (!previousItem.isEmpty()) {
                    ItemStack afterInsert = this.blockEntity.getBehaviour(DirectBeltInputBehaviour.TYPE).tryExportingToBeltFunnel(previousItem, null, false);
                    if (afterInsert == null) {
                        return false;
                    }
                    if (previousItem.getCount() != afterInsert.getCount()) {
                        this.processingOutputBuffer.setStackInSlot(slot, afterInsert);
                        this.blockEntity.notifyUpdate();
                        return true;
                    }
                }
            }
            ItemStack previousItem = this.heldItem.stack;
            ItemStack afterInsertx = this.blockEntity.getBehaviour(DirectBeltInputBehaviour.TYPE).tryExportingToBeltFunnel(previousItem, null, false);
            if (afterInsertx == null) {
                return false;
            } else if (previousItem.getCount() != afterInsertx.getCount()) {
                if (afterInsertx.isEmpty()) {
                    this.heldItem = null;
                } else {
                    this.heldItem.stack = afterInsertx;
                }
                this.blockEntity.notifyUpdate();
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        Level level = this.getWorld();
        BlockPos pos = this.getPos();
        ItemHelper.dropContents(level, pos, this.processingOutputBuffer);
        for (TransportedItemStack transportedItemStack : this.incoming) {
            Block.popResource(level, pos, transportedItemStack.stack);
        }
        if (!this.getHeldItemStack().isEmpty()) {
            Block.popResource(level, pos, this.getHeldItemStack());
        }
    }

    @Override
    public void unload() {
        if (this.lazyItemHandler != null) {
            this.lazyItemHandler.invalidate();
        }
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        if (this.heldItem != null) {
            compound.put("HeldItem", this.heldItem.serializeNBT());
        }
        compound.put("OutputBuffer", this.processingOutputBuffer.serializeNBT());
        if (this.canMergeItems() && !this.incoming.isEmpty()) {
            compound.put("Incoming", NBTHelper.writeCompoundList(this.incoming, TransportedItemStack::serializeNBT));
        }
    }

    @Override
    public void read(CompoundTag compound, boolean clientPacket) {
        this.heldItem = null;
        if (compound.contains("HeldItem")) {
            this.heldItem = TransportedItemStack.read(compound.getCompound("HeldItem"));
        }
        this.processingOutputBuffer.deserializeNBT(compound.getCompound("OutputBuffer"));
        if (this.canMergeItems()) {
            ListTag list = compound.getList("Incoming", 10);
            this.incoming = NBTHelper.readCompoundList(list, TransportedItemStack::read);
        }
    }

    public void addSubBehaviours(List<BlockEntityBehaviour> behaviours) {
        behaviours.add(new DirectBeltInputBehaviour(this.blockEntity).allowingBeltFunnels().setInsertionHandler(this::tryInsertingFromSide).considerOccupiedWhen(this::isOccupied));
        this.transportedHandler = new TransportedItemStackHandlerBehaviour(this.blockEntity, this::applyToAllItems).withStackPlacement(this::getWorldPositionOf);
        behaviours.add(this.transportedHandler);
    }

    public ItemStack getHeldItemStack() {
        return this.heldItem == null ? ItemStack.EMPTY : this.heldItem.stack;
    }

    public boolean canMergeItems() {
        return this.allowMerge;
    }

    public int getPresentStackSize() {
        int cumulativeStackSize = 0;
        cumulativeStackSize += this.getHeldItemStack().getCount();
        for (int slot = 0; slot < this.processingOutputBuffer.getSlots(); slot++) {
            cumulativeStackSize += this.processingOutputBuffer.getStackInSlot(slot).getCount();
        }
        return cumulativeStackSize;
    }

    public int getRemainingSpace() {
        int cumulativeStackSize = this.getPresentStackSize();
        for (TransportedItemStack transportedItemStack : this.incoming) {
            cumulativeStackSize += transportedItemStack.stack.getCount();
        }
        int fromGetter = (Integer) this.maxStackSize.get();
        return (fromGetter == 0 ? 64 : fromGetter) - cumulativeStackSize;
    }

    public ItemStack insert(TransportedItemStack heldItem, boolean simulate) {
        if (!(Boolean) this.canAcceptItems.get()) {
            return heldItem.stack;
        } else if (!this.acceptedItems.test(heldItem.stack)) {
            return heldItem.stack;
        } else if (this.canMergeItems()) {
            int remainingSpace = this.getRemainingSpace();
            ItemStack inserted = heldItem.stack;
            if (remainingSpace <= 0) {
                return inserted;
            } else if (this.heldItem != null && !ItemHelper.canItemStackAmountsStack(this.heldItem.stack, inserted)) {
                return inserted;
            } else {
                ItemStack returned = ItemStack.EMPTY;
                if (remainingSpace < inserted.getCount()) {
                    returned = ItemHandlerHelper.copyStackWithSize(heldItem.stack, inserted.getCount() - remainingSpace);
                    if (!simulate) {
                        TransportedItemStack copy = heldItem.copy();
                        copy.stack.setCount(remainingSpace);
                        if (this.heldItem != null) {
                            this.incoming.add(copy);
                        } else {
                            this.heldItem = copy;
                        }
                    }
                } else if (!simulate) {
                    if (this.heldItem != null) {
                        this.incoming.add(heldItem);
                    } else {
                        this.heldItem = heldItem;
                    }
                }
                return returned;
            }
        } else {
            if (!simulate) {
                if (this.isEmpty()) {
                    if (heldItem.insertedFrom.getAxis().isHorizontal()) {
                        AllSoundEvents.DEPOT_SLIDE.playOnServer(this.getWorld(), this.getPos());
                    } else {
                        AllSoundEvents.DEPOT_PLOP.playOnServer(this.getWorld(), this.getPos());
                    }
                }
                this.heldItem = heldItem;
                this.onHeldInserted.accept(heldItem.stack);
            }
            return ItemStack.EMPTY;
        }
    }

    public void setHeldItem(TransportedItemStack heldItem) {
        this.heldItem = heldItem;
    }

    public void removeHeldItem() {
        this.heldItem = null;
    }

    public void setCenteredHeldItem(TransportedItemStack heldItem) {
        this.heldItem = heldItem;
        this.heldItem.beltPosition = 0.5F;
        this.heldItem.prevBeltPosition = 0.5F;
    }

    public <T> LazyOptional<T> getItemCapability(Capability<T> cap, Direction side) {
        return this.lazyItemHandler.cast();
    }

    private boolean isOccupied(Direction side) {
        if (!this.getHeldItemStack().isEmpty() && !this.canMergeItems()) {
            return true;
        } else {
            return !this.isOutputEmpty() && !this.canMergeItems() ? true : !(Boolean) this.canAcceptItems.get();
        }
    }

    private ItemStack tryInsertingFromSide(TransportedItemStack transportedStack, Direction side, boolean simulate) {
        ItemStack inserted = transportedStack.stack;
        if (this.isOccupied(side)) {
            return inserted;
        } else {
            int size = transportedStack.stack.getCount();
            transportedStack = transportedStack.copy();
            transportedStack.beltPosition = side.getAxis().isVertical() ? 0.5F : 0.0F;
            transportedStack.insertedFrom = side;
            transportedStack.prevSideOffset = transportedStack.sideOffset;
            transportedStack.prevBeltPosition = transportedStack.beltPosition;
            ItemStack remainder = this.insert(transportedStack, simulate);
            if (remainder.getCount() != size) {
                this.blockEntity.notifyUpdate();
            }
            return remainder;
        }
    }

    private void applyToAllItems(float maxDistanceFromCentre, Function<TransportedItemStack, TransportedItemStackHandlerBehaviour.TransportedResult> processFunction) {
        if (this.heldItem != null) {
            if (!(0.5F - this.heldItem.beltPosition > maxDistanceFromCentre)) {
                boolean dirty = false;
                TransportedItemStack transportedItemStack = this.heldItem;
                ItemStack stackBefore = transportedItemStack.stack.copy();
                TransportedItemStackHandlerBehaviour.TransportedResult result = (TransportedItemStackHandlerBehaviour.TransportedResult) processFunction.apply(transportedItemStack);
                if (result != null && !result.didntChangeFrom(stackBefore)) {
                    dirty = true;
                    this.heldItem = null;
                    if (result.hasHeldOutput()) {
                        this.setCenteredHeldItem(result.getHeldOutput());
                    }
                    for (TransportedItemStack added : result.getOutputs()) {
                        if (this.getHeldItemStack().isEmpty()) {
                            this.setCenteredHeldItem(added);
                        } else {
                            ItemStack remainder = ItemHandlerHelper.insertItemStacked(this.processingOutputBuffer, added.stack, false);
                            Vec3 vec = VecHelper.getCenterOf(this.blockEntity.m_58899_());
                            Containers.dropItemStack(this.blockEntity.m_58904_(), vec.x, vec.y + 0.5, vec.z, remainder);
                        }
                    }
                    if (dirty) {
                        this.blockEntity.notifyUpdate();
                    }
                }
            }
        }
    }

    public boolean isEmpty() {
        return this.heldItem == null && this.isOutputEmpty();
    }

    public boolean isOutputEmpty() {
        for (int i = 0; i < this.processingOutputBuffer.getSlots(); i++) {
            if (!this.processingOutputBuffer.getStackInSlot(i).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private Vec3 getWorldPositionOf(TransportedItemStack transported) {
        return VecHelper.getCenterOf(this.blockEntity.m_58899_());
    }

    @Override
    public BehaviourType<?> getType() {
        return TYPE;
    }

    public boolean isItemValid(ItemStack stack) {
        return this.acceptedItems.test(stack);
    }
}