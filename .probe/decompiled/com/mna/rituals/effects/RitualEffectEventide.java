package com.mna.rituals.effects;

import com.mna.api.rituals.IRitualContext;
import com.mna.api.rituals.RitualEffect;
import com.mna.entities.EntityInit;
import com.mna.entities.rituals.TimeChangeBall;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public class RitualEffectEventide extends RitualEffect {

    public RitualEffectEventide(ResourceLocation ritualName) {
        super(ritualName);
    }

    @Override
    protected boolean applyRitualEffect(IRitualContext context) {
        if (!context.getLevel().isDay()) {
            return false;
        } else {
            TimeChangeBall auroraBall = new TimeChangeBall(EntityInit.STARBALL_ENTITY.get(), context.getLevel());
            auroraBall.m_146884_(Vec3.atCenterOf(context.getCenter().above(2)));
            context.getLevel().m_7967_(auroraBall);
            auroraBall.setTimeChangeType(TimeChangeBall.TIME_CHANGE_NIGHT);
            return true;
        }
    }

    @Override
    protected int getApplicationTicks(IRitualContext context) {
        return 120;
    }

    @Override
    public Component canRitualStart(IRitualContext context) {
        return !context.getLevel().isDay() ? Component.translatable("ritual.mna.eventide.nighttime") : null;
    }
}