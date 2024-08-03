package me.jellysquid.mods.lithium.common.entity.nearby_tracker;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.Reference2LongOpenHashMap;
import java.util.List;
import me.jellysquid.mods.lithium.common.util.tuples.Range6Int;
import net.minecraft.core.SectionPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;

public class NearbyEntityTracker<T extends LivingEntity> implements NearbyEntityListener {

    private final Class<T> clazz;

    private final LivingEntity entity;

    private final Reference2LongOpenHashMap<T> nearbyEntities = new Reference2LongOpenHashMap(0);

    private long counter;

    private final Range6Int chunkBoxRadius;

    public NearbyEntityTracker(Class<T> clazz, LivingEntity entity, Vec3i boxRadius) {
        this.clazz = clazz;
        this.entity = entity;
        this.chunkBoxRadius = new Range6Int(1 + SectionPos.blockToSectionCoord(boxRadius.getX()), 1 + SectionPos.blockToSectionCoord(boxRadius.getY()), 1 + SectionPos.blockToSectionCoord(boxRadius.getZ()), 1 + SectionPos.blockToSectionCoord(boxRadius.getX()), 1 + SectionPos.blockToSectionCoord(boxRadius.getY()), 1 + SectionPos.blockToSectionCoord(boxRadius.getZ()));
    }

    @Override
    public Class<? extends Entity> getEntityClass() {
        return this.clazz;
    }

    @Override
    public Range6Int getChunkRange() {
        return this.chunkBoxRadius;
    }

    @Override
    public void onEntityEnteredRange(Entity entity) {
        if (this.clazz.isInstance(entity)) {
            this.nearbyEntities.put((LivingEntity) entity, this.counter++);
        }
    }

    @Override
    public void onEntityLeftRange(Entity entity) {
        if (!this.nearbyEntities.isEmpty() && this.clazz.isInstance(entity)) {
            this.nearbyEntities.removeLong(entity);
        }
    }

    public T getClosestEntity(AABB box, TargetingConditions targetPredicate, double x, double y, double z) {
        T nearest = null;
        double nearestDistance = Double.POSITIVE_INFINITY;
        ObjectIterator var12 = this.nearbyEntities.keySet().iterator();
        while (var12.hasNext()) {
            T entity = (T) var12.next();
            double distance;
            if ((box == null || box.intersects(entity.m_20191_())) && (distance = entity.m_20275_(x, y, z)) <= nearestDistance && targetPredicate.test(this.getEntity(), entity)) {
                if (distance == nearestDistance) {
                    nearest = this.getFirst(nearest, entity);
                } else {
                    nearest = entity;
                }
                nearestDistance = distance;
            }
        }
        return nearest;
    }

    private T getFirst(T entity1, T entity2) {
        if (this.getEntityClass() == Player.class) {
            List<? extends Player> players = this.getEntity().m_20193_().m_6907_();
            return players.indexOf((Player) entity1) < players.indexOf((Player) entity2) ? entity1 : entity2;
        } else {
            long pos1 = SectionPos.asLong(entity1.m_20183_());
            long pos2 = SectionPos.asLong(entity2.m_20183_());
            if (pos1 < pos2) {
                return entity1;
            } else if (pos2 < pos1) {
                return entity2;
            } else {
                return this.nearbyEntities.getLong(entity1) < this.nearbyEntities.getLong(entity2) ? entity1 : entity2;
            }
        }
    }

    public String toString() {
        return super.toString() + " for entity class: " + this.clazz.getName() + ", around entity: " + this.getEntity().toString() + " with NBT: " + this.getEntity().m_20240_(new CompoundTag());
    }

    LivingEntity getEntity() {
        return this.entity;
    }
}