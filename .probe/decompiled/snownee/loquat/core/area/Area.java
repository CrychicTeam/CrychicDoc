package snownee.loquat.core.area;

import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.longs.LongCollection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public abstract class Area {

    private final Set<String> tags = new LinkedHashSet(4);

    private final Map<String, Zone> zones = new HashMap(2);

    @Nullable
    private UUID uuid;

    @Nullable
    private CompoundTag attachedData;

    public abstract boolean contains(int var1, int var2, int var3);

    public abstract boolean contains(double var1, double var3, double var5);

    public final boolean contains(Vec3 pos) {
        return this.contains(pos.x, pos.y, pos.z);
    }

    public final boolean contains(Vec3i pos) {
        return this.contains(pos.getX(), pos.getY(), pos.getZ());
    }

    public abstract boolean intersects(AABB var1);

    public abstract boolean inside(AABB var1);

    public abstract boolean contains(AABB var1);

    public abstract Vec3 getCenter();

    public abstract Vec3 getOrigin();

    public abstract Stream<BlockPos> allBlockPosIn();

    public abstract Area.Type<?> getType();

    public void getRandomPos(RandomSource random, String zoneId, BlockPos.MutableBlockPos pos) {
        Zone zone = (Zone) this.zones.get(zoneId);
        Preconditions.checkNotNull(zone, "Zone %s not found", zoneId);
        double sum = zone.aabbs().stream().mapToDouble(AABB::m_82309_).sum();
        double r = random.nextDouble() * sum;
        for (AABB aabb : zone.aabbs()) {
            if (r < aabb.getSize()) {
                double x = aabb.minX + random.nextDouble() * (aabb.maxX - aabb.minX);
                double y = aabb.minY + random.nextDouble() * (aabb.maxY - aabb.minY);
                double z = aabb.minZ + random.nextDouble() * (aabb.maxZ - aabb.minZ);
                pos.set(x, y, z);
                return;
            }
            r -= aabb.getSize();
        }
        throw new IllegalStateException();
    }

    public abstract Area transform(StructurePlaceSettings var1, BlockPos var2);

    public abstract Object getBounds();

    public abstract LongCollection getChunksIn();

    public abstract AABB getRoughAABB();

    public abstract double distanceToSqr(Vec3 var1);

    public Optional<BlockPos.MutableBlockPos> findSpawnPos(ServerLevel world, String zoneId, Entity entity) {
        int attempts = 10;
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        List<LivingEntity> nearbyEntities = world.m_6443_(LivingEntity.class, this.getRoughAABB(), EntitySelector.NO_SPECTATORS);
        int bestAttemptX = 0;
        int bestAttemptY = 0;
        int bestAttemptZ = 0;
        double bestAttemptScore = Double.NEGATIVE_INFINITY;
        SpawnPlacements.Type placementType = SpawnPlacements.getPlacementType(entity.getType());
        for (int i = 0; i < attempts; i++) {
            this.getRandomPos(world.f_46441_, zoneId, pos);
            if (NaturalSpawner.isSpawnPositionOk(placementType, world, pos, entity.getType())) {
                entity.moveTo(pos, entity.getYRot(), entity.getXRot());
                if (world.m_45786_(entity)) {
                    double score = 0.0;
                    for (LivingEntity nearbyEntity : nearbyEntities) {
                        double distance = entity.distanceToSqr(nearbyEntity);
                        if (nearbyEntity instanceof Player) {
                            if (distance < 100.0) {
                                score -= 100.0 - distance;
                            }
                        } else if (distance < 9.0) {
                            score -= 9.0 - distance;
                        }
                    }
                    if (score > bestAttemptScore) {
                        bestAttemptScore = score;
                        bestAttemptX = pos.m_123341_();
                        bestAttemptY = pos.m_123342_();
                        bestAttemptZ = pos.m_123343_();
                        if (score == 0.0) {
                            break;
                        }
                    }
                }
            }
        }
        return bestAttemptScore == Double.NEGATIVE_INFINITY ? Optional.empty() : Optional.of(pos.set(bestAttemptX, bestAttemptY, bestAttemptZ));
    }

    public CompoundTag getOrCreateAttachedData() {
        if (this.attachedData == null) {
            this.attachedData = new CompoundTag();
        }
        return this.attachedData;
    }

    public abstract Optional<VoxelShape> getVoxelShape();

    public Set<String> getTags() {
        return this.tags;
    }

    public Map<String, Zone> getZones() {
        return this.zones;
    }

    @Nullable
    public UUID getUuid() {
        return this.uuid;
    }

    public void setUuid(@Nullable UUID uuid) {
        this.uuid = uuid;
    }

    @Nullable
    public CompoundTag getAttachedData() {
        return this.attachedData;
    }

    public void setAttachedData(@Nullable CompoundTag attachedData) {
        this.attachedData = attachedData;
    }

    public abstract static class Type<T extends Area> {

        public abstract T deserialize(CompoundTag var1);

        public abstract CompoundTag serialize(CompoundTag var1, T var2);
    }
}