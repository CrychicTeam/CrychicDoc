package net.mehvahdjukaar.amendments.common.block;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import net.mehvahdjukaar.amendments.common.tile.CandleSkullBlockTile;
import net.mehvahdjukaar.moonlight.api.block.IRecolorable;
import net.mehvahdjukaar.moonlight.api.set.BlocksColorAPI;
import net.mehvahdjukaar.moonlight.api.util.math.MthUtils;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class WallCandleSkullBlock extends AbstractCandleSkullBlock implements IRecolorable {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    private static final Map<Direction, VoxelShape[]> SHAPES = Util.make(() -> {
        Map<Direction, VoxelShape[]> m = new HashMap();
        for (Direction dir : Direction.Plane.HORIZONTAL) {
            m.put(dir, new VoxelShape[] { MthUtils.rotateVoxelShape(ONE_AABB.move(0.0, 0.0, 0.25), dir), MthUtils.rotateVoxelShape(TWO_AABB.move(0.0, 0.0, 0.25), dir), MthUtils.rotateVoxelShape(THREE_AABB.move(0.0, 0.0, 0.25), dir), MthUtils.rotateVoxelShape(FOUR_AABB.move(0.0, 0.0, 0.25), dir) });
        }
        return m;
    });

    protected static final Map<Direction, Int2ObjectMap<List<Vec3>>> H_PARTICLE_OFFSETS = Util.make(() -> {
        Map<Direction, Int2ObjectMap<List<Vec3>>> temp = new Object2ObjectOpenHashMap(4);
        for (Direction dir : Direction.Plane.HORIZONTAL) {
            temp.put(dir, new Int2ObjectArrayMap(4));
            PARTICLE_OFFSETS.forEach((key, value) -> {
                List<Vec3> transformedList = new ArrayList();
                for (Vec3 v : value) {
                    transformedList.add(MthUtils.rotateVec3(new Vec3(v.x - 0.5, v.y, v.z + 0.25 - 0.5), dir).add(0.5, 0.0, 0.5));
                }
                ((Int2ObjectMap) temp.get(dir)).put(key, transformedList);
            });
        }
        return temp;
    });

    public WallCandleSkullBlock(BlockBehaviour.Properties properties) {
        this(properties, () -> ParticleTypes.SMALL_FLAME);
    }

    public WallCandleSkullBlock(BlockBehaviour.Properties properties, Supplier<ParticleType<? extends ParticleOptions>> particle) {
        super(properties, particle);
        this.m_49959_((BlockState) this.m_49966_().m_61124_(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(FACING);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return (BlockState) state.m_61124_(FACING, rotation.rotate((Direction) state.m_61143_(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.m_60717_(mirror.getRotation((Direction) state.m_61143_(FACING)));
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return ((VoxelShape[]) SHAPES.get(pState.m_61143_(FACING)))[pState.m_61143_(CANDLES) - 1];
    }

    @Override
    protected Iterable<Vec3> getParticleOffsets(BlockState pState) {
        return (Iterable<Vec3>) ((Int2ObjectMap) H_PARTICLE_OFFSETS.get(pState.m_61143_(FACING))).get(pState.m_61143_(CANDLES));
    }

    @Override
    public boolean tryRecolor(Level level, BlockPos blockPos, BlockState blockState, @Nullable DyeColor dyeColor) {
        if (level.getBlockEntity(blockPos) instanceof CandleSkullBlockTile tile) {
            BlockState c = tile.getCandle();
            if (!c.m_60795_()) {
                Block otherCandle = BlocksColorAPI.changeColor(c.m_60734_(), dyeColor);
                if (otherCandle != null && !c.m_60713_(otherCandle)) {
                    tile.setCandle(otherCandle.withPropertiesOf(c));
                    tile.m_6596_();
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean isDefaultColor(Level level, BlockPos blockPos, BlockState blockState) {
        if (level.getBlockEntity(blockPos) instanceof CandleSkullBlockTile tile) {
            BlockState c = tile.getCandle();
            return BlocksColorAPI.isDefaultColor(c.m_60734_());
        } else {
            return false;
        }
    }
}