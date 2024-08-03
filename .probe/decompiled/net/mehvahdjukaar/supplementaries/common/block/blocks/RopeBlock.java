package net.mehvahdjukaar.supplementaries.common.block.blocks;

import com.google.common.collect.Maps;
import com.google.common.collect.UnmodifiableIterator;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.Util;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class RopeBlock extends AbstractRopeBlock {

    public static final BooleanProperty NORTH = BlockStateProperties.NORTH;

    public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;

    public static final BooleanProperty WEST = BlockStateProperties.WEST;

    public static final BooleanProperty EAST = BlockStateProperties.EAST;

    public static final BooleanProperty UP = BlockStateProperties.UP;

    public static final BooleanProperty DOWN = BlockStateProperties.DOWN;

    public static final Map<Direction, BooleanProperty> FACING_TO_PROPERTY_MAP = Util.make(Maps.newEnumMap(Direction.class), directions -> {
        directions.put(Direction.NORTH, NORTH);
        directions.put(Direction.EAST, EAST);
        directions.put(Direction.SOUTH, SOUTH);
        directions.put(Direction.WEST, WEST);
        directions.put(Direction.UP, UP);
        directions.put(Direction.DOWN, DOWN);
    });

    public RopeBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) this.m_49966_().m_61124_(UP, true)).m_61124_(DOWN, true)).m_61124_(NORTH, false)).m_61124_(SOUTH, false)).m_61124_(EAST, false)).m_61124_(WEST, false));
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
        UnmodifiableIterator var9 = this.f_49792_.getPossibleStates().iterator();
        while (var9.hasNext()) {
            BlockState state = (BlockState) var9.next();
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
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(NORTH, SOUTH, EAST, WEST, UP, DOWN);
    }

    @Override
    public boolean hasConnection(Direction dir, BlockState state) {
        return (Boolean) state.m_61143_((Property) FACING_TO_PROPERTY_MAP.get(dir));
    }

    @Override
    public BlockState setConnection(Direction dir, BlockState state, boolean value) {
        return (BlockState) state.m_61124_((Property) FACING_TO_PROPERTY_MAP.get(dir), value);
    }
}