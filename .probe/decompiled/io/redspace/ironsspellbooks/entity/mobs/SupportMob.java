package io.redspace.ironsspellbooks.entity.mobs;

import javax.annotation.Nullable;
import net.minecraft.world.entity.LivingEntity;

public interface SupportMob {

    @Nullable
    LivingEntity getSupportTarget();

    void setSupportTarget(LivingEntity var1);
}