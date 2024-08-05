package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
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
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class FenceGateBlock extends HorizontalDirectionalBlock {

    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public static final BooleanProperty IN_WALL = BlockStateProperties.IN_WALL;

    protected static final VoxelShape Z_SHAPE = Block.box(0.0, 0.0, 6.0, 16.0, 16.0, 10.0);

    protected static final VoxelShape X_SHAPE = Block.box(6.0, 0.0, 0.0, 10.0, 16.0, 16.0);

    protected static final VoxelShape Z_SHAPE_LOW = Block.box(0.0, 0.0, 6.0, 16.0, 13.0, 10.0);

    protected static final VoxelShape X_SHAPE_LOW = Block.box(6.0, 0.0, 0.0, 10.0, 13.0, 16.0);

    protected static final VoxelShape Z_COLLISION_SHAPE = Block.box(0.0, 0.0, 6.0, 16.0, 24.0, 10.0);

    protected static final VoxelShape X_COLLISION_SHAPE = Block.box(6.0, 0.0, 0.0, 10.0, 24.0, 16.0);

    protected static final VoxelShape Z_SUPPORT_SHAPE = Block.box(0.0, 5.0, 6.0, 16.0, 24.0, 10.0);

    protected static final VoxelShape X_SUPPORT_SHAPE = Block.box(6.0, 5.0, 0.0, 10.0, 24.0, 16.0);

    protected static final VoxelShape Z_OCCLUSION_SHAPE = Shapes.or(Block.box(0.0, 5.0, 7.0, 2.0, 16.0, 9.0), Block.box(14.0, 5.0, 7.0, 16.0, 16.0, 9.0));

    protected static final VoxelShape X_OCCLUSION_SHAPE = Shapes.or(Block.box(7.0, 5.0, 0.0, 9.0, 16.0, 2.0), Block.box(7.0, 5.0, 14.0, 9.0, 16.0, 16.0));

    protected static final VoxelShape Z_OCCLUSION_SHAPE_LOW = Shapes.or(Block.box(0.0, 2.0, 7.0, 2.0, 13.0, 9.0), Block.box(14.0, 2.0, 7.0, 16.0, 13.0, 9.0));

    protected static final VoxelShape X_OCCLUSION_SHAPE_LOW = Shapes.or(Block.box(7.0, 2.0, 0.0, 9.0, 13.0, 2.0), Block.box(7.0, 2.0, 14.0, 9.0, 13.0, 16.0));

    private final WoodType type;

    public FenceGateBlock(BlockBehaviour.Properties blockBehaviourProperties0, WoodType woodType1) {
        super(blockBehaviourProperties0.sound(woodType1.soundType()));
        this.type = woodType1;
        this.m_49959_((BlockState) ((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(OPEN, false)).m_61124_(POWERED, false)).m_61124_(IN_WALL, false));
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        if ((Boolean) blockState0.m_61143_(IN_WALL)) {
            return ((Direction) blockState0.m_61143_(f_54117_)).getAxis() == Direction.Axis.X ? X_SHAPE_LOW : Z_SHAPE_LOW;
        } else {
            return ((Direction) blockState0.m_61143_(f_54117_)).getAxis() == Direction.Axis.X ? X_SHAPE : Z_SHAPE;
        }
    }

    @Override
    public BlockState updateShape(BlockState blockState0, Direction direction1, BlockState blockState2, LevelAccessor levelAccessor3, BlockPos blockPos4, BlockPos blockPos5) {
        Direction.Axis $$6 = direction1.getAxis();
        if (((Direction) blockState0.m_61143_(f_54117_)).getClockWise().getAxis() != $$6) {
            return super.m_7417_(blockState0, direction1, blockState2, levelAccessor3, blockPos4, blockPos5);
        } else {
            boolean $$7 = this.isWall(blockState2) || this.isWall(levelAccessor3.m_8055_(blockPos4.relative(direction1.getOpposite())));
            return (BlockState) blockState0.m_61124_(IN_WALL, $$7);
        }
    }

    @Override
    public VoxelShape getBlockSupportShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2) {
        if ((Boolean) blockState0.m_61143_(OPEN)) {
            return Shapes.empty();
        } else {
            return ((Direction) blockState0.m_61143_(f_54117_)).getAxis() == Direction.Axis.Z ? Z_SUPPORT_SHAPE : X_SUPPORT_SHAPE;
        }
    }

    @Override
    public VoxelShape getCollisionShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        if ((Boolean) blockState0.m_61143_(OPEN)) {
            return Shapes.empty();
        } else {
            return ((Direction) blockState0.m_61143_(f_54117_)).getAxis() == Direction.Axis.Z ? Z_COLLISION_SHAPE : X_COLLISION_SHAPE;
        }
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2) {
        if ((Boolean) blockState0.m_61143_(IN_WALL)) {
            return ((Direction) blockState0.m_61143_(f_54117_)).getAxis() == Direction.Axis.X ? X_OCCLUSION_SHAPE_LOW : Z_OCCLUSION_SHAPE_LOW;
        } else {
            return ((Direction) blockState0.m_61143_(f_54117_)).getAxis() == Direction.Axis.X ? X_OCCLUSION_SHAPE : Z_OCCLUSION_SHAPE;
        }
    }

    @Override
    public boolean isPathfindable(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, PathComputationType pathComputationType3) {
        switch(pathComputationType3) {
            case LAND:
                return (Boolean) blockState0.m_61143_(OPEN);
            case WATER:
                return false;
            case AIR:
                return (Boolean) blockState0.m_61143_(OPEN);
            default:
                return false;
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext0) {
        Level $$1 = blockPlaceContext0.m_43725_();
        BlockPos $$2 = blockPlaceContext0.getClickedPos();
        boolean $$3 = $$1.m_276867_($$2);
        Direction $$4 = blockPlaceContext0.m_8125_();
        Direction.Axis $$5 = $$4.getAxis();
        boolean $$6 = $$5 == Direction.Axis.Z && (this.isWall($$1.getBlockState($$2.west())) || this.isWall($$1.getBlockState($$2.east()))) || $$5 == Direction.Axis.X && (this.isWall($$1.getBlockState($$2.north())) || this.isWall($$1.getBlockState($$2.south())));
        return (BlockState) ((BlockState) ((BlockState) ((BlockState) this.m_49966_().m_61124_(f_54117_, $$4)).m_61124_(OPEN, $$3)).m_61124_(POWERED, $$3)).m_61124_(IN_WALL, $$6);
    }

    private boolean isWall(BlockState blockState0) {
        return blockState0.m_204336_(BlockTags.WALLS);
    }

    @Override
    public InteractionResult use(BlockState blockState0, Level level1, BlockPos blockPos2, Player player3, InteractionHand interactionHand4, BlockHitResult blockHitResult5) {
        if ((Boolean) blockState0.m_61143_(OPEN)) {
            blockState0 = (BlockState) blockState0.m_61124_(OPEN, false);
            level1.setBlock(blockPos2, blockState0, 10);
        } else {
            Direction $$6 = player3.m_6350_();
            if (blockState0.m_61143_(f_54117_) == $$6.getOpposite()) {
                blockState0 = (BlockState) blockState0.m_61124_(f_54117_, $$6);
            }
            blockState0 = (BlockState) blockState0.m_61124_(OPEN, true);
            level1.setBlock(blockPos2, blockState0, 10);
        }
        boolean $$7 = (Boolean) blockState0.m_61143_(OPEN);
        level1.playSound(player3, blockPos2, $$7 ? this.type.fenceGateOpen() : this.type.fenceGateClose(), SoundSource.BLOCKS, 1.0F, level1.getRandom().nextFloat() * 0.1F + 0.9F);
        level1.m_142346_(player3, $$7 ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, blockPos2);
        return InteractionResult.sidedSuccess(level1.isClientSide);
    }

    @Override
    public void neighborChanged(BlockState blockState0, Level level1, BlockPos blockPos2, Block block3, BlockPos blockPos4, boolean boolean5) {
        if (!level1.isClientSide) {
            boolean $$6 = level1.m_276867_(blockPos2);
            if ((Boolean) blockState0.m_61143_(POWERED) != $$6) {
                level1.setBlock(blockPos2, (BlockState) ((BlockState) blockState0.m_61124_(POWERED, $$6)).m_61124_(OPEN, $$6), 2);
                if ((Boolean) blockState0.m_61143_(OPEN) != $$6) {
                    level1.playSound(null, blockPos2, $$6 ? this.type.fenceGateOpen() : this.type.fenceGateClose(), SoundSource.BLOCKS, 1.0F, level1.getRandom().nextFloat() * 0.1F + 0.9F);
                    level1.m_142346_(null, $$6 ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, blockPos2);
                }
            }
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(f_54117_, OPEN, POWERED, IN_WALL);
    }

    public static boolean connectsToDirection(BlockState blockState0, Direction direction1) {
        return ((Direction) blockState0.m_61143_(f_54117_)).getAxis() == direction1.getClockWise().getAxis();
    }
}