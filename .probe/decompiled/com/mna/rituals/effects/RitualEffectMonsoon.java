package com.mna.rituals.effects;

import com.mna.api.rituals.IRitualContext;
import com.mna.api.rituals.RitualEffect;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Items;

public class RitualEffectMonsoon extends RitualEffect {

    public RitualEffectMonsoon(ResourceLocation ritualName) {
        super(ritualName);
    }

    @Override
    protected boolean applyRitualEffect(IRitualContext context) {
        boolean thunder = context.getCollectedReagents(i -> i.getItem() == Items.GRAY_WOOL).size() == 5;
        if (thunder) {
            ((ServerLevel) context.getLevel()).setWeatherParameters(0, 6000, true, true);
        } else {
            ((ServerLevel) context.getLevel()).setWeatherParameters(0, 6000, true, false);
        }
        return true;
    }

    @Override
    protected int getApplicationTicks(IRitualContext context) {
        return 10;
    }
}