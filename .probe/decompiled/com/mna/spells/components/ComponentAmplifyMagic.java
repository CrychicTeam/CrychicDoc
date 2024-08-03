package com.mna.spells.components;

import com.mna.api.affinity.Affinity;
import com.mna.api.faction.IFaction;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.attributes.AttributeValuePair;
import com.mna.effects.EffectInit;
import com.mna.factions.Factions;
import net.minecraft.resources.ResourceLocation;

public class ComponentAmplifyMagic extends PotionEffectComponent {

    public ComponentAmplifyMagic(ResourceLocation guiIcon) {
        super(guiIcon, EffectInit.AMPLIFY_MAGIC, new AttributeValuePair(Attribute.DURATION, 120.0F, 30.0F, 600.0F, 30.0F, 15.0F), new AttributeValuePair(Attribute.MAGNITUDE, 1.0F, 1.0F, 5.0F, 1.0F, 15.0F));
    }

    @Override
    public IFaction getFactionRequirement() {
        return Factions.COUNCIL;
    }

    @Override
    public int requiredXPForRote() {
        return 100;
    }

    @Override
    public Affinity getAffinity() {
        return Affinity.ARCANE;
    }

    @Override
    public float initialComplexity() {
        return 10.0F;
    }

    @Override
    public boolean targetsBlocks() {
        return false;
    }
}