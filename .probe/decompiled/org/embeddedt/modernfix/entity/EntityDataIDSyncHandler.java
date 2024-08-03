package org.embeddedt.modernfix.entity;

import com.mojang.datafixers.util.Pair;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import org.embeddedt.modernfix.ModernFix;
import org.embeddedt.modernfix.packet.EntityIDSyncPacket;
import org.embeddedt.modernfix.platform.ModernFixPlatformHooks;

public class EntityDataIDSyncHandler {

    private static Map<Class<? extends Entity>, List<Pair<String, Integer>>> fieldsToSyncMap;

    public static void onDatapackSyncEvent(ServerPlayer targetPlayer) {
        if (targetPlayer != null) {
            if (fieldsToSyncMap == null) {
                fieldsToSyncMap = new HashMap();
                Map<Class<? extends Entity>, Integer> entityPoolMap = SynchedEntityData.ENTITY_ID_POOL;
                List<Field> fieldsToSync = new ArrayList();
                for (Class<? extends Entity> eClass : entityPoolMap.keySet()) {
                    fieldsToSync.clear();
                    try {
                        Field[] classFields = eClass.getDeclaredFields();
                        for (Field field : classFields) {
                            if (Modifier.isStatic(field.getModifiers())) {
                                field.setAccessible(true);
                                Object o = field.get(null);
                                if (o != null && EntityDataAccessor.class.isAssignableFrom(o.getClass())) {
                                    fieldsToSync.add(field);
                                }
                            }
                        }
                        for (Field fieldx : fieldsToSync) {
                            int id = ((EntityDataAccessor) fieldx.get(null)).id;
                            ((List) fieldsToSyncMap.computeIfAbsent(eClass, k -> new ArrayList())).add(Pair.of(fieldx.getName(), id));
                        }
                    } catch (Throwable var11) {
                        ModernFix.LOGGER.error("Skipping entity ID sync for {}: {}", eClass.getName(), var11);
                    }
                }
            }
            EntityIDSyncPacket packet = new EntityIDSyncPacket(fieldsToSyncMap);
            ModernFix.LOGGER.debug("Sending ID correction packet to client with " + fieldsToSyncMap.size() + " classes");
            ModernFixPlatformHooks.INSTANCE.sendPacket(targetPlayer, packet);
        }
    }
}