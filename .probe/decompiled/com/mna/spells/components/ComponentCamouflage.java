package com.mna.spells.components;

import com.mna.api.affinity.Affinity;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.attributes.AttributeValuePair;
import com.mna.effects.EffectInit;
import net.minecraft.resources.ResourceLocation;

public class ComponentCamouflage extends PotionEffectComponent {

    public ComponentCamouflage(ResourceLocation guiIcon) {
        super(guiIcon, EffectInit.CAMOUFLAGE, new AttributeValuePair(Attribute.DURATION, 30.0F, 30.0F, 300.0F, 30.0F, 2.0F), new AttributeValuePair(Attribute.LESSER_MAGNITUDE, 1.0F, 1.0F, 2.0F, 1.0F, 10.0F));
    }

    @Override
    public int requiredXPForRote() {
        return 200;
    }

    @Override
    public Affinity getAffinity() {
        return Affinity.WATER;
    }

    @Override
    public float initialComplexity() {
        return 10.0F;
    }
}