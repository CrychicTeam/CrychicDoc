package com.mna.rituals.effects;

import com.mna.api.rituals.IRitualContext;
import com.mna.api.rituals.RitualEffect;
import com.mna.entities.utility.PresentItem;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public abstract class RitualEffectCreateEssence extends RitualEffect {

    public RitualEffectCreateEssence(ResourceLocation ritualName) {
        super(ritualName);
    }

    public abstract ItemStack getOutputStack();

    @Override
    protected boolean applyRitualEffect(IRitualContext context) {
        this.SpawnItem(context.getCenter(), context.getLevel());
        return true;
    }

    protected void SpawnItem(BlockPos ritualCenter, Level world) {
        PresentItem entity = new PresentItem(world, (double) ((float) ritualCenter.m_123341_() + 0.5F), (double) ritualCenter.above().m_123342_(), (double) ((float) ritualCenter.m_123343_() + 0.5F), this.getOutputStack());
        world.m_7967_(entity);
    }

    @Override
    protected int getApplicationTicks(IRitualContext context) {
        return 20;
    }
}