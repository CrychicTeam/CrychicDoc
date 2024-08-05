package com.mna.spells.components;

import com.mna.api.affinity.Affinity;
import com.mna.api.spells.SpellPartTags;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.attributes.AttributeValuePair;
import com.mna.effects.EffectInit;
import java.util.Arrays;
import java.util.List;
import net.minecraft.resources.ResourceLocation;

public class ComponentWindWall extends PotionEffectComponent {

    public ComponentWindWall(ResourceLocation guiIcon) {
        super(guiIcon, EffectInit.WIND_WALL, new AttributeValuePair(Attribute.DURATION, 120.0F, 30.0F, 600.0F, 30.0F, 10.0F), new AttributeValuePair(Attribute.PRECISION, 0.0F, 0.0F, 1.0F, 1.0F, 20.0F));
    }

    @Override
    public int requiredXPForRote() {
        return 200;
    }

    @Override
    public Affinity getAffinity() {
        return Affinity.WIND;
    }

    @Override
    public float initialComplexity() {
        return 20.0F;
    }

    @Override
    public SpellPartTags getUseTag() {
        return SpellPartTags.FRIENDLY;
    }

    @Override
    public List<Affinity> getValidTinkerAffinities() {
        return Arrays.asList(Affinity.WATER, Affinity.WIND, Affinity.LIGHTNING, Affinity.ICE);
    }
}