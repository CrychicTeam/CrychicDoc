package com.mna.spells.components;

import com.mna.api.affinity.Affinity;
import com.mna.api.spells.SpellPartTags;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.attributes.AttributeValuePair;
import com.mna.effects.EffectInit;
import net.minecraft.resources.ResourceLocation;

public class ComponentBindWounds extends PotionEffectComponent {

    public ComponentBindWounds(ResourceLocation guiIcon) {
        super(guiIcon, EffectInit.BIND_WOUNDS, new AttributeValuePair(Attribute.DURATION, 30.0F, 30.0F, 180.0F, 30.0F, 12.0F), new AttributeValuePair(Attribute.LESSER_MAGNITUDE, 1.0F, 1.0F, 3.0F, 1.0F, 10.0F));
    }

    @Override
    public Affinity getAffinity() {
        return Affinity.WATER;
    }

    @Override
    public float initialComplexity() {
        return 30.0F;
    }

    @Override
    public int requiredXPForRote() {
        return 80;
    }

    @Override
    public SpellPartTags getUseTag() {
        return SpellPartTags.SELF;
    }
}