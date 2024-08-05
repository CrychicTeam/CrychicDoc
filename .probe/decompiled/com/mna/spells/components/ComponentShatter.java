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
import com.mna.particles.types.movers.ParticleVelocityMover;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

public class ComponentShatter extends SpellEffect {

    public ComponentShatter(ResourceLocation guiIcon) {
        super(guiIcon, new AttributeValuePair(Attribute.DAMAGE, 10.0F, 10.0F, 40.0F, 5.0F, 7.0F));
    }

    @Override
    public int requiredXPForRote() {
        return 200;
    }

    @Override
    public ComponentApplicationResult ApplyEffect(SpellSource source, SpellTarget target, IModifiedSpellPart<SpellEffect> modificationData, SpellContext context) {
        if (!target.isLivingEntity()) {
            return ComponentApplicationResult.FAIL;
        } else {
            LivingEntity living = target.getLivingEntity();
            if (living.m_146890_()) {
                living.m_146917_(0);
                float damage = modificationData.getValue(Attribute.DAMAGE) * GeneralConfig.getDamageMultiplier();
                living.hurt(DamageHelper.createSourcedType(DamageHelper.FROST, context.getLevel().registryAccess(), source.getCaster()), damage);
                context.getServerLevel().m_6263_(null, living.m_20185_(), living.m_20186_(), living.m_20189_(), SFX.Spell.Impact.Single.ICE, SoundSource.HOSTILE, 1.0F, 1.0F);
                context.getServerLevel().m_6263_(null, living.m_20185_(), living.m_20186_(), living.m_20189_(), SoundEvents.GLASS_BREAK, SoundSource.HOSTILE, 1.0F, 1.0F);
                return ComponentApplicationResult.SUCCESS;
            } else {
                return ComponentApplicationResult.FAIL;
            }
        }
    }

    @Override
    public void SpawnParticles(Level world, Vec3 impact_position, Vec3 normal, int age, LivingEntity caster, ISpellDefinition recipe) {
        if (age == 0) {
            RandomSource rnd = world.getRandom();
            ItemStack ice = new ItemStack(Blocks.ICE);
            for (int i = 0; i < 25; i++) {
                double dx = (-0.5 + rnd.nextGaussian()) * 0.25;
                double dy = 0.25 + 0.5 * rnd.nextDouble();
                double dz = (-0.5 + rnd.nextGaussian()) * 0.25;
                world.addParticle(new MAParticleType(ParticleInit.ITEM.get()).setStack(ice).setMover(new ParticleVelocityMover(dx, dy, dz, false)).setGravity(0.05F).setMaxAge(30).setPhysics(true), impact_position.x, impact_position.y + rnd.nextGaussian(), impact_position.z, dx, dy, dz);
            }
        }
    }

    @Override
    public boolean targetsBlocks() {
        return false;
    }

    @Override
    public Affinity getAffinity() {
        return Affinity.ICE;
    }

    @Override
    public float initialComplexity() {
        return 20.0F;
    }

    @Override
    public SpellPartTags getUseTag() {
        return SpellPartTags.HARMFUL;
    }
}