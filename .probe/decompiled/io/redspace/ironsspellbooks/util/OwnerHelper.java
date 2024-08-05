package io.redspace.ironsspellbooks.util;

import java.util.UUID;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class OwnerHelper {

    public static LivingEntity getAndCacheOwner(Level level, LivingEntity cachedOwner, UUID summonerUUID) {
        if (cachedOwner != null && cachedOwner.isAlive()) {
            return cachedOwner;
        } else if (summonerUUID != null && level instanceof ServerLevel serverLevel) {
            if (serverLevel.getEntity(summonerUUID) instanceof LivingEntity livingEntity) {
                cachedOwner = livingEntity;
            }
            return cachedOwner;
        } else {
            return null;
        }
    }

    public static void serializeOwner(CompoundTag compoundTag, UUID ownerUUID) {
        if (ownerUUID != null) {
            compoundTag.putUUID("Summoner", ownerUUID);
        }
    }

    public static UUID deserializeOwner(CompoundTag compoundTag) {
        return compoundTag.hasUUID("Summoner") ? compoundTag.getUUID("Summoner") : null;
    }
}