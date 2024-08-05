package net.mehvahdjukaar.supplementaries.common.block.blocks;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableList;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.Supplier;
import net.mehvahdjukaar.moonlight.api.block.IColored;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.mehvahdjukaar.moonlight.api.util.math.MthUtils;
import net.mehvahdjukaar.supplementaries.common.block.IRopeConnection;
import net.mehvahdjukaar.supplementaries.common.utils.MiscUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class CandleHolderBlock extends LightUpWaterBlock implements IColored {

    protected static final VoxelShape SHAPE_FLOOR = Block.box(5.0, 0.0, 5.0, 11.0, 14.0, 11.0);

    protected static final VoxelShape SHAPE_WALL_NORTH = Block.box(5.0, 0.0, 11.0, 11.0, 14.0, 16.0);

    protected static final VoxelShape SHAPE_WALL_SOUTH = Block.box(5.0, 0.0, 0.0, 11.0, 14.0, 5.0);

    protected static final VoxelShape SHAPE_WALL_WEST = Block.box(11.0, 0.0, 5.0, 16.0, 14.0, 11.0);

    protected static final VoxelShape SHAPE_WALL_EAST = Block.box(0.0, 0.0, 5.0, 5.0, 14.0, 11.0);

    protected static final VoxelShape SHAPE_CEILING = Block.box(5.0, 3.0, 5.0, 11.0, 16.0, 11.0);

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public static final EnumProperty<AttachFace> FACE = BlockStateProperties.ATTACH_FACE;

    public static final IntegerProperty CANDLES = BlockStateProperties.CANDLES;

    private static final EnumMap<Direction, EnumMap<AttachFace, Int2ObjectMap<List<Vec3>>>> PARTICLE_OFFSETS = new EnumMap(Direction.class);

    private static final List<Vec3> S2_FLOOR_1 = List.of(new Vec3(0.5, 0.875, 0.5));

    private static final List<Vec3> S2_FLOOR_3 = List.of(new Vec3(0.1875, 0.875, 0.5), new Vec3(0.5, 0.9375, 0.5), new Vec3(0.8125, 0.875, 0.5));

    private static final List<Vec3> S2_FLOOR_3f = List.of(new Vec3(0.5, 0.875, 0.1875), new Vec3(0.5, 0.9375, 0.5), new Vec3(0.5, 0.875, 0.8125));

    private final Supplier<Boolean> isFromSuppSquared = Suppliers.memoize(() -> !Utils.getID((Block) this).getNamespace().equals("supplementaries"));

    @Nullable
    private final DyeColor color;

    private final Supplier<ParticleType<? extends ParticleOptions>> particle;

    public CandleHolderBlock(DyeColor color, BlockBehaviour.Properties properties) {
        this(color, properties, () -> ParticleTypes.SMALL_FLAME);
    }

    public CandleHolderBlock(DyeColor color, BlockBehaviour.Properties properties, Supplier<ParticleType<? extends ParticleOptions>> particle) {
        super(properties.lightLevel(CandleHolderBlock::lightLevel));
        this.color = color;
        this.particle = particle;
        this.m_49959_((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(WATERLOGGED, false)).m_61124_(LIT, false)).m_61124_(FACE, AttachFace.FLOOR)).m_61124_(FACING, Direction.NORTH)).m_61124_(CANDLES, 1));
    }

    private static int lightLevel(BlockState state) {
        if ((Boolean) state.m_61143_(LIT)) {
            int candles = (Integer) state.m_61143_(CANDLES);
            return 7 + candles * 2;
        } else {
            return 0;
        }
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter worldIn, BlockPos pos, PathComputationType type) {
        return false;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState blockState = context.m_43725_().getBlockState(context.getClickedPos());
        if (blockState.m_60713_(this)) {
            return (BlockState) blockState.m_61124_(CANDLES, Math.min(4, (Integer) blockState.m_61143_(CANDLES) + 1));
        } else {
            boolean flag = context.m_43725_().getFluidState(context.getClickedPos()).getType() == Fluids.WATER;
            for (Direction direction : context.getNearestLookingDirections()) {
                BlockState blockstate;
                if (direction.getAxis() == Direction.Axis.Y) {
                    blockstate = (BlockState) ((BlockState) this.m_49966_().m_61124_(FACE, direction == Direction.UP ? AttachFace.CEILING : AttachFace.FLOOR)).m_61124_(FACING, context.m_8125_());
                } else {
                    blockstate = (BlockState) ((BlockState) this.m_49966_().m_61124_(FACE, AttachFace.WALL)).m_61124_(FACING, direction.getOpposite());
                }
                if (blockstate.m_60710_(context.m_43725_(), context.getClickedPos())) {
                    return (BlockState) ((BlockState) blockstate.m_61124_(WATERLOGGED, flag)).m_61124_(LIT, false);
                }
            }
            return null;
        }
    }

    @Override
    public boolean canBeReplaced(BlockState state, BlockPlaceContext useContext) {
        return !useContext.m_7078_() && useContext.m_43722_().is(this.m_5456_()) && (Integer) state.m_61143_(CANDLES) < 4 || super.m_6864_(state, useContext);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACE, FACING, CANDLES);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return switch((AttachFace) state.m_61143_(FACE)) {
            case FLOOR ->
                SHAPE_FLOOR;
            case WALL ->
                {
                    switch((Direction) state.m_61143_(FACING)) {
                        case SOUTH:
                            yield SHAPE_WALL_SOUTH;
                        case WEST:
                            yield SHAPE_WALL_WEST;
                        case EAST:
                            yield SHAPE_WALL_EAST;
                        default:
                            yield SHAPE_WALL_NORTH;
                    }
                }
            case CEILING ->
                SHAPE_CEILING;
        };
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (context instanceof EntityCollisionContext ec && ec.getEntity() instanceof Projectile) {
            return state.m_60808_(level, pos);
        }
        return Shapes.empty();
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
        if (state.m_61143_(FACE) == AttachFace.FLOOR) {
            return m_49863_(worldIn, pos.below(), Direction.UP);
        } else {
            return state.m_61143_(FACE) == AttachFace.CEILING ? IRopeConnection.isSupportingCeiling(pos.above(), worldIn) : isSideSolidForDirection(worldIn, pos, ((Direction) state.m_61143_(FACING)).getOpposite());
        }
    }

    private void addParticlesAndSound(Level level, Vec3 offset, RandomSource random) {
        float f = random.nextFloat();
        if (f < 0.3F) {
            level.addParticle(ParticleTypes.SMOKE, offset.x, offset.y, offset.z, 0.0, 0.0, 0.0);
            if (f < 0.17F) {
                level.playLocalSound(offset.x + 0.5, offset.y + 0.5, offset.z + 0.5, SoundEvents.CANDLE_AMBIENT, SoundSource.BLOCKS, 1.0F + random.nextFloat(), random.nextFloat() * 0.7F + 0.3F, false);
            }
        }
        level.addParticle((ParticleOptions) this.particle.get(), offset.x, offset.y, offset.z, 0.0, 0.0, 0.0);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource rand) {
        if ((Boolean) state.m_61143_(LIT)) {
            this.getParticleOffset(state).forEach(v -> this.addParticlesAndSound(level, v.add((double) pos.m_123341_(), (double) pos.m_123342_(), (double) pos.m_123343_()), rand));
        }
    }

    private List<Vec3> getParticleOffset(BlockState state) {
        Direction direction = (Direction) state.m_61143_(FACING);
        AttachFace face = (AttachFace) state.m_61143_(FACE);
        int candles = (Integer) state.m_61143_(CANDLES);
        List<Vec3> v = (List<Vec3>) ((Int2ObjectMap) ((EnumMap) PARTICLE_OFFSETS.get(direction)).get(face)).get(candles);
        if ((Boolean) this.isFromSuppSquared.get() && face == AttachFace.FLOOR) {
            if (candles == 1) {
                return S2_FLOOR_1;
            }
            if (candles == 3) {
                return direction.getAxis() == Direction.Axis.Z ? S2_FLOOR_3 : S2_FLOOR_3f;
            }
        }
        return v;
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
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
        return getFacing(stateIn).getOpposite() == facing && !stateIn.m_60710_(worldIn, currentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    protected static Direction getFacing(BlockState state) {
        return switch((AttachFace) state.m_61143_(FACE)) {
            case FLOOR ->
                Direction.UP;
            case CEILING ->
                Direction.DOWN;
            default ->
                (Direction) state.m_61143_(FACING);
        };
    }

    public static boolean isSideSolidForDirection(LevelReader reader, BlockPos pos, Direction direction) {
        BlockPos blockpos = pos.relative(direction);
        return reader.m_8055_(blockpos).m_60783_(reader, blockpos, direction.getOpposite());
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.m_5871_(stack, worldIn, tooltip, flagIn);
        if (MiscUtils.showsHints(worldIn, flagIn)) {
            tooltip.add(Component.translatable("message.supplementaries.candle_holder").withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC));
        }
    }

    @Nullable
    @Override
    public DyeColor getColor() {
        return this.color;
    }

    @Override
    public boolean supportsBlankColor() {
        return true;
    }

    @Override
    public boolean canBeExtinguishedBy(ItemStack item) {
        return item.isEmpty() || super.canBeExtinguishedBy(item);
    }

    @Override
    public void playExtinguishSound(LevelAccessor world, BlockPos pos) {
        world.playSound(null, pos, SoundEvents.CANDLE_EXTINGUISH, SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    @Override
    public void spawnSmokeParticles(BlockState state, BlockPos pos, LevelAccessor level) {
        ((CandleHolderBlock) state.m_60734_()).getParticleOffset(state).forEach(vec3 -> level.addParticle(ParticleTypes.SMOKE, (double) pos.m_123341_() + vec3.x(), (double) pos.m_123342_() + vec3.y(), (double) pos.m_123343_() + vec3.z(), 0.0, 0.1F, 0.0));
    }

    static {
        EnumMap<AttachFace, Int2ObjectMap<List<Vec3>>> temp = new EnumMap(AttachFace.class);
        Int2ObjectMap<List<Vec3>> int2ObjectMap = new Int2ObjectArrayMap();
        int2ObjectMap.put(1, List.of(new Vec3(0.5, 0.6875, 0.5)));
        int2ObjectMap.put(2, List.of(new Vec3(0.3125, 0.875, 0.5), new Vec3(0.6875, 0.875, 0.5)));
        int2ObjectMap.put(3, List.of(new Vec3(0.1875, 0.9375, 0.5), new Vec3(0.5, 0.9375, 0.5), new Vec3(0.8125, 0.9375, 0.5)));
        int2ObjectMap.put(4, List.of(new Vec3(0.1875, 1.0, 0.5), new Vec3(0.8125, 1.0, 0.5), new Vec3(0.5, 0.9375, 0.25), new Vec3(0.5, 0.9375, 0.75)));
        temp.put(AttachFace.FLOOR, Int2ObjectMaps.unmodifiable(int2ObjectMap));
        int2ObjectMap = new Int2ObjectArrayMap();
        int2ObjectMap.put(1, List.of(new Vec3(0.5, 0.9375, 0.1875)));
        int2ObjectMap.put(2, List.of(new Vec3(0.3125, 0.9375, 0.1875), new Vec3(0.6875, 0.9375, 0.1875)));
        int2ObjectMap.put(3, List.of(new Vec3(0.8125, 0.9375, 0.1875), new Vec3(0.1875, 0.9375, 0.1875), new Vec3(0.5, 0.9375, 0.25)));
        int2ObjectMap.put(4, List.of(new Vec3(0.1875, 1.0, 0.1875), new Vec3(0.8125, 1.0, 0.1875), new Vec3(0.3125, 0.875, 0.3125), new Vec3(0.6875, 0.875, 0.3125)));
        temp.put(AttachFace.WALL, Int2ObjectMaps.unmodifiable(int2ObjectMap));
        int2ObjectMap = new Int2ObjectArrayMap();
        int2ObjectMap.put(1, List.of(new Vec3(0.5, 0.5625, 0.5)));
        int2ObjectMap.put(2, List.of(new Vec3(0.25, 0.875, 0.5), new Vec3(0.75, 0.875, 0.5)));
        int2ObjectMap.put(3, List.of(new Vec3(0.5, 0.875, 0.75), new Vec3(0.75, 0.875, 0.375), new Vec3(0.25, 0.875, 0.375)));
        int2ObjectMap.put(4, List.of(new Vec3(0.1875, 0.8125, 0.1875), new Vec3(0.8125, 0.8125, 0.1875), new Vec3(0.8125, 0.8125, 0.8125), new Vec3(0.1875, 0.8125, 0.8125)));
        temp.put(AttachFace.CEILING, Int2ObjectMaps.unmodifiable(int2ObjectMap));
        for (Direction direction : Direction.values()) {
            EnumMap<AttachFace, Int2ObjectMap<List<Vec3>>> newFaceMap = new EnumMap(AttachFace.class);
            for (Entry<AttachFace, Int2ObjectMap<List<Vec3>>> faceList : temp.entrySet()) {
                Int2ObjectMap<List<Vec3>> newCandleList = new Int2ObjectArrayMap();
                newCandleList.defaultReturnValue(List.of());
                int c = 1;
                Int2ObjectMap<List<Vec3>> oldVec = (Int2ObjectMap<List<Vec3>>) faceList.getValue();
                for (int i = 1; i < 5; i++) {
                    ArrayList<Vec3> vectorsList = new ArrayList();
                    for (Vec3 vec : (List) oldVec.get(i)) {
                        vectorsList.add(MthUtils.rotateVec3(vec.subtract(0.5, 0.5, 0.5), direction.getOpposite()).add(0.5, 0.5, 0.5));
                    }
                    newCandleList.put(c++, ImmutableList.copyOf(vectorsList));
                }
                newFaceMap.put((AttachFace) faceList.getKey(), Int2ObjectMaps.unmodifiable(newCandleList));
            }
            PARTICLE_OFFSETS.put(direction, newFaceMap);
        }
    }
}