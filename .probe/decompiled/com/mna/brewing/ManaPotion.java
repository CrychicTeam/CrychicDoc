package com.mna.brewing;

import com.mna.effects.EffectInit;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;

public class ManaPotion extends Potion {

    public ManaPotion(int amplifier) {
        super(new MobEffectInstance(EffectInit.INSTANT_MANA.get(), 0, amplifier));
    }
}