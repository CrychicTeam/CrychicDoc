package com.mna.spells.components;

import com.mna.api.affinity.Affinity;
import com.mna.api.faction.IFaction;
import com.mna.api.spells.SpellPartTags;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.attributes.AttributeValuePair;
import com.mna.effects.EffectInit;
import com.mna.factions.Factions;
import java.util.Arrays;
import java.util.List;
import net.minecraft.resources.ResourceLocation;

public class ComponentFossilize extends PotionEffectComponent {

    public ComponentFossilize(ResourceLocation guiIcon) {
        super(guiIcon, EffectInit.FORTIFICATION, new AttributeValuePair(Attribute.DURATION, 30.0F, 30.0F, 600.0F, 30.0F, 10.0F));
    }

    @Override
    public int requiredXPForRote() {
        return 200;
    }

    @Override
    public Affinity getAffinity() {
        return Affinity.EARTH;
    }

    @Override
    public float initialComplexity() {
        return 20.0F;
    }

    @Override
    public IFaction getFactionRequirement() {
        return Factions.UNDEAD;
    }

    @Override
    public SpellPartTags getUseTag() {
        return SpellPartTags.NEUTRAL;
    }

    @Override
    public List<Affinity> getValidTinkerAffinities() {
        return Arrays.asList(Affinity.EARTH, Affinity.ICE, Affinity.ARCANE, Affinity.FIRE);
    }
}