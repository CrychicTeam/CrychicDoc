package net.mehvahdjukaar.supplementaries.common.block.blocks;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.UnmodifiableIterator;
import com.google.common.collect.ImmutableMap.Builder;
import java.util.Map;
import net.mehvahdjukaar.moonlight.api.block.ILightable;
import net.mehvahdjukaar.moonlight.api.platform.ForgeHelper;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.mehvahdjukaar.supplementaries.common.block.ModBlockProperties;
import net.mehvahdjukaar.supplementaries.common.misc.explosion.GunpowderExplosion;
import net.mehvahdjukaar.supplementaries.configs.CommonConfigs;
import net.mehvahdjukaar.supplementaries.integration.CompatObjects;
import net.mehvahdjukaar.supplementaries.reg.ModSounds;
import net.mehvahdjukaar.supplementaries.reg.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.AbstractCandleBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RedstoneTorchBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.RedstoneSide;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class GunpowderBlock extends LightUpBlock {

    public static final EnumProperty<RedstoneSide> NORTH = BlockStateProperties.NORTH_REDSTONE;

    public static final EnumProperty<RedstoneSide> EAST = BlockStateProperties.EAST_REDSTONE;

    public static final EnumProperty<RedstoneSide> SOUTH = BlockStateProperties.SOUTH_REDSTONE;

    public static final EnumProperty<RedstoneSide> WEST = BlockStateProperties.WEST_REDSTONE;

    public static final IntegerProperty BURNING = ModBlockProperties.BURNING;

    public static final Map<Direction, EnumProperty<RedstoneSide>> PROPERTY_BY_DIRECTION = Maps.newEnumMap(ImmutableMap.of(Direction.NORTH, NORTH, Direction.EAST, EAST, Direction.SOUTH, SOUTH, Direction.WEST, WEST));

    private static final VoxelShape SHAPE_DOT = Block.box(3.0, 0.0, 3.0, 13.0, 1.0, 13.0);

    private static final Map<Direction, VoxelShape> SHAPES_FLOOR = Maps.newEnumMap(ImmutableMap.of(Direction.NORTH, Block.box(3.0, 0.0, 0.0, 13.0, 1.0, 13.0), Direction.SOUTH, Block.box(3.0, 0.0, 3.0, 13.0, 1.0, 16.0), Direction.EAST, Block.box(3.0, 0.0, 3.0, 16.0, 1.0, 13.0), Direction.WEST, Block.box(0.0, 0.0, 3.0, 13.0, 1.0, 13.0)));

    private static final Map<Direction, VoxelShape> SHAPES_UP = Maps.newEnumMap(ImmutableMap.of(Direction.NORTH, Shapes.or((VoxelShape) SHAPES_FLOOR.get(Direction.NORTH), Block.box(3.0, 0.0, 0.0, 13.0, 16.0, 1.0)), Direction.SOUTH, Shapes.or((VoxelShape) SHAPES_FLOOR.get(Direction.SOUTH), Block.box(3.0, 0.0, 15.0, 13.0, 16.0, 16.0)), Direction.EAST, Shapes.or((VoxelShape) SHAPES_FLOOR.get(Direction.EAST), Block.box(15.0, 0.0, 3.0, 16.0, 16.0, 13.0)), Direction.WEST, Shapes.or((VoxelShape) SHAPES_FLOOR.get(Direction.WEST), Block.box(0.0, 0.0, 3.0, 1.0, 16.0, 13.0))));

    private final Map<BlockState, VoxelShape> SHAPES_CACHE;

    private final BlockState crossState;

    private static int getDelay() {
        return (Integer) CommonConfigs.Tweaks.GUNPOWDER_BURN_SPEED.get();
    }

    private static int getSpreadAge() {
        return (Integer) CommonConfigs.Tweaks.GUNPOWDER_SPREAD_AGE.get();
    }

    public GunpowderBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(NORTH, RedstoneSide.NONE)).m_61124_(EAST, RedstoneSide.NONE)).m_61124_(SOUTH, RedstoneSide.NONE)).m_61124_(WEST, RedstoneSide.NONE)).m_61124_(BURNING, 0));
        this.crossState = (BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) this.m_49966_().m_61124_(NORTH, RedstoneSide.SIDE)).m_61124_(EAST, RedstoneSide.SIDE)).m_61124_(SOUTH, RedstoneSide.SIDE)).m_61124_(WEST, RedstoneSide.SIDE)).m_61124_(BURNING, 0);
        Builder<BlockState, VoxelShape> builder = ImmutableMap.builder();
        UnmodifiableIterator var3 = this.m_49965_().getPossibleStates().iterator();
        while (var3.hasNext()) {
            BlockState blockstate = (BlockState) var3.next();
            if ((Integer) blockstate.m_61143_(BURNING) == 0) {
                builder.put(blockstate, this.calculateVoxelShape(blockstate));
            }
        }
        this.SHAPES_CACHE = builder.build();
        RegHelper.registerBlockFlammability(this, 60, 300);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, SOUTH, WEST, BURNING);
    }

    private VoxelShape calculateVoxelShape(BlockState state) {
        VoxelShape voxelshape = SHAPE_DOT;
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            RedstoneSide redstoneside = (RedstoneSide) state.m_61143_((Property) PROPERTY_BY_DIRECTION.get(direction));
            if (redstoneside == RedstoneSide.SIDE) {
                voxelshape = Shapes.or(voxelshape, (VoxelShape) SHAPES_FLOOR.get(direction));
            } else if (redstoneside == RedstoneSide.UP) {
                voxelshape = Shapes.or(voxelshape, (VoxelShape) SHAPES_UP.get(direction));
            }
        }
        return voxelshape;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return (VoxelShape) this.SHAPES_CACHE.get(state.m_61124_(BURNING, 0));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.getConnectionState(context.m_43725_(), this.crossState, context.getClickedPos());
    }

    private BlockState getConnectionState(BlockGetter world, BlockState state, BlockPos pos) {
        boolean flag = isDot(state);
        state = this.getMissingConnections(world, (BlockState) this.m_49966_().m_61124_(BURNING, (Integer) state.m_61143_(BURNING)), pos);
        if (!flag || !isDot(state)) {
            boolean flag1 = ((RedstoneSide) state.m_61143_(NORTH)).isConnected();
            boolean flag2 = ((RedstoneSide) state.m_61143_(SOUTH)).isConnected();
            boolean flag3 = ((RedstoneSide) state.m_61143_(EAST)).isConnected();
            boolean flag4 = ((RedstoneSide) state.m_61143_(WEST)).isConnected();
            boolean flag5 = !flag1 && !flag2;
            boolean flag6 = !flag3 && !flag4;
            if (!flag4 && flag5) {
                state = (BlockState) state.m_61124_(WEST, RedstoneSide.SIDE);
            }
            if (!flag3 && flag5) {
                state = (BlockState) state.m_61124_(EAST, RedstoneSide.SIDE);
            }
            if (!flag1 && flag6) {
                state = (BlockState) state.m_61124_(NORTH, RedstoneSide.SIDE);
            }
            if (!flag2 && flag6) {
                state = (BlockState) state.m_61124_(SOUTH, RedstoneSide.SIDE);
            }
        }
        return state;
    }

    private BlockState getMissingConnections(BlockGetter world, BlockState state, BlockPos pos) {
        boolean flag = !world.getBlockState(pos.above()).m_60796_(world, pos);
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            if (!((RedstoneSide) state.m_61143_((Property) PROPERTY_BY_DIRECTION.get(direction))).isConnected()) {
                RedstoneSide redstoneside = this.getConnectingSide(world, pos, direction, flag);
                state = (BlockState) state.m_61124_((Property) PROPERTY_BY_DIRECTION.get(direction), redstoneside);
            }
        }
        return state;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState otherState, LevelAccessor world, BlockPos pos, BlockPos otherPos) {
        BlockState newState;
        if (direction == Direction.DOWN) {
            newState = this.canSurvive(state, world, pos) ? state : Blocks.AIR.defaultBlockState();
        } else if (direction == Direction.UP) {
            newState = this.getConnectionState(world, state, pos);
        } else {
            RedstoneSide redstoneside = this.getConnectingSide(world, pos, direction);
            newState = redstoneside.isConnected() == ((RedstoneSide) state.m_61143_((Property) PROPERTY_BY_DIRECTION.get(direction))).isConnected() && !isCross(state) ? (BlockState) state.m_61124_((Property) PROPERTY_BY_DIRECTION.get(direction), redstoneside) : this.getConnectionState(world, (BlockState) ((BlockState) this.crossState.m_61124_(BURNING, (Integer) state.m_61143_(BURNING))).m_61124_((Property) PROPERTY_BY_DIRECTION.get(direction), redstoneside), pos);
        }
        return newState;
    }

    private static boolean isCross(BlockState state) {
        return ((RedstoneSide) state.m_61143_(NORTH)).isConnected() && ((RedstoneSide) state.m_61143_(SOUTH)).isConnected() && ((RedstoneSide) state.m_61143_(EAST)).isConnected() && ((RedstoneSide) state.m_61143_(WEST)).isConnected();
    }

    private static boolean isDot(BlockState state) {
        return !((RedstoneSide) state.m_61143_(NORTH)).isConnected() && !((RedstoneSide) state.m_61143_(SOUTH)).isConnected() && !((RedstoneSide) state.m_61143_(EAST)).isConnected() && !((RedstoneSide) state.m_61143_(WEST)).isConnected();
    }

    @Override
    public void updateIndirectNeighbourShapes(BlockState state, LevelAccessor world, BlockPos pos, int var1, int var2) {
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            RedstoneSide redstoneside = (RedstoneSide) state.m_61143_((Property) PROPERTY_BY_DIRECTION.get(direction));
            if (redstoneside != RedstoneSide.NONE && !world.m_8055_(mutable.setWithOffset(pos, direction)).m_60713_(this)) {
                mutable.move(Direction.DOWN);
                BlockState blockstate = world.m_8055_(mutable);
                if (!blockstate.m_60713_(Blocks.OBSERVER)) {
                    BlockPos blockpos = mutable.m_121945_(direction.getOpposite());
                    BlockState blockstate1 = blockstate.m_60728_(direction.getOpposite(), world.m_8055_(blockpos), world, mutable, blockpos);
                    m_49908_(blockstate, blockstate1, world, mutable, var1, var2);
                }
                mutable.setWithOffset(pos, direction).move(Direction.UP);
                BlockState blockstate3 = world.m_8055_(mutable);
                if (!blockstate3.m_60713_(Blocks.OBSERVER)) {
                    BlockPos pos1 = mutable.m_121945_(direction.getOpposite());
                    BlockState blockstate2 = blockstate3.m_60728_(direction.getOpposite(), world.m_8055_(pos1), world, mutable, pos1);
                    m_49908_(blockstate3, blockstate2, world, mutable, var1, var2);
                }
            }
        }
    }

    private RedstoneSide getConnectingSide(BlockGetter world, BlockPos pos, Direction dir) {
        return this.getConnectingSide(world, pos, dir, !world.getBlockState(pos.above()).m_60796_(world, pos));
    }

    private RedstoneSide getConnectingSide(BlockGetter world, BlockPos pos, Direction dir, boolean canClimbUp) {
        BlockPos blockpos = pos.relative(dir);
        BlockState blockstate = world.getBlockState(blockpos);
        if (canClimbUp) {
            boolean flag = this.canSurviveOn(world, blockpos, blockstate);
            if (flag && this.canConnectTo(world.getBlockState(blockpos.above()), world, blockpos.above(), null)) {
                if (blockstate.m_60783_(world, blockpos, dir.getOpposite())) {
                    return RedstoneSide.UP;
                }
                return RedstoneSide.SIDE;
            }
        }
        return this.canConnectTo(blockstate, world, blockpos, dir) || !blockstate.m_60796_(world, blockpos) && this.canConnectTo(world.getBlockState(blockpos.below()), world, blockpos.below(), null) ? RedstoneSide.SIDE : RedstoneSide.NONE;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        BlockPos blockpos = pos.below();
        BlockState blockstate = world.m_8055_(blockpos);
        return this.canSurviveOn(world, blockpos, blockstate);
    }

    private boolean canSurviveOn(BlockGetter world, BlockPos pos, BlockState state) {
        return state.m_60783_(world, pos, Direction.UP) || state.m_60713_(Blocks.HOPPER);
    }

    protected boolean canConnectTo(BlockState state, BlockGetter world, BlockPos pos, @Nullable Direction dir) {
        Block b = state.m_60734_();
        return state.m_204336_(ModTags.LIGHTS_GUNPOWDER) || b instanceof ILightable || b instanceof TntBlock || b instanceof AbstractCandleBlock || b == CompatObjects.NUKE_BLOCK.get();
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return switch(rotation) {
            case CLOCKWISE_180 ->
                (BlockState) ((BlockState) ((BlockState) ((BlockState) state.m_61124_(NORTH, (RedstoneSide) state.m_61143_(SOUTH))).m_61124_(EAST, (RedstoneSide) state.m_61143_(WEST))).m_61124_(SOUTH, (RedstoneSide) state.m_61143_(NORTH))).m_61124_(WEST, (RedstoneSide) state.m_61143_(EAST));
            case COUNTERCLOCKWISE_90 ->
                (BlockState) ((BlockState) ((BlockState) ((BlockState) state.m_61124_(NORTH, (RedstoneSide) state.m_61143_(EAST))).m_61124_(EAST, (RedstoneSide) state.m_61143_(SOUTH))).m_61124_(SOUTH, (RedstoneSide) state.m_61143_(WEST))).m_61124_(WEST, (RedstoneSide) state.m_61143_(NORTH));
            case CLOCKWISE_90 ->
                (BlockState) ((BlockState) ((BlockState) ((BlockState) state.m_61124_(NORTH, (RedstoneSide) state.m_61143_(WEST))).m_61124_(EAST, (RedstoneSide) state.m_61143_(NORTH))).m_61124_(SOUTH, (RedstoneSide) state.m_61143_(EAST))).m_61124_(WEST, (RedstoneSide) state.m_61143_(SOUTH));
            default ->
                state;
        };
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return switch(mirror) {
            case LEFT_RIGHT ->
                (BlockState) ((BlockState) state.m_61124_(NORTH, (RedstoneSide) state.m_61143_(SOUTH))).m_61124_(SOUTH, (RedstoneSide) state.m_61143_(NORTH));
            case FRONT_BACK ->
                (BlockState) ((BlockState) state.m_61124_(EAST, (RedstoneSide) state.m_61143_(WEST))).m_61124_(WEST, (RedstoneSide) state.m_61143_(EAST));
            default ->
                super.m_6943_(state, mirror);
        };
    }

    @Override
    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean moving) {
        if (!oldState.m_60713_(state.m_60734_()) && !world.isClientSide) {
            world.m_186460_(pos, this, getDelay());
            for (Direction direction : Direction.Plane.VERTICAL) {
                world.updateNeighborsAt(pos.relative(direction), this);
            }
            this.updateNeighborsOfNeighboringWires(world, pos);
        }
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!isMoving && !state.m_60713_(newState.m_60734_())) {
            super.m_6810_(state, world, pos, newState, isMoving);
            if (!world.isClientSide) {
                for (Direction direction : Direction.values()) {
                    world.updateNeighborsAt(pos.relative(direction), this);
                }
                this.updateNeighborsOfNeighboringWires(world, pos);
            }
        }
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean moving) {
        super.m_6861_(state, world, pos, neighborBlock, neighborPos, moving);
        if (!world.isClientSide) {
            world.m_186460_(pos, this, getDelay());
        }
    }

    private void updateNeighborsOfNeighboringWires(Level world, BlockPos pos) {
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            this.checkCornerChangeAt(world, pos.relative(direction));
        }
        for (Direction direction1 : Direction.Plane.HORIZONTAL) {
            BlockPos blockpos = pos.relative(direction1);
            if (world.getBlockState(blockpos).m_60796_(world, blockpos)) {
                this.checkCornerChangeAt(world, blockpos.above());
            } else {
                this.checkCornerChangeAt(world, blockpos.below());
            }
        }
    }

    private void checkCornerChangeAt(Level world, BlockPos pos) {
        if (world.getBlockState(pos).m_60713_(this)) {
            world.updateNeighborsAt(pos, this);
            for (Direction direction : Direction.values()) {
                world.updateNeighborsAt(pos.relative(direction), this);
            }
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        InteractionResult lightUp = super.use(state, world, pos, player, hand, hit);
        if (lightUp.consumesAction()) {
            return lightUp;
        } else {
            if (Utils.mayPerformBlockAction(player, pos, player.m_21120_(hand)) && (isCross(state) || isDot(state))) {
                BlockState blockstate = isCross(state) ? this.m_49966_() : this.crossState;
                blockstate = (BlockState) blockstate.m_61124_(BURNING, (Integer) state.m_61143_(BURNING));
                blockstate = this.getConnectionState(world, blockstate, pos);
                if (blockstate != state) {
                    world.setBlock(pos, blockstate, 3);
                    this.updatesOnShapeChange(world, pos, state, blockstate);
                    return InteractionResult.SUCCESS;
                }
            }
            return InteractionResult.PASS;
        }
    }

    private void updatesOnShapeChange(Level world, BlockPos pos, BlockState state, BlockState newState) {
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            BlockPos blockpos = pos.relative(direction);
            if (((RedstoneSide) state.m_61143_((Property) PROPERTY_BY_DIRECTION.get(direction))).isConnected() != ((RedstoneSide) newState.m_61143_((Property) PROPERTY_BY_DIRECTION.get(direction))).isConnected() && world.getBlockState(blockpos).m_60796_(world, blockpos)) {
                world.updateNeighborsAtExceptFromFacing(blockpos, newState.m_60734_(), direction.getOpposite());
            }
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        int burning = (Integer) state.m_61143_(BURNING);
        if (!world.f_46443_) {
            if (burning == 8) {
                world.m_7471_(pos, false);
                createMiniExplosion(world, pos, false);
            } else if (burning > 0) {
                if (burning >= getSpreadAge()) {
                    this.lightUpNeighbouringWires(pos, state, world);
                }
                world.m_46597_(pos, (BlockState) state.m_61124_(BURNING, burning + 1));
                world.m_186460_(pos, this, getDelay());
            } else {
                for (Direction dir : Direction.values()) {
                    BlockPos p = pos.relative(dir);
                    if (isFireSource(world, p)) {
                        this.lightUp(null, state, pos, world, ILightable.FireSourceType.FLAMING_ARROW);
                        world.m_186460_(pos, this, getDelay());
                        break;
                    }
                }
            }
        }
    }

    public static void createMiniExplosion(Level world, BlockPos pos, boolean alwaysFire) {
        GunpowderExplosion explosion = new GunpowderExplosion(world, null, (double) pos.m_123341_(), (double) pos.m_123342_(), (double) pos.m_123343_(), 0.5F);
        if (!ForgeHelper.onExplosionStart(world, explosion)) {
            explosion.explode();
            explosion.finalizeExplosion(alwaysFire);
        }
    }

    @Override
    public boolean lightUp(Entity entity, BlockState state, BlockPos pos, LevelAccessor world, ILightable.FireSourceType fireSourceType) {
        boolean ret = super.lightUp(entity, state, pos, world, fireSourceType);
        if (ret) {
            if (!world.m_5776_()) {
                ((Level) world).blockEvent(pos, this, 0, 0);
            }
            world.scheduleTick(pos, this, getDelay());
        }
        return ret;
    }

    private void lightUpByWire(BlockState state, BlockPos pos, LevelAccessor level) {
        if (!this.isLitUp(state, level, pos)) {
            if (!level.m_5776_()) {
                ((Level) level).blockEvent(pos, this, 0, 0);
            }
            this.setLitUp(state, level, pos, true);
            level.playSound(null, pos, (SoundEvent) ModSounds.GUNPOWDER_IGNITE.get(), SoundSource.BLOCKS, 2.0F, 1.9F + level.getRandom().nextFloat() * 0.1F);
        }
    }

    protected void lightUpNeighbouringWires(BlockPos pos, BlockState state, Level world) {
        for (Direction dir : Direction.Plane.HORIZONTAL) {
            RedstoneSide side = (RedstoneSide) state.m_61143_((Property) PROPERTY_BY_DIRECTION.get(dir));
            BlockState neighbourState;
            BlockPos p;
            if (side == RedstoneSide.UP) {
                p = pos.relative(dir).above();
                neighbourState = world.getBlockState(p);
            } else {
                if (side != RedstoneSide.SIDE) {
                    continue;
                }
                p = pos.relative(dir);
                neighbourState = world.getBlockState(p);
                if (!neighbourState.m_60713_(this) && !neighbourState.m_60796_(world, pos)) {
                    p = p.below();
                    neighbourState = world.getBlockState(p);
                }
            }
            if (neighbourState.m_60713_(this)) {
                world.m_186460_(p, this, Math.max(getDelay() - 1, 1));
                this.lightUpByWire(neighbourState, p, world);
            }
        }
    }

    public static boolean isFireSource(BlockState state) {
        Block b = state.m_60734_();
        if (b instanceof TorchBlock && !(b instanceof RedstoneTorchBlock)) {
            return true;
        } else {
            return state.m_204336_(ModTags.LIGHTABLE_BY_GUNPOWDER) && state.m_61138_(BlockStateProperties.LIT) ? (Boolean) state.m_61143_(BlockStateProperties.LIT) : state.m_204336_(ModTags.LIGHTS_GUNPOWDER);
        }
    }

    public static boolean isFireSource(LevelAccessor world, BlockPos pos) {
        BlockState state = world.m_8055_(pos);
        return isFireSource(state);
    }

    @Override
    public void fallOn(Level world, BlockState state, BlockPos pos, Entity entity, float height) {
        super.m_142072_(world, state, pos, entity, height);
        if (height > 1.0F) {
            this.extinguish(entity, world.getBlockState(pos), pos, world);
        }
    }

    public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
        return 60;
    }

    public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
        return 300;
    }

    public void onCaughtFire(BlockState state, Level world, BlockPos pos, @Nullable Direction face, @Nullable LivingEntity igniter) {
    }

    @Override
    public boolean isLitUp(BlockState state, BlockGetter level, BlockPos pos) {
        return (Integer) state.m_61143_(BURNING) != 0;
    }

    @Override
    public void setLitUp(BlockState state, LevelAccessor world, BlockPos pos, boolean lit) {
        world.m_7731_(pos, (BlockState) state.m_61124_(BURNING, lit ? 1 : 0), 3);
    }

    @Override
    public boolean triggerEvent(BlockState state, Level world, BlockPos pos, int eventID, int eventParam) {
        if (eventID == 0) {
            this.animateTick((BlockState) state.m_61124_(BURNING, 1), world, pos, world.random);
            return true;
        } else {
            return super.m_8133_(state, world, pos, eventID, eventParam);
        }
    }

    @Override
    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
        int i = (Integer) state.m_61143_(BURNING);
        if (i != 0) {
            for (Direction direction : Direction.Plane.HORIZONTAL) {
                RedstoneSide redstoneside = (RedstoneSide) state.m_61143_((Property) PROPERTY_BY_DIRECTION.get(direction));
                switch(redstoneside) {
                    case UP:
                        this.spawnParticlesAlongLine(world, random, pos, i, direction, Direction.UP, -0.5F, 0.5F);
                    case SIDE:
                        this.spawnParticlesAlongLine(world, random, pos, i, Direction.DOWN, direction, 0.0F, 0.5F);
                        break;
                    case NONE:
                    default:
                        this.spawnParticlesAlongLine(world, random, pos, i, Direction.DOWN, direction, 0.0F, 0.3F);
                }
            }
        }
    }

    private void spawnParticlesAlongLine(Level world, RandomSource rand, BlockPos pos, int burning, Direction dir1, Direction dir2, float from, float to) {
        float f = to - from;
        float in = (7.5F - (float) (burning - 1)) / 7.5F;
        if (rand.nextFloat() < 1.0F * f * in) {
            float f2 = from + f * rand.nextFloat();
            double x = (double) pos.m_123341_() + 0.5 + (double) (0.4375F * (float) dir1.getStepX()) + (double) (f2 * (float) dir2.getStepX());
            double y = (double) pos.m_123342_() + 0.5 + (double) (0.4375F * (float) dir1.getStepY()) + (double) (f2 * (float) dir2.getStepY());
            double z = (double) pos.m_123343_() + 0.5 + (double) (0.4375F * (float) dir1.getStepZ()) + (double) (f2 * (float) dir2.getStepZ());
            float velY = (float) burning / 15.0F * 0.03F;
            float velX = rand.nextFloat() * 0.02F - 0.01F;
            float velZ = rand.nextFloat() * 0.02F - 0.01F;
            world.addParticle(ParticleTypes.FLAME, x, y, z, (double) velX, (double) velY, (double) velZ);
            world.addParticle(ParticleTypes.LARGE_SMOKE, x, y, z, (double) velX, (double) velY, (double) velZ);
        }
    }
}