package com.mna.spells.components;

import com.mna.api.affinity.Affinity;
import com.mna.api.faction.IFaction;
import com.mna.api.spells.SpellPartTags;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.attributes.AttributeValuePair;
import com.mna.effects.EffectInit;
import com.mna.factions.Factions;
import net.minecraft.resources.ResourceLocation;

public class ComponentSunder extends PotionEffectComponent {

    public ComponentSunder(ResourceLocation guiIcon) {
        super(guiIcon, EffectInit.SUNDER, new AttributeValuePair(Attribute.DURATION, 15.0F, 15.0F, 180.0F, 15.0F, 2.0F), new AttributeValuePair(Attribute.MAGNITUDE, 1.0F, 1.0F, 15.0F, 1.0F, 5.0F));
    }

    @Override
    public IFaction getFactionRequirement() {
        return Factions.FEY;
    }

    @Override
    public int requiredXPForRote() {
        return 100;
    }

    @Override
    public Affinity getAffinity() {
        return Affinity.FIRE;
    }

    @Override
    public float initialComplexity() {
        return 30.0F;
    }

    @Override
    public boolean targetsBlocks() {
        return false;
    }

    @Override
    public SpellPartTags getUseTag() {
        return SpellPartTags.HARMFUL;
    }
}