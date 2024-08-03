package dev.latvian.mods.kubejs.misc;

import dev.latvian.mods.kubejs.registry.BuilderBase;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import org.jetbrains.annotations.Nullable;

public class PotionBuilder extends BuilderBase<Potion> {

    public transient List<MobEffectInstance> mobEffects = new ArrayList();

    public PotionBuilder(ResourceLocation i) {
        super(i);
    }

    @Override
    public RegistryInfo getRegistryType() {
        return RegistryInfo.POTION;
    }

    public Potion createObject() {
        return new Potion(this.id.getPath(), (MobEffectInstance[]) this.mobEffects.toArray(new MobEffectInstance[0]));
    }

    public PotionBuilder addEffect(MobEffectInstance effect) {
        this.mobEffects.add(effect);
        return this;
    }

    public PotionBuilder effect(MobEffect effect) {
        return this.effect(effect, 0, 0);
    }

    public PotionBuilder effect(MobEffect effect, int duration) {
        return this.effect(effect, duration, 0);
    }

    public PotionBuilder effect(MobEffect effect, int duration, int amplifier) {
        return this.effect(effect, duration, amplifier, false, true);
    }

    public PotionBuilder effect(MobEffect effect, int duration, int amplifier, boolean ambient, boolean visible) {
        return this.effect(effect, duration, amplifier, ambient, visible, visible);
    }

    public PotionBuilder effect(MobEffect effect, int duration, int amplifier, boolean ambient, boolean visible, boolean showIcon) {
        return this.effect(effect, duration, amplifier, ambient, visible, showIcon, null);
    }

    public PotionBuilder effect(MobEffect effect, int duration, int amplifier, boolean ambient, boolean visible, boolean showIcon, @Nullable MobEffectInstance hiddenEffect) {
        return this.addEffect(new MobEffectInstance(effect, duration, amplifier, ambient, visible, showIcon, hiddenEffect, effect.createFactorData()));
    }
}