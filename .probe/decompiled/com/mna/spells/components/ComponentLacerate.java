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
import com.mna.effects.EffectInit;
import com.mna.factions.Factions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

public class ComponentLacerate extends PotionEffectComponent {

    public ComponentLacerate(ResourceLocation guiIcon) {
        super(guiIcon, EffectInit.LACERATE, new AttributeValuePair(Attribute.DURATION, 5.0F, 1.0F, 10.0F, 3.0F, 10.0F), new AttributeValuePair(Attribute.MAGNITUDE, 1.0F, 1.0F, 3.0F, 1.0F, 20.0F));
    }

    @Override
    public ComponentApplicationResult ApplyEffect(SpellSource source, SpellTarget target, IModifiedSpellPart<SpellEffect> modificationData, SpellContext context) {
        if (target.isLivingEntity() && target.getLivingEntity().hasEffect(MobEffects.REGENERATION)) {
            MobEffectInstance activeEffect = target.getLivingEntity().getEffect(MobEffects.REGENERATION);
            if (!activeEffect.isAmbient()) {
                int newAmp = activeEffect.getAmplifier() - (int) modificationData.getValue(Attribute.MAGNITUDE);
                int remDir = activeEffect.getDuration();
                target.getLivingEntity().removeEffect(MobEffects.REGENERATION);
                if (newAmp >= 0) {
                    target.getLivingEntity().addEffect(new MobEffectInstance(MobEffects.REGENERATION, remDir, newAmp));
                }
                return ComponentApplicationResult.SUCCESS;
            }
        }
        return super.ApplyEffect(source, target, modificationData, context);
    }

    @Override
    public boolean canBeChanneled() {
        return false;
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
    public IFaction getFactionRequirement() {
        return Factions.UNDEAD;
    }

    @Override
    public SpellPartTags getUseTag() {
        return SpellPartTags.HARMFUL;
    }

    @Override
    public float initialComplexity() {
        return 30.0F;
    }

    @Override
    public boolean isSilverSpell() {
        return true;
    }
}