package com.mna.items.constructs.parts.arms;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import com.mna.api.ManaAndArtificeMod;
import com.mna.api.entities.construct.ConstructCapability;
import com.mna.api.entities.construct.ConstructMaterial;
import com.mna.api.entities.construct.ConstructSlot;
import com.mna.api.entities.construct.ItemConstructPart;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.common.ForgeMod;

public class ConstructPartGrabberArmLeft extends ItemConstructPart {

    private Multimap<Attribute, AttributeModifier> defaultModifiers;

    private int reachIncrease;

    public ConstructPartGrabberArmLeft(ConstructMaterial material, int reachIncrease) {
        super(material, ConstructSlot.LEFT_ARM, 1);
        this.reachIncrease = reachIncrease;
    }

    @Override
    public ConstructCapability[] getEnabledCapabilities() {
        return new ConstructCapability[] { ConstructCapability.MELEE_ATTACK, ConstructCapability.PLANT, ConstructCapability.CARRY, ConstructCapability.CRAFT };
    }

    @Override
    public float getAttackDamage() {
        return 1.0F;
    }

    @Override
    public int getAttackSpeedModifier() {
        return 5;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
        if (this.defaultModifiers == null) {
            Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.put(ForgeMod.BLOCK_REACH.get(), new AttributeModifier(ManaAndArtificeMod.REACH_DISTANCE_UUID, "Reach modifier", (double) this.reachIncrease, AttributeModifier.Operation.ADDITION));
            this.defaultModifiers = builder.build();
        }
        return slot == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.m_7167_(slot);
    }
}