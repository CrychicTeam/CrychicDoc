package com.github.alexmodguy.alexscaves.server.entity.util;

import com.github.alexmodguy.alexscaves.server.entity.living.DeepOneBaseEntity;
import net.minecraft.world.entity.player.Player;

public enum DeepOneReaction {

    STALKING(0.0, 80.0), AGGRESSIVE(0.0, 40.0), NEUTRAL(10.0, 25.0), HELPFUL(8.0, 30.0);

    private double minDistance;

    private double maxDistance;

    private DeepOneReaction(double minDistance, double maxDistance) {
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
    }

    public double getMinDistance() {
        return this.minDistance;
    }

    public double getMaxDistance() {
        return this.maxDistance;
    }

    public static DeepOneReaction fromReputation(int rep) {
        if (rep <= -10) {
            return AGGRESSIVE;
        } else if (rep <= 10) {
            return STALKING;
        } else {
            return rep <= 30 ? NEUTRAL : HELPFUL;
        }
    }

    public boolean validPlayer(DeepOneBaseEntity deepOne, Player player) {
        if (this == STALKING && player.m_20186_() > deepOne.m_20186_() + 15.0) {
            return false;
        } else {
            return this != AGGRESSIVE && this != HELPFUL ? player.m_20072_() || !deepOne.m_20072_() : true;
        }
    }
}