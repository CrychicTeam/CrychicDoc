package com.mna.api.rituals;

import com.mna.api.sound.SFX;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;

public abstract class RitualEffect {

    private ResourceLocation ritualName;

    public RitualEffect(ResourceLocation ritualName) {
        this.ritualName = ritualName;
        MinecraftForge.EVENT_BUS.register(this);
    }

    public final boolean matchRitual(ResourceLocation completedRecipeId) {
        return this.ritualName.equals(completedRecipeId);
    }

    protected boolean matchReagents(IRitualContext context) {
        return true;
    }

    public final boolean onRitualCompleted(IRitualContext context) {
        return this.matchReagents(context) ? this.applyRitualEffect(context) : false;
    }

    public final int getRitualCompleteDelay(IRitualContext context) {
        return this.getApplicationTicks(context);
    }

    public final boolean getDynamicReagents(ItemStack supplier, IRitualContext context) {
        return this.modifyRitualReagentsAndPatterns(supplier, context);
    }

    public final boolean handlesRitual(ResourceLocation ritualID) {
        return this.ritualName.equals(ritualID);
    }

    protected boolean modifyRitualReagentsAndPatterns(ItemStack dataStack, IRitualContext context) {
        return true;
    }

    protected abstract boolean applyRitualEffect(IRitualContext var1);

    protected abstract int getApplicationTicks(IRitualContext var1);

    @Nullable
    public Component canRitualStart(IRitualContext context) {
        return null;
    }

    public boolean applyStartCheckInCreative() {
        return false;
    }

    @OnlyIn(Dist.CLIENT)
    public boolean spawnRitualParticles(IRitualContext context) {
        return false;
    }

    public SoundEvent getLoopSound(IRitualContext context) {
        return SFX.Loops.ENDER;
    }

    public static class PhantomRitualEffect extends RitualEffect {

        public PhantomRitualEffect(ResourceLocation ritualName) {
            super(ritualName);
        }

        @Override
        protected boolean applyRitualEffect(IRitualContext context) {
            return false;
        }

        @Override
        protected int getApplicationTicks(IRitualContext context) {
            return 0;
        }
    }
}