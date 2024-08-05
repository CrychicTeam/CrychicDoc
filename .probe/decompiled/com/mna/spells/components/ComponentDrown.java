package com.mna.spells.components;

import com.mna.api.affinity.Affinity;
import com.mna.api.entities.DamageHelper;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.api.sound.SFX;
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
import com.mna.config.GeneralConfig;
import com.mna.effects.EffectInit;
import java.util.Arrays;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;

public class ComponentDrown extends SpellEffect {

    public ComponentDrown(ResourceLocation guiIcon) {
        super(guiIcon, new AttributeValuePair(Attribute.MAGNITUDE, 1.0F, 1.0F, 3.0F, 1.0F, 15.0F), new AttributeValuePair(Attribute.DAMAGE, 10.0F, 10.0F, 30.0F, 1.0F, 2.0F), new AttributeValuePair(Attribute.DURATION, 5.0F, 2.0F, 5.0F, 1.0F, 10.0F));
    }

    @Override
    public ComponentApplicationResult ApplyEffect(SpellSource source, SpellTarget target, IModifiedSpellPart<SpellEffect> modificationData, SpellContext context) {
        if (target.isLivingEntity() && !target.getLivingEntity().canDrownInFluidType(ForgeMod.WATER_TYPE.get()) && !MobEffectUtil.hasWaterBreathing(target.getLivingEntity())) {
            int magnitude = (int) modificationData.getValue(Attribute.MAGNITUDE);
            int air_per_magnitude = (int) ((float) target.getLivingEntity().m_6062_() * 0.2F);
            int air_removed = air_per_magnitude * magnitude;
            target.getLivingEntity().m_20301_(target.getLivingEntity().m_20146_() - air_removed);
            if (target.getLivingEntity().m_20146_() <= 0 && !target.getLivingEntity().hasEffect(MobEffects.WATER_BREATHING)) {
                target.getLivingEntity().hurt(DamageHelper.createSourcedType(DamageTypes.DROWN, context.getLevel().registryAccess(), source.getCaster()), modificationData.getValue(Attribute.DAMAGE) * GeneralConfig.getDamageMultiplier());
            }
            target.getLivingEntity().addEffect(new MobEffectInstance(EffectInit.ASPHYXIATE.get(), (int) modificationData.getValue(Attribute.DURATION) * 20, 0));
            return ComponentApplicationResult.SUCCESS;
        } else {
            return ComponentApplicationResult.FAIL;
        }
    }

    @Override
    public Affinity getAffinity() {
        return Affinity.WATER;
    }

    @Override
    public float initialComplexity() {
        return 40.0F;
    }

    @Override
    public SoundEvent SoundEffect() {
        return SFX.Spell.Impact.Single.WATER;
    }

    @Override
    public void SpawnParticles(Level world, Vec3 impact_position, Vec3 normal, int age, LivingEntity caster, ISpellDefinition recipe) {
        if (age <= 10) {
            float particle_spread = 1.0F;
            float v = 1.0F;
            int particleCount = 10;
            for (int i = 0; i < particleCount; i++) {
                Vec3 velocity = new Vec3(0.0, Math.random() * (double) v, 0.0);
                world.addParticle(recipe.colorParticle(new MAParticleType(ParticleInit.WATER.get()), caster), impact_position.x + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, impact_position.y + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, impact_position.z + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, velocity.x, velocity.y, velocity.z);
            }
        }
    }

    @Override
    public boolean targetsBlocks() {
        return false;
    }

    @Override
    public int requiredXPForRote() {
        return 500;
    }

    @Override
    public SpellPartTags getUseTag() {
        return SpellPartTags.HARMFUL;
    }

    @Override
    public List<Affinity> getValidTinkerAffinities() {
        return Arrays.asList(Affinity.ENDER, Affinity.WATER, Affinity.ICE);
    }
}