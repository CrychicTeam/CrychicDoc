package net.minecraft.world.level.block;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class TrapDoorBlock extends HorizontalDirectionalBlock implements SimpleWaterloggedBlock {

    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;

    public static final EnumProperty<Half> HALF = BlockStateProperties.HALF;

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    protected static final int AABB_THICKNESS = 3;

    protected static final VoxelShape EAST_OPEN_AABB = Block.box(0.0, 0.0, 0.0, 3.0, 16.0, 16.0);

    protected static final VoxelShape WEST_OPEN_AABB = Block.box(13.0, 0.0, 0.0, 16.0, 16.0, 16.0);

    protected static final VoxelShape SOUTH_OPEN_AABB = Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 3.0);

    protected static final VoxelShape NORTH_OPEN_AABB = Block.box(0.0, 0.0, 13.0, 16.0, 16.0, 16.0);

    protected static final VoxelShape BOTTOM_AABB = Block.box(0.0, 0.0, 0.0, 16.0, 3.0, 16.0);

    protected static final VoxelShape TOP_AABB = Block.box(0.0, 13.0, 0.0, 16.0, 16.0, 16.0);

    private final BlockSetType type;

    protected TrapDoorBlock(BlockBehaviour.Properties blockBehaviourProperties0, BlockSetType blockSetType1) {
        super(blockBehaviourProperties0.sound(blockSetType1.soundType()));
        this.type = blockSetType1;
        this.m_49959_((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(f_54117_, Direction.NORTH)).m_61124_(OPEN, false)).m_61124_(HALF, Half.BOTTOM)).m_61124_(POWERED, false)).m_61124_(WATERLOGGED, false));
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        if (!(Boolean) blockState0.m_61143_(OPEN)) {
            return blockState0.m_61143_(HALF) == Half.TOP ? TOP_AABB : BOTTOM_AABB;
        } else {
            switch((Direction) blockState0.m_61143_(f_54117_)) {
                case NORTH:
                default:
                    return NORTH_OPEN_AABB;
                case SOUTH:
                    return SOUTH_OPEN_AABB;
                case WEST:
                    return WEST_OPEN_AABB;
                case EAST:
                    return EAST_OPEN_AABB;
            }
        }
    }

    @Override
    public boolean isPathfindable(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, PathComputationType pathComputationType3) {
        switch(pathComputationType3) {
            case LAND:
                return (Boolean) blockState0.m_61143_(OPEN);
            case WATER:
                return (Boolean) blockState0.m_61143_(WATERLOGGED);
            case AIR:
                return (Boolean) blockState0.m_61143_(OPEN);
            default:
                return false;
        }
    }

    @Override
    public InteractionResult use(BlockState blockState0, Level level1, BlockPos blockPos2, Player player3, InteractionHand interactionHand4, BlockHitResult blockHitResult5) {
        if (!this.type.canOpenByHand()) {
            return InteractionResult.PASS;
        } else {
            blockState0 = (BlockState) blockState0.m_61122_(OPEN);
            level1.setBlock(blockPos2, blockState0, 2);
            if ((Boolean) blockState0.m_61143_(WATERLOGGED)) {
                level1.m_186469_(blockPos2, Fluids.WATER, Fluids.WATER.m_6718_(level1));
            }
            this.playSound(player3, level1, blockPos2, (Boolean) blockState0.m_61143_(OPEN));
            return InteractionResult.sidedSuccess(level1.isClientSide);
        }
    }

    protected void playSound(@Nullable Player player0, Level level1, BlockPos blockPos2, boolean boolean3) {
        level1.playSound(player0, blockPos2, boolean3 ? this.type.trapdoorOpen() : this.type.trapdoorClose(), SoundSource.BLOCKS, 1.0F, level1.getRandom().nextFloat() * 0.1F + 0.9F);
        level1.m_142346_(player0, boolean3 ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, blockPos2);
    }

    @Override
    public void neighborChanged(BlockState blockState0, Level level1, BlockPos blockPos2, Block block3, BlockPos blockPos4, boolean boolean5) {
        if (!level1.isClientSide) {
            boolean $$6 = level1.m_276867_(blockPos2);
            if ($$6 != (Boolean) blockState0.m_61143_(POWERED)) {
                if ((Boolean) blockState0.m_61143_(OPEN) != $$6) {
                    blockState0 = (BlockState) blockState0.m_61124_(OPEN, $$6);
                    this.playSound(null, level1, blockPos2, $$6);
                }
                level1.setBlock(blockPos2, (BlockState) blockState0.m_61124_(POWERED, $$6), 2);
                if ((Boolean) blockState0.m_61143_(WATERLOGGED)) {
                    level1.m_186469_(blockPos2, Fluids.WATER, Fluids.WATER.m_6718_(level1));
                }
            }
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext0) {
        BlockState $$1 = this.m_49966_();
        FluidState $$2 = blockPlaceContext0.m_43725_().getFluidState(blockPlaceContext0.getClickedPos());
        Direction $$3 = blockPlaceContext0.m_43719_();
        if (!blockPlaceContext0.replacingClickedOnBlock() && $$3.getAxis().isHorizontal()) {
            $$1 = (BlockState) ((BlockState) $$1.m_61124_(f_54117_, $$3)).m_61124_(HALF, blockPlaceContext0.m_43720_().y - (double) blockPlaceContext0.getClickedPos().m_123342_() > 0.5 ? Half.TOP : Half.BOTTOM);
        } else {
            $$1 = (BlockState) ((BlockState) $$1.m_61124_(f_54117_, blockPlaceContext0.m_8125_().getOpposite())).m_61124_(HALF, $$3 == Direction.UP ? Half.BOTTOM : Half.TOP);
        }
        if (blockPlaceContext0.m_43725_().m_276867_(blockPlaceContext0.getClickedPos())) {
            $$1 = (BlockState) ((BlockState) $$1.m_61124_(OPEN, true)).m_61124_(POWERED, true);
        }
        return (BlockState) $$1.m_61124_(WATERLOGGED, $$2.getType() == Fluids.WATER);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(f_54117_, OPEN, HALF, POWERED, WATERLOGGED);
    }

    @Override
    public FluidState getFluidState(BlockState blockState0) {
        return blockState0.m_61143_(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.m_5888_(blockState0);
    }

    @Override
    public BlockState updateShape(BlockState blockState0, Direction direction1, BlockState blockState2, LevelAccessor levelAccessor3, BlockPos blockPos4, BlockPos blockPos5) {
        if ((Boolean) blockState0.m_61143_(WATERLOGGED)) {
            levelAccessor3.scheduleTick(blockPos4, Fluids.WATER, Fluids.WATER.m_6718_(levelAccessor3));
        }
        return super.m_7417_(blockState0, direction1, blockState2, levelAccessor3, blockPos4, blockPos5);
    }
}