package com.mna.spells.components;

import com.mna.api.affinity.Affinity;
import com.mna.api.spells.SpellPartTags;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.attributes.AttributeValuePair;
import com.mna.effects.EffectInit;
import net.minecraft.resources.ResourceLocation;

public class ComponentDisjunction extends PotionEffectComponent {

    public ComponentDisjunction(ResourceLocation guiIcon) {
        super(guiIcon, EffectInit.DISJUNCTION, new AttributeValuePair(Attribute.MAGNITUDE, 1.0F, 1.0F, 10.0F, 1.0F, 50.0F), new AttributeValuePair(Attribute.DURATION, 15.0F, 15.0F, 600.0F, 15.0F, 10.0F));
    }

    @Override
    public int requiredXPForRote() {
        return 200;
    }

    @Override
    public Affinity getAffinity() {
        return Affinity.ENDER;
    }

    @Override
    public float initialComplexity() {
        return 50.0F;
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