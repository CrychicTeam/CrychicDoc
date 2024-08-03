package com.mna.spells.components;

import com.mna.api.affinity.Affinity;
import com.mna.api.spells.SpellPartTags;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.attributes.AttributeValuePair;
import java.util.Arrays;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;

public class ComponentWither extends PotionEffectComponent {

    public ComponentWither(ResourceLocation guiIcon) {
        super(guiIcon, MobEffects.WITHER, new AttributeValuePair(Attribute.DURATION, 10.0F, 10.0F, 30.0F, 1.0F, 0.75F), new AttributeValuePair(Attribute.MAGNITUDE, 2.0F, 2.0F, 5.0F, 1.0F, 0.75F));
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
        return 10.0F;
    }

    @Override
    public SpellPartTags getUseTag() {
        return SpellPartTags.HARMFUL;
    }

    @Override
    public List<Affinity> getValidTinkerAffinities() {
        return Arrays.asList(Affinity.ENDER, Affinity.WATER, Affinity.EARTH, Affinity.ICE);
    }
}