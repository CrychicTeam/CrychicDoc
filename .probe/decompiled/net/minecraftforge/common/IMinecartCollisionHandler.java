package net.minecraftforge.common;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.phys.AABB;

public interface IMinecartCollisionHandler {

    void onEntityCollision(AbstractMinecart var1, Entity var2);

    AABB getCollisionBox(AbstractMinecart var1, Entity var2);

    AABB getMinecartCollisionBox(AbstractMinecart var1);

    AABB getBoundingBox(AbstractMinecart var1);
}