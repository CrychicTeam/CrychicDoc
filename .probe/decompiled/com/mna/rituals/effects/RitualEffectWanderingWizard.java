package com.mna.rituals.effects;

import com.mna.api.rituals.IRitualContext;
import com.mna.api.rituals.RitualEffect;
import com.mna.entities.utility.WanderingWizard;
import net.minecraft.resources.ResourceLocation;

public class RitualEffectWanderingWizard extends RitualEffect {

    public RitualEffectWanderingWizard(ResourceLocation ritualName) {
        super(ritualName);
    }

    @Override
    protected boolean applyRitualEffect(IRitualContext context) {
        if (context.getLevel().isClientSide()) {
            return true;
        } else {
            WanderingWizard entity = new WanderingWizard(context.getLevel());
            entity.m_6034_((double) context.getCenter().m_123341_() + 0.5, (double) context.getCenter().m_123342_(), (double) context.getCenter().m_123343_() + 0.5);
            entity.m_35891_(1200);
            context.getLevel().m_7967_(entity);
            return true;
        }
    }

    @Override
    protected int getApplicationTicks(IRitualContext context) {
        return 0;
    }
}