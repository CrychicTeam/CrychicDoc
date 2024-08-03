package com.mna.api.entities;

import net.minecraft.world.entity.Entity;

public interface ISummonTargetPredicate {

    boolean shouldSummonTarget(Entity var1, boolean var2);
}