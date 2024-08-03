package com.mna.spells.components;

import com.mna.api.affinity.Affinity;
import com.mna.api.faction.IFaction;
import com.mna.api.spells.ComponentApplicationResult;
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

public class ComponentLifeDrain extends SpellEffect {

    public ComponentLifeDrain(ResourceLocation guiIcon) {
        super(guiIcon, new AttributeValuePair(Attribute.DAMAGE, 4.0F, 4.0F, 10.0F, 1.0F, 10.0F));
    }

    @Override
    public ComponentApplicationResult ApplyEffect(SpellSource source, SpellTarget target, IModifiedSpellPart<SpellEffect> modificationData, SpellContext context) {
        if (!target.isLivingEntity()) {
            return ComponentApplicationResult.FAIL;
        } else {
            LivingEntity tgt = target.getLivingEntity();
            float preHP = tgt.getHealth();
            if (tgt.hurt(tgt.m_269291_().starve(), modificationData.getValue(Attribute.DAMAGE) * GeneralConfig.getDamageMultiplier())) {
                float postHP = tgt.getHealth();
                float delta = preHP - postHP;
                source.getCaster().heal(delta / 2.0F);
            }
            return ComponentApplicationResult.SUCCESS;
        }
    }

    @Override
    public Affinity getAffinity() {
        return Affinity.HELLFIRE;
    }

    @Override
    public float initialComplexity() {
        return 50.0F;
    }

    @Override
    public int requiredXPForRote() {
        return 200;
    }

    @Override
    public boolean canBeChanneled() {
        return false;
    }

    @Override
    public IFaction getFactionRequirement() {
        return Factions.DEMONS;
    }

    @Override
    public boolean isSilverSpell() {
        return true;
    }

    @Override
    public boolean targetsBlocks() {
        return false;
    }
}