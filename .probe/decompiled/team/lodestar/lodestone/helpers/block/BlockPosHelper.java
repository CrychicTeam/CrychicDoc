package team.lodestar.lodestone.helpers.block;

import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;

public class BlockPosHelper {

    public static Vec3 fromBlockPos(BlockPos pos) {
        return new Vec3((double) pos.m_123341_(), (double) pos.m_123342_(), (double) pos.m_123343_());
    }

    public static Vec3 withinBlock(RandomSource rand, BlockPos pos) {
        double x = (double) pos.m_123341_() + rand.nextDouble();
        double y = (double) pos.m_123342_() + rand.nextDouble();
        double z = (double) pos.m_123343_() + rand.nextDouble();
        return new Vec3(x, y, z);
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
}