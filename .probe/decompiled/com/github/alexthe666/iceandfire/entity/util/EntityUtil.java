package com.github.alexthe666.iceandfire.entity.util;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityMutlipartPart;
import java.util.UUID;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EntityUtil {

    public static void updatePart(@Nullable EntityMutlipartPart part, @NotNull LivingEntity parent) {
        if (part != null && parent.m_9236_() instanceof ServerLevel serverLevel && !parent.m_213877_()) {
            if (!part.shouldContinuePersisting()) {
                UUID uuid = part.m_20148_();
                Entity existing = serverLevel.getEntity(uuid);
                if (existing != null && existing != part) {
                    while (serverLevel.getEntity(uuid) != null) {
                        uuid = Mth.createInsecureUUID(parent.getRandom());
                    }
                    IceAndFire.LOGGER.debug("Updated the UUID of [{}] due to a clash with [{}]", part, existing);
                }
                part.m_20084_(uuid);
                serverLevel.addFreshEntity(part);
            }
            part.setParent(parent);
        }
    }
}