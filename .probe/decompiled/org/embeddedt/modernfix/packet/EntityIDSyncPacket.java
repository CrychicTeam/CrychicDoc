package org.embeddedt.modernfix.packet;

import com.mojang.datafixers.util.Pair;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.Entity;
import org.embeddedt.modernfix.ModernFix;

public class EntityIDSyncPacket {

    private Map<Class<? extends Entity>, List<Pair<String, Integer>>> map;

    public EntityIDSyncPacket(Map<Class<? extends Entity>, List<Pair<String, Integer>>> map) {
        this.map = map;
    }

    public Map<Class<? extends Entity>, List<Pair<String, Integer>>> getFieldInfo() {
        return this.map;
    }

    public EntityIDSyncPacket() {
        this.map = new HashMap();
    }

    public void serialize(FriendlyByteBuf buf) {
        buf.writeVarInt(this.map.keySet().size());
        for (Entry<Class<? extends Entity>, List<Pair<String, Integer>>> entry : this.map.entrySet()) {
            buf.writeUtf(((Class) entry.getKey()).getName());
            buf.writeVarInt(((List) entry.getValue()).size());
            for (Pair<String, Integer> field : (List) entry.getValue()) {
                buf.writeUtf((String) field.getFirst());
                buf.writeVarInt((Integer) field.getSecond());
            }
        }
    }

    public static EntityIDSyncPacket deserialize(FriendlyByteBuf buf) {
        EntityIDSyncPacket self = new EntityIDSyncPacket();
        int numEntityClasses = buf.readVarInt();
        for (int i = 0; i < numEntityClasses; i++) {
            String clzName = buf.readUtf();
            try {
                Class<?> clz;
                try {
                    clz = Class.forName(clzName);
                } catch (ClassNotFoundException var11) {
                    ModernFix.LOGGER.warn("Entity class not found: {}", clzName);
                    break;
                }
                if (!Entity.class.isAssignableFrom(clz)) {
                    ModernFix.LOGGER.error("Not an entity: " + clzName);
                    break;
                }
                int numFields = buf.readVarInt();
                for (int j = 0; j < numFields; j++) {
                    String fieldName = buf.readUtf();
                    int id = buf.readVarInt();
                    Field f = clz.getDeclaredField(fieldName);
                    if (Modifier.isStatic(f.getModifiers())) {
                        f.setAccessible(true);
                        if (!EntityDataAccessor.class.isAssignableFrom(f.get(null).getClass())) {
                            ModernFix.LOGGER.error("Not a data accessor field: " + clz + "." + fieldName);
                        } else {
                            ((List) self.map.computeIfAbsent(clz, k -> new ArrayList())).add(Pair.of(fieldName, id));
                        }
                    }
                }
            } catch (ReflectiveOperationException var12) {
                ModernFix.LOGGER.error("Error deserializing packet", var12);
            }
        }
        return self;
    }
}