package org.violetmoon.quark.content.automation.block;

import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.ticks.TickPriority;
import net.minecraftforge.event.ForgeEventFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.quark.content.automation.base.RandomizerPowerState;
import org.violetmoon.zeta.block.ZetaBlock;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.registry.RenderLayerRegistry;

public class RedstoneRandomizerBlock extends ZetaBlock {

    protected static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0);

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public static final EnumProperty<RandomizerPowerState> POWERED = EnumProperty.create("powered", RandomizerPowerState.class);

    public RedstoneRandomizerBlock(String regname, @Nullable ZetaModule module, BlockBehaviour.Properties properties) {
        super(regname, module, properties);
        this.m_49959_((BlockState) ((BlockState) this.m_49966_().m_61124_(FACING, Direction.NORTH)).m_61124_(POWERED, RandomizerPowerState.OFF));
        if (module != null) {
            module.zeta.renderLayerRegistry.put(this, RenderLayerRegistry.Layer.CUTOUT);
            this.setCreativeTab(CreativeModeTabs.REDSTONE_BLOCKS, Blocks.COMPARATOR, false);
        }
    }

    @Override
    public void tick(@NotNull BlockState state, @NotNull ServerLevel world, @NotNull BlockPos pos, @NotNull RandomSource rand) {
        boolean isPowered = this.isPowered(state);
        boolean willBePowered = this.shouldBePowered(world, pos, state);
        if (isPowered != willBePowered) {
            if (!willBePowered) {
                state = (BlockState) state.m_61124_(POWERED, RandomizerPowerState.OFF);
            } else {
                state = (BlockState) state.m_61124_(POWERED, rand.nextBoolean() ? RandomizerPowerState.LEFT : RandomizerPowerState.RIGHT);
            }
            world.m_46597_(pos, state);
        }
    }

    protected void updateState(Level world, BlockPos pos, BlockState state) {
        boolean isPowered = this.isPowered(state);
        boolean willBePowered = this.shouldBePowered(world, pos, state);
        if (isPowered != willBePowered && !world.m_183326_().willTickThisTick(pos, this)) {
            TickPriority priority = isPowered ? TickPriority.VERY_HIGH : TickPriority.HIGH;
            world.m_186464_(pos, this, 2, priority);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, POWERED);
    }

    @NotNull
    @Override
    public VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter world, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return SHAPE;
    }

    @Override
    public boolean canSurvive(@NotNull BlockState state, @NotNull LevelReader world, BlockPos pos) {
        return m_49936_(world, pos.below());
    }

    protected boolean isPowered(BlockState state) {
        return state.m_61143_(POWERED) != RandomizerPowerState.OFF;
    }

    @Override
    public int getDirectSignal(BlockState blockState, @NotNull BlockGetter blockAccess, @NotNull BlockPos pos, @NotNull Direction side) {
        return blockState.m_60746_(blockAccess, pos, side);
    }

    @Override
    public int getSignal(BlockState blockState, @NotNull BlockGetter blockAccess, @NotNull BlockPos pos, @NotNull Direction side) {
        RandomizerPowerState powerState = (RandomizerPowerState) blockState.m_61143_(POWERED);
        return switch(powerState) {
            case RIGHT ->
                ((Direction) blockState.m_61143_(FACING)).getClockWise() == side ? 15 : 0;
            case LEFT ->
                ((Direction) blockState.m_61143_(FACING)).getCounterClockWise() == side ? 15 : 0;
            default ->
                0;
        };
    }

    @Override
    public void neighborChanged(BlockState state, @NotNull Level world, @NotNull BlockPos pos, @NotNull Block blockIn, @NotNull BlockPos fromPos, boolean isMoving) {
        if (state.m_60710_(world, pos)) {
            this.updateState(world, pos, state);
        } else {
            breakAndDrop(this, state, world, pos);
        }
    }

    public static void breakAndDrop(Block block, BlockState state, Level world, BlockPos pos) {
        m_49892_(state, world, pos, null);
        world.removeBlock(pos, false);
        for (Direction direction : Direction.values()) {
            world.updateNeighborsAt(pos.relative(direction), block);
        }
    }

    protected boolean shouldBePowered(Level world, BlockPos pos, BlockState state) {
        return this.calculateInputStrength(world, pos, state) > 0;
    }

    protected int calculateInputStrength(Level world, BlockPos pos, BlockState state) {
        Direction face = (Direction) state.m_61143_(FACING);
        BlockPos checkPos = pos.relative(face);
        int strength = world.m_277185_(checkPos, face);
        if (strength >= 15) {
            return strength;
        } else {
            BlockState checkState = world.getBlockState(checkPos);
            return Math.max(strength, checkState.m_60734_() == Blocks.REDSTONE_WIRE ? (Integer) checkState.m_61143_(RedStoneWireBlock.POWER) : 0);
        }
    }

    @Override
    public boolean isSignalSource(@NotNull BlockState state) {
        return true;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return (BlockState) this.m_49966_().m_61124_(FACING, context.m_8125_().getOpposite());
    }

    @Override
    public void setPlacedBy(@NotNull Level world, @NotNull BlockPos pos, @NotNull BlockState state, LivingEntity placer, @NotNull ItemStack stack) {
        if (this.shouldBePowered(world, pos, state)) {
            world.m_186460_(pos, this, 1);
        }
    }

    @Override
    public void onPlace(@NotNull BlockState state, @NotNull Level world, @NotNull BlockPos pos, @NotNull BlockState oldState, boolean isMoving) {
        notifyNeighbors(this, world, pos, state);
    }

    @Override
    public void onRemove(@NotNull BlockState state, @NotNull Level world, @NotNull BlockPos pos, @NotNull BlockState newState, boolean isMoving) {
        if (!isMoving && state.m_60734_() != newState.m_60734_()) {
            super.m_6810_(state, world, pos, newState, false);
            notifyNeighbors(this, world, pos, state);
        }
    }

    public static void notifyNeighbors(Block block, Level world, BlockPos pos, BlockState state) {
        Direction face = (Direction) state.m_61143_(FACING);
        BlockPos neighborPos = pos.relative(face.getOpposite());
        if (!ForgeEventFactory.onNeighborNotify(world, pos, world.getBlockState(pos), EnumSet.of(face.getOpposite()), false).isCanceled()) {
            world.neighborChanged(neighborPos, block, pos);
            world.updateNeighborsAtExceptFromFacing(neighborPos, block, face);
        }
    }

    @Override
    public void animateTick(BlockState stateIn, @NotNull Level worldIn, @NotNull BlockPos pos, @NotNull RandomSource rand) {
        if (stateIn.m_61143_(POWERED) != RandomizerPowerState.OFF) {
            double x = (double) pos.m_123341_() + 0.5 + ((double) rand.nextFloat() - 0.5) * 0.2;
            double y = (double) pos.m_123342_() + 0.4 + ((double) rand.nextFloat() - 0.5) * 0.2;
            double z = (double) pos.m_123343_() + 0.5 + ((double) rand.nextFloat() - 0.5) * 0.2;
            worldIn.addParticle(DustParticleOptions.REDSTONE, x, y, z, 0.0, 0.0, 0.0);
        }
    }
}