package com.mna.spells.components;

import com.mna.api.affinity.Affinity;
import com.mna.api.faction.IFaction;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.api.spells.ComponentApplicationResult;
import com.mna.api.spells.SpellPartTags;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.attributes.AttributeValuePair;
import com.mna.api.spells.base.IModifiedSpellPart;
import com.mna.api.spells.base.ISpellDefinition;
import com.mna.api.spells.parts.SpellEffect;
import com.mna.api.spells.targeting.SpellContext;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import com.mna.factions.Factions;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ComponentWindShear extends SpellEffect {

    public ComponentWindShear(ResourceLocation guiIcon) {
        super(guiIcon, new AttributeValuePair(Attribute.HEIGHT, 1.0F, 1.0F, 15.0F, 1.0F, 0.5F), new AttributeValuePair(Attribute.PRECISION, 5.0F, 1.0F, 5.0F, 0.5F, 1.5F));
    }

    @Override
    public int requiredXPForRote() {
        return 250;
    }

    @Override
    public ComponentApplicationResult ApplyEffect(SpellSource source, SpellTarget target, IModifiedSpellPart<SpellEffect> modificationData, SpellContext context) {
        if (!target.isBlock() && target.isLivingEntity() && !target.getLivingEntity().m_20096_() && source.hasCasterReference()) {
            float precision = Math.max(modificationData.getValue(Attribute.PRECISION), 1.0F);
            float precisionMax = modificationData.getMaximumValue(Attribute.PRECISION);
            float precisionMin = modificationData.getMinimumValue(Attribute.PRECISION);
            float heightTarget = modificationData.getValue(Attribute.HEIGHT);
            float heightMaximum = modificationData.getMaximumValue(Attribute.HEIGHT);
            float heightMinimum = modificationData.getMinimumValue(Attribute.HEIGHT);
            float precisionPct = (precision - precisionMin) / (precisionMax - precisionMin);
            float heightPct = (heightTarget - heightMinimum) / (heightMaximum - heightMinimum);
            float baselineDamage = 5.0F;
            float maximumVariableDamage = 5.0F + 20.0F * precisionPct;
            int distanceToGround = 0;
            for (BlockPos startPos = target.getLivingEntity().m_20183_(); startPos.m_123342_() > context.getServerLevel().m_141937_() && context.getServerLevel().m_8055_(startPos).m_60795_(); startPos = startPos.below()) {
                distanceToGround++;
            }
            if (distanceToGround == 0) {
                return ComponentApplicationResult.FAIL;
            } else {
                float damageMultiplier = 0.05F + 0.95F * heightPct;
                float decay = 0.7F;
                int rangeBands = (int) Math.floor((double) (Math.abs((float) distanceToGround - heightTarget) / precision));
                float damage = (baselineDamage + maximumVariableDamage * (float) Math.pow((double) decay, (double) rangeBands)) * damageMultiplier;
                target.getLivingEntity().hurt(target.getLivingEntity().m_269291_().mobAttack(source.getCaster()), damage);
                return ComponentApplicationResult.SUCCESS;
            }
        } else {
            return ComponentApplicationResult.FAIL;
        }
    }

    @Override
    public boolean targetsBlocks() {
        return false;
    }

    @Override
    public Affinity getAffinity() {
        return Affinity.WIND;
    }

    @Override
    public SpellPartTags getUseTag() {
        return SpellPartTags.HARMFUL;
    }

    @Override
    public IFaction getFactionRequirement() {
        return Factions.FEY;
    }

    @Override
    public float initialComplexity() {
        return 15.0F;
    }

    @Override
    public void SpawnParticles(Level world, Vec3 impact_position, Vec3 normal, int age, LivingEntity caster, ISpellDefinition recipe) {
        if (age <= 10) {
            for (int i = 0; i < 3; i++) {
                Vec3 offset = new Vec3(-1.0 + Math.random() * 2.0, 0.0, -1.0 + Math.random() * 2.0);
                Vec3 velocity = offset.normalize().scale(-0.5);
                world.addParticle(new MAParticleType(ParticleInit.AIR_VELOCITY.get()).setScale(0.15F).setColor(30, 30, 30), impact_position.x + offset.x, impact_position.y + 0.5, impact_position.z + offset.z, velocity.x, velocity.y, velocity.z);
            }
        }
    }
}