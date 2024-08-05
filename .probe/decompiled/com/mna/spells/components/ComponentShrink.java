package com.mna.spells.components;

import com.mna.api.affinity.Affinity;
import com.mna.api.faction.IFaction;
import com.mna.api.spells.ComponentApplicationResult;
import com.mna.api.spells.SpellPartTags;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.attributes.AttributeValuePair;
import com.mna.api.spells.base.IModifiedSpellPart;
import com.mna.api.spells.parts.SpellEffect;
import com.mna.api.spells.targeting.SpellContext;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import com.mna.blocks.BlockInit;
import com.mna.effects.EffectInit;
import com.mna.factions.Factions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class ComponentShrink extends PotionEffectComponent {

    public ComponentShrink(ResourceLocation guiIcon) {
        super(guiIcon, EffectInit.REDUCE, new AttributeValuePair(Attribute.DURATION, 120.0F, 30.0F, 600.0F, 30.0F, 5.0F), new AttributeValuePair(Attribute.LESSER_MAGNITUDE, 3.0F, 1.0F, 9.0F, 1.0F, 5.0F), new AttributeValuePair(Attribute.MAGNITUDE, 1.0F, 1.0F, 4.0F, 1.0F, 20.0F));
        this.addPermanencyReagent(new ItemStack(BlockInit.CERUBLOSSOM.get()), new IFaction[] { Factions.FEY });
        this.makePermanentFor(new IFaction[] { Factions.FEY });
    }

    @Override
    public ComponentApplicationResult ApplyEffect(SpellSource source, SpellTarget target, IModifiedSpellPart<SpellEffect> modificationData, SpellContext context) {
        if (source.getCaster() != target.getEntity()) {
            int range = (int) modificationData.getValue(Attribute.MAGNITUDE);
            if (!this.casterTeamCheck(source, target) || !this.magnitudeHealthCheck(source, target, range, 20)) {
                return ComponentApplicationResult.FAIL;
            }
        }
        return super.ApplyEffect(source, target, modificationData, context);
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
        return 40.0F;
    }

    @Override
    public boolean targetsBlocks() {
        return false;
    }

    @Override
    public SpellPartTags getUseTag() {
        return SpellPartTags.SELF;
    }
}