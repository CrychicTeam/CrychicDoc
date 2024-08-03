package com.github.alexthe666.alexsmobs.entity.util;

import com.github.alexthe666.alexsmobs.entity.EntitySquidGrapple;
import com.github.alexthe666.citadel.server.entity.CitadelEntityData;
import java.util.UUID;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class SquidGrappleUtil {

    private static final String HOOK_1 = "SquidGrappleHook1AlexsMobs";

    private static final String HOOK_2 = "SquidGrappleHook2AlexsMobs";

    private static final String HOOK_3 = "SquidGrappleHook3AlexsMobs";

    private static final String HOOK_4 = "SquidGrappleHook4AlexsMobs";

    private static final String LAST_REPLACED_HOOK = "LastSquidGrappleHookAlexsMobs";

    public static int onFireHook(LivingEntity entity, UUID newHookUUID) {
        CompoundTag tag = CitadelEntityData.getOrCreateCitadelTag(entity);
        int index = getFirstAvailableHookIndex(entity);
        String indexStr = getHookStrFromIndex(index);
        if (tag.contains(indexStr)) {
            EntitySquidGrapple hook = getHookEntity(entity.m_9236_(), tag.getUUID(indexStr));
            if (hook != null && !hook.m_213877_()) {
                hook.setWithdrawing(true);
            }
        }
        tag.putUUID(indexStr, newHookUUID);
        CitadelEntityData.setCitadelTag(entity, tag);
        return index;
    }

    public static int getFirstAvailableHookIndex(LivingEntity entity) {
        int nulls = getAnyNullHooks(entity);
        if (nulls != -1) {
            return nulls;
        } else {
            int i = getHookCount(entity);
            if (i < 4) {
                return i;
            } else {
                CompoundTag tag = CitadelEntityData.getOrCreateCitadelTag(entity);
                int j = tag.getInt("LastSquidGrappleHookAlexsMobs");
                tag.putInt("LastSquidGrappleHookAlexsMobs", (j + 1) % 4);
                CitadelEntityData.setCitadelTag(entity, tag);
                return j;
            }
        }
    }

    public static String getHookStrFromIndex(int i) {
        switch(i) {
            case 0:
                return "SquidGrappleHook1AlexsMobs";
            case 1:
                return "SquidGrappleHook2AlexsMobs";
            case 2:
                return "SquidGrappleHook3AlexsMobs";
            case 3:
                return "SquidGrappleHook4AlexsMobs";
            default:
                return "SquidGrappleHook1AlexsMobs";
        }
    }

    public static int getAnyNullHooks(LivingEntity entity) {
        CompoundTag tag = CitadelEntityData.getOrCreateCitadelTag(entity);
        if (!tag.contains("SquidGrappleHook1AlexsMobs") || getHookEntity(entity.m_9236_(), tag.getUUID("SquidGrappleHook1AlexsMobs")) == null) {
            return 0;
        } else if (!tag.contains("SquidGrappleHook2AlexsMobs") || getHookEntity(entity.m_9236_(), tag.getUUID("SquidGrappleHook2AlexsMobs")) == null) {
            return 1;
        } else if (!tag.contains("SquidGrappleHook3AlexsMobs") || getHookEntity(entity.m_9236_(), tag.getUUID("SquidGrappleHook3AlexsMobs")) == null) {
            return 2;
        } else {
            return tag.contains("SquidGrappleHook4AlexsMobs") && getHookEntity(entity.m_9236_(), tag.getUUID("SquidGrappleHook4AlexsMobs")) != null ? -1 : 3;
        }
    }

    public static int getHookCount(LivingEntity entity) {
        CompoundTag tag = CitadelEntityData.getOrCreateCitadelTag(entity);
        int count = 0;
        if (tag.contains("SquidGrappleHook1AlexsMobs") && getHookEntity(entity.m_9236_(), tag.getUUID("SquidGrappleHook1AlexsMobs")) != null) {
            count++;
        }
        if (tag.contains("SquidGrappleHook2AlexsMobs") && getHookEntity(entity.m_9236_(), tag.getUUID("SquidGrappleHook2AlexsMobs")) != null) {
            count++;
        }
        if (tag.contains("SquidGrappleHook3AlexsMobs") && getHookEntity(entity.m_9236_(), tag.getUUID("SquidGrappleHook3AlexsMobs")) != null) {
            count++;
        }
        if (tag.contains("SquidGrappleHook4AlexsMobs") && getHookEntity(entity.m_9236_(), tag.getUUID("SquidGrappleHook4AlexsMobs")) != null) {
            count++;
        }
        return count;
    }

    public static EntitySquidGrapple getHookEntity(Level level, UUID id) {
        if (id != null && !level.isClientSide) {
            Entity e = ((ServerLevel) level).getEntity(id);
            return e instanceof EntitySquidGrapple ? (EntitySquidGrapple) e : null;
        } else {
            return null;
        }
    }
}