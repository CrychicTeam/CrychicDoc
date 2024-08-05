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
import com.mna.config.GeneralConfig;
import com.mna.factions.Factions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class ComponentBackdraft extends SpellEffect {

    public ComponentBackdraft(ResourceLocation guiIcon) {
        super(guiIcon, new AttributeValuePair(Attribute.RANGE, 3.0F, 1.0F, 6.0F, 0.5F, 3.5F), new AttributeValuePair(Attribute.DAMAGE, 0.5F, 0.5F, 2.0F, 0.25F, 2.0F));
    }

    @Override
    public int requiredXPForRote() {
        return 200;
    }

    @Override
    public ComponentApplicationResult ApplyEffect(SpellSource source, SpellTarget target, IModifiedSpellPart<SpellEffect> modificationData, SpellContext context) {
        if (target.isLivingEntity() && target.getLivingEntity().m_6060_()) {
            int fireTicks = target.getLivingEntity().m_20094_();
            int damageSteps = fireTicks / 20;
            target.getLivingEntity().hurt(target.getLivingEntity().m_269291_().onFire(), (float) damageSteps * modificationData.getValue(Attribute.DAMAGE) * GeneralConfig.getDamageMultiplier());
            target.getLivingEntity().m_20095_();
            int targetFireTicks = fireTicks / 2;
            context.getServerLevel().m_45976_(LivingEntity.class, target.getLivingEntity().m_20191_().inflate((double) modificationData.getValue(Attribute.RANGE))).stream().forEach(e -> {
                if (e != source.getCaster() && e != target.getLivingEntity()) {
                    if (e.m_20094_() < targetFireTicks) {
                        e.m_7311_(targetFireTicks);
                    }
                }
            });
            return ComponentApplicationResult.SUCCESS;
        } else {
            return ComponentApplicationResult.FAIL;
        }
    }

    @Override
    public Affinity getAffinity() {
        return Affinity.FIRE;
    }

    @Override
    public IFaction getFactionRequirement() {
        return Factions.DEMONS;
    }

    @Override
    public boolean targetsBlocks() {
        return false;
    }

    @Override
    public boolean canBeChanneled() {
        return false;
    }

    @Override
    public float initialComplexity() {
        return 10.0F;
    }

    @Override
    public SpellPartTags getUseTag() {
        return SpellPartTags.HARMFUL;
    }
}