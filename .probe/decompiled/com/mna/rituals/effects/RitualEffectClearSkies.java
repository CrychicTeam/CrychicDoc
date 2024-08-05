package com.mna.rituals.effects;

import com.mna.api.rituals.IRitualContext;
import com.mna.api.rituals.RitualEffect;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;

public class RitualEffectClearSkies extends RitualEffect {

    public RitualEffectClearSkies(ResourceLocation ritualName) {
        super(ritualName);
    }

    @Override
    protected boolean applyRitualEffect(IRitualContext context) {
        ((ServerLevel) context.getLevel()).setWeatherParameters(6000, 0, false, false);
        return true;
    }

    @Override
    protected int getApplicationTicks(IRitualContext context) {
        return 10;
    }
}