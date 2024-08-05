package com.simibubi.create.content.fluids.transfer;

import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.fluid.FluidHelper;
import com.simibubi.create.foundation.utility.BBHelper;
import com.simibubi.create.foundation.utility.Iterate;
import it.unimi.dsi.fastutil.PriorityQueue;
import it.unimi.dsi.fastutil.objects.ObjectHeapPriorityQueue;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraftforge.fluids.FluidStack;

public class FluidDrainingBehaviour extends FluidManipulationBehaviour {

    public static final BehaviourType<FluidDrainingBehaviour> TYPE = new BehaviourType<>();

    Fluid fluid;

    Set<BlockPos> validationSet;

    PriorityQueue<FluidManipulationBehaviour.BlockPosEntry> queue;

    boolean isValid;

    List<FluidManipulationBehaviour.BlockPosEntry> validationFrontier;

    Set<BlockPos> validationVisited = new HashSet();

    Set<BlockPos> newValidationSet;

    public FluidDrainingBehaviour(SmartBlockEntity be) {
        super(be);
        this.validationFrontier = new ArrayList();
        this.validationSet = new HashSet();
        this.newValidationSet = new HashSet();
        this.queue = new ObjectHeapPriorityQueue(this::comparePositions);
    }

    @Nullable
    public boolean pullNext(BlockPos root, boolean simulate) {
        if (!this.frontier.isEmpty()) {
            return false;
        } else if (!Objects.equals(root, this.rootPos)) {
            this.rebuildContext(root);
            return false;
        } else if (this.counterpartActed) {
            this.counterpartActed = false;
            this.softReset(root);
            return false;
        } else {
            if (this.affectedArea == null) {
                this.affectedArea = BoundingBox.fromCorners(root, root);
            }
            Level world = this.getWorld();
            if (!this.queue.isEmpty() && !this.isValid) {
                this.rebuildContext(root);
                return false;
            } else {
                if (this.validationFrontier.isEmpty() && !this.queue.isEmpty() && !simulate && this.revalidateIn == 0) {
                    this.revalidate(root);
                }
                if (this.infinite) {
                    this.blockEntity.award(AllAdvancements.HOSE_PULLEY);
                    if (FluidHelper.isLava(this.fluid)) {
                        this.blockEntity.award(AllAdvancements.HOSE_PULLEY_LAVA);
                    }
                    this.playEffect(world, root, this.fluid, true);
                    return true;
                } else {
                    while (!this.queue.isEmpty()) {
                        BlockPos currentPos = ((FluidManipulationBehaviour.BlockPosEntry) this.queue.first()).pos();
                        BlockState blockState = world.getBlockState(currentPos);
                        BlockState emptied = blockState;
                        Fluid fluid = Fluids.EMPTY;
                        if (blockState.m_61138_(BlockStateProperties.WATERLOGGED) && (Boolean) blockState.m_61143_(BlockStateProperties.WATERLOGGED)) {
                            emptied = (BlockState) blockState.m_61124_(BlockStateProperties.WATERLOGGED, false);
                            fluid = Fluids.WATER;
                        } else if (blockState.m_60734_() instanceof LiquidBlock) {
                            LiquidBlock flowingFluid = (LiquidBlock) blockState.m_60734_();
                            emptied = Blocks.AIR.defaultBlockState();
                            if ((Integer) blockState.m_61143_(LiquidBlock.LEVEL) != 0) {
                                this.affectedArea = BBHelper.encapsulate(this.affectedArea, BoundingBox.fromCorners(currentPos, currentPos));
                                if (!this.blockEntity.isVirtual()) {
                                    world.setBlock(currentPos, emptied, 18);
                                }
                                this.queue.dequeue();
                                if (this.queue.isEmpty()) {
                                    this.isValid = this.checkValid(world, this.rootPos);
                                    this.reset();
                                }
                                continue;
                            }
                            fluid = flowingFluid.getFluid();
                        } else if (blockState.m_60819_().getType() != Fluids.EMPTY && blockState.m_60742_(world, currentPos, CollisionContext.empty()).isEmpty()) {
                            fluid = blockState.m_60819_().getType();
                            emptied = Blocks.AIR.defaultBlockState();
                        }
                        if (this.fluid == null) {
                            this.fluid = fluid;
                        }
                        if (this.fluid.isSame(fluid)) {
                            if (simulate) {
                                return true;
                            }
                            this.playEffect(world, currentPos, fluid, true);
                            this.blockEntity.award(AllAdvancements.HOSE_PULLEY);
                            if (!this.blockEntity.isVirtual()) {
                                world.setBlock(currentPos, emptied, 18);
                            }
                            this.affectedArea = BBHelper.encapsulate(this.affectedArea, currentPos);
                            this.queue.dequeue();
                            if (this.queue.isEmpty()) {
                                this.isValid = this.checkValid(world, this.rootPos);
                                this.reset();
                            } else if (!this.validationSet.contains(currentPos)) {
                                this.reset();
                            }
                            return true;
                        }
                        this.queue.dequeue();
                        if (this.queue.isEmpty()) {
                            this.isValid = this.checkValid(world, this.rootPos);
                            this.reset();
                        }
                    }
                    if (this.rootPos == null) {
                        return false;
                    } else {
                        if (this.isValid) {
                            this.rebuildContext(root);
                        }
                        return false;
                    }
                }
            }
        }
    }

    protected void softReset(BlockPos root) {
        this.queue.clear();
        this.validationSet.clear();
        this.newValidationSet.clear();
        this.validationFrontier.clear();
        this.validationVisited.clear();
        this.visited.clear();
        this.infinite = false;
        this.setValidationTimer();
        this.frontier.add(new FluidManipulationBehaviour.BlockPosEntry(root, 0));
        this.blockEntity.sendData();
    }

    protected boolean checkValid(Level world, BlockPos root) {
        BlockPos currentPos = root;
        for (int timeout = 1000; timeout > 0 && !root.equals(this.blockEntity.m_58899_()); timeout--) {
            FluidDrainingBehaviour.FluidBlockType canPullFluidsFrom = this.canPullFluidsFrom(world.getBlockState(currentPos), currentPos);
            if (canPullFluidsFrom != FluidDrainingBehaviour.FluidBlockType.FLOWING) {
                if (canPullFluidsFrom == FluidDrainingBehaviour.FluidBlockType.SOURCE) {
                    return true;
                }
                break;
            }
            for (Direction d : Iterate.directions) {
                BlockPos side = currentPos.relative(d);
                if (this.canPullFluidsFrom(world.getBlockState(side), side) == FluidDrainingBehaviour.FluidBlockType.SOURCE) {
                    return true;
                }
            }
            currentPos = currentPos.above();
        }
        return false;
    }

    @Override
    public void read(CompoundTag nbt, boolean clientPacket) {
        super.read(nbt, clientPacket);
        if (!clientPacket && this.affectedArea != null) {
            this.frontier.add(new FluidManipulationBehaviour.BlockPosEntry(this.rootPos, 0));
        }
    }

    protected FluidDrainingBehaviour.FluidBlockType canPullFluidsFrom(BlockState blockState, BlockPos pos) {
        if (blockState.m_61138_(BlockStateProperties.WATERLOGGED) && (Boolean) blockState.m_61143_(BlockStateProperties.WATERLOGGED)) {
            return FluidDrainingBehaviour.FluidBlockType.SOURCE;
        } else if (blockState.m_60734_() instanceof LiquidBlock) {
            return blockState.m_61143_(LiquidBlock.LEVEL) == 0 ? FluidDrainingBehaviour.FluidBlockType.SOURCE : FluidDrainingBehaviour.FluidBlockType.FLOWING;
        } else {
            return blockState.m_60819_().getType() != Fluids.EMPTY && blockState.m_60742_(this.getWorld(), pos, CollisionContext.empty()).isEmpty() ? FluidDrainingBehaviour.FluidBlockType.SOURCE : FluidDrainingBehaviour.FluidBlockType.NONE;
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.rootPos != null) {
            this.isValid = this.checkValid(this.getWorld(), this.rootPos);
        }
        if (!this.frontier.isEmpty()) {
            this.continueSearch();
        } else if (!this.validationFrontier.isEmpty()) {
            this.continueValidation();
        } else {
            if (this.revalidateIn > 0) {
                this.revalidateIn--;
            }
        }
    }

    @Override
    public void lazyTick() {
        super.lazyTick();
    }

    public void rebuildContext(BlockPos root) {
        this.reset();
        this.rootPos = root;
        this.affectedArea = BoundingBox.fromCorners(this.rootPos, this.rootPos);
        if (this.isValid) {
            this.frontier.add(new FluidManipulationBehaviour.BlockPosEntry(root, 0));
        }
    }

    public void revalidate(BlockPos root) {
        this.validationFrontier.clear();
        this.validationVisited.clear();
        this.newValidationSet.clear();
        this.validationFrontier.add(new FluidManipulationBehaviour.BlockPosEntry(root, 0));
        this.setValidationTimer();
    }

    private void continueSearch() {
        try {
            this.fluid = this.search(this.fluid, this.frontier, this.visited, (e, d) -> {
                this.queue.enqueue(new FluidManipulationBehaviour.BlockPosEntry(e, d));
                this.validationSet.add(e);
            }, false);
        } catch (FluidManipulationBehaviour.ChunkNotLoadedException var3) {
            this.blockEntity.sendData();
            this.frontier.clear();
            this.visited.clear();
        }
        int maxBlocks = this.maxBlocks();
        if (this.visited.size() > maxBlocks && this.canDrainInfinitely(this.fluid) && !this.queue.isEmpty()) {
            this.infinite = true;
            BlockPos firstValid = ((FluidManipulationBehaviour.BlockPosEntry) this.queue.first()).pos();
            this.frontier.clear();
            this.visited.clear();
            this.queue.clear();
            this.queue.enqueue(new FluidManipulationBehaviour.BlockPosEntry(firstValid, 0));
            this.blockEntity.sendData();
        } else if (this.frontier.isEmpty()) {
            this.blockEntity.sendData();
            this.visited.clear();
        }
    }

    private void continueValidation() {
        try {
            this.search(this.fluid, this.validationFrontier, this.validationVisited, (e, d) -> this.newValidationSet.add(e), false);
        } catch (FluidManipulationBehaviour.ChunkNotLoadedException var2) {
            this.validationFrontier.clear();
            this.validationVisited.clear();
            this.setLongValidationTimer();
            return;
        }
        int maxBlocks = this.maxBlocks();
        if (this.validationVisited.size() > maxBlocks && this.canDrainInfinitely(this.fluid)) {
            if (!this.infinite) {
                this.reset();
            }
            this.validationFrontier.clear();
            this.setLongValidationTimer();
        } else if (this.validationFrontier.isEmpty()) {
            if (this.infinite) {
                this.reset();
            } else {
                this.validationSet = this.newValidationSet;
                this.newValidationSet = new HashSet();
                this.validationVisited.clear();
            }
        }
    }

    @Override
    public void reset() {
        super.reset();
        this.fluid = null;
        this.rootPos = null;
        this.queue.clear();
        this.validationSet.clear();
        this.newValidationSet.clear();
        this.validationFrontier.clear();
        this.validationVisited.clear();
        this.blockEntity.sendData();
    }

    @Override
    public BehaviourType<?> getType() {
        return TYPE;
    }

    protected boolean isSearching() {
        return !this.frontier.isEmpty();
    }

    public FluidStack getDrainableFluid(BlockPos rootPos) {
        return this.fluid != null && !this.isSearching() && this.pullNext(rootPos, true) ? new FluidStack(this.fluid, 1000) : FluidStack.EMPTY;
    }

    static enum FluidBlockType {

        NONE, SOURCE, FLOWING
    }
}