package com.mna.rituals.effects;

import com.mna.api.rituals.IRitualContext;
import com.mna.api.rituals.RitualEffect;
import com.mna.entities.boss.effects.Bifrost;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public class RitualEffectSummonOdin extends RitualEffect {

    public RitualEffectSummonOdin(ResourceLocation ritualName) {
        super(ritualName);
    }

    @Override
    public Component canRitualStart(IRitualContext context) {
        if (!context.getLevel().m_45527_(context.getCenter())) {
            return Component.translatable("mna:rituals/odins_call.sky");
        } else {
            return (Component) (context.getLevel().isRainingAt(context.getCenter()) && context.getLevel().isThundering() ? super.canRitualStart(context) : Component.translatable("mna:rituals/odins_call.storm"));
        }
    }

    @Override
    protected boolean applyRitualEffect(IRitualContext context) {
        Bifrost bifrost = new Bifrost(context.getLevel(), new Vec3((double) context.getCenter().m_123341_(), (double) (context.getCenter().m_123342_() + 10), (double) context.getCenter().m_123343_()));
        context.getLevel().m_7967_(bifrost);
        return true;
    }

    @Override
    protected int getApplicationTicks(IRitualContext context) {
        return 0;
    }
}