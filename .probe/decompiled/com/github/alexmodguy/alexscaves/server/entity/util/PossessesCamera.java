package com.github.alexmodguy.alexscaves.server.entity.util;

import net.minecraft.world.entity.Entity;

public interface PossessesCamera {

    float getPossessionStrength(float var1);

    boolean instant();

    boolean isPossessionBreakable();

    void onPossessionKeyPacket(Entity var1, int var2);
}