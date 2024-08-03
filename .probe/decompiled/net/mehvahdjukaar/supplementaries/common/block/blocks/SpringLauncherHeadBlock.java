package net.mehvahdjukaar.supplementaries.common.block.blocks;

import java.util.Arrays;
import net.mehvahdjukaar.supplementaries.common.block.tiles.SpringLauncherArmBlockTile;
import net.mehvahdjukaar.supplementaries.configs.CommonConfigs;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
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
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SpringLauncherHeadBlock extends DirectionalBlock {

    protected static final VoxelShape PISTON_EXTENSION_EAST_AABB = Block.box(12.0, 0.0, 0.0, 16.0, 16.0, 16.0);

    protected static final VoxelShape PISTON_EXTENSION_WEST_AABB = Block.box(0.0, 0.0, 0.0, 4.0, 16.0, 16.0);

    protected static final VoxelShape PISTON_EXTENSION_SOUTH_AABB = Block.box(0.0, 0.0, 12.0, 16.0, 16.0, 16.0);

    protected static final VoxelShape PISTON_EXTENSION_NORTH_AABB = Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 4.0);

    protected static final VoxelShape PISTON_EXTENSION_UP_AABB = Block.box(0.0, 12.0, 0.0, 16.0, 16.0, 16.0);

    protected static final VoxelShape PISTON_EXTENSION_DOWN_AABB = Block.box(0.0, 0.0, 0.0, 16.0, 4.0, 16.0);

    protected static final VoxelShape UP_ARM_AABB = Block.box(1.0, -4.0, 1.0, 15.0, 12.0, 15.0);

    protected static final VoxelShape DOWN_ARM_AABB = Block.box(1.0, 4.0, 1.0, 15.0, 20.0, 15.0);

    protected static final VoxelShape SOUTH_ARM_AABB = Block.box(1.0, 1.0, -4.0, 15.0, 15.0, 12.0);

    protected static final VoxelShape NORTH_ARM_AABB = Block.box(1.0, 1.0, 4.0, 15.0, 15.0, 20.0);

    protected static final VoxelShape EAST_ARM_AABB = Block.box(-4.0, 1.0, 1.0, 12.0, 15.0, 15.0);

    protected static final VoxelShape WEST_ARM_AABB = Block.box(4.0, 1.0, 1.0, 20.0, 15.0, 15.0);

    protected static final VoxelShape SHORT_UP_ARM_AABB = Block.box(1.0, 0.0, 1.0, 15.0, 12.0, 15.0);

    protected static final VoxelShape SHORT_DOWN_ARM_AABB = Block.box(1.0, 4.0, 1.0, 15.0, 16.0, 15.0);

    protected static final VoxelShape SHORT_SOUTH_ARM_AABB = Block.box(1.0, 1.0, 0.0, 15.0, 15.0, 12.0);

    protected static final VoxelShape SHORT_NORTH_ARM_AABB = Block.box(1.0, 1.0, 4.0, 15.0, 15.0, 16.0);

    protected static final VoxelShape SHORT_EAST_ARM_AABB = Block.box(0.0, 1.0, 1.0, 12.0, 15.0, 15.0);

    protected static final VoxelShape SHORT_WEST_ARM_AABB = Block.box(4.0, 1.0, 1.0, 16.0, 15.0, 15.0);

    private static final VoxelShape[] EXTENDED_SHAPES = getShapesForExtension(true);

    private static final VoxelShape[] UNEXTENDED_SHAPES = getShapesForExtension(false);

    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public static final BooleanProperty SHORT = BlockStateProperties.SHORT;

    public SpringLauncherHeadBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(SHORT, false)).m_61124_(FACING, Direction.NORTH));
    }

    private static VoxelShape[] getShapesForExtension(boolean extended) {
        return (VoxelShape[]) Arrays.stream(Direction.values()).map(direction -> getShapeForDirection(direction, extended)).toArray(VoxelShape[]::new);
    }

    private static VoxelShape getShapeForDirection(Direction direction, boolean shortArm) {
        return switch(direction) {
            case UP ->
                Shapes.or(PISTON_EXTENSION_UP_AABB, shortArm ? SHORT_UP_ARM_AABB : UP_ARM_AABB);
            case NORTH ->
                Shapes.or(PISTON_EXTENSION_NORTH_AABB, shortArm ? SHORT_NORTH_ARM_AABB : NORTH_ARM_AABB);
            case SOUTH ->
                Shapes.or(PISTON_EXTENSION_SOUTH_AABB, shortArm ? SHORT_SOUTH_ARM_AABB : SOUTH_ARM_AABB);
            case WEST ->
                Shapes.or(PISTON_EXTENSION_WEST_AABB, shortArm ? SHORT_WEST_ARM_AABB : WEST_ARM_AABB);
            case EAST ->
                Shapes.or(PISTON_EXTENSION_EAST_AABB, shortArm ? SHORT_EAST_ARM_AABB : EAST_ARM_AABB);
            default ->
                Shapes.or(PISTON_EXTENSION_DOWN_AABB, shortArm ? SHORT_DOWN_ARM_AABB : DOWN_ARM_AABB);
        };
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return (state.m_61143_(SHORT) ? EXTENDED_SHAPES : UNEXTENDED_SHAPES)[((Direction) state.m_61143_(FACING)).ordinal()];
    }

    @Override
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entityIn, float fallDistance) {
        if (!entityIn.isSuppressingBounce() && state.m_61143_(FACING) == Direction.UP) {
            entityIn.causeFallDamage(fallDistance, 0.0F, level.damageSources().fall());
            if (entityIn instanceof LivingEntity && !level.isClientSide && fallDistance > (float) ((Integer) CommonConfigs.Redstone.LAUNCHER_HEIGHT.get()).intValue()) {
                level.setBlock(pos, (BlockState) ((BlockState) ((Block) ModRegistry.SPRING_LAUNCHER_ARM.get()).defaultBlockState().m_61124_(SpringLauncherArmBlock.EXTENDING, false)).m_61124_(FACING, (Direction) state.m_61143_(FACING)), 3);
                if (level.getBlockEntity(pos) instanceof SpringLauncherArmBlockTile tile) {
                    tile.retractOnFallOn();
                }
            }
        } else {
            super.m_142072_(level, state, pos, entityIn, fallDistance);
        }
    }

    private void bounceEntity(Entity entity) {
        Vec3 vector3d = entity.getDeltaMovement();
        if (vector3d.y < 0.0) {
            double d0 = entity instanceof LivingEntity ? 1.0 : 0.8;
            entity.setDeltaMovement(vector3d.x, -vector3d.y * d0, vector3d.z);
        }
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
        return true;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, SHORT);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return (BlockState) state.m_61124_(FACING, rot.rotate((Direction) state.m_61143_(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.m_60717_(mirrorIn.getRotation((Direction) state.m_61143_(FACING)));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return (BlockState) this.m_49966_().m_61124_(FACING, context.getNearestLookingDirection().getOpposite());
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        return new ItemStack((ItemLike) ModRegistry.SPRING_LAUNCHER.get());
    }

    @Override
    public void playerWillDestroy(Level worldIn, BlockPos pos, BlockState state, Player player) {
        if (!worldIn.isClientSide && player.getAbilities().instabuild) {
            BlockPos blockpos = pos.relative(((Direction) state.m_61143_(FACING)).getOpposite());
            Block block = worldIn.getBlockState(blockpos).m_60734_();
            if (block instanceof SpringLauncherBlock) {
                worldIn.removeBlock(blockpos, false);
            }
        }
        super.m_5707_(worldIn, pos, state, player);
    }

    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        BlockState comp = (BlockState) ((BlockState) ((Block) ModRegistry.SPRING_LAUNCHER_ARM.get()).defaultBlockState().m_61124_(SpringLauncherArmBlock.EXTENDING, false)).m_61124_(FACING, (Direction) state.m_61143_(FACING));
        if (state.m_60734_() != newState.m_60734_() && newState != comp) {
            super.m_6810_(state, worldIn, pos, newState, isMoving);
            Direction direction = ((Direction) state.m_61143_(FACING)).getOpposite();
            pos = pos.relative(direction);
            BlockState blockstate = worldIn.getBlockState(pos);
            if (blockstate.m_60734_() instanceof SpringLauncherBlock && (Boolean) blockstate.m_61143_(BlockStateProperties.EXTENDED)) {
                m_49950_(blockstate, worldIn, pos);
                worldIn.removeBlock(pos, false);
            }
        }
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
        return facing.getOpposite() == stateIn.m_61143_(FACING) && !stateIn.m_60710_(worldIn, currentPos) ? Blocks.AIR.defaultBlockState() : super.m_7417_(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
        BlockState bs = worldIn.m_8055_(pos.relative(((Direction) state.m_61143_(FACING)).getOpposite()));
        return bs == ((BlockState) ((Block) ModRegistry.SPRING_LAUNCHER.get()).defaultBlockState().m_61124_(BlockStateProperties.EXTENDED, true)).m_61124_(FACING, (Direction) state.m_61143_(FACING));
    }

    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if (state.m_60710_(worldIn, pos)) {
            BlockPos blockpos = pos.relative(((Direction) state.m_61143_(FACING)).getOpposite());
            worldIn.getBlockState(blockpos).m_60690_(worldIn, blockpos, blockIn, fromPos, false);
        }
    }
}