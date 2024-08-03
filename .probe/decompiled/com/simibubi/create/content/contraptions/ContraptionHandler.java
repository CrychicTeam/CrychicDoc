package com.simibubi.create.content.contraptions;

import com.simibubi.create.foundation.utility.VecHelper;
import com.simibubi.create.foundation.utility.WorldAttached;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectLists;
import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ContraptionHandler {

    public static WorldAttached<Map<Integer, WeakReference<AbstractContraptionEntity>>> loadedContraptions = new WorldAttached<>($ -> new HashMap());

    static WorldAttached<List<AbstractContraptionEntity>> queuedAdditions = new WorldAttached<>($ -> ObjectLists.synchronize(new ObjectArrayList()));

    public static void tick(Level world) {
        Map<Integer, WeakReference<AbstractContraptionEntity>> map = loadedContraptions.get(world);
        List<AbstractContraptionEntity> queued = queuedAdditions.get(world);
        for (AbstractContraptionEntity contraptionEntity : queued) {
            map.put(contraptionEntity.m_19879_(), new WeakReference(contraptionEntity));
        }
        queued.clear();
        Collection<WeakReference<AbstractContraptionEntity>> values = map.values();
        Iterator<WeakReference<AbstractContraptionEntity>> iterator = values.iterator();
        while (iterator.hasNext()) {
            WeakReference<AbstractContraptionEntity> weakReference = (WeakReference<AbstractContraptionEntity>) iterator.next();
            AbstractContraptionEntity contraptionEntity = (AbstractContraptionEntity) weakReference.get();
            if (contraptionEntity == null || !contraptionEntity.isAliveOrStale()) {
                iterator.remove();
            } else if (!contraptionEntity.m_6084_()) {
                contraptionEntity.staleTicks--;
            } else {
                ContraptionCollider.collideEntities(contraptionEntity);
            }
        }
    }

    public static void addSpawnedContraptionsToCollisionList(Entity entity, Level world) {
        if (entity instanceof AbstractContraptionEntity) {
            queuedAdditions.get(world).add((AbstractContraptionEntity) entity);
        }
    }

    public static void entitiesWhoJustDismountedGetSentToTheRightLocation(LivingEntity entityLiving, Level world) {
        if (world.isClientSide) {
            CompoundTag data = entityLiving.getPersistentData();
            if (data.contains("ContraptionDismountLocation")) {
                Vec3 position = VecHelper.readNBT(data.getList("ContraptionDismountLocation", 6));
                if (entityLiving.m_20202_() == null) {
                    entityLiving.m_19890_(position.x, position.y, position.z, entityLiving.m_146908_(), entityLiving.m_146909_());
                }
                data.remove("ContraptionDismountLocation");
                entityLiving.m_6853_(false);
            }
        }
    }
}