package team.lodestar.lodestone.helpers.block;

import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.phys.AABB;

public class BlockEntityHelper {

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
}