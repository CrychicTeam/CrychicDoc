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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ComponentMistForm extends PotionEffectComponent {

    public ComponentMistForm(ResourceLocation guiIcon) {
        super(guiIcon, EffectInit.MIST_FORM, new AttributeValuePair(Attribute.DURATION, 120.0F, 30.0F, 600.0F, 30.0F, 5.0F));
        this.addReagent(new ItemStack(Items.PHANTOM_MEMBRANE), new IFaction[] { Factions.UNDEAD });
    }

    @Override
    public IFaction getFactionRequirement() {
        return Factions.UNDEAD;
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
        return 75.0F;
    }

    @Override
    public boolean targetsBlocks() {
        return false;
    }

    @Override
    public SpellPartTags getUseTag() {
        return SpellPartTags.SELF;
    }

    @Override
    public List<Affinity> getValidTinkerAffinities() {
        return Arrays.asList(Affinity.ARCANE, Affinity.WATER, Affinity.WIND, Affinity.ICE);
    }
}