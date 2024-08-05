package com.simibubi.create.content.logistics.tunnel;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.kinetics.belt.BeltBlockEntity;
import com.simibubi.create.content.kinetics.belt.BeltHelper;
import com.simibubi.create.content.kinetics.belt.behaviour.DirectBeltInputBehaviour;
import com.simibubi.create.content.logistics.funnel.BeltFunnelBlock;
import com.simibubi.create.content.logistics.funnel.FunnelBlock;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.SidedFilteringBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.INamedIconOptions;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollOptionBehaviour;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.utility.BlockHelper;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.NBTHelper;
import com.simibubi.create.infrastructure.config.AllConfigs;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import org.apache.commons.lang3.tuple.Pair;

public class BrassTunnelBlockEntity extends BeltTunnelBlockEntity implements IHaveGoggleInformation {

    SidedFilteringBehaviour filtering;

    boolean connectedLeft;

    boolean connectedRight;

    ItemStack stackToDistribute;

    Direction stackEnteredFrom;

    float distributionProgress;

    int distributionDistanceLeft;

    int distributionDistanceRight;

    int previousOutputIndex;

    Couple<List<Pair<BlockPos, Direction>>> distributionTargets = Couple.create(ArrayList::new);

    private boolean syncedOutputActive;

    private Set<BrassTunnelBlockEntity> syncSet = new HashSet();

    protected ScrollOptionBehaviour<BrassTunnelBlockEntity.SelectionMode> selectionMode;

    private LazyOptional<IItemHandler> beltCapability;

    private LazyOptional<IItemHandler> tunnelCapability;

    private static Random rand = new Random();

    private static Map<Pair<BrassTunnelBlockEntity, Direction>, ItemStack> distributed = new IdentityHashMap();

    private static Set<Pair<BrassTunnelBlockEntity, Direction>> full = new HashSet();

    public BrassTunnelBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.stackToDistribute = ItemStack.EMPTY;
        this.stackEnteredFrom = null;
        this.beltCapability = LazyOptional.empty();
        this.tunnelCapability = LazyOptional.of(() -> new BrassTunnelItemHandler(this));
        this.previousOutputIndex = 0;
        this.syncedOutputActive = false;
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
        behaviours.add(this.selectionMode = new ScrollOptionBehaviour(BrassTunnelBlockEntity.SelectionMode.class, Lang.translateDirect("logistics.when_multiple_outputs_available"), this, new BrassTunnelModeSlot()));
        this.selectionMode.onlyActiveWhen(this::hasDistributionBehaviour);
        this.selectionMode.withCallback(setting -> {
            for (boolean side : Iterate.trueAndFalse) {
                if (this.isConnected(side)) {
                    BrassTunnelBlockEntity adjacent = this.getAdjacent(side);
                    if (adjacent != null) {
                        adjacent.selectionMode.setValue(setting);
                    }
                }
            }
        });
    }

    @Override
    public void tick() {
        super.tick();
        BeltBlockEntity beltBelow = BeltHelper.getSegmentBE(this.f_58857_, this.f_58858_.below());
        if (this.distributionProgress > 0.0F) {
            this.distributionProgress--;
        }
        if (beltBelow != null && beltBelow.getSpeed() != 0.0F) {
            if (!this.stackToDistribute.isEmpty() || this.syncedOutputActive) {
                if (!this.f_58857_.isClientSide || this.isVirtual()) {
                    if (this.distributionProgress == -1.0F) {
                        this.distributionTargets.forEach(List::clear);
                        this.distributionDistanceLeft = 0;
                        this.distributionDistanceRight = 0;
                        this.syncSet.clear();
                        List<Pair<BrassTunnelBlockEntity, Direction>> validOutputs = this.gatherValidOutputs();
                        if (this.selectionMode.get() == BrassTunnelBlockEntity.SelectionMode.SYNCHRONIZE) {
                            boolean allEmpty = true;
                            boolean allFull = true;
                            for (BrassTunnelBlockEntity be : this.syncSet) {
                                boolean hasStack = !be.stackToDistribute.isEmpty();
                                allEmpty &= !hasStack;
                                allFull &= hasStack;
                            }
                            boolean notifySyncedOut = !allEmpty;
                            if (allFull || allEmpty) {
                                this.syncSet.forEach(be -> be.syncedOutputActive = notifySyncedOut);
                            }
                        }
                        if (validOutputs != null) {
                            if (!this.stackToDistribute.isEmpty()) {
                                for (Pair<BrassTunnelBlockEntity, Direction> pair : validOutputs) {
                                    BrassTunnelBlockEntity tunnel = (BrassTunnelBlockEntity) pair.getKey();
                                    Direction output = (Direction) pair.getValue();
                                    if (this.insertIntoTunnel(tunnel, output, this.stackToDistribute, true) != null) {
                                        this.distributionTargets.get(!tunnel.flapFilterEmpty(output)).add(Pair.of(tunnel.f_58858_, output));
                                        int distance = tunnel.f_58858_.m_123341_() + tunnel.f_58858_.m_123343_() - this.f_58858_.m_123341_() - this.f_58858_.m_123343_();
                                        if (distance < 0) {
                                            this.distributionDistanceLeft = Math.max(this.distributionDistanceLeft, -distance);
                                        } else {
                                            this.distributionDistanceRight = Math.max(this.distributionDistanceRight, distance);
                                        }
                                    }
                                }
                                if (!this.distributionTargets.getFirst().isEmpty() || !this.distributionTargets.getSecond().isEmpty()) {
                                    if (this.selectionMode.get() != BrassTunnelBlockEntity.SelectionMode.SYNCHRONIZE || this.syncedOutputActive) {
                                        this.distributionProgress = (float) AllConfigs.server().logistics.brassTunnelTimer.get().intValue();
                                        this.sendData();
                                    }
                                }
                            }
                        }
                    } else if (this.distributionProgress == 0.0F) {
                        this.distributionTargets.forEach(list -> {
                            if (!this.stackToDistribute.isEmpty()) {
                                List<Pair<BrassTunnelBlockEntity, Direction>> validTargets = new ArrayList();
                                for (Pair<BlockPos, Direction> pairx : list) {
                                    BlockPos tunnelPos = (BlockPos) pairx.getKey();
                                    Direction outputx = (Direction) pairx.getValue();
                                    if (!tunnelPos.equals(this.f_58858_) || outputx != this.stackEnteredFrom) {
                                        BlockEntity be = this.f_58857_.getBlockEntity(tunnelPos);
                                        if (be instanceof BrassTunnelBlockEntity) {
                                            validTargets.add(Pair.of((BrassTunnelBlockEntity) be, outputx));
                                        }
                                    }
                                }
                                this.distribute(validTargets);
                                this.distributionProgress = -1.0F;
                            }
                        });
                    }
                }
            }
        }
    }

    private void distribute(List<Pair<BrassTunnelBlockEntity, Direction>> validTargets) {
        int amountTargets = validTargets.size();
        if (amountTargets != 0) {
            distributed.clear();
            full.clear();
            int indexStart = this.previousOutputIndex % amountTargets;
            BrassTunnelBlockEntity.SelectionMode mode = (BrassTunnelBlockEntity.SelectionMode) this.selectionMode.get();
            boolean force = mode == BrassTunnelBlockEntity.SelectionMode.FORCED_ROUND_ROBIN || mode == BrassTunnelBlockEntity.SelectionMode.FORCED_SPLIT;
            boolean split = mode == BrassTunnelBlockEntity.SelectionMode.FORCED_SPLIT || mode == BrassTunnelBlockEntity.SelectionMode.SPLIT;
            boolean robin = mode == BrassTunnelBlockEntity.SelectionMode.FORCED_ROUND_ROBIN || mode == BrassTunnelBlockEntity.SelectionMode.ROUND_ROBIN;
            if (mode == BrassTunnelBlockEntity.SelectionMode.RANDOMIZE) {
                indexStart = rand.nextInt(amountTargets);
            }
            if (mode == BrassTunnelBlockEntity.SelectionMode.PREFER_NEAREST || mode == BrassTunnelBlockEntity.SelectionMode.SYNCHRONIZE) {
                indexStart = 0;
            }
            ItemStack toDistribute = this.stackToDistribute.copy();
            for (boolean distributeAgain : Iterate.trueAndFalse) {
                ItemStack toDistributeThisCycle = null;
                int remainingOutputs = amountTargets;
                int leftovers = 0;
                for (boolean simulate : Iterate.trueAndFalse) {
                    if (remainingOutputs == 0) {
                        break;
                    }
                    leftovers = 0;
                    int index = indexStart;
                    int stackSize = toDistribute.getCount();
                    int splitStackSize = stackSize / remainingOutputs;
                    int splitRemainder = stackSize % remainingOutputs;
                    int visited = 0;
                    toDistributeThisCycle = toDistribute.copy();
                    if (force || split || !simulate) {
                        while (visited < amountTargets) {
                            Pair<BrassTunnelBlockEntity, Direction> pair = (Pair<BrassTunnelBlockEntity, Direction>) validTargets.get(index);
                            BrassTunnelBlockEntity tunnel = (BrassTunnelBlockEntity) pair.getKey();
                            Direction side = (Direction) pair.getValue();
                            index = (index + 1) % amountTargets;
                            visited++;
                            if (full.contains(pair)) {
                                if (split && simulate) {
                                    remainingOutputs--;
                                }
                            } else {
                                int count = split ? splitStackSize + (splitRemainder > 0 ? 1 : 0) : stackSize;
                                ItemStack toOutput = ItemHandlerHelper.copyStackWithSize(toDistributeThisCycle, count);
                                boolean testWithIncreasedCount = distributed.containsKey(pair);
                                int increasedCount = testWithIncreasedCount ? ((ItemStack) distributed.get(pair)).getCount() : 0;
                                if (testWithIncreasedCount) {
                                    toOutput.grow(increasedCount);
                                }
                                ItemStack remainder = this.insertIntoTunnel(tunnel, side, toOutput, true);
                                if (remainder != null && remainder.getCount() != (testWithIncreasedCount ? count + 1 : count)) {
                                    if (!remainder.isEmpty() && !simulate) {
                                        full.add(pair);
                                    }
                                    if (!simulate) {
                                        toOutput.shrink(remainder.getCount());
                                        distributed.put(pair, toOutput);
                                    }
                                    leftovers += remainder.getCount();
                                    toDistributeThisCycle.shrink(count);
                                    if (toDistributeThisCycle.isEmpty()) {
                                        break;
                                    }
                                    splitRemainder--;
                                    if (!split) {
                                        break;
                                    }
                                } else {
                                    if (force) {
                                        return;
                                    }
                                    if (split && simulate) {
                                        remainingOutputs--;
                                    }
                                    if (!simulate) {
                                        full.add(pair);
                                    }
                                    if (robin) {
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
                toDistribute.setCount(toDistributeThisCycle.getCount() + leftovers);
                if (leftovers == 0 && distributeAgain || !split) {
                    break;
                }
            }
            int failedTransferrals = 0;
            for (Entry<Pair<BrassTunnelBlockEntity, Direction>, ItemStack> entry : distributed.entrySet()) {
                Pair<BrassTunnelBlockEntity, Direction> pair = (Pair<BrassTunnelBlockEntity, Direction>) entry.getKey();
                failedTransferrals += this.insertIntoTunnel((BrassTunnelBlockEntity) pair.getKey(), (Direction) pair.getValue(), (ItemStack) entry.getValue(), false).getCount();
            }
            toDistribute.grow(failedTransferrals);
            this.stackToDistribute = ItemHandlerHelper.copyStackWithSize(this.stackToDistribute, toDistribute.getCount());
            if (this.stackToDistribute.isEmpty()) {
                this.stackEnteredFrom = null;
            }
            this.previousOutputIndex++;
            this.previousOutputIndex %= amountTargets;
            this.notifyUpdate();
        }
    }

    public void setStackToDistribute(ItemStack stack, @Nullable Direction enteredFrom) {
        this.stackToDistribute = stack;
        this.stackEnteredFrom = enteredFrom;
        this.distributionProgress = -1.0F;
        this.sendData();
        this.m_6596_();
    }

    public ItemStack getStackToDistribute() {
        return this.stackToDistribute;
    }

    public List<ItemStack> grabAllStacksOfGroup(boolean simulate) {
        List<ItemStack> list = new ArrayList();
        ItemStack own = this.getStackToDistribute();
        if (!own.isEmpty()) {
            list.add(own);
            if (!simulate) {
                this.setStackToDistribute(ItemStack.EMPTY, null);
            }
        }
        for (boolean left : Iterate.trueAndFalse) {
            BrassTunnelBlockEntity adjacent = this;
            while (adjacent != null) {
                if (!this.f_58857_.isLoaded(adjacent.m_58899_())) {
                    return null;
                }
                adjacent = adjacent.getAdjacent(left);
                if (adjacent != null) {
                    ItemStack other = adjacent.getStackToDistribute();
                    if (!other.isEmpty()) {
                        list.add(other);
                        if (!simulate) {
                            adjacent.setStackToDistribute(ItemStack.EMPTY, null);
                        }
                    }
                }
            }
        }
        return list;
    }

    @Nullable
    protected ItemStack insertIntoTunnel(BrassTunnelBlockEntity tunnel, Direction side, ItemStack stack, boolean simulate) {
        if (stack.isEmpty()) {
            return stack;
        } else if (!tunnel.testFlapFilter(side, stack)) {
            return null;
        } else {
            BeltBlockEntity below = BeltHelper.getSegmentBE(this.f_58857_, tunnel.f_58858_.below());
            if (below == null) {
                return null;
            } else {
                BlockPos offset = tunnel.m_58899_().below().relative(side);
                DirectBeltInputBehaviour sideOutput = BlockEntityBehaviour.get(this.f_58857_, offset, DirectBeltInputBehaviour.TYPE);
                if (sideOutput != null) {
                    if (!sideOutput.canInsertFromSide(side)) {
                        return null;
                    } else {
                        ItemStack result = sideOutput.handleInsertion(stack, side, simulate);
                        if (result.isEmpty() && !simulate) {
                            tunnel.flap(side, false);
                        }
                        return result;
                    }
                } else {
                    Direction movementFacing = below.getMovementFacing();
                    if (side == movementFacing && !BlockHelper.hasBlockSolidSide(this.f_58857_.getBlockState(offset), this.f_58857_, offset, side.getOpposite())) {
                        BeltBlockEntity controllerBE = below.getControllerBE();
                        if (controllerBE == null) {
                            return null;
                        } else {
                            if (!simulate) {
                                tunnel.flap(side, true);
                                float beltMovementSpeed = below.getDirectionAwareBeltMovementSpeed();
                                float movementSpeed = Math.max(Math.abs(beltMovementSpeed), 0.125F);
                                int additionalOffset = beltMovementSpeed > 0.0F ? 1 : 0;
                                Vec3 outPos = BeltHelper.getVectorForOffset(controllerBE, (float) (below.index + additionalOffset));
                                Vec3 outMotion = Vec3.atLowerCornerOf(side.getNormal()).scale((double) movementSpeed).add(0.0, 0.125, 0.0);
                                outPos.add(outMotion.normalize());
                                ItemEntity entity = new ItemEntity(this.f_58857_, outPos.x, outPos.y + 0.375, outPos.z, stack);
                                entity.m_20256_(outMotion);
                                entity.setDefaultPickUpDelay();
                                entity.f_19864_ = true;
                                this.f_58857_.m_7967_(entity);
                            }
                            return ItemStack.EMPTY;
                        }
                    } else {
                        return null;
                    }
                }
            }
        }
    }

    public boolean testFlapFilter(Direction side, ItemStack stack) {
        if (this.filtering == null) {
            return false;
        } else if (this.filtering.get(side) == null) {
            FilteringBehaviour adjacentFilter = BlockEntityBehaviour.get(this.f_58857_, this.f_58858_.relative(side), FilteringBehaviour.TYPE);
            return adjacentFilter == null ? true : adjacentFilter.test(stack);
        } else {
            return this.filtering.test(side, stack);
        }
    }

    public boolean flapFilterEmpty(Direction side) {
        if (this.filtering == null) {
            return false;
        } else if (this.filtering.get(side) == null) {
            FilteringBehaviour adjacentFilter = BlockEntityBehaviour.get(this.f_58857_, this.f_58858_.relative(side), FilteringBehaviour.TYPE);
            return adjacentFilter == null ? true : adjacentFilter.getFilter().isEmpty();
        } else {
            return this.filtering.getFilter(side).isEmpty();
        }
    }

    @Override
    public void initialize() {
        if (this.filtering == null) {
            this.filtering = this.createSidedFilter();
            this.attachBehaviourLate(this.filtering);
        }
        super.initialize();
    }

    public boolean canInsert(Direction side, ItemStack stack) {
        if (this.filtering != null && !this.filtering.test(side, stack)) {
            return false;
        } else {
            return !this.hasDistributionBehaviour() ? true : this.stackToDistribute.isEmpty();
        }
    }

    public boolean hasDistributionBehaviour() {
        if (this.flaps.isEmpty()) {
            return false;
        } else if (!this.connectedLeft && !this.connectedRight) {
            BlockState blockState = this.m_58900_();
            if (!AllBlocks.BRASS_TUNNEL.has(blockState)) {
                return false;
            } else {
                Direction.Axis axis = (Direction.Axis) blockState.m_61143_(BrassTunnelBlock.HORIZONTAL_AXIS);
                for (Direction direction : this.flaps.keySet()) {
                    if (direction.getAxis() != axis) {
                        return true;
                    }
                }
                return false;
            }
        } else {
            return true;
        }
    }

    private List<Pair<BrassTunnelBlockEntity, Direction>> gatherValidOutputs() {
        List<Pair<BrassTunnelBlockEntity, Direction>> validOutputs = new ArrayList();
        boolean synchronize = this.selectionMode.get() == BrassTunnelBlockEntity.SelectionMode.SYNCHRONIZE;
        this.addValidOutputsOf(this, validOutputs);
        for (boolean left : Iterate.trueAndFalse) {
            BrassTunnelBlockEntity adjacent = this;
            while (adjacent != null) {
                if (!this.f_58857_.isLoaded(adjacent.m_58899_())) {
                    return null;
                }
                adjacent = adjacent.getAdjacent(left);
                if (adjacent != null) {
                    this.addValidOutputsOf(adjacent, validOutputs);
                }
            }
        }
        return !this.syncedOutputActive && synchronize ? null : validOutputs;
    }

    private void addValidOutputsOf(BrassTunnelBlockEntity tunnelBE, List<Pair<BrassTunnelBlockEntity, Direction>> validOutputs) {
        this.syncSet.add(tunnelBE);
        BeltBlockEntity below = BeltHelper.getSegmentBE(this.f_58857_, tunnelBE.f_58858_.below());
        if (below != null) {
            Direction movementFacing = below.getMovementFacing();
            BlockState blockState = this.m_58900_();
            if (AllBlocks.BRASS_TUNNEL.has(blockState)) {
                boolean prioritizeSides = tunnelBE == this;
                for (boolean sidePass : Iterate.trueAndFalse) {
                    if (prioritizeSides || !sidePass) {
                        for (Direction direction : Iterate.horizontalDirections) {
                            if ((direction != movementFacing || below.getSpeed() != 0.0F) && (!prioritizeSides || sidePass != (direction.getAxis() == movementFacing.getAxis())) && direction != movementFacing.getOpposite() && tunnelBE.sides.contains(direction)) {
                                BlockPos offset = tunnelBE.f_58858_.below().relative(direction);
                                BlockState potentialFunnel = this.f_58857_.getBlockState(offset.above());
                                if (!(potentialFunnel.m_60734_() instanceof BeltFunnelBlock) || potentialFunnel.m_61143_(BeltFunnelBlock.SHAPE) != BeltFunnelBlock.Shape.PULLING || FunnelBlock.getFunnelFacing(potentialFunnel) != direction) {
                                    DirectBeltInputBehaviour inputBehaviour = BlockEntityBehaviour.get(this.f_58857_, offset, DirectBeltInputBehaviour.TYPE);
                                    if (inputBehaviour == null) {
                                        if (direction == movementFacing && !BlockHelper.hasBlockSolidSide(this.f_58857_.getBlockState(offset), this.f_58857_, offset, direction.getOpposite())) {
                                            validOutputs.add(Pair.of(tunnelBE, direction));
                                        }
                                    } else if (inputBehaviour.canInsertFromSide(direction)) {
                                        validOutputs.add(Pair.of(tunnelBE, direction));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void addBehavioursDeferred(List<BlockEntityBehaviour> behaviours) {
        super.addBehavioursDeferred(behaviours);
        this.filtering = this.createSidedFilter();
        behaviours.add(this.filtering);
    }

    protected SidedFilteringBehaviour createSidedFilter() {
        return new SidedFilteringBehaviour(this, new BrassTunnelFilterSlot(), this::makeFilter, this::isValidFaceForFilter);
    }

    private FilteringBehaviour makeFilter(Direction side, FilteringBehaviour filter) {
        return filter;
    }

    private boolean isValidFaceForFilter(Direction side) {
        return this.sides.contains(side);
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        compound.putBoolean("SyncedOutput", this.syncedOutputActive);
        compound.putBoolean("ConnectedLeft", this.connectedLeft);
        compound.putBoolean("ConnectedRight", this.connectedRight);
        compound.put("StackToDistribute", this.stackToDistribute.serializeNBT());
        if (this.stackEnteredFrom != null) {
            NBTHelper.writeEnum(compound, "StackEnteredFrom", this.stackEnteredFrom);
        }
        compound.putFloat("DistributionProgress", this.distributionProgress);
        compound.putInt("PreviousIndex", this.previousOutputIndex);
        compound.putInt("DistanceLeft", this.distributionDistanceLeft);
        compound.putInt("DistanceRight", this.distributionDistanceRight);
        for (boolean filtered : Iterate.trueAndFalse) {
            compound.put(filtered ? "FilteredTargets" : "Targets", NBTHelper.writeCompoundList((Iterable) this.distributionTargets.get(filtered), pair -> {
                CompoundTag nbt = new CompoundTag();
                nbt.put("Pos", NbtUtils.writeBlockPos((BlockPos) pair.getKey()));
                nbt.putInt("Face", ((Direction) pair.getValue()).get3DDataValue());
                return nbt;
            }));
        }
        super.write(compound, clientPacket);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        boolean wasConnectedLeft = this.connectedLeft;
        boolean wasConnectedRight = this.connectedRight;
        this.syncedOutputActive = compound.getBoolean("SyncedOutput");
        this.connectedLeft = compound.getBoolean("ConnectedLeft");
        this.connectedRight = compound.getBoolean("ConnectedRight");
        this.stackToDistribute = ItemStack.of(compound.getCompound("StackToDistribute"));
        this.stackEnteredFrom = compound.contains("StackEnteredFrom") ? NBTHelper.readEnum(compound, "StackEnteredFrom", Direction.class) : null;
        this.distributionProgress = compound.getFloat("DistributionProgress");
        this.previousOutputIndex = compound.getInt("PreviousIndex");
        this.distributionDistanceLeft = compound.getInt("DistanceLeft");
        this.distributionDistanceRight = compound.getInt("DistanceRight");
        for (boolean filtered : Iterate.trueAndFalse) {
            this.distributionTargets.set(filtered, NBTHelper.readCompoundList(compound.getList(filtered ? "FilteredTargets" : "Targets", 10), nbt -> {
                BlockPos pos = NbtUtils.readBlockPos(nbt.getCompound("Pos"));
                Direction face = Direction.from3DDataValue(nbt.getInt("Face"));
                return Pair.of(pos, face);
            }));
        }
        super.read(compound, clientPacket);
        if (clientPacket) {
            if (wasConnectedLeft != this.connectedLeft || wasConnectedRight != this.connectedRight) {
                this.requestModelDataUpdate();
                if (this.m_58898_()) {
                    this.f_58857_.sendBlockUpdated(this.m_58899_(), this.m_58900_(), this.m_58900_(), 16);
                }
            }
            this.filtering.updateFilterPresence();
        }
    }

    public boolean isConnected(boolean leftSide) {
        return leftSide ? this.connectedLeft : this.connectedRight;
    }

    @Override
    public void updateTunnelConnections() {
        super.updateTunnelConnections();
        boolean connectivityChanged = false;
        boolean nowConnectedLeft = this.determineIfConnected(true);
        boolean nowConnectedRight = this.determineIfConnected(false);
        if (this.connectedLeft != nowConnectedLeft) {
            this.connectedLeft = nowConnectedLeft;
            connectivityChanged = true;
            BrassTunnelBlockEntity adjacent = this.getAdjacent(true);
            if (adjacent != null && !this.f_58857_.isClientSide) {
                adjacent.updateTunnelConnections();
                adjacent.selectionMode.setValue(this.selectionMode.getValue());
            }
        }
        if (this.connectedRight != nowConnectedRight) {
            this.connectedRight = nowConnectedRight;
            connectivityChanged = true;
            BrassTunnelBlockEntity adjacent = this.getAdjacent(false);
            if (adjacent != null && !this.f_58857_.isClientSide) {
                adjacent.updateTunnelConnections();
                adjacent.selectionMode.setValue(this.selectionMode.getValue());
            }
        }
        if (this.filtering != null) {
            this.filtering.updateFilterPresence();
        }
        if (connectivityChanged) {
            this.sendData();
        }
    }

    protected boolean determineIfConnected(boolean leftSide) {
        if (this.flaps.isEmpty()) {
            return false;
        } else {
            BrassTunnelBlockEntity adjacentTunnelBE = this.getAdjacent(leftSide);
            return adjacentTunnelBE != null && !adjacentTunnelBE.flaps.isEmpty();
        }
    }

    @Nullable
    protected BrassTunnelBlockEntity getAdjacent(boolean leftSide) {
        if (!this.m_58898_()) {
            return null;
        } else {
            BlockState blockState = this.m_58900_();
            if (!AllBlocks.BRASS_TUNNEL.has(blockState)) {
                return null;
            } else {
                Direction.Axis axis = (Direction.Axis) blockState.m_61143_(BrassTunnelBlock.HORIZONTAL_AXIS);
                Direction baseDirection = Direction.get(Direction.AxisDirection.POSITIVE, axis);
                Direction direction = leftSide ? baseDirection.getCounterClockWise() : baseDirection.getClockWise();
                BlockPos adjacentPos = this.f_58858_.relative(direction);
                BlockState adjacentBlockState = this.f_58857_.getBlockState(adjacentPos);
                if (!AllBlocks.BRASS_TUNNEL.has(adjacentBlockState)) {
                    return null;
                } else if (adjacentBlockState.m_61143_(BrassTunnelBlock.HORIZONTAL_AXIS) != axis) {
                    return null;
                } else {
                    BlockEntity adjacentBE = this.f_58857_.getBlockEntity(adjacentPos);
                    if (adjacentBE.isRemoved()) {
                        return null;
                    } else {
                        return !(adjacentBE instanceof BrassTunnelBlockEntity) ? null : (BrassTunnelBlockEntity) adjacentBE;
                    }
                }
            }
        }
    }

    @Override
    public void invalidate() {
        super.invalidate();
        this.tunnelCapability.invalidate();
    }

    @Override
    public void destroy() {
        super.destroy();
        Block.popResource(this.f_58857_, this.f_58858_, this.stackToDistribute);
        this.stackEnteredFrom = null;
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction side) {
        return capability == ForgeCapabilities.ITEM_HANDLER ? this.tunnelCapability.cast() : super.getCapability(capability, side);
    }

    public LazyOptional<IItemHandler> getBeltCapability() {
        if (!this.beltCapability.isPresent()) {
            BlockEntity blockEntity = this.f_58857_.getBlockEntity(this.f_58858_.below());
            if (blockEntity != null) {
                this.beltCapability = blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER);
            }
        }
        return this.beltCapability;
    }

    public boolean canTakeItems() {
        return this.stackToDistribute.isEmpty() && !this.syncedOutputActive;
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        List<ItemStack> allStacks = this.grabAllStacksOfGroup(true);
        if (allStacks.isEmpty()) {
            return false;
        } else {
            tooltip.add(componentSpacing.plainCopy().append(Lang.translateDirect("tooltip.brass_tunnel.contains")).withStyle(ChatFormatting.WHITE));
            for (ItemStack item : allStacks) {
                tooltip.add(componentSpacing.plainCopy().append(Lang.translateDirect("tooltip.brass_tunnel.contains_entry", Components.translatable(item.getDescriptionId()).getString(), item.getCount())).withStyle(ChatFormatting.GRAY));
            }
            tooltip.add(componentSpacing.plainCopy().append(Lang.translateDirect("tooltip.brass_tunnel.retrieve")).withStyle(ChatFormatting.DARK_GRAY));
            return true;
        }
    }

    public static enum SelectionMode implements INamedIconOptions {

        SPLIT(AllIcons.I_TUNNEL_SPLIT),
        FORCED_SPLIT(AllIcons.I_TUNNEL_FORCED_SPLIT),
        ROUND_ROBIN(AllIcons.I_TUNNEL_ROUND_ROBIN),
        FORCED_ROUND_ROBIN(AllIcons.I_TUNNEL_FORCED_ROUND_ROBIN),
        PREFER_NEAREST(AllIcons.I_TUNNEL_PREFER_NEAREST),
        RANDOMIZE(AllIcons.I_TUNNEL_RANDOMIZE),
        SYNCHRONIZE(AllIcons.I_TUNNEL_SYNCHRONIZE);

        private final String translationKey;

        private final AllIcons icon;

        private SelectionMode(AllIcons icon) {
            this.icon = icon;
            this.translationKey = "tunnel.selection_mode." + Lang.asId(this.name());
        }

        @Override
        public AllIcons getIcon() {
            return this.icon;
        }

        @Override
        public String getTranslationKey() {
            return this.translationKey;
        }
    }
}