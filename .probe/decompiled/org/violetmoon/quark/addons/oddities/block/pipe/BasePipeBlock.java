package org.violetmoon.quark.addons.oddities.block.pipe;

import java.util.HashSet;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.quark.addons.oddities.block.be.PipeBlockEntity;
import org.violetmoon.quark.addons.oddities.module.PipesModule;
import org.violetmoon.zeta.block.ZetaBlock;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.registry.RenderLayerRegistry;
import org.violetmoon.zeta.util.MiscUtil;

public abstract class BasePipeBlock extends ZetaBlock implements EntityBlock {

    protected static final BooleanProperty DOWN = BlockStateProperties.DOWN;

    protected static final BooleanProperty UP = BlockStateProperties.UP;

    protected static final BooleanProperty NORTH = BlockStateProperties.NORTH;

    protected static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;

    protected static final BooleanProperty WEST = BlockStateProperties.WEST;

    protected static final BooleanProperty EAST = BlockStateProperties.EAST;

    protected static BooleanProperty property(Direction direction) {
        return switch(direction) {
            case DOWN ->
                DOWN;
            case UP ->
                UP;
            case NORTH ->
                NORTH;
            case SOUTH ->
                SOUTH;
            case WEST ->
                WEST;
            case EAST ->
                EAST;
        };
    }

    protected BasePipeBlock(String name, SoundType soundType, @Nullable ZetaModule module) {
        this(name, module, BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.HAT).strength(3.0F, 10.0F).sound(soundType).noOcclusion());
    }

    protected BasePipeBlock(String name, @Nullable ZetaModule module, BlockBehaviour.Properties properties) {
        super(name, module, properties);
        this.m_49959_(this.getDefaultPipeState());
        if (module != null) {
            this.setCreativeTab(CreativeModeTabs.REDSTONE_BLOCKS);
            module.zeta.renderLayerRegistry.put(this, RenderLayerRegistry.Layer.CUTOUT);
        }
    }

    public BlockState getDefaultPipeState() {
        return (BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) this.m_49966_().m_61124_(DOWN, false)).m_61124_(UP, false)).m_61124_(NORTH, false)).m_61124_(SOUTH, false)).m_61124_(WEST, false)).m_61124_(EAST, false);
    }

    boolean isPipeWaterlogged(BlockState state) {
        return false;
    }

    public boolean allowsFullConnection(PipeBlockEntity.ConnectionType conn) {
        return conn.isSolid;
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        ItemStack stack = player.m_21120_(handIn);
        if (stack.getItem() == Items.STICK) {
            Set<BlockPos> found = new HashSet();
            boolean fixedAny = false;
            Set<BlockPos> candidates = new HashSet();
            Set<BlockPos> newCandidates = new HashSet();
            candidates.add(pos);
            do {
                for (BlockPos cand : candidates) {
                    for (Direction d : Direction.values()) {
                        BlockPos offPos = cand.relative(d);
                        BlockState offState = worldIn.getBlockState(offPos);
                        if (offState.m_60734_() == this && !candidates.contains(offPos) && !found.contains(offPos)) {
                            newCandidates.add(offPos);
                        }
                    }
                    BlockState curr = worldIn.getBlockState(cand);
                    BlockState target = this.getTargetState(worldIn, cand);
                    if (!target.equals(curr)) {
                        fixedAny = true;
                        worldIn.setBlock(cand, target, 6);
                    }
                }
                found.addAll(candidates);
                candidates = newCandidates;
                newCandidates = new HashSet();
            } while (!candidates.isEmpty());
            if (fixedAny) {
                return InteractionResult.sidedSuccess(worldIn.isClientSide);
            }
        }
        return super.m_6227_(state, worldIn, pos, player, handIn, hit);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation direction) {
        BlockState newState = state;
        for (Direction dir : Direction.values()) {
            newState = (BlockState) newState.m_61124_(property(dir), (Boolean) state.m_61143_(property(direction.rotate(dir))));
        }
        return newState;
    }

    @NotNull
    @Override
    public BlockState mirror(@NotNull BlockState state, @NotNull Mirror mirror) {
        BlockState newState = state;
        for (Direction dir : Direction.values()) {
            newState = (BlockState) newState.m_61124_(property(dir), (Boolean) state.m_61143_(property(mirror.mirror(dir))));
        }
        return newState;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighbor, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        for (Direction d : Direction.values()) {
            PipeBlockEntity.ConnectionType type = PipeBlockEntity.computeConnectionTo(level, pos, d);
            boolean fullConnection = this.allowsFullConnection(type);
            state = (BlockState) state.m_61124_(MiscUtil.directionProperty(d), fullConnection);
        }
        if (level.m_7702_(pos) instanceof PipeBlockEntity tile) {
            tile.refreshVisualConnections();
        }
        return state;
    }

    @Override
    public void neighborChanged(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull Block pNeighborBlock, @NotNull BlockPos pNeighborPos, boolean pMovedByPiston) {
        super.m_6861_(pState, pLevel, pPos, pNeighborBlock, pNeighborPos, pMovedByPiston);
        if (pLevel.getBlockEntity(pPos) instanceof PipeBlockEntity tile) {
            tile.refreshVisualConnections();
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.getTargetState(context.m_43725_(), context.getClickedPos());
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack stack) {
        this.refreshVisualConnections(level, pos);
        super.m_6402_(level, pos, state, entity, stack);
    }

    @Override
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pMovedByPiston) {
        this.refreshVisualConnections(pLevel, pPos);
    }

    public void refreshVisualConnections(Level pLevel, BlockPos pPos) {
        if (pLevel.getBlockEntity(pPos) instanceof PipeBlockEntity tile) {
            tile.refreshVisualConnections();
        }
    }

    protected BlockState getTargetState(LevelAccessor level, BlockPos pos) {
        BlockState newState = this.m_49966_();
        for (Direction facing : Direction.values()) {
            PipeBlockEntity.ConnectionType type = PipeBlockEntity.computeConnectionTo(level, pos, facing);
            newState = (BlockState) newState.m_61124_(MiscUtil.directionProperty(facing), this.allowsFullConnection(type));
        }
        return newState;
    }

    public static boolean isConnected(BlockState state, Direction side) {
        return (Boolean) state.m_61143_(MiscUtil.directionProperty(side));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(UP, DOWN, NORTH, SOUTH, WEST, EAST);
    }

    @Override
    public boolean hasAnalogOutputSignal(@NotNull BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(@NotNull BlockState blockState, Level worldIn, @NotNull BlockPos pos) {
        return worldIn.getBlockEntity(pos) instanceof PipeBlockEntity pipe ? pipe.getComparatorOutput() : 0;
    }

    @Override
    public void onRemove(@NotNull BlockState state, Level worldIn, @NotNull BlockPos pos, @NotNull BlockState newState, boolean isMoving) {
        if (worldIn.getBlockEntity(pos) instanceof PipeBlockEntity pipe) {
            pipe.dropAllItems();
        }
        super.m_6810_(state, worldIn, pos, newState, isMoving);
    }

    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new PipeBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level world, @NotNull BlockState state, @NotNull BlockEntityType<T> type) {
        return createTickerHelper(type, PipesModule.blockEntityType, PipeBlockEntity::tick);
    }
}