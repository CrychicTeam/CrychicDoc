package snownee.loquat.core.area;

import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongCollection;
import it.unimi.dsi.fastutil.longs.LongList;
import it.unimi.dsi.fastutil.longs.LongSet;
import java.util.Optional;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import snownee.loquat.AreaTypes;
import snownee.loquat.util.AABBSerializer;
import snownee.loquat.util.LoquatUtil;
import snownee.loquat.util.TransformUtil;

public class AABBArea extends Area {

    private final AABB aabb;

    public static AABBArea of(double x1, double y1, double z1, double x2, double y2, double z2) {
        return new AABBArea(new AABB(x1, y1, z1, x2, y2, z2));
    }

    public static AABBArea of(Vec3 pos1, Vec3 pos2) {
        return new AABBArea(new AABB(pos1, pos2));
    }

    @Override
    public boolean contains(int x, int y, int z) {
        return this.aabb.intersects((double) x, (double) y, (double) z, (double) (x + 1), (double) (y + 1), (double) (z + 1));
    }

    @Override
    public boolean contains(double x, double y, double z) {
        return this.aabb.contains(x, y, z);
    }

    @Override
    public boolean intersects(AABB aabb) {
        return this.aabb.intersects(aabb);
    }

    @Override
    public boolean inside(AABB aabb2) {
        return LoquatUtil.isAABBFullyInsideAABB(this.aabb, aabb2);
    }

    @Override
    public boolean contains(AABB aabb2) {
        return LoquatUtil.isAABBFullyInsideAABB(aabb2, this.aabb);
    }

    @Override
    public Vec3 getCenter() {
        return this.aabb.getCenter();
    }

    @Override
    public Vec3 getOrigin() {
        return new Vec3(this.aabb.minX, this.aabb.minY, this.aabb.minZ);
    }

    public AABBArea transform(StructurePlaceSettings settings, BlockPos offset) {
        return new AABBArea(TransformUtil.transform(settings, offset, this.aabb));
    }

    @Override
    public Stream<BlockPos> allBlockPosIn() {
        return BlockPos.betweenClosedStream(this.aabb.deflate(0.25));
    }

    @Override
    public Object getBounds() {
        return this.aabb;
    }

    @Override
    public AABB getRoughAABB() {
        return this.getAabb();
    }

    @Override
    public double distanceToSqr(Vec3 vec) {
        return this.aabb.contains(vec) ? 0.0 : (Double) this.aabb.clip(vec, this.getCenter()).map(vec::m_82557_).orElse(Double.MAX_VALUE);
    }

    @Override
    public Optional<VoxelShape> getVoxelShape() {
        return Optional.of(Shapes.create(this.aabb));
    }

    @Override
    public LongCollection getChunksIn() {
        int minX = SectionPos.blockToSectionCoord(this.aabb.minX);
        int minZ = SectionPos.blockToSectionCoord(this.aabb.minZ);
        int maxX = SectionPos.blockToSectionCoord(this.aabb.maxX);
        int maxZ = SectionPos.blockToSectionCoord(this.aabb.maxZ);
        if (minX == maxX && minZ == maxZ) {
            return LongSet.of(ChunkPos.asLong(minX, minZ));
        } else {
            LongList list = new LongArrayList((maxX - minX + 1) * (maxZ - minZ + 1));
            for (int x = minX; x <= maxX; x++) {
                for (int z = minZ; z <= maxZ; z++) {
                    list.add(ChunkPos.asLong(x, z));
                }
            }
            return list;
        }
    }

    @Override
    public Area.Type<?> getType() {
        return AreaTypes.BOX;
    }

    public AABBArea(AABB aabb) {
        this.aabb = aabb;
    }

    public AABB getAabb() {
        return this.aabb;
    }

    public static class Type extends Area.Type<AABBArea> {

        public AABBArea deserialize(CompoundTag data) {
            return new AABBArea(AABBSerializer.read(data.getList("AABB", 6)));
        }

        public CompoundTag serialize(CompoundTag data, AABBArea area) {
            data.put("AABB", AABBSerializer.write(area.aabb));
            return data;
        }
    }
}