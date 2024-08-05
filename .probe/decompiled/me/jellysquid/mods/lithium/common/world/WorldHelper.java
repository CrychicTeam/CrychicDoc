package me.jellysquid.mods.lithium.common.world;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import me.jellysquid.mods.lithium.common.client.ClientWorldAccessor;
import me.jellysquid.mods.lithium.common.entity.EntityClassGroup;
import me.jellysquid.mods.lithium.common.entity.pushable.EntityPushablePredicate;
import me.jellysquid.mods.lithium.common.world.chunk.ClassGroupFilterableList;
import me.jellysquid.mods.lithium.mixin.chunk.entity_class_groups.ClientEntityManagerAccessor;
import me.jellysquid.mods.lithium.mixin.chunk.entity_class_groups.EntityTrackingSectionAccessor;
import me.jellysquid.mods.lithium.mixin.chunk.entity_class_groups.ServerEntityManagerAccessor;
import me.jellysquid.mods.lithium.mixin.chunk.entity_class_groups.ServerWorldAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.util.AbortableIterationConsumer;
import net.minecraft.util.ClassInstanceMultiMap;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.EntityGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntitySectionStorage;
import net.minecraft.world.phys.AABB;

public class WorldHelper {

    public static final boolean CUSTOM_TYPE_FILTERABLE_LIST_DISABLED = !ClassGroupFilterableList.class.isAssignableFrom(ClassInstanceMultiMap.class);

    public static List<Entity> getEntitiesForCollision(EntityGetter entityView, AABB box, Entity collidingEntity) {
        if (!CUSTOM_TYPE_FILTERABLE_LIST_DISABLED && entityView instanceof Level world && (collidingEntity == null || !EntityClassGroup.MINECART_BOAT_LIKE_COLLISION.contains(collidingEntity.getClass()))) {
            EntitySectionStorage<Entity> cache = getEntityCacheOrNull(world);
            if (cache != null) {
                world.getProfiler().incrementCounter("getEntities");
                return getEntitiesOfClassGroup(cache, collidingEntity, EntityClassGroup.NoDragonClassGroup.BOAT_SHULKER_LIKE_COLLISION, box);
            }
        }
        return entityView.getEntities(collidingEntity, box);
    }

    public static EntitySectionStorage<Entity> getEntityCacheOrNull(Level world) {
        if (world instanceof ClientWorldAccessor) {
            return ((ClientEntityManagerAccessor) ((ClientWorldAccessor) world).getEntityManager()).getCache();
        } else {
            return world instanceof ServerWorldAccessor ? ((ServerEntityManagerAccessor) ((ServerWorldAccessor) world).getEntityManager()).getCache() : null;
        }
    }

    public static List<Entity> getEntitiesOfClassGroup(EntitySectionStorage<Entity> cache, Entity collidingEntity, EntityClassGroup.NoDragonClassGroup entityClassGroup, AABB box) {
        ArrayList<Entity> entities = new ArrayList();
        cache.forEachAccessibleNonEmptySection(box, section -> {
            ClassInstanceMultiMap<Entity> allEntities = ((EntityTrackingSectionAccessor) section).getCollection();
            Collection<Entity> entitiesOfType = ((ClassGroupFilterableList) allEntities).getAllOfGroupType(entityClassGroup);
            if (!entitiesOfType.isEmpty()) {
                for (Entity entity : entitiesOfType) {
                    if (entity.getBoundingBox().intersects(box) && !entity.isSpectator() && entity != collidingEntity) {
                        entities.add(entity);
                    }
                }
            }
            return AbortableIterationConsumer.Continuation.CONTINUE;
        });
        return entities;
    }

    public static List<Entity> getPushableEntities(Level world, EntitySectionStorage<Entity> cache, Entity except, AABB box, EntityPushablePredicate<? super Entity> entityPushablePredicate) {
        ArrayList<Entity> entities = new ArrayList();
        cache.forEachAccessibleNonEmptySection(box, section -> ((ClimbingMobCachingSection) section).collectPushableEntities(world, except, box, entityPushablePredicate, entities));
        return entities;
    }

    public static boolean areNeighborsWithinSameChunk(BlockPos pos) {
        int localX = pos.m_123341_() & 15;
        int localZ = pos.m_123343_() & 15;
        return localX > 0 && localZ > 0 && localX < 15 && localZ < 15;
    }

    public static boolean areNeighborsWithinSameChunkSection(BlockPos pos) {
        int localX = pos.m_123341_() & 15;
        int localY = pos.m_123342_() & 15;
        int localZ = pos.m_123343_() & 15;
        return localX > 0 && localY > 0 && localZ > 0 && localX < 15 && localY < 15 && localZ < 15;
    }

    public static boolean arePosWithinSameChunk(BlockPos pos1, BlockPos pos2) {
        return pos1.m_123341_() >> 4 == pos2.m_123341_() >> 4 && pos1.m_123343_() >> 4 == pos2.m_123343_() >> 4;
    }
}