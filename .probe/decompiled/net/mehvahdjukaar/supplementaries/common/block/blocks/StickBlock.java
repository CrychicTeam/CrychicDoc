package net.mehvahdjukaar.supplementaries.common.block.blocks;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import net.mehvahdjukaar.moonlight.api.block.IRotatable;
import net.mehvahdjukaar.moonlight.api.block.WaterBlock;
import net.mehvahdjukaar.supplementaries.common.block.ModBlockProperties;
import net.mehvahdjukaar.supplementaries.common.block.tiles.FlagBlockTile;
import net.mehvahdjukaar.supplementaries.configs.CommonConfigs;
import net.mehvahdjukaar.supplementaries.integration.CompatHandler;
import net.mehvahdjukaar.supplementaries.integration.FarmersDelightCompat;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class StickBlock extends WaterBlock implements IRotatable {

    protected static final VoxelShape Y_AXIS_AABB = Block.box(7.0, 0.0, 7.0, 9.0, 16.0, 9.0);

    protected static final VoxelShape Z_AXIS_AABB = Block.box(7.0, 7.0, 0.0, 9.0, 9.0, 16.0);

    protected static final VoxelShape X_AXIS_AABB = Block.box(0.0, 7.0, 7.0, 16.0, 9.0, 9.0);

    protected static final VoxelShape Y_Z_AXIS_AABB = Shapes.or(Y_AXIS_AABB, Z_AXIS_AABB);

    protected static final VoxelShape Y_X_AXIS_AABB = Shapes.or(Y_AXIS_AABB, X_AXIS_AABB);

    protected static final VoxelShape X_Z_AXIS_AABB = Shapes.or(X_AXIS_AABB, Z_AXIS_AABB);

    protected static final VoxelShape X_Y_Z_AXIS_AABB = Shapes.or(X_AXIS_AABB, Y_AXIS_AABB, Z_AXIS_AABB);

    public static final BooleanProperty AXIS_X = ModBlockProperties.AXIS_X;

    public static final BooleanProperty AXIS_Y = ModBlockProperties.AXIS_Y;

    public static final BooleanProperty AXIS_Z = ModBlockProperties.AXIS_Z;

    public static final Map<Direction.Axis, BooleanProperty> AXIS2PROPERTY = Map.of(Direction.Axis.X, AXIS_X, Direction.Axis.Y, AXIS_Y, Direction.Axis.Z, AXIS_Z);

    private final int fireSpread;

    public StickBlock(BlockBehaviour.Properties properties, int fireSpread) {
        super(properties);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(WATERLOGGED, Boolean.FALSE)).m_61124_(AXIS_Y, true)).m_61124_(AXIS_X, false)).m_61124_(AXIS_Z, false));
        this.fireSpread = fireSpread;
    }

    public StickBlock(BlockBehaviour.Properties properties) {
        this(properties, 60);
    }

    public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
        return state.m_61143_(BlockStateProperties.WATERLOGGED) ? 0 : this.fireSpread;
    }

    public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
        return state.m_61143_(BlockStateProperties.WATERLOGGED) ? 0 : this.fireSpread;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(AXIS_X, AXIS_Y, AXIS_Z);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext context) {
        boolean x = (Boolean) state.m_61143_(AXIS_X);
        boolean y = (Boolean) state.m_61143_(AXIS_Y);
        boolean z = (Boolean) state.m_61143_(AXIS_Z);
        return getStickShape(x, y, z);
    }

    public static VoxelShape getStickShape(boolean x, boolean y, boolean z) {
        if (x) {
            if (y) {
                return z ? X_Y_Z_AXIS_AABB : Y_X_AXIS_AABB;
            } else {
                return z ? X_Z_AXIS_AABB : X_AXIS_AABB;
            }
        } else if (z) {
            return y ? Y_Z_AXIS_AABB : Z_AXIS_AABB;
        } else {
            return Y_AXIS_AABB;
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState blockstate = context.m_43725_().getBlockState(context.getClickedPos());
        BooleanProperty axis = (BooleanProperty) AXIS2PROPERTY.get(context.m_43719_().getAxis());
        return !blockstate.m_60713_(this) && (!CompatHandler.FARMERS_DELIGHT || !FarmersDelightCompat.canAddStickToTomato(blockstate, axis)) ? (BlockState) ((BlockState) super.getStateForPlacement(context).m_61124_(AXIS_Y, false)).m_61124_(axis, true) : (BlockState) blockstate.m_61124_(axis, true);
    }

    @Override
    public boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
        if (!context.m_7078_() && context.m_43722_().is(this.m_5456_())) {
            BooleanProperty axis = (BooleanProperty) AXIS2PROPERTY.get(context.m_43719_().getAxis());
            if (!(Boolean) state.m_61143_(axis)) {
                return true;
            }
        }
        return super.m_6864_(state, context);
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter worldIn, BlockPos pos, PathComputationType type) {
        return false;
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!player.m_21120_(hand).isEmpty() || hand != InteractionHand.MAIN_HAND || !(Boolean) CommonConfigs.Building.FLAG_POLE.get()) {
            return InteractionResult.PASS;
        } else if (this != ModRegistry.STICK_BLOCK.get()) {
            return InteractionResult.PASS;
        } else if (world.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            Direction moveDir = player.m_6144_() ? Direction.DOWN : Direction.UP;
            findConnectedFlag(world, pos, Direction.UP, moveDir, 0);
            findConnectedFlag(world, pos, Direction.DOWN, moveDir, 0);
            return InteractionResult.CONSUME;
        }
    }

    private static boolean isVertical(BlockState state) {
        return (Boolean) state.m_61143_(AXIS_Y) && !(Boolean) state.m_61143_(AXIS_X) && !(Boolean) state.m_61143_(AXIS_Z);
    }

    public static boolean findConnectedFlag(Level world, BlockPos pos, Direction searchDir, Direction moveDir, int it) {
        if (it > (Integer) CommonConfigs.Building.FLAG_POLE_LENGTH.get()) {
            return false;
        } else {
            BlockState state = world.getBlockState(pos);
            Block b = state.m_60734_();
            if (b == ModRegistry.STICK_BLOCK.get() && isVertical(state)) {
                return findConnectedFlag(world, pos.relative(searchDir), searchDir, moveDir, it + 1);
            } else {
                if (b instanceof FlagBlock && it != 0) {
                    BlockPos toPos = pos.relative(moveDir);
                    BlockState stick = world.getBlockState(toPos);
                    if (world.getBlockEntity(pos) instanceof FlagBlockTile tile && stick.m_60734_() == ModRegistry.STICK_BLOCK.get() && isVertical(stick)) {
                        world.setBlockAndUpdate(pos, stick);
                        world.setBlockAndUpdate(toPos, state);
                        CompoundTag tag = tile.m_187482_();
                        BlockEntity te = world.getBlockEntity(toPos);
                        if (te != null) {
                            te.load(tag);
                        }
                        world.playSound(null, toPos, SoundEvents.WOOL_PLACE, SoundSource.BLOCKS, 1.0F, 1.4F);
                        return true;
                    }
                }
                return false;
            }
        }
    }

    public BlockState applyRotationLock(Level world, BlockPos blockPos, BlockState state, Direction dir, int half) {
        int i = 0;
        if ((Boolean) state.m_61143_(AXIS_X)) {
            i++;
        }
        if ((Boolean) state.m_61143_(AXIS_Y)) {
            i++;
        }
        if ((Boolean) state.m_61143_(AXIS_Z)) {
            i++;
        }
        if (i == 1) {
            ((BlockState) ((BlockState) ((BlockState) state.m_61124_(AXIS_Z, false)).m_61124_(AXIS_X, false)).m_61124_(AXIS_Y, false)).m_61124_((Property) AXIS2PROPERTY.get(dir.getAxis()), true);
        }
        return state;
    }

    @Override
    public Optional<BlockState> getRotatedState(BlockState state, LevelAccessor world, BlockPos pos, Rotation rotation, Direction axis, @Nullable Vec3 hit) {
        if (rotation == Rotation.CLOCKWISE_180) {
            return Optional.empty();
        } else {
            boolean x = (Boolean) state.m_61143_(AXIS_X);
            boolean y = (Boolean) state.m_61143_(AXIS_Y);
            boolean z = (Boolean) state.m_61143_(AXIS_Z);
            BlockState newState = switch(axis.getAxis()) {
                case Y ->
                    (BlockState) ((BlockState) state.m_61124_(AXIS_X, z)).m_61124_(AXIS_Z, x);
                case X ->
                    (BlockState) ((BlockState) state.m_61124_(AXIS_Y, z)).m_61124_(AXIS_Z, y);
                case Z ->
                    (BlockState) ((BlockState) state.m_61124_(AXIS_X, y)).m_61124_(AXIS_Y, x);
            };
            return newState != state ? Optional.of(newState) : Optional.empty();
        }
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return (BlockState) this.getRotatedState(state, null, null, rotation, Direction.UP, null).orElse(state);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder pBuilder) {
        int i = 0;
        if ((Boolean) state.m_61143_(AXIS_X)) {
            i++;
        }
        if ((Boolean) state.m_61143_(AXIS_Y)) {
            i++;
        }
        if ((Boolean) state.m_61143_(AXIS_Z)) {
            i++;
        }
        return List.of(new ItemStack(this.m_5456_(), i));
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (this == ModRegistry.STICK_BLOCK.get()) {
        }
        return super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }
}