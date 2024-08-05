package com.github.alexthe666.alexsmobs.entity.util;

import com.github.alexthe666.alexsmobs.entity.EntityTendonSegment;
import com.github.alexthe666.citadel.Citadel;
import com.github.alexthe666.citadel.server.entity.CitadelEntityData;
import com.github.alexthe666.citadel.server.message.PropertiesMessage;
import java.util.UUID;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class TendonWhipUtil {

    private static final String LAST_TENDON_UUID = "LastTendonUUIDAlexsMobs";

    private static final String LAST_TENDON_ID = "LastTendonIDAlexsMobs";

    private static void sync(LivingEntity enchanted, CompoundTag tag) {
        CitadelEntityData.setCitadelTag(enchanted, tag);
        if (!enchanted.m_9236_().isClientSide) {
            Citadel.sendMSGToAll(new PropertiesMessage("CitadelTagUpdate", tag, enchanted.m_19879_()));
        } else {
            Citadel.sendMSGToServer(new PropertiesMessage("CitadelTagUpdate", tag, enchanted.m_19879_()));
        }
    }

    public static void setLastTendon(LivingEntity entity, EntityTendonSegment tendon) {
        CompoundTag tag = CitadelEntityData.getOrCreateCitadelTag(entity);
        if (tendon == null) {
            tag.remove("LastTendonUUIDAlexsMobs");
            tag.putInt("LastTendonIDAlexsMobs", -1);
        } else {
            tag.putUUID("LastTendonUUIDAlexsMobs", tendon.m_20148_());
            tag.putInt("LastTendonIDAlexsMobs", tendon.m_19879_());
        }
        sync(entity, tag);
    }

    private static UUID getLastTendonUUID(LivingEntity entity) {
        CompoundTag tag = CitadelEntityData.getOrCreateCitadelTag(entity);
        return tag.contains("LastTendonUUIDAlexsMobs") ? tag.getUUID("LastTendonUUIDAlexsMobs") : null;
    }

    public static int getLastTendonId(LivingEntity entity) {
        CompoundTag tag = CitadelEntityData.getOrCreateCitadelTag(entity);
        return tag.contains("LastTendonIDAlexsMobs") ? tag.getInt("LastTendonIDAlexsMobs") : -1;
    }

    public static void retractFarTendons(Level level, LivingEntity player) {
        EntityTendonSegment last = getLastTendon(player);
        if (last != null) {
            last.m_142687_(Entity.RemovalReason.DISCARDED);
            setLastTendon(player, null);
        }
    }

    public static boolean canLaunchTendons(Level level, LivingEntity player) {
        EntityTendonSegment last = getLastTendon(player);
        return last == null ? true : last.m_213877_() || last.m_20270_(player) > 30.0F;
    }

    public static EntityTendonSegment getLastTendon(LivingEntity player) {
        UUID uuid = getLastTendonUUID(player);
        int id = getLastTendonId(player);
        if (!player.m_9236_().isClientSide) {
            if (uuid != null) {
                Entity e = player.m_9236_().getEntity(id);
                return e instanceof EntityTendonSegment ? (EntityTendonSegment) e : null;
            }
        } else if (id != -1) {
            Entity e = player.m_9236_().getEntity(id);
            return e instanceof EntityTendonSegment ? (EntityTendonSegment) e : null;
        }
        return null;
    }
}