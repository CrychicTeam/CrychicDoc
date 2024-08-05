package com.mna.spells.components;

import com.mna.api.affinity.Affinity;
import com.mna.api.faction.IFaction;
import com.mna.api.spells.SpellPartTags;
import com.mna.api.spells.SpellReagent;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.attributes.AttributeValuePair;
import com.mna.blocks.BlockInit;
import com.mna.effects.EffectInit;
import com.mna.factions.Factions;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ComponentLevitation extends PotionEffectComponent {

    List<SpellReagent> permanenceReagents;

    public ComponentLevitation(ResourceLocation guiIcon) {
        super(guiIcon, EffectInit.LEVITATION, new AttributeValuePair(Attribute.DURATION, 120.0F, 30.0F, 600.0F, 30.0F, 10.0F));
        this.addReagent(new ItemStack(Items.FEATHER), false, false, true, new IFaction[] { Factions.FEY });
        this.addPermanencyReagent(new ItemStack(BlockInit.TARMA_ROOT.get()), new IFaction[] { Factions.FEY });
        this.makePermanentFor(new IFaction[] { Factions.FEY });
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
    public SpellPartTags getUseTag() {
        return SpellPartTags.FRIENDLY;
    }

    @Override
    public float initialComplexity() {
        return 20.0F;
    }
}