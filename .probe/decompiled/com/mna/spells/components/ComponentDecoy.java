package com.mna.spells.components;

import com.mna.api.affinity.Affinity;
import com.mna.api.faction.IFaction;
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
import com.mna.entities.sorcery.EntityDecoy;
import com.mna.factions.Factions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ComponentDecoy extends SpellEffect {

    public ComponentDecoy(ResourceLocation guiIcon) {
        super(guiIcon, new AttributeValuePair(Attribute.RADIUS, 3.0F, 3.0F, 20.0F, 1.0F, 2.0F), new AttributeValuePair(Attribute.LESSER_MAGNITUDE, 1.0F, 1.0F, 10.0F, 1.0F, 2.0F), new AttributeValuePair(Attribute.SPEED, 1.0F, 0.5F, 1.5F, 0.1F, 1.0F));
    }

    @Override
    public ComponentApplicationResult ApplyEffect(SpellSource source, SpellTarget target, IModifiedSpellPart<SpellEffect> modificationData, SpellContext context) {
        if (context.countAffectedBlocks(this) <= 0 && source.isPlayerCaster()) {
            EntityDecoy decoy = new EntityDecoy(source.getPlayer(), context.getServerLevel(), target.isBlock() ? target.getPosition().add(0.0, 1.0, 0.0) : target.getPosition(), modificationData.getValue(Attribute.SPEED), modificationData.getValue(Attribute.RADIUS), modificationData.getValue(Attribute.LESSER_MAGNITUDE));
            context.getServerLevel().addFreshEntity(decoy);
            target.overrideSpellTarget(decoy);
            context.addAffectedBlock(this, target.getBlock());
            return ComponentApplicationResult.SUCCESS;
        } else {
            return ComponentApplicationResult.FAIL;
        }
    }

    @Override
    public SoundEvent SoundEffect() {
        return SFX.Spell.Cast.WIND;
    }

    @Override
    public Affinity getAffinity() {
        return Affinity.WIND;
    }

    @Override
    public void SpawnParticles(Level world, Vec3 impact_position, Vec3 normal, int age, LivingEntity caster, ISpellDefinition recipe) {
        if (age <= 10) {
            float particle_spread = 1.0F;
            float v = 0.4F;
            int particleCount = 10;
            for (int i = 0; i < particleCount; i++) {
                Vec3 velocity = new Vec3(0.0, Math.random() * (double) v, 0.0);
                world.addParticle(recipe.colorParticle(new MAParticleType(ParticleInit.AIR_VELOCITY.get()).setScale(0.2F).setColor(10, 10, 10), caster), impact_position.x + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, impact_position.y + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, impact_position.z + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, velocity.x, velocity.y, velocity.z);
            }
        }
    }

    @Override
    public float initialComplexity() {
        return 20.0F;
    }

    @Override
    public boolean targetsBlocks() {
        return true;
    }

    @Override
    public boolean canBeChanneled() {
        return false;
    }

    @Override
    public IFaction getFactionRequirement() {
        return Factions.FEY;
    }

    @Override
    public int requiredXPForRote() {
        return 300;
    }

    @Override
    public SpellPartTags getUseTag() {
        return SpellPartTags.NEUTRAL;
    }
}