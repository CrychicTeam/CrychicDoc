package net.minecraft.world.level.block.piston;

import java.util.Arrays;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.PistonType;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class PistonHeadBlock extends DirectionalBlock {

    public static final EnumProperty<PistonType> TYPE = BlockStateProperties.PISTON_TYPE;

    public static final BooleanProperty SHORT = BlockStateProperties.SHORT;

    public static final float PLATFORM = 4.0F;

    protected static final VoxelShape EAST_AABB = Block.box(12.0, 0.0, 0.0, 16.0, 16.0, 16.0);

    protected static final VoxelShape WEST_AABB = Block.box(0.0, 0.0, 0.0, 4.0, 16.0, 16.0);

    protected static final VoxelShape SOUTH_AABB = Block.box(0.0, 0.0, 12.0, 16.0, 16.0, 16.0);

    protected static final VoxelShape NORTH_AABB = Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 4.0);

    protected static final VoxelShape UP_AABB = Block.box(0.0, 12.0, 0.0, 16.0, 16.0, 16.0);

    protected static final VoxelShape DOWN_AABB = Block.box(0.0, 0.0, 0.0, 16.0, 4.0, 16.0);

    protected static final float AABB_OFFSET = 2.0F;

    protected static final float EDGE_MIN = 6.0F;

    protected static final float EDGE_MAX = 10.0F;

    protected static final VoxelShape UP_ARM_AABB = Block.box(6.0, -4.0, 6.0, 10.0, 12.0, 10.0);

    protected static final VoxelShape DOWN_ARM_AABB = Block.box(6.0, 4.0, 6.0, 10.0, 20.0, 10.0);

    protected static final VoxelShape SOUTH_ARM_AABB = Block.box(6.0, 6.0, -4.0, 10.0, 10.0, 12.0);

    protected static final VoxelShape NORTH_ARM_AABB = Block.box(6.0, 6.0, 4.0, 10.0, 10.0, 20.0);

    protected static final VoxelShape EAST_ARM_AABB = Block.box(-4.0, 6.0, 6.0, 12.0, 10.0, 10.0);

    protected static final VoxelShape WEST_ARM_AABB = Block.box(4.0, 6.0, 6.0, 20.0, 10.0, 10.0);

    protected static final VoxelShape SHORT_UP_ARM_AABB = Block.box(6.0, 0.0, 6.0, 10.0, 12.0, 10.0);

    protected static final VoxelShape SHORT_DOWN_ARM_AABB = Block.box(6.0, 4.0, 6.0, 10.0, 16.0, 10.0);

    protected static final VoxelShape SHORT_SOUTH_ARM_AABB = Block.box(6.0, 6.0, 0.0, 10.0, 10.0, 12.0);

    protected static final VoxelShape SHORT_NORTH_ARM_AABB = Block.box(6.0, 6.0, 4.0, 10.0, 10.0, 16.0);

    protected static final VoxelShape SHORT_EAST_ARM_AABB = Block.box(0.0, 6.0, 6.0, 12.0, 10.0, 10.0);

    protected static final VoxelShape SHORT_WEST_ARM_AABB = Block.box(4.0, 6.0, 6.0, 16.0, 10.0, 10.0);

    private static final VoxelShape[] SHAPES_SHORT = makeShapes(true);

    private static final VoxelShape[] SHAPES_LONG = makeShapes(false);

    private static VoxelShape[] makeShapes(boolean boolean0) {
        return (VoxelShape[]) Arrays.stream(Direction.values()).map(p_60316_ -> calculateShape(p_60316_, boolean0)).toArray(VoxelShape[]::new);
    }

    private static VoxelShape calculateShape(Direction direction0, boolean boolean1) {
        switch(direction0) {
            case DOWN:
            default:
                return Shapes.or(DOWN_AABB, boolean1 ? SHORT_DOWN_ARM_AABB : DOWN_ARM_AABB);
            case UP:
                return Shapes.or(UP_AABB, boolean1 ? SHORT_UP_ARM_AABB : UP_ARM_AABB);
            case NORTH:
                return Shapes.or(NORTH_AABB, boolean1 ? SHORT_NORTH_ARM_AABB : NORTH_ARM_AABB);
            case SOUTH:
                return Shapes.or(SOUTH_AABB, boolean1 ? SHORT_SOUTH_ARM_AABB : SOUTH_ARM_AABB);
            case WEST:
                return Shapes.or(WEST_AABB, boolean1 ? SHORT_WEST_ARM_AABB : WEST_ARM_AABB);
            case EAST:
                return Shapes.or(EAST_AABB, boolean1 ? SHORT_EAST_ARM_AABB : EAST_ARM_AABB);
        }
    }

    public PistonHeadBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(f_52588_, Direction.NORTH)).m_61124_(TYPE, PistonType.DEFAULT)).m_61124_(SHORT, false));
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState blockState0) {
        return true;
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        return (blockState0.m_61143_(SHORT) ? SHAPES_SHORT : SHAPES_LONG)[((Direction) blockState0.m_61143_(f_52588_)).ordinal()];
    }

    private boolean isFittingBase(BlockState blockState0, BlockState blockState1) {
        Block $$2 = blockState0.m_61143_(TYPE) == PistonType.DEFAULT ? Blocks.PISTON : Blocks.STICKY_PISTON;
        return blockState1.m_60713_($$2) && (Boolean) blockState1.m_61143_(PistonBaseBlock.EXTENDED) && blockState1.m_61143_(f_52588_) == blockState0.m_61143_(f_52588_);
    }

    @Override
    public void playerWillDestroy(Level level0, BlockPos blockPos1, BlockState blockState2, Player player3) {
        if (!level0.isClientSide && player3.getAbilities().instabuild) {
            BlockPos $$4 = blockPos1.relative(((Direction) blockState2.m_61143_(f_52588_)).getOpposite());
            if (this.isFittingBase(blockState2, level0.getBlockState($$4))) {
                level0.m_46961_($$4, false);
            }
        }
        super.m_5707_(level0, blockPos1, blockState2, player3);
    }

    @Override
    public void onRemove(BlockState blockState0, Level level1, BlockPos blockPos2, BlockState blockState3, boolean boolean4) {
        if (!blockState0.m_60713_(blockState3.m_60734_())) {
            super.m_6810_(blockState0, level1, blockPos2, blockState3, boolean4);
            BlockPos $$5 = blockPos2.relative(((Direction) blockState0.m_61143_(f_52588_)).getOpposite());
            if (this.isFittingBase(blockState0, level1.getBlockState($$5))) {
                level1.m_46961_($$5, true);
            }
        }
    }

    @Override
    public BlockState updateShape(BlockState blockState0, Direction direction1, BlockState blockState2, LevelAccessor levelAccessor3, BlockPos blockPos4, BlockPos blockPos5) {
        return direction1.getOpposite() == blockState0.m_61143_(f_52588_) && !blockState0.m_60710_(levelAccessor3, blockPos4) ? Blocks.AIR.defaultBlockState() : super.m_7417_(blockState0, direction1, blockState2, levelAccessor3, blockPos4, blockPos5);
    }

    @Override
    public boolean canSurvive(BlockState blockState0, LevelReader levelReader1, BlockPos blockPos2) {
        BlockState $$3 = levelReader1.m_8055_(blockPos2.relative(((Direction) blockState0.m_61143_(f_52588_)).getOpposite()));
        return this.isFittingBase(blockState0, $$3) || $$3.m_60713_(Blocks.MOVING_PISTON) && $$3.m_61143_(f_52588_) == blockState0.m_61143_(f_52588_);
    }

    @Override
    public void neighborChanged(BlockState blockState0, Level level1, BlockPos blockPos2, Block block3, BlockPos blockPos4, boolean boolean5) {
        if (blockState0.m_60710_(level1, blockPos2)) {
            level1.neighborChanged(blockPos2.relative(((Direction) blockState0.m_61143_(f_52588_)).getOpposite()), block3, blockPos4);
        }
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter blockGetter0, BlockPos blockPos1, BlockState blockState2) {
        return new ItemStack(blockState2.m_61143_(TYPE) == PistonType.STICKY ? Blocks.STICKY_PISTON : Blocks.PISTON);
    }

    @Override
    public BlockState rotate(BlockState blockState0, Rotation rotation1) {
        return (BlockState) blockState0.m_61124_(f_52588_, rotation1.rotate((Direction) blockState0.m_61143_(f_52588_)));
    }

    @Override
    public BlockState mirror(BlockState blockState0, Mirror mirror1) {
        return blockState0.m_60717_(mirror1.getRotation((Direction) blockState0.m_61143_(f_52588_)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(f_52588_, TYPE, SHORT);
    }

    @Override
    public boolean isPathfindable(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, PathComputationType pathComputationType3) {
        return false;
    }
}