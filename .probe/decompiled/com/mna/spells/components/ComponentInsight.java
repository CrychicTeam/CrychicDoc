package com.mna.spells.components;

import com.mna.api.affinity.Affinity;
import com.mna.api.spells.SpellPartTags;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.attributes.AttributeValuePair;
import com.mna.effects.EffectInit;
import net.minecraft.resources.ResourceLocation;

public class ComponentInsight extends PotionEffectComponent {

    public ComponentInsight(ResourceLocation guiIcon) {
        super(guiIcon, EffectInit.INSIGHT, new AttributeValuePair(Attribute.DURATION, 30.0F, 10.0F, 600.0F, 30.0F, 1.0F));
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
        return 5.0F;
    }

    @Override
    public SpellPartTags getUseTag() {
        return SpellPartTags.UTILITY;
    }
}