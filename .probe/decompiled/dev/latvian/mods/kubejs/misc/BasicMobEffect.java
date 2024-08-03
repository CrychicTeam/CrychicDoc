package dev.latvian.mods.kubejs.misc;

import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.script.ScriptType;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.jetbrains.annotations.NotNull;

public class BasicMobEffect extends MobEffect {

    private final MobEffectBuilder.EffectTickCallback effectTickCallback;

    private final Map<ResourceLocation, AttributeModifier> modifierMap;

    private final Map<Attribute, AttributeModifier> attributeMap;

    private boolean modified = false;

    private final ResourceLocation id;

    public BasicMobEffect(BasicMobEffect.Builder builder) {
        super(builder.category, builder.color);
        this.effectTickCallback = builder.effectTick;
        this.modifierMap = builder.attributeModifiers;
        this.attributeMap = new HashMap();
        this.id = builder.id;
    }

    @Override
    public void applyEffectTick(@NotNull LivingEntity livingEntity, int i) {
        try {
            this.effectTickCallback.applyEffectTick(livingEntity, i);
        } catch (Throwable var4) {
            ScriptType.STARTUP.console.error("Error while ticking mob effect " + this.id + " for entity " + livingEntity.m_7755_().getString(), var4);
        }
    }

    private void applyAttributeModifications() {
        if (!this.modified) {
            this.modifierMap.forEach((r, m) -> this.attributeMap.put(RegistryInfo.ATTRIBUTE.getValue(r), m));
            this.modified = true;
        }
    }

    @Override
    public Map<Attribute, AttributeModifier> getAttributeModifiers() {
        this.applyAttributeModifications();
        return this.attributeMap;
    }

    @Override
    public void removeAttributeModifiers(LivingEntity livingEntity, AttributeMap attributeMap, int i) {
        this.applyAttributeModifications();
        for (Entry<Attribute, AttributeModifier> entry : this.attributeMap.entrySet()) {
            AttributeInstance attributeInstance = attributeMap.getInstance((Attribute) entry.getKey());
            if (attributeInstance != null) {
                attributeInstance.removeModifier((AttributeModifier) entry.getValue());
            }
        }
    }

    @Override
    public void addAttributeModifiers(LivingEntity livingEntity, AttributeMap attributeMap, int i) {
        this.applyAttributeModifications();
        for (Entry<Attribute, AttributeModifier> attributeAttributeModifierEntry : this.attributeMap.entrySet()) {
            AttributeInstance attributeInstance = attributeMap.getInstance((Attribute) attributeAttributeModifierEntry.getKey());
            if (attributeInstance != null) {
                AttributeModifier attributeModifier = (AttributeModifier) attributeAttributeModifierEntry.getValue();
                attributeInstance.removeModifier(attributeModifier);
                attributeInstance.addPermanentModifier(new AttributeModifier(attributeModifier.getId(), this.m_19481_() + " " + i, this.m_7048_(i, attributeModifier), attributeModifier.getOperation()));
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int i, int j) {
        return this.effectTickCallback != null;
    }

    public static class Builder extends MobEffectBuilder {

        public Builder(ResourceLocation i) {
            super(i);
        }

        public MobEffect createObject() {
            return new BasicMobEffect(this);
        }
    }
}