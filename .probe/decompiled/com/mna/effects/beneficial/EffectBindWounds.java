package com.mna.effects.beneficial;

import com.mna.effects.interfaces.INoCreeperLingering;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;

public class EffectBindWounds extends MobEffect implements INoCreeperLingering {

    public static final String PERSISTENT_DATA_KEY_LASTPOS = "bind_wounds_last_pos";

    public EffectBindWounds() {
        super(MobEffectCategory.BENEFICIAL, 0);
    }

    @Override
    public void applyEffectTick(LivingEntity living, int amplifier) {
        BlockPos lastPos = null;
        BlockPos curPos = living.m_20183_();
        if (living.getPersistentData().contains("bind_wounds_last_pos")) {
            lastPos = BlockPos.of(living.getPersistentData().getLong("bind_wounds_last_pos"));
        }
        if (lastPos != null && !lastPos.equals(curPos)) {
            living.removeEffect(this);
        } else {
            living.heal(1.0F);
        }
        living.getPersistentData().putLong("bind_wounds_last_pos", curPos.asLong());
    }

    @Override
    public void removeAttributeModifiers(LivingEntity living, AttributeMap p_111187_2_, int p_111187_3_) {
        living.getPersistentData().remove("bind_wounds_last_pos");
        super.removeAttributeModifiers(living, p_111187_2_, p_111187_3_);
    }

    @Override
    public void addAttributeModifiers(LivingEntity living, AttributeMap p_111185_2_, int p_111185_3_) {
        living.getPersistentData().remove("bind_wounds_last_pos");
        super.addAttributeModifiers(living, p_111185_2_, p_111185_3_);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        int baseline = 30;
        int targetDuration = baseline / (amplifier + 1);
        return duration % targetDuration == 0;
    }
}