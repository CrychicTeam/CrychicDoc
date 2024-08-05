package io.redspace.ironsspellbooks.item.armor;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public enum UpgradeTypes implements UpgradeType {

    FIRE_SPELL_POWER("fire_power", AttributeRegistry.FIRE_SPELL_POWER.get(), AttributeModifier.Operation.MULTIPLY_BASE, 0.05F),
    ICE_SPELL_POWER("ice_power", AttributeRegistry.ICE_SPELL_POWER.get(), AttributeModifier.Operation.MULTIPLY_BASE, 0.05F),
    LIGHTNING_SPELL_POWER("lightning_power", AttributeRegistry.LIGHTNING_SPELL_POWER.get(), AttributeModifier.Operation.MULTIPLY_BASE, 0.05F),
    HOLY_SPELL_POWER("holy_power", AttributeRegistry.HOLY_SPELL_POWER.get(), AttributeModifier.Operation.MULTIPLY_BASE, 0.05F),
    ENDER_SPELL_POWER("ender_power", AttributeRegistry.ENDER_SPELL_POWER.get(), AttributeModifier.Operation.MULTIPLY_BASE, 0.05F),
    BLOOD_SPELL_POWER("blood_power", AttributeRegistry.BLOOD_SPELL_POWER.get(), AttributeModifier.Operation.MULTIPLY_BASE, 0.05F),
    EVOCATION_SPELL_POWER("evocation_power", AttributeRegistry.EVOCATION_SPELL_POWER.get(), AttributeModifier.Operation.MULTIPLY_BASE, 0.05F),
    NATURE_SPELL_POWER("nature_power", AttributeRegistry.NATURE_SPELL_POWER.get(), AttributeModifier.Operation.MULTIPLY_BASE, 0.05F),
    COOLDOWN("cooldown", AttributeRegistry.COOLDOWN_REDUCTION.get(), AttributeModifier.Operation.MULTIPLY_BASE, 0.05F),
    SPELL_RESISTANCE("spell_resistance", AttributeRegistry.SPELL_RESIST.get(), AttributeModifier.Operation.MULTIPLY_BASE, 0.05F),
    MANA("mana", AttributeRegistry.MAX_MANA.get(), AttributeModifier.Operation.ADDITION, 50.0F),
    ATTACK_DAMAGE("melee_damage", Attributes.ATTACK_DAMAGE, AttributeModifier.Operation.MULTIPLY_BASE, 0.05F),
    ATTACK_SPEED("melee_speed", Attributes.ATTACK_SPEED, AttributeModifier.Operation.MULTIPLY_BASE, 0.05F),
    HEALTH("health", Attributes.MAX_HEALTH, AttributeModifier.Operation.ADDITION, 2.0F);

    final Attribute attribute;

    final AttributeModifier.Operation operation;

    final float amountPerUpgrade;

    final ResourceLocation id;

    private UpgradeTypes(String key, Attribute attribute, AttributeModifier.Operation operation, float amountPerUpgrade) {
        this.id = IronsSpellbooks.id(key);
        this.attribute = attribute;
        this.operation = operation;
        this.amountPerUpgrade = amountPerUpgrade;
        UpgradeType.registerUpgrade(this);
    }

    @Override
    public Attribute getAttribute() {
        return this.attribute;
    }

    @Override
    public AttributeModifier.Operation getOperation() {
        return this.operation;
    }

    @Override
    public float getAmountPerUpgrade() {
        return this.amountPerUpgrade;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }
}