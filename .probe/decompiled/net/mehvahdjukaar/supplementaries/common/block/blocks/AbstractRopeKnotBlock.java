package net.mehvahdjukaar.supplementaries.common.block.blocks;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.UnmodifiableIterator;
import com.mojang.datafixers.util.Pair;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import net.mehvahdjukaar.moonlight.api.block.IRotatable;
import net.mehvahdjukaar.moonlight.api.block.MimicBlock;
import net.mehvahdjukaar.moonlight.api.util.math.MthUtils;
import net.mehvahdjukaar.supplementaries.Supplementaries;
import net.mehvahdjukaar.supplementaries.common.block.ModBlockProperties;
import net.mehvahdjukaar.supplementaries.common.block.tiles.RopeKnotBlockTile;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractRopeKnotBlock extends MimicBlock implements SimpleWaterloggedBlock, EntityBlock, IRotatable {

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.AXIS;

    public static final EnumProperty<ModBlockProperties.PostType> POST_TYPE = ModBlockProperties.POST_TYPE;

    public static final BooleanProperty DOWN = BlockStateProperties.DOWN;

    public static final BooleanProperty UP = BlockStateProperties.UP;

    public static final BooleanProperty NORTH = BlockStateProperties.NORTH;

    public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;

    public static final BooleanProperty WEST = BlockStateProperties.WEST;

    public static final BooleanProperty EAST = BlockStateProperties.EAST;

    private final Map<BlockState, VoxelShape> shapeMap;

    protected AbstractRopeKnotBlock(BlockBehaviour.Properties properties) {
        super(properties);
        Pair<Map<BlockState, VoxelShape>, Map<BlockState, VoxelShape>> s = this.makeShapes();
        this.shapeMap = (Map<BlockState, VoxelShape>) s.getFirst();
        this.m_49959_((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(AXIS, Direction.Axis.Y)).m_61124_(WATERLOGGED, false)).m_61124_(POST_TYPE, ModBlockProperties.PostType.POST)).m_61124_(NORTH, false)).m_61124_(SOUTH, false)).m_61124_(WEST, false)).m_61124_(EAST, false)).m_61124_(UP, false)).m_61124_(DOWN, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED, POST_TYPE, AXIS, NORTH, SOUTH, WEST, EAST, UP, DOWN);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new RopeKnotBlockTile(pPos, pState);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        if (world.getBlockEntity(pos) instanceof RopeKnotBlockTile tile) {
            try {
                return tile.getShape();
            } catch (Exception var7) {
                Supplementaries.LOGGER.error("Failed to get block shape for rope knot block at {}", pos);
            }
        }
        return super.m_5940_(state, world, pos, context);
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState state, BlockGetter reader, BlockPos pos) {
        return (VoxelShape) this.shapeMap.getOrDefault(state.m_61124_(WATERLOGGED, false), Shapes.block());
    }

    @Override
    public VoxelShape getBlockSupportShape(BlockState state, BlockGetter reader, BlockPos pos) {
        return (VoxelShape) this.shapeMap.getOrDefault(state.m_61124_(WATERLOGGED, false), Shapes.block());
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        if (world.getBlockEntity(pos) instanceof RopeKnotBlockTile tile) {
            try {
                return tile.getCollisionShape();
            } catch (Exception var7) {
                Supplementaries.LOGGER.error("Failed to get collision shape for rope knot block at {}", pos);
            }
        }
        return super.m_5939_(state, world, pos, context);
    }

    protected Pair<Map<BlockState, VoxelShape>, Map<BlockState, VoxelShape>> makeShapes() {
        Map<BlockState, VoxelShape> shapesBuilder = new HashMap();
        Map<BlockState, VoxelShape> collisionBuilder = new HashMap();
        VoxelShape down = Block.box(6.0, 0.0, 6.0, 10.0, 13.0, 10.0);
        VoxelShape up = Block.box(6.0, 9.0, 6.0, 10.0, 16.0, 10.0);
        VoxelShape north = this.getSideShape();
        VoxelShape south = MthUtils.rotateVoxelShape(north, Direction.SOUTH);
        VoxelShape west = MthUtils.rotateVoxelShape(north, Direction.WEST);
        VoxelShape east = MthUtils.rotateVoxelShape(north, Direction.EAST);
        UnmodifiableIterator var9 = this.f_49792_.getPossibleStates().iterator();
        while (var9.hasNext()) {
            BlockState state = (BlockState) var9.next();
            if (!(Boolean) state.m_61143_(WATERLOGGED)) {
                int w = ((ModBlockProperties.PostType) state.m_61143_(POST_TYPE)).getWidth();
                double o = (double) (16 - w) / 2.0;
                VoxelShape v;
                VoxelShape c;
                switch((Direction.Axis) state.m_61143_(AXIS)) {
                    case X:
                        v = Block.box(0.0, o, o, 16.0, o + (double) w, o + (double) w);
                        c = v;
                        break;
                    case Z:
                        v = Block.box(o, o, 0.0, o + (double) w, o + (double) w, 16.0);
                        c = v;
                        break;
                    default:
                        v = Block.box(o, 0.0, o, o + (double) w, 16.0, o + (double) w);
                        c = Block.box(o, 0.0, o, o + (double) w, 24.0, o + (double) w);
                }
                if ((Boolean) state.m_61143_(DOWN)) {
                    v = Shapes.or(v, down);
                }
                if ((Boolean) state.m_61143_(UP)) {
                    v = Shapes.or(v, up);
                }
                if ((Boolean) state.m_61143_(NORTH)) {
                    v = Shapes.or(v, north);
                }
                if ((Boolean) state.m_61143_(SOUTH)) {
                    v = Shapes.or(v, south);
                }
                if ((Boolean) state.m_61143_(WEST)) {
                    v = Shapes.or(v, west);
                }
                if ((Boolean) state.m_61143_(EAST)) {
                    v = Shapes.or(v, east);
                }
                c = Shapes.or(c, v);
                c = c.optimize();
                v = v.optimize();
                boolean flag = true;
                for (VoxelShape existing : shapesBuilder.values()) {
                    if (existing.equals(v)) {
                        shapesBuilder.put(state, existing);
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    shapesBuilder.put(state, v);
                }
                boolean flag2 = true;
                for (VoxelShape existingx : collisionBuilder.values()) {
                    if (existingx.equals(c)) {
                        collisionBuilder.put(state, existingx);
                        flag2 = false;
                        break;
                    }
                }
                if (flag2) {
                    collisionBuilder.put(state, c);
                }
            }
        }
        return Pair.of(ImmutableMap.copyOf(shapesBuilder), ImmutableMap.copyOf(collisionBuilder));
    }

    public abstract VoxelShape getSideShape();

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        state = switch(rotation) {
            case CLOCKWISE_180 ->
                (BlockState) ((BlockState) ((BlockState) ((BlockState) state.m_61124_(NORTH, (Boolean) state.m_61143_(SOUTH))).m_61124_(EAST, (Boolean) state.m_61143_(WEST))).m_61124_(SOUTH, (Boolean) state.m_61143_(NORTH))).m_61124_(WEST, (Boolean) state.m_61143_(EAST));
            case COUNTERCLOCKWISE_90 ->
                (BlockState) ((BlockState) ((BlockState) ((BlockState) state.m_61124_(NORTH, (Boolean) state.m_61143_(EAST))).m_61124_(EAST, (Boolean) state.m_61143_(SOUTH))).m_61124_(SOUTH, (Boolean) state.m_61143_(WEST))).m_61124_(WEST, (Boolean) state.m_61143_(NORTH));
            case CLOCKWISE_90 ->
                (BlockState) ((BlockState) ((BlockState) ((BlockState) state.m_61124_(NORTH, (Boolean) state.m_61143_(WEST))).m_61124_(EAST, (Boolean) state.m_61143_(NORTH))).m_61124_(SOUTH, (Boolean) state.m_61143_(EAST))).m_61124_(WEST, (Boolean) state.m_61143_(SOUTH));
            default ->
                state;
        };
        if (rotation == Rotation.CLOCKWISE_180) {
            return state;
        } else {
            return switch((Direction.Axis) state.m_61143_(AXIS)) {
                case X ->
                    (BlockState) state.m_61124_(AXIS, Direction.Axis.Z);
                case Z ->
                    (BlockState) state.m_61124_(AXIS, Direction.Axis.X);
                default ->
                    state;
            };
        }
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return switch(mirror) {
            case LEFT_RIGHT ->
                (BlockState) ((BlockState) state.m_61124_(NORTH, (Boolean) state.m_61143_(SOUTH))).m_61124_(SOUTH, (Boolean) state.m_61143_(NORTH));
            case FRONT_BACK ->
                (BlockState) ((BlockState) state.m_61124_(EAST, (Boolean) state.m_61143_(WEST))).m_61124_(WEST, (Boolean) state.m_61143_(EAST));
            default ->
                super.m_6943_(state, mirror);
        };
    }

    @Override
    public Optional<BlockState> getRotatedState(BlockState state, LevelAccessor world, BlockPos pos, Rotation rotation, Direction axis, @Nullable Vec3 hit) {
        return axis.getAxis() == Direction.Axis.Y ? Optional.ofNullable(this.rotate(state, rotation)) : Optional.empty();
    }

    @Override
    public void onRotated(BlockState newState, BlockState oldState, LevelAccessor world, BlockPos pos, Rotation rotation, Direction axis, @Nullable Vec3 hit) {
        if (axis.getAxis() == Direction.Axis.Y && world.m_7702_(pos) instanceof RopeKnotBlockTile tile) {
            BlockState mimic = tile.getHeldBlock();
            BlockState newMimic = tile.getHeldBlock().m_60717_(rotation);
            if (newMimic != mimic) {
                tile.setHeldBlock(newMimic);
                tile.setChanged();
            }
        }
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.m_61143_(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.m_5888_(state);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidstate = context.m_43725_().getFluidState(context.getClickedPos());
        boolean flag = fluidstate.is(FluidTags.WATER) && fluidstate.getAmount() == 8;
        return (BlockState) this.m_49966_().m_61124_(WATERLOGGED, flag);
    }

    @Nullable
    public static BlockState convertToRopeKnot(ModBlockProperties.PostType type, BlockState state, Level world, BlockPos pos) {
        Direction.Axis axis = Direction.Axis.Y;
        if (state.m_61138_(BlockStateProperties.AXIS)) {
            axis = (Direction.Axis) state.m_61143_(BlockStateProperties.AXIS);
        }
        BlockState newState = (BlockState) ((BlockState) ((Block) ModRegistry.ROPE_KNOT.get()).defaultBlockState().m_61124_(AXIS, axis)).m_61124_(POST_TYPE, type);
        newState = Block.updateFromNeighbourShapes(newState, world, pos);
        if (!world.setBlock(pos, newState, 0)) {
            return null;
        } else {
            if (world.getBlockEntity(pos) instanceof RopeKnotBlockTile tile) {
                tile.setHeldBlock(state);
                tile.setChanged();
            }
            newState.m_60701_(world, pos, 6);
            return newState;
        }
    }
}