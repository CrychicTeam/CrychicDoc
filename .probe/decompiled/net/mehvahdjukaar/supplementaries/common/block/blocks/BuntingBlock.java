package net.mehvahdjukaar.supplementaries.common.block.blocks;

import com.google.common.collect.Maps;
import com.google.common.collect.UnmodifiableIterator;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import net.mehvahdjukaar.moonlight.api.block.IRotatable;
import net.mehvahdjukaar.moonlight.api.block.ItemDisplayTile;
import net.mehvahdjukaar.supplementaries.common.block.ModBlockProperties;
import net.mehvahdjukaar.supplementaries.common.block.tiles.BuntingBlockTile;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public class BuntingBlock extends AbstractRopeBlock implements EntityBlock, IRotatable {

    public static final EnumProperty<ModBlockProperties.Bunting> NORTH = ModBlockProperties.NORTH_BUNTING;

    public static final EnumProperty<ModBlockProperties.Bunting> SOUTH = ModBlockProperties.SOUTH_BUNTING;

    public static final EnumProperty<ModBlockProperties.Bunting> WEST = ModBlockProperties.WEST_BUNTING;

    public static final EnumProperty<ModBlockProperties.Bunting> EAST = ModBlockProperties.EAST_BUNTING;

    public static final BooleanProperty UP = BlockStateProperties.UP;

    public static final BooleanProperty DOWN = BlockStateProperties.DOWN;

    public static final Map<Direction, EnumProperty<ModBlockProperties.Bunting>> HORIZONTAL_FACING_TO_PROPERTY_MAP = Util.make(Maps.newEnumMap(Direction.class), directions -> {
        directions.put(Direction.NORTH, NORTH);
        directions.put(Direction.EAST, EAST);
        directions.put(Direction.SOUTH, SOUTH);
        directions.put(Direction.WEST, WEST);
    });

    public final Map<BlockState, BlockState> buntingToRope = new Object2ObjectOpenHashMap();

    public BuntingBlock(BlockBehaviour.Properties properties) {
        super(properties);
        UnmodifiableIterator var2 = this.f_49792_.getPossibleStates().iterator();
        while (var2.hasNext()) {
            BlockState state = (BlockState) var2.next();
            BlockState state1 = state;
            for (Direction dir : Direction.Plane.HORIZONTAL) {
                state1 = this.setConnection(dir, state1, this.hasConnection(dir, state1));
            }
            this.buntingToRope.put(state, state1);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(NORTH, SOUTH, WEST, EAST, UP, DOWN);
    }

    @Override
    protected Map<BlockState, VoxelShape> makeShapes() {
        Map<BlockState, VoxelShape> shapes = new HashMap();
        VoxelShape down = Block.box(6.0, 0.0, 6.0, 10.0, 13.0, 10.0);
        VoxelShape up = Block.box(6.0, 9.0, 6.0, 10.0, 16.0, 10.0);
        VoxelShape north = Block.box(6.0, 9.0, 0.0, 10.0, 13.0, 10.0);
        VoxelShape south = Block.box(6.0, 9.0, 6.0, 10.0, 13.0, 16.0);
        VoxelShape west = Block.box(0.0, 9.0, 6.0, 10.0, 13.0, 10.0);
        VoxelShape east = Block.box(6.0, 9.0, 6.0, 16.0, 13.0, 10.0);
        VoxelShape knot = Block.box(6.0, 9.0, 6.0, 10.0, 13.0, 10.0);
        VoxelShape northBunting = Block.box(7.0, 0.0, 0.0, 9.0, 10.0, 8.0);
        VoxelShape southBunting = Block.box(7.0, 0.0, 8.0, 9.0, 10.0, 16.0);
        VoxelShape westBunting = Block.box(0.0, 0.0, 7.0, 8.0, 10.0, 9.0);
        VoxelShape eastBunting = Block.box(8.0, 0.0, 7.0, 16.0, 10.0, 9.0);
        UnmodifiableIterator var13 = this.f_49792_.getPossibleStates().iterator();
        while (var13.hasNext()) {
            BlockState state = (BlockState) var13.next();
            if (!(Boolean) state.m_61143_(WATERLOGGED)) {
                VoxelShape v = Shapes.empty();
                if ((Boolean) state.m_61143_(KNOT)) {
                    v = Shapes.or(knot);
                }
                if ((Boolean) state.m_61143_(DOWN)) {
                    v = Shapes.or(v, down);
                }
                if ((Boolean) state.m_61143_(UP)) {
                    v = Shapes.or(v, up);
                }
                ModBlockProperties.Bunting n = (ModBlockProperties.Bunting) state.m_61143_(NORTH);
                if (n.isConnected()) {
                    v = Shapes.or(v, north);
                }
                if (n.hasBunting()) {
                    v = Shapes.or(v, northBunting);
                }
                ModBlockProperties.Bunting s = (ModBlockProperties.Bunting) state.m_61143_(SOUTH);
                if (s.isConnected()) {
                    v = Shapes.or(v, south);
                }
                if (s.hasBunting()) {
                    v = Shapes.or(v, southBunting);
                }
                ModBlockProperties.Bunting w = (ModBlockProperties.Bunting) state.m_61143_(WEST);
                if (w.isConnected()) {
                    v = Shapes.or(v, west);
                }
                if (w.hasBunting()) {
                    v = Shapes.or(v, westBunting);
                }
                ModBlockProperties.Bunting e = (ModBlockProperties.Bunting) state.m_61143_(EAST);
                if (e.isConnected()) {
                    v = Shapes.or(v, east);
                }
                if (e.hasBunting()) {
                    v = Shapes.or(v, eastBunting);
                }
                v = v.optimize();
                boolean flag = true;
                for (VoxelShape existing : shapes.values()) {
                    if (existing.equals(v)) {
                        shapes.put(state, existing);
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    shapes.put(state, v);
                }
            }
        }
        return new Object2ObjectOpenHashMap(shapes);
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.m_60734_() != newState.m_60734_()) {
            if (world.getBlockEntity(pos) instanceof ItemDisplayTile tile) {
                Containers.dropContents(world, pos, tile);
            }
            super.m_6810_(state, world, pos, newState, isMoving);
        }
    }

    @Override
    public boolean hasConnection(Direction dir, BlockState state) {
        if (dir == Direction.DOWN) {
            return (Boolean) state.m_61143_(DOWN);
        } else {
            return dir == Direction.UP ? (Boolean) state.m_61143_(UP) : ((ModBlockProperties.Bunting) state.m_61143_((Property) HORIZONTAL_FACING_TO_PROPERTY_MAP.get(dir))).isConnected();
        }
    }

    @Override
    public BlockState setConnection(Direction dir, BlockState state, boolean value) {
        if (dir == Direction.DOWN) {
            return (BlockState) state.m_61124_(DOWN, value);
        } else {
            return dir == Direction.UP ? (BlockState) state.m_61124_(UP, value) : (BlockState) state.m_61124_((Property) HORIZONTAL_FACING_TO_PROPERTY_MAP.get(dir), value ? ModBlockProperties.Bunting.ROPE : ModBlockProperties.Bunting.NONE);
        }
    }

    @Override
    public Optional<BlockState> getRotatedState(BlockState state, LevelAccessor world, BlockPos pos, Rotation rotation, Direction axis, @Nullable Vec3 hit) {
        return Optional.of(state);
    }

    @Override
    public Optional<Direction> rotateOverAxis(BlockState state, LevelAccessor level, BlockPos pos, Rotation rotation, Direction axis, @Nullable Vec3 hit) {
        if (axis.getAxis() == Direction.Axis.Y && level.m_7702_(pos) instanceof BuntingBlockTile tile) {
            Map<Direction, ItemStack> newMap = new HashMap();
            for (Direction dir : Direction.Plane.HORIZONTAL) {
                ItemStack stack = tile.m_8020_(dir.get2DDataValue());
                if (!stack.isEmpty()) {
                    Direction newDir = rotation.rotate(dir);
                    if (!canSupportBunting(state, newDir.get2DDataValue())) {
                        return Optional.empty();
                    }
                    newMap.put(newDir, stack);
                }
            }
            if (!newMap.isEmpty()) {
                tile.m_6211_();
                newMap.forEach((dirx, stackx) -> tile.m_6836_(dirx.get2DDataValue(), stackx));
                return Optional.of(axis);
            }
        }
        return Optional.empty();
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return super.getCollisionShape((BlockState) this.buntingToRope.get(state), worldIn, pos, context);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BuntingBlockTile(pos, state);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (level.getBlockEntity(pos) instanceof BuntingBlockTile tile && tile.isAccessibleBy(player)) {
            Optional<Direction> closest = findClosestConnection(state, pos, hit);
            if (closest.isPresent()) {
                return tile.interact(player, handIn, ((Direction) closest.get()).get2DDataValue());
            }
        }
        return InteractionResult.PASS;
    }

    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter world, BlockPos pos, Player player) {
        Optional<Direction> closest = findClosestConnection(state, pos, target);
        if (world.getBlockEntity(pos) instanceof BuntingBlockTile tile && closest.isPresent()) {
            ItemStack held = tile.m_8020_(((Direction) closest.get()).get2DDataValue());
            if (!held.isEmpty()) {
                return held.copy();
            }
        }
        return this.getCloneItemStack(world, pos, state);
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        return ((Item) ModRegistry.ROPE_ITEM.get()).getDefaultInstance();
    }

    private static Optional<Direction> findClosestConnection(BlockState state, BlockPos pos, HitResult hit) {
        Vector3f hitPos = hit.getLocation().subtract((double) pos.m_123341_() + 0.5, 0.0, (double) pos.m_123343_() + 0.5).toVector3f();
        List<Direction> availableDir = Direction.Plane.HORIZONTAL.stream().filter(dir -> ((AbstractRopeBlock) state.m_60734_()).hasConnection(dir, state)).toList();
        return availableDir.stream().min((a, b) -> {
            Vector3f v1 = a.step();
            Vector3f v2 = b.step();
            float d1 = v1.distanceSquared(hitPos);
            float d2 = v2.distanceSquared(hitPos);
            return Float.compare(d1, d2);
        });
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor level, BlockPos pos, BlockPos facingPos) {
        BlockState newState = super.updateShape(stateIn, facing, facingState, level, pos, facingPos);
        if (facing.getAxis().isHorizontal() && this.hasConnection(facing, stateIn) && level.m_7702_(pos) instanceof BuntingBlockTile tile) {
            int index = facing.get2DDataValue();
            ItemStack item = tile.m_8020_(index);
            if (!item.isEmpty()) {
                if (!canSupportBunting(newState, index)) {
                    if (level instanceof Level l) {
                        this.popItem(l, pos, item, facing);
                    }
                    tile.m_6836_(index, ItemStack.EMPTY);
                    newState = (BlockState) newState.m_61124_((Property) HORIZONTAL_FACING_TO_PROPERTY_MAP.get(facing), ModBlockProperties.Bunting.NONE);
                } else {
                    newState = (BlockState) newState.m_61124_((Property) HORIZONTAL_FACING_TO_PROPERTY_MAP.get(facing), ModBlockProperties.Bunting.BUNTING);
                }
            }
            if (tile.m_7983_()) {
                newState = toRope(newState);
            }
        }
        return newState;
    }

    public void popItem(Level level, BlockPos pos, ItemStack stack, Direction dir) {
        if (!level.isClientSide && !stack.isEmpty() && level.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS)) {
            double h = (double) EntityType.ITEM.getHeight() / 2.0 + 0.25;
            Vector3f step = dir.step().mul(0.25F);
            double x = (double) (step.x + (float) pos.m_123341_()) + 0.5;
            double y = (double) (step.y + (float) pos.m_123342_()) + 0.5 - h;
            double z = (double) (step.z + (float) pos.m_123343_()) + 0.5;
            ItemEntity itemEntity = new ItemEntity(level, x, y, z, stack.copy());
            itemEntity.setDefaultPickUpDelay();
            level.m_7967_(itemEntity);
        }
    }

    public static boolean canSupportBunting(BlockState state, int index) {
        Direction dir = Direction.from2DDataValue(index);
        return ((ModBlockProperties.Bunting) state.m_61143_((Property) HORIZONTAL_FACING_TO_PROPERTY_MAP.get(dir))).isConnected();
    }

    @Nullable
    public static BlockState fromRope(BlockState state, BlockHitResult hit) {
        BlockState s = fromRope(state);
        Optional<Direction> closest = findClosestConnection(state, hit.getBlockPos(), hit);
        return (BlockState) closest.map(direction -> (BlockState) s.m_61124_((Property) HORIZONTAL_FACING_TO_PROPERTY_MAP.get(direction), ModBlockProperties.Bunting.BUNTING)).orElse(null);
    }

    public static BlockState fromRope(BlockState state) {
        BuntingBlock block = null;
        BlockState s = block.m_152465_(state);
        for (Direction dir : Direction.Plane.HORIZONTAL) {
            s = block.setConnection(dir, s, ((RopeBlock) state.m_60734_()).hasConnection(dir, state));
        }
        return s;
    }

    public static BlockState toRope(BlockState state) {
        RopeBlock block = (RopeBlock) ModRegistry.ROPE.get();
        BlockState s = block.m_152465_(state);
        for (Direction dir : Direction.Plane.HORIZONTAL) {
            s = block.setConnection(dir, s, ((BuntingBlock) state.m_60734_()).hasConnection(dir, state));
        }
        return s;
    }
}