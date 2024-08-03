package com.mna.effects.beneficial;

import com.mna.effects.interfaces.IDoubleTapEndEarly;
import com.mna.effects.interfaces.IInputDisable;
import com.mna.effects.interfaces.INoCreeperLingering;
import java.util.EnumSet;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.player.Player;

public class EffectPossession extends MobEffect implements IInputDisable, IDoubleTapEndEarly, INoCreeperLingering {

    public EffectPossession() {
        super(MobEffectCategory.HARMFUL, 0);
    }

    @Override
    public void addAttributeModifiers(LivingEntity entityLivingBaseIn, AttributeMap attributeMapIn, int amplifier) {
        super.addAttributeModifiers(entityLivingBaseIn, attributeMapIn, amplifier);
        if (entityLivingBaseIn instanceof Mob) {
            ((Mob) entityLivingBaseIn).setNoAi(true);
        }
    }

    @Override
    public void removeAttributeModifiers(LivingEntity entityLivingBaseIn, AttributeMap attributeMapIn, int amplifier) {
        super.removeAttributeModifiers(entityLivingBaseIn, attributeMapIn, amplifier);
        if (entityLivingBaseIn instanceof Mob) {
            ((Mob) entityLivingBaseIn).setNoAi(false);
        }
    }

    @Override
    public EnumSet<IInputDisable.InputMask> getDisabledFlags() {
        return EnumSet.of(IInputDisable.InputMask.LEFT_CLICK, IInputDisable.InputMask.RIGHT_CLICK, IInputDisable.InputMask.MOVEMENT);
    }

    @Override
    public boolean canEndEarly(Player player, MobEffectInstance effect) {
        return effect.getAmplifier() > 0;
    }

    @Override
    public void onRemoved(Player player, MobEffectInstance effect) {
        if (player.getPersistentData().contains("posessed_entity_id")) {
            int id = player.getPersistentData().getInt("posessed_entity_id");
            Entity e = player.m_9236_().getEntity(id);
            if (e != null && e instanceof Mob) {
                ((Mob) e).m_21195_(this);
            }
        }
    }
}