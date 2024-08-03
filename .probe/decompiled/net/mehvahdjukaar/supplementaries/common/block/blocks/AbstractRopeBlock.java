package net.mehvahdjukaar.supplementaries.common.block.blocks;

import java.util.Map;
import net.mehvahdjukaar.moonlight.api.block.WaterBlock;
import net.mehvahdjukaar.supplementaries.common.block.IRopeConnection;
import net.mehvahdjukaar.supplementaries.common.block.ModBlockProperties;
import net.mehvahdjukaar.supplementaries.common.misc.RopeHelper;
import net.mehvahdjukaar.supplementaries.common.utils.ItemsUtil;
import net.mehvahdjukaar.supplementaries.configs.CommonConfigs;
import net.mehvahdjukaar.supplementaries.integration.CompatHandler;
import net.mehvahdjukaar.supplementaries.integration.DecoBlocksCompat;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public abstract class AbstractRopeBlock extends WaterBlock implements IRopeConnection {

    public static final VoxelShape COLLISION_SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 13.0, 16.0);

    public static final BooleanProperty KNOT = ModBlockProperties.KNOT;

    private final Map<BlockState, VoxelShape> shapes;

    public AbstractRopeBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(KNOT, false)).m_61124_(WATERLOGGED, false));
        this.shapes = this.makeShapes();
    }

    @Override
    public boolean canBeReplaced(BlockState state, Fluid fluid) {
        return false;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return (VoxelShape) this.shapes.getOrDefault(state.m_61124_(WATERLOGGED, false), Shapes.block());
    }

    protected abstract Map<BlockState, VoxelShape> makeShapes();

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED, KNOT);
    }

    public boolean isLadder(BlockState state, LevelReader world, BlockPos pos, LivingEntity entity) {
        return this.hasConnection(Direction.DOWN, state) && (this.hasConnection(Direction.UP, state) || entity.m_20182_().y() - (double) pos.m_123342_() < 0.8125);
    }

    public abstract boolean hasConnection(Direction var1, BlockState var2);

    public abstract BlockState setConnection(Direction var1, BlockState var2, boolean var3);

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        if ((this.hasConnection(Direction.UP, state) || !context.isAbove(COLLISION_SHAPE, pos, true) && this.hasConnection(Direction.DOWN, state)) && context instanceof EntityCollisionContext ec && ec.getEntity() instanceof LivingEntity) {
            return Shapes.empty();
        }
        return this.getShape(state, worldIn, pos, context);
    }

    public boolean shouldConnectToDir(BlockState thisState, BlockPos currentPos, LevelReader world, Direction dir) {
        if (dir.getAxis().isHorizontal() && !(Boolean) CommonConfigs.Functional.ROPE_HORIZONTAL.get()) {
            return false;
        } else {
            BlockPos facingPos = currentPos.relative(dir);
            return this.shouldConnectToFace(thisState, world.m_8055_(facingPos), facingPos, dir, world);
        }
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
        super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
        if (!worldIn.m_5776_()) {
            worldIn.scheduleTick(currentPos, this, 1);
        }
        if (facing == Direction.UP) {
            stateIn = this.setConnection(Direction.DOWN, stateIn, this.shouldConnectToDir(stateIn, currentPos, worldIn, Direction.DOWN));
        }
        stateIn = this.setConnection(facing, stateIn, this.shouldConnectToDir(stateIn, currentPos, worldIn, facing));
        if (facing == Direction.DOWN && !worldIn.m_5776_() && CompatHandler.DECO_BLOCKS) {
            DecoBlocksCompat.tryConvertingRopeChandelier(facingState, worldIn, facingPos);
        }
        return (BlockState) stateIn.m_61124_(KNOT, this.hasMiddleKnot(stateIn));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level world = context.m_43725_();
        BlockPos pos = context.getClickedPos();
        boolean hasWater = context.m_43725_().getFluidState(pos).getType() == Fluids.WATER;
        BlockState state = this.m_49966_();
        for (Direction dir : Direction.values()) {
            state = this.setConnection(dir, state, this.shouldConnectToDir(state, pos, world, dir));
        }
        state = (BlockState) state.m_61124_(WATERLOGGED, hasWater);
        return (BlockState) state.m_61124_(KNOT, this.hasMiddleKnot(state));
    }

    @Override
    public void onPlace(BlockState state, Level worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!worldIn.isClientSide) {
            worldIn.m_186460_(pos, this, 1);
            if (CompatHandler.DECO_BLOCKS) {
                BlockPos down = pos.below();
                DecoBlocksCompat.tryConvertingRopeChandelier(worldIn.getBlockState(down), worldIn, down);
            }
        }
    }

    public boolean hasMiddleKnot(BlockState state) {
        boolean up = this.hasConnection(Direction.UP, state);
        boolean down = this.hasConnection(Direction.DOWN, state);
        boolean north = this.hasConnection(Direction.NORTH, state);
        boolean east = this.hasConnection(Direction.EAST, state);
        boolean south = this.hasConnection(Direction.SOUTH, state);
        boolean west = this.hasConnection(Direction.WEST, state);
        return (!up || !down || north || south || east || west) && (up || down || !north || !south || east || west) && (up || down || north || south || !east || !west);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        BlockPos.MutableBlockPos mutable = pos.mutable().move(Direction.UP);
        BlockState upstate = world.m_8055_(mutable);
        if (upstate.m_60713_(this)) {
            return true;
        } else if (IRopeConnection.isSupportingCeiling(mutable, world)) {
            return true;
        } else {
            if ((Boolean) CommonConfigs.Functional.ROPE_HORIZONTAL.get()) {
                for (Direction direction : Direction.Plane.HORIZONTAL) {
                    BlockPos facingPos = mutable.setWithOffset(pos, direction);
                    BlockState sideState = world.m_8055_(facingPos);
                    Block b = sideState.m_60734_();
                    if (b instanceof AbstractRopeBlock) {
                        return true;
                    }
                    if (this.shouldConnectToFace(this.m_49966_(), sideState, facingPos, direction, world)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand) {
        if (!this.canSurvive(state, level, pos)) {
            level.m_46961_(pos, true);
        } else {
            for (Direction dir : Direction.values()) {
                if (dir != Direction.UP && level.m_8055_(pos.relative(dir)).m_204336_(BlockTags.FIRE)) {
                    level.m_186460_(pos.relative(dir), Blocks.FIRE, 2 + level.f_46441_.nextInt(1));
                    for (Direction d2 : Direction.Plane.HORIZONTAL) {
                        BlockPos fp = pos.relative(d2);
                        if (BaseFireBlock.canBePlacedAt(level, fp, d2.getOpposite())) {
                            BlockState fireState = BaseFireBlock.getState(level, fp);
                            if (fireState.m_61138_(FireBlock.AGE)) {
                                fireState = (BlockState) fireState.m_61124_(FireBlock.AGE, 14);
                            }
                            level.m_46597_(fp, fireState);
                            level.m_186460_(pos.relative(dir), Blocks.FIRE, 2 + level.f_46441_.nextInt(1));
                        }
                    }
                    return;
                }
            }
        }
    }

    public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
        return state.m_61143_(BlockStateProperties.WATERLOGGED) ? 0 : 10;
    }

    public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
        return state.m_61143_(BlockStateProperties.WATERLOGGED) ? 0 : 100;
    }

    private static boolean findConnectedPulley(Level world, BlockPos pos, Player player, int it, Rotation rot) {
        if (it > 64) {
            return false;
        } else {
            BlockState state = world.getBlockState(pos);
            Block b = state.m_60734_();
            if (b instanceof AbstractRopeBlock) {
                return findConnectedPulley(world, pos.above(), player, it + 1, rot);
            } else {
                if (b instanceof PulleyBlock pulley && it != 0) {
                    return pulley.windPulley(state, pos, world, rot, null);
                }
                return false;
            }
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        ItemStack stack = player.m_21120_(handIn);
        Item i = stack.getItem();
        if (i != this.m_5456_()) {
            if (stack.isEmpty()) {
                if (this.hasConnection(Direction.UP, state) && findConnectedPulley(world, pos, player, 0, player.m_6144_() ? Rotation.COUNTERCLOCKWISE_90 : Rotation.CLOCKWISE_90)) {
                    return InteractionResult.sidedSuccess(world.isClientSide);
                }
                if (!player.m_6144_() && handIn == InteractionHand.MAIN_HAND && world.getBlockState(pos.below()).m_60734_() == this && RopeHelper.removeRopeDown(pos.below(), world, this)) {
                    world.playSound(player, pos, SoundEvents.LEASH_KNOT_PLACE, SoundSource.BLOCKS, 1.0F, 0.6F);
                    if (!player.getAbilities().instabuild) {
                        ItemsUtil.addStackToExisting(player, new ItemStack(this), true);
                    }
                    return InteractionResult.sidedSuccess(world.isClientSide);
                }
            } else if (i instanceof ShearsItem) {
                if (this.hasConnection(Direction.DOWN, state)) {
                    if (!world.isClientSide) {
                        world.playSound(null, pos, SoundEvents.SNOW_GOLEM_SHEAR, player == null ? SoundSource.BLOCKS : SoundSource.PLAYERS, 0.8F, 1.3F);
                        BlockState newState = (BlockState) this.setConnection(Direction.DOWN, state, false).m_61124_(KNOT, true);
                        world.setBlock(pos, newState, 3);
                    }
                    return InteractionResult.sidedSuccess(world.isClientSide);
                }
                return InteractionResult.PASS;
            }
            return InteractionResult.PASS;
        } else {
            if (hit.getDirection().getAxis() == Direction.Axis.Y || this.hasConnection(Direction.DOWN, state)) {
                if (this.hasConnection(Direction.UP, state) && !this.hasConnection(Direction.DOWN, state)) {
                    state = this.setConnection(Direction.DOWN, state, true);
                    world.setBlock(pos, state, 0);
                }
                if (RopeHelper.addRopeDown(pos.below(), world, player, handIn, this)) {
                    SoundType soundtype = state.m_60827_();
                    world.playSound(player, pos, soundtype.getPlaceSound(), SoundSource.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
                    if (!player.getAbilities().instabuild) {
                        stack.shrink(1);
                    }
                    return InteractionResult.sidedSuccess(world.isClientSide);
                }
            }
            return InteractionResult.PASS;
        }
    }

    @Override
    public void entityInside(BlockState state, Level worldIn, BlockPos pos, Entity entityIn) {
        super.m_7892_(state, worldIn, pos, entityIn);
        if (entityIn instanceof Arrow && !worldIn.isClientSide) {
            worldIn.m_46953_(pos, true, entityIn);
            worldIn.playSound(null, pos, SoundEvents.LEASH_KNOT_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
    }

    @Override
    public boolean skipRendering(BlockState pState, BlockState pAdjacentBlockState, Direction pSide) {
        return pAdjacentBlockState.m_60713_(this) || super.m_6104_(pState, pAdjacentBlockState, pSide);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return switch(rotation) {
            case CLOCKWISE_180 ->
                {
                    state = this.setConnection(Direction.NORTH, state, this.hasConnection(Direction.SOUTH, state));
                    state = this.setConnection(Direction.EAST, state, this.hasConnection(Direction.WEST, state));
                    state = this.setConnection(Direction.SOUTH, state, this.hasConnection(Direction.NORTH, state));
                    state = this.setConnection(Direction.WEST, state, this.hasConnection(Direction.EAST, state));
                    yield state;
                }
            case COUNTERCLOCKWISE_90 ->
                {
                    state = this.setConnection(Direction.NORTH, state, this.hasConnection(Direction.EAST, state));
                    state = this.setConnection(Direction.EAST, state, this.hasConnection(Direction.SOUTH, state));
                    state = this.setConnection(Direction.SOUTH, state, this.hasConnection(Direction.WEST, state));
                    state = this.setConnection(Direction.WEST, state, this.hasConnection(Direction.NORTH, state));
                    yield state;
                }
            case CLOCKWISE_90 ->
                {
                    state = this.setConnection(Direction.NORTH, state, this.hasConnection(Direction.WEST, state));
                    state = this.setConnection(Direction.EAST, state, this.hasConnection(Direction.NORTH, state));
                    state = this.setConnection(Direction.SOUTH, state, this.hasConnection(Direction.EAST, state));
                    state = this.setConnection(Direction.WEST, state, this.hasConnection(Direction.SOUTH, state));
                    yield state;
                }
            default ->
                state;
        };
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return switch(mirror) {
            case LEFT_RIGHT ->
                {
                    state = this.setConnection(Direction.NORTH, state, this.hasConnection(Direction.SOUTH, state));
                    state = this.setConnection(Direction.SOUTH, state, this.hasConnection(Direction.NORTH, state));
                    yield state;
                }
            case FRONT_BACK ->
                {
                    state = this.setConnection(Direction.EAST, state, this.hasConnection(Direction.WEST, state));
                    state = this.setConnection(Direction.WEST, state, this.hasConnection(Direction.EAST, state));
                    yield state;
                }
            default ->
                super.m_6943_(state, mirror);
        };
    }

    @Override
    public boolean canSideAcceptConnection(BlockState state, Direction direction) {
        return true;
    }
}