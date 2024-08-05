package dev.latvian.mods.kubejs.misc;

import dev.latvian.mods.kubejs.registry.BuilderBase;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.rhino.mod.util.color.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public abstract class MobEffectBuilder extends BuilderBase<MobEffect> {

    public transient MobEffectCategory category = MobEffectCategory.NEUTRAL;

    public transient MobEffectBuilder.EffectTickCallback effectTick;

    public transient Map<ResourceLocation, AttributeModifier> attributeModifiers;

    public transient int color = 16777215;

    public MobEffectBuilder(ResourceLocation i) {
        super(i);
        this.effectTick = null;
        this.attributeModifiers = new HashMap();
    }

    @Override
    public final RegistryInfo getRegistryType() {
        return RegistryInfo.MOB_EFFECT;
    }

    public MobEffectBuilder modifyAttribute(ResourceLocation attribute, String identifier, double d, AttributeModifier.Operation operation) {
        AttributeModifier attributeModifier = new AttributeModifier(new UUID((long) identifier.hashCode(), (long) identifier.hashCode()), identifier, d, operation);
        this.attributeModifiers.put(attribute, attributeModifier);
        return this;
    }

    public MobEffectBuilder category(MobEffectCategory c) {
        this.category = c;
        return this;
    }

    public MobEffectBuilder harmful() {
        return this.category(MobEffectCategory.HARMFUL);
    }

    public MobEffectBuilder beneficial() {
        return this.category(MobEffectCategory.BENEFICIAL);
    }

    public MobEffectBuilder effectTick(MobEffectBuilder.EffectTickCallback effectTick) {
        this.effectTick = effectTick;
        return this;
    }

    public MobEffectBuilder color(Color col) {
        this.color = col.getRgbJS();
        return this;
    }

    @FunctionalInterface
    public interface EffectTickCallback {

        void applyEffectTick(LivingEntity var1, int var2);
    }
}