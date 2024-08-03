package com.simibubi.create.content.fluids.transfer;

import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.fluid.FluidHelper;
import com.simibubi.create.foundation.utility.BBHelper;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.infrastructure.config.AllConfigs;
import it.unimi.dsi.fastutil.PriorityQueue;
import it.unimi.dsi.fastutil.objects.ObjectHeapPriorityQueue;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.ticks.LevelTicks;

public class FluidFillingBehaviour extends FluidManipulationBehaviour {

    public static final BehaviourType<FluidFillingBehaviour> TYPE = new BehaviourType<>();

    PriorityQueue<FluidManipulationBehaviour.BlockPosEntry> queue = new ObjectHeapPriorityQueue((p, p2) -> -this.comparePositions(p, p2));

    List<FluidManipulationBehaviour.BlockPosEntry> infinityCheckFrontier;

    Set<BlockPos> infinityCheckVisited;

    public FluidFillingBehaviour(SmartBlockEntity be) {
        super(be);
        this.revalidateIn = 1;
        this.infinityCheckFrontier = new ArrayList();
        this.infinityCheckVisited = new HashSet();
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.infinityCheckFrontier.isEmpty() && this.rootPos != null) {
            Fluid fluid = this.getWorld().getFluidState(this.rootPos).getType();
            if (fluid != Fluids.EMPTY) {
                this.continueValidation(fluid);
            }
        }
        if (this.revalidateIn > 0) {
            this.revalidateIn--;
        }
    }

    protected void continueValidation(Fluid fluid) {
        try {
            this.search(fluid, this.infinityCheckFrontier, this.infinityCheckVisited, (p, d) -> this.infinityCheckFrontier.add(new FluidManipulationBehaviour.BlockPosEntry(p, d)), true);
        } catch (FluidManipulationBehaviour.ChunkNotLoadedException var3) {
            this.infinityCheckFrontier.clear();
            this.infinityCheckVisited.clear();
            this.setLongValidationTimer();
            return;
        }
        int maxBlocks = this.maxBlocks();
        if (this.infinityCheckVisited.size() > maxBlocks && maxBlocks != -1 && !this.fillInfinite()) {
            if (!this.infinite) {
                this.reset();
                this.infinite = true;
                this.blockEntity.sendData();
            }
            this.infinityCheckFrontier.clear();
            this.setLongValidationTimer();
        } else if (this.infinityCheckFrontier.isEmpty()) {
            if (this.infinite) {
                this.reset();
            } else {
                this.infinityCheckVisited.clear();
            }
        }
    }

    public boolean tryDeposit(Fluid fluid, BlockPos root, boolean simulate) {
        if (!Objects.equals(root, this.rootPos)) {
            this.reset();
            this.rootPos = root;
            this.queue.enqueue(new FluidManipulationBehaviour.BlockPosEntry(root, 0));
            this.affectedArea = BoundingBox.fromCorners(this.rootPos, this.rootPos);
            return false;
        } else if (this.counterpartActed) {
            this.counterpartActed = false;
            this.softReset(root);
            return false;
        } else {
            if (this.affectedArea == null) {
                this.affectedArea = BoundingBox.fromCorners(root, root);
            }
            if (this.revalidateIn == 0) {
                this.visited.clear();
                this.infinityCheckFrontier.clear();
                this.infinityCheckVisited.clear();
                this.infinityCheckFrontier.add(new FluidManipulationBehaviour.BlockPosEntry(root, 0));
                this.setValidationTimer();
                this.softReset(root);
            }
            Level world = this.getWorld();
            int maxRange = this.maxRange();
            int maxRangeSq = maxRange * maxRange;
            int maxBlocks = this.maxBlocks();
            boolean evaporate = world.dimensionType().ultraWarm() && FluidHelper.isTag(fluid, FluidTags.WATER);
            boolean canPlaceSources = AllConfigs.server().fluids.fluidFillPlaceFluidSourceBlocks.get();
            if ((this.fillInfinite() || !this.infinite) && !evaporate && canPlaceSources) {
                boolean success = false;
                for (int i = 0; !success && !this.queue.isEmpty() && i < 1024; i++) {
                    FluidManipulationBehaviour.BlockPosEntry entry = (FluidManipulationBehaviour.BlockPosEntry) this.queue.first();
                    BlockPos currentPos = entry.pos();
                    if (this.visited.contains(currentPos)) {
                        this.queue.dequeue();
                    } else {
                        if (!simulate) {
                            this.visited.add(currentPos);
                        }
                        if (this.visited.size() >= maxBlocks && maxBlocks != -1) {
                            this.infinite = true;
                            if (!this.fillInfinite()) {
                                this.visited.clear();
                                this.queue.clear();
                                return false;
                            }
                        }
                        FluidFillingBehaviour.SpaceType spaceType = this.getAtPos(world, currentPos, fluid);
                        if (spaceType != FluidFillingBehaviour.SpaceType.BLOCKING) {
                            if (spaceType == FluidFillingBehaviour.SpaceType.FILLABLE) {
                                success = true;
                                if (!simulate) {
                                    this.playEffect(world, currentPos, fluid, false);
                                    BlockState blockState = world.getBlockState(currentPos);
                                    if (!blockState.m_61138_(BlockStateProperties.WATERLOGGED) || !fluid.isSame(Fluids.WATER)) {
                                        this.replaceBlock(world, currentPos, blockState);
                                        if (!this.blockEntity.isVirtual()) {
                                            world.setBlock(currentPos, FluidHelper.convertToStill(fluid).defaultFluidState().createLegacyBlock(), 18);
                                        }
                                    } else if (!this.blockEntity.isVirtual()) {
                                        world.setBlock(currentPos, this.updatePostWaterlogging((BlockState) blockState.m_61124_(BlockStateProperties.WATERLOGGED, true)), 18);
                                    }
                                    if (world.m_183324_() instanceof LevelTicks<Fluid> serverTickList) {
                                        serverTickList.clearArea(new BoundingBox(currentPos));
                                    }
                                    this.affectedArea = BBHelper.encapsulate(this.affectedArea, currentPos);
                                }
                            }
                            if (simulate && success) {
                                return true;
                            }
                            this.visited.add(currentPos);
                            this.queue.dequeue();
                            for (Direction side : Iterate.directions) {
                                if (side != Direction.UP) {
                                    BlockPos offsetPos = currentPos.relative(side);
                                    if (!this.visited.contains(offsetPos) && !(offsetPos.m_123331_(this.rootPos) > (double) maxRangeSq)) {
                                        FluidFillingBehaviour.SpaceType nextSpaceType = this.getAtPos(world, offsetPos, fluid);
                                        if (nextSpaceType != FluidFillingBehaviour.SpaceType.BLOCKING) {
                                            this.queue.enqueue(new FluidManipulationBehaviour.BlockPosEntry(offsetPos, entry.distance() + 1));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if (!simulate && success) {
                    this.blockEntity.award(AllAdvancements.HOSE_PULLEY);
                }
                return success;
            } else {
                FluidState fluidState = world.getFluidState(this.rootPos);
                boolean equivalentTo = fluidState.getType().isSame(fluid);
                if (!equivalentTo && !evaporate && canPlaceSources) {
                    return false;
                } else if (simulate) {
                    return true;
                } else {
                    this.playEffect(world, root, fluid, false);
                    if (evaporate) {
                        int ix = root.m_123341_();
                        int j = root.m_123342_();
                        int k = root.m_123343_();
                        world.playSound(null, (double) ix, (double) j, (double) k, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 2.6F + (world.random.nextFloat() - world.random.nextFloat()) * 0.8F);
                    } else if (!canPlaceSources) {
                        this.blockEntity.award(AllAdvancements.HOSE_PULLEY);
                    }
                    return true;
                }
            }
        }
    }

    protected void softReset(BlockPos root) {
        this.visited.clear();
        this.queue.clear();
        this.queue.enqueue(new FluidManipulationBehaviour.BlockPosEntry(root, 0));
        this.infinite = false;
        this.setValidationTimer();
        this.blockEntity.sendData();
    }

    protected FluidFillingBehaviour.SpaceType getAtPos(Level world, BlockPos pos, Fluid toFill) {
        BlockState blockState = world.getBlockState(pos);
        FluidState fluidState = blockState.m_60819_();
        if (blockState.m_61138_(BlockStateProperties.WATERLOGGED)) {
            return toFill.isSame(Fluids.WATER) ? (blockState.m_61143_(BlockStateProperties.WATERLOGGED) ? FluidFillingBehaviour.SpaceType.FILLED : FluidFillingBehaviour.SpaceType.FILLABLE) : FluidFillingBehaviour.SpaceType.BLOCKING;
        } else if (blockState.m_60734_() instanceof LiquidBlock) {
            return blockState.m_61143_(LiquidBlock.LEVEL) == 0 ? (toFill.isSame(fluidState.getType()) ? FluidFillingBehaviour.SpaceType.FILLED : FluidFillingBehaviour.SpaceType.BLOCKING) : FluidFillingBehaviour.SpaceType.FILLABLE;
        } else if (fluidState.getType() != Fluids.EMPTY && blockState.m_60742_(this.getWorld(), pos, CollisionContext.empty()).isEmpty()) {
            return toFill.isSame(fluidState.getType()) ? FluidFillingBehaviour.SpaceType.FILLED : FluidFillingBehaviour.SpaceType.BLOCKING;
        } else {
            return this.canBeReplacedByFluid(world, pos, blockState) ? FluidFillingBehaviour.SpaceType.FILLABLE : FluidFillingBehaviour.SpaceType.BLOCKING;
        }
    }

    protected void replaceBlock(Level world, BlockPos pos, BlockState state) {
        BlockEntity blockEntity = state.m_155947_() ? world.getBlockEntity(pos) : null;
        Block.dropResources(state, world, pos, blockEntity);
    }

    protected boolean canBeReplacedByFluid(BlockGetter world, BlockPos pos, BlockState pState) {
        Block block = pState.m_60734_();
        if (block instanceof DoorBlock || pState.m_204336_(BlockTags.SIGNS) || pState.m_60713_(Blocks.LADDER) || pState.m_60713_(Blocks.SUGAR_CANE) || pState.m_60713_(Blocks.BUBBLE_COLUMN)) {
            return false;
        } else {
            return !pState.m_60713_(Blocks.NETHER_PORTAL) && !pState.m_60713_(Blocks.END_PORTAL) && !pState.m_60713_(Blocks.END_GATEWAY) && !pState.m_60713_(Blocks.STRUCTURE_VOID) ? !pState.m_280555_() : false;
        }
    }

    protected BlockState updatePostWaterlogging(BlockState state) {
        if (state.m_61138_(BlockStateProperties.LIT)) {
            state = (BlockState) state.m_61124_(BlockStateProperties.LIT, false);
        }
        return state;
    }

    @Override
    public void reset() {
        super.reset();
        this.queue.clear();
        this.infinityCheckFrontier.clear();
        this.infinityCheckVisited.clear();
    }

    @Override
    public BehaviourType<?> getType() {
        return TYPE;
    }

    static enum SpaceType {

        FILLABLE, FILLED, BLOCKING
    }
}