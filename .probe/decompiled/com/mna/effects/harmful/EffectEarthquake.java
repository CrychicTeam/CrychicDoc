package com.mna.effects.harmful;

import com.mna.ManaAndArtifice;
import com.mna.effects.interfaces.IInputDisable;
import com.mna.effects.interfaces.INoCreeperLingering;
import java.util.EnumSet;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;

public class EffectEarthquake extends MobEffect implements INoCreeperLingering, IInputDisable {

    public EffectEarthquake() {
        super(MobEffectCategory.HARMFUL, 0);
    }

    @Override
    public void applyEffectTick(LivingEntity living, int amplifier) {
        if (living.m_20089_() != Pose.SWIMMING) {
            living.m_20124_(Pose.SWIMMING);
            living.m_6210_();
        }
        if (living instanceof Player) {
            ManaAndArtifice.instance.proxy.setFlightEnabled((Player) living, false);
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public EnumSet<IInputDisable.InputMask> getDisabledFlags() {
        return EnumSet.of(IInputDisable.InputMask.JUMP);
    }
}