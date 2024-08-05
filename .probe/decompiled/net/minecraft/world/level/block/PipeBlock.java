package net.minecraft.world.level.block;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class PipeBlock extends Block {

    private static final Direction[] DIRECTIONS = Direction.values();

    public static final BooleanProperty NORTH = BlockStateProperties.NORTH;

    public static final BooleanProperty EAST = BlockStateProperties.EAST;

    public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;

    public static final BooleanProperty WEST = BlockStateProperties.WEST;

    public static final BooleanProperty UP = BlockStateProperties.UP;

    public static final BooleanProperty DOWN = BlockStateProperties.DOWN;

    public static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION = ImmutableMap.copyOf(Util.make(Maps.newEnumMap(Direction.class), p_55164_ -> {
        p_55164_.put(Direction.NORTH, NORTH);
        p_55164_.put(Direction.EAST, EAST);
        p_55164_.put(Direction.SOUTH, SOUTH);
        p_55164_.put(Direction.WEST, WEST);
        p_55164_.put(Direction.UP, UP);
        p_55164_.put(Direction.DOWN, DOWN);
    }));

    protected final VoxelShape[] shapeByIndex;

    protected PipeBlock(float float0, BlockBehaviour.Properties blockBehaviourProperties1) {
        super(blockBehaviourProperties1);
        this.shapeByIndex = this.makeShapes(float0);
    }

    private VoxelShape[] makeShapes(float float0) {
        float $$1 = 0.5F - float0;
        float $$2 = 0.5F + float0;
        VoxelShape $$3 = Block.box((double) ($$1 * 16.0F), (double) ($$1 * 16.0F), (double) ($$1 * 16.0F), (double) ($$2 * 16.0F), (double) ($$2 * 16.0F), (double) ($$2 * 16.0F));
        VoxelShape[] $$4 = new VoxelShape[DIRECTIONS.length];
        for (int $$5 = 0; $$5 < DIRECTIONS.length; $$5++) {
            Direction $$6 = DIRECTIONS[$$5];
            $$4[$$5] = Shapes.box(0.5 + Math.min((double) (-float0), (double) $$6.getStepX() * 0.5), 0.5 + Math.min((double) (-float0), (double) $$6.getStepY() * 0.5), 0.5 + Math.min((double) (-float0), (double) $$6.getStepZ() * 0.5), 0.5 + Math.max((double) float0, (double) $$6.getStepX() * 0.5), 0.5 + Math.max((double) float0, (double) $$6.getStepY() * 0.5), 0.5 + Math.max((double) float0, (double) $$6.getStepZ() * 0.5));
        }
        VoxelShape[] $$7 = new VoxelShape[64];
        for (int $$8 = 0; $$8 < 64; $$8++) {
            VoxelShape $$9 = $$3;
            for (int $$10 = 0; $$10 < DIRECTIONS.length; $$10++) {
                if (($$8 & 1 << $$10) != 0) {
                    $$9 = Shapes.or($$9, $$4[$$10]);
                }
            }
            $$7[$$8] = $$9;
        }
        return $$7;
    }

    @Override
    public boolean propagatesSkylightDown(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2) {
        return false;
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        return this.shapeByIndex[this.getAABBIndex(blockState0)];
    }

    protected int getAABBIndex(BlockState blockState0) {
        int $$1 = 0;
        for (int $$2 = 0; $$2 < DIRECTIONS.length; $$2++) {
            if ((Boolean) blockState0.m_61143_((Property) PROPERTY_BY_DIRECTION.get(DIRECTIONS[$$2]))) {
                $$1 |= 1 << $$2;
            }
        }
        return $$1;
    }
}