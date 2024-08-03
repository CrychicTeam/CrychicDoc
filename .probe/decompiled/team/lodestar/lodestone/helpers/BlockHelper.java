package team.lodestar.lodestone.helpers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class BlockHelper {

    public static BlockState getBlockStateWithExistingProperties(BlockState oldState, BlockState newState) {
        BlockState finalState = newState;
        for (Property<?> property : oldState.m_61147_()) {
            if (newState.m_61138_(property)) {
                finalState = newStateWithOldProperty(oldState, finalState, property);
            }
        }
        return finalState;
    }

    public static BlockState setBlockStateWithExistingProperties(Level level, BlockPos pos, BlockState newState, int flags) {
        BlockState oldState = level.getBlockState(pos);
        BlockState finalState = getBlockStateWithExistingProperties(oldState, newState);
        level.sendBlockUpdated(pos, oldState, finalState, flags);
        level.setBlock(pos, finalState, flags);
        return finalState;
    }

    public static <T extends Comparable<T>> BlockState newStateWithOldProperty(BlockState oldState, BlockState newState, Property<T> property) {
        return (BlockState) newState.m_61124_(property, oldState.m_61143_(property));
    }

    public static CompoundTag saveBlockPos(CompoundTag compoundNBT, BlockPos pos) {
        compoundNBT.putInt("X", pos.m_123341_());
        compoundNBT.putInt("Y", pos.m_123342_());
        compoundNBT.putInt("Z", pos.m_123343_());
        return compoundNBT;
    }

    public static CompoundTag saveBlockPos(CompoundTag compoundNBT, BlockPos pos, String extra) {
        compoundNBT.putInt(extra + "_X", pos.m_123341_());
        compoundNBT.putInt(extra + "_Y", pos.m_123342_());
        compoundNBT.putInt(extra + "_Z", pos.m_123343_());
        return compoundNBT;
    }

    public static BlockPos loadBlockPos(CompoundTag tag) {
        return tag.contains("X") ? new BlockPos(tag.getInt("X"), tag.getInt("Y"), tag.getInt("Z")) : null;
    }

    public static BlockPos loadBlockPos(CompoundTag tag, String extra) {
        return tag.contains(extra + "_X") ? new BlockPos(tag.getInt(extra + "_X"), tag.getInt(extra + "_Y"), tag.getInt(extra + "_Z")) : null;
    }

    public static <T> Collection<T> getBlockEntities(Class<T> type, Level level, BlockPos pos, int range, Predicate<T> predicate) {
        return (Collection<T>) getBlockEntitiesStream(type, level, pos, range, predicate).collect(Collectors.toSet());
    }

    public static <T> Stream<T> getBlockEntitiesStream(Class<T> type, Level level, BlockPos pos, int range, Predicate<T> predicate) {
        return getBlockEntitiesStream(type, level, pos, range, range, range, predicate);
    }

    public static <T> Collection<T> getBlockEntities(Class<T> type, Level level, BlockPos pos, int x, int z, Predicate<T> predicate) {
        return (Collection<T>) getBlockEntitiesStream(type, level, pos, x, z, predicate).collect(Collectors.toSet());
    }

    public static <T> Stream<T> getBlockEntitiesStream(Class<T> type, Level level, BlockPos pos, int x, int z, Predicate<T> predicate) {
        return getBlockEntitiesStream(type, level, pos, x, z).filter(predicate);
    }

    public static <T> Collection<T> getBlockEntities(Class<T> type, Level level, BlockPos pos, int x, int y, int z, Predicate<T> predicate) {
        return (Collection<T>) getBlockEntitiesStream(type, level, pos, x, y, z, predicate).collect(Collectors.toSet());
    }

    public static <T> Stream<T> getBlockEntitiesStream(Class<T> type, Level level, BlockPos pos, int x, int y, int z, Predicate<T> predicate) {
        return getBlockEntitiesStream(type, level, pos, x, y, z).filter(predicate);
    }

    public static <T> Collection<T> getBlockEntities(Class<T> type, Level level, BlockPos pos, int range) {
        return getBlockEntities(type, level, pos, range, range, range);
    }

    public static <T> Stream<T> getBlockEntitiesStream(Class<T> type, Level level, BlockPos pos, int range) {
        return getBlockEntitiesStream(type, level, pos, range, range, range);
    }

    public static <T> Collection<T> getBlockEntities(Class<T> type, Level level, BlockPos pos, int x, int z) {
        return (Collection<T>) getBlockEntitiesStream(type, level, pos, x, z).collect(Collectors.toSet());
    }

    public static <T> Stream<T> getBlockEntitiesStream(Class<T> type, Level level, BlockPos pos, int x, int z) {
        return getBlockEntitiesStream(type, level, new AABB((double) pos.m_123341_() - (double) x, (double) pos.m_123342_(), (double) pos.m_123343_() - (double) z, (double) pos.m_123341_() + (double) x, (double) pos.m_123342_() + 1.0, (double) pos.m_123343_() + (double) z));
    }

    public static <T> Collection<T> getBlockEntities(Class<T> type, Level level, BlockPos pos, int x, int y, int z) {
        return (Collection<T>) getBlockEntitiesStream(type, level, pos, x, y, z).collect(Collectors.toSet());
    }

    public static <T> Stream<T> getBlockEntitiesStream(Class<T> type, Level level, BlockPos pos, int x, int y, int z) {
        return getBlockEntitiesStream(type, level, pos, -x, -y, -z, x, y, z);
    }

    public static <T> Collection<T> getBlockEntities(Class<T> type, Level level, BlockPos pos, int x1, int y1, int z1, int x2, int y2, int z2) {
        return (Collection<T>) getBlockEntitiesStream(type, level, pos, x1, y1, z1, x2, y2, z2).collect(Collectors.toSet());
    }

    public static <T> Stream<T> getBlockEntitiesStream(Class<T> type, Level level, BlockPos pos, int x1, int y1, int z1, int x2, int y2, int z2) {
        return getBlockEntitiesStream(type, level, new AABB((double) pos.m_123341_() + 1.5 + (double) x1, (double) pos.m_123342_() + 1.5 + (double) y1, (double) pos.m_123343_() + 1.5 + (double) z1, (double) pos.m_123341_() + 0.5 + (double) x2, (double) pos.m_123342_() + 0.5 + (double) y2, (double) pos.m_123343_() + 0.5 + (double) z2));
    }

    public static <T> Collection<T> getBlockEntities(Class<T> type, Level world, AABB bb) {
        return (Collection<T>) getBlockEntitiesStream(type, world, bb).collect(Collectors.toSet());
    }

    public static <T> Stream<T> getBlockEntitiesStream(Class<T> type, Level world, AABB bb) {
        return IntStream.iterate((int) Math.floor(bb.minX), i -> (double) i < Math.ceil(bb.maxX) + 16.0, i -> i + 16).boxed().flatMap(i -> IntStream.iterate((int) Math.floor(bb.minZ), j -> (double) j < Math.ceil(bb.maxZ) + 16.0, j -> j + 16).boxed().flatMap(j -> {
            ChunkAccess c = world.m_46865_(new BlockPos(i, 0, j));
            return c.getBlockEntitiesPos().stream();
        })).filter(p -> bb.contains((double) p.m_123341_() + 0.5, (double) p.m_123342_() + 0.5, (double) p.m_123343_() + 0.5)).map(world::m_7702_).filter(type::isInstance).map(type::cast);
    }

    public static Collection<BlockPos> getBlocks(BlockPos pos, int range, Predicate<BlockPos> predicate) {
        return (Collection<BlockPos>) getBlocksStream(pos, range, predicate).collect(Collectors.toSet());
    }

    public static Stream<BlockPos> getBlocksStream(BlockPos pos, int range, Predicate<BlockPos> predicate) {
        return getBlocksStream(pos, range, range, range, predicate);
    }

    public static Collection<BlockPos> getBlocks(BlockPos pos, int x, int y, int z, Predicate<BlockPos> predicate) {
        return (Collection<BlockPos>) getBlocksStream(pos, x, y, z, predicate).collect(Collectors.toSet());
    }

    public static Stream<BlockPos> getBlocksStream(BlockPos pos, int x, int y, int z, Predicate<BlockPos> predicate) {
        return getBlocksStream(pos, x, y, z).filter(predicate);
    }

    public static Collection<BlockPos> getBlocks(BlockPos pos, int x, int y, int z) {
        return (Collection<BlockPos>) getBlocksStream(pos, x, y, z).collect(Collectors.toSet());
    }

    public static Stream<BlockPos> getBlocksStream(BlockPos pos, int x, int y, int z) {
        return getBlocksStream(pos, -x, -y, -z, x, y, z);
    }

    public static Collection<BlockPos> getBlocks(BlockPos pos, int x1, int y1, int z1, int x2, int y2, int z2) {
        return (Collection<BlockPos>) getBlocksStream(pos, x1, y1, z1, x2, y2, z2).collect(Collectors.toSet());
    }

    public static Stream<BlockPos> getBlocksStream(BlockPos pos, int x1, int y1, int z1, int x2, int y2, int z2) {
        return IntStream.rangeClosed(x1, x2).boxed().flatMap(i -> IntStream.rangeClosed(y1, y2).boxed().flatMap(j -> IntStream.rangeClosed(z1, z2).boxed().map(k -> pos.offset(i, j, k))));
    }

    public static Collection<BlockPos> getPlaneOfBlocks(BlockPos pos, int range, Predicate<BlockPos> predicate) {
        return (Collection<BlockPos>) getPlaneOfBlocksStream(pos, range, predicate).collect(Collectors.toSet());
    }

    public static Stream<BlockPos> getPlaneOfBlocksStream(BlockPos pos, int range, Predicate<BlockPos> predicate) {
        return getPlaneOfBlocksStream(pos, range, range, predicate);
    }

    public static Collection<BlockPos> getPlaneOfBlocks(BlockPos pos, int x, int z, Predicate<BlockPos> predicate) {
        return (Collection<BlockPos>) getPlaneOfBlocksStream(pos, x, z, predicate).collect(Collectors.toSet());
    }

    public static Stream<BlockPos> getPlaneOfBlocksStream(BlockPos pos, int x, int z, Predicate<BlockPos> predicate) {
        return getPlaneOfBlocksStream(pos, x, z).filter(predicate);
    }

    public static Collection<BlockPos> getPlaneOfBlocks(BlockPos pos, int x, int z) {
        return (Collection<BlockPos>) getPlaneOfBlocksStream(pos, x, z).collect(Collectors.toSet());
    }

    public static Stream<BlockPos> getPlaneOfBlocksStream(BlockPos pos, int x, int z) {
        return getPlaneOfBlocksStream(pos, -x, -z, x, z);
    }

    public static Collection<BlockPos> getPlaneOfBlocks(BlockPos pos, int x1, int z1, int x2, int z2) {
        return (Collection<BlockPos>) getPlaneOfBlocksStream(pos, x1, z1, x2, z2).collect(Collectors.toSet());
    }

    public static Stream<BlockPos> getPlaneOfBlocksStream(BlockPos pos, int x1, int z1, int x2, int z2) {
        return IntStream.rangeClosed(x1, x2).boxed().flatMap(x -> IntStream.rangeClosed(z1, z2).boxed().map(z -> pos.offset(x, 0, z)));
    }

    public static Collection<BlockPos> getSphereOfBlocks(BlockPos pos, float range, Predicate<BlockPos> predicate) {
        return (Collection<BlockPos>) getSphereOfBlocksStream(pos, range, predicate).collect(Collectors.toSet());
    }

    public static Stream<BlockPos> getSphereOfBlocksStream(BlockPos pos, float range, Predicate<BlockPos> predicate) {
        return getSphereOfBlocksStream(pos, range, range).filter(predicate);
    }

    public static Collection<BlockPos> getSphereOfBlocks(BlockPos pos, float width, float height, Predicate<BlockPos> predicate) {
        return (Collection<BlockPos>) getSphereOfBlocksStream(pos, width, height, predicate).collect(Collectors.toSet());
    }

    public static Stream<BlockPos> getSphereOfBlocksStream(BlockPos pos, float width, float height, Predicate<BlockPos> predicate) {
        return getSphereOfBlocksStream(pos, width, height).filter(predicate);
    }

    public static Collection<BlockPos> getSphereOfBlocks(BlockPos pos, float range) {
        return (Collection<BlockPos>) getSphereOfBlocksStream(pos, range).collect(Collectors.toSet());
    }

    public static Stream<BlockPos> getSphereOfBlocksStream(BlockPos pos, float range) {
        return getSphereOfBlocksStream(pos, range, range);
    }

    public static Collection<BlockPos> getSphereOfBlocks(BlockPos pos, float width, float height) {
        return (Collection<BlockPos>) getSphereOfBlocksStream(pos, width, height).collect(Collectors.toSet());
    }

    public static Stream<BlockPos> getSphereOfBlocksStream(BlockPos pos, float width, float height) {
        return IntStream.rangeClosed((int) (-width), (int) width).boxed().flatMap(x -> IntStream.rangeClosed((int) (-height), (int) height).boxed().flatMap(y -> IntStream.rangeClosed((int) (-width), (int) width).boxed().filter(z -> {
            double d = Math.sqrt((double) (x * x + y * y + z * z));
            return d <= (double) width;
        }).map(z -> pos.offset(x, y, z))));
    }

    public static Collection<BlockPos> getNeighboringBlocks(BlockPos current) {
        return getBlocks(current, -1, -1, -1, 1, 1, 1);
    }

    public static Stream<BlockPos> getNeighboringBlocksStream(BlockPos current) {
        return getBlocksStream(current, -1, -1, -1, 1, 1, 1);
    }

    public static Collection<BlockPos> getPath(BlockPos start, BlockPos end, int speed, boolean inclusive, Level level) {
        Parrot parrot = new Parrot(EntityType.PARROT, level);
        parrot.m_6034_((double) start.m_123341_() + 0.5, (double) start.m_123342_() - 0.5, (double) start.m_123343_() + 0.5);
        parrot.m_21573_().moveTo((double) end.m_123341_() + 0.5, (double) end.m_123342_() - 0.5, (double) end.m_123343_() + 0.5, (double) speed);
        Path path = parrot.m_21573_().getPath();
        parrot.m_146870_();
        int nodes = path != null ? path.getNodeCount() : 0;
        ArrayList<BlockPos> positions = new ArrayList();
        for (int i = 0; i < nodes; i++) {
            Node node = path.getNode(i);
            positions.add(new BlockPos(node.x, (int) ((double) node.y - 0.5), node.z));
        }
        if (!inclusive) {
            positions.remove(0);
            positions.remove(positions.size() - 1);
        }
        return positions;
    }

    public static void updateState(Level level, BlockPos pos) {
        updateState(level.getBlockState(pos), level, pos);
    }

    public static void updateState(BlockState state, Level level, BlockPos pos) {
        level.sendBlockUpdated(pos, state, state, 2);
        level.blockEntityChanged(pos);
    }

    public static void updateAndNotifyState(Level level, BlockPos pos) {
        updateAndNotifyState(level.getBlockState(pos), level, pos);
    }

    public static void updateAndNotifyState(BlockState state, Level level, BlockPos pos) {
        updateState(state, level, pos);
        state.m_60701_(level, pos, 2);
        level.updateNeighbourForOutputSignal(pos, state.m_60734_());
    }

    public static Vec3 fromBlockPos(BlockPos pos) {
        return new Vec3((double) pos.m_123341_(), (double) pos.m_123342_(), (double) pos.m_123343_());
    }

    public static Vec3 withinBlock(Random rand, BlockPos pos) {
        double x = (double) pos.m_123341_() + rand.nextDouble();
        double y = (double) pos.m_123342_() + rand.nextDouble();
        double z = (double) pos.m_123343_() + rand.nextDouble();
        return new Vec3(x, y, z);
    }
}