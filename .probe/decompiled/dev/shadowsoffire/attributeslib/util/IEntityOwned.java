package dev.shadowsoffire.attributeslib.util;

import net.minecraft.world.entity.LivingEntity;

public interface IEntityOwned {

    LivingEntity getOwner();

    void setOwner(LivingEntity var1);
}