package com.mna.spells.components;

import com.mna.api.affinity.Affinity;
import com.mna.api.faction.IFaction;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.api.spells.ComponentApplicationResult;
import com.mna.api.spells.SpellPartTags;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.attributes.AttributeValuePair;
import com.mna.api.spells.base.IDamageComponent;
import com.mna.api.spells.base.IModifiedSpellPart;
import com.mna.api.spells.base.ISpellDefinition;
import com.mna.api.spells.parts.SpellEffect;
import com.mna.api.spells.targeting.SpellContext;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import com.mna.config.GeneralConfig;
import com.mna.entities.utility.MAExplosion;
import com.mna.factions.Factions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ComponentExplosion extends SpellEffect implements IDamageComponent {

    public ComponentExplosion(ResourceLocation guiIcon) {
        super(guiIcon, new AttributeValuePair(Attribute.RADIUS, 1.0F, 1.0F, 10.0F, 1.0F, 4.0F), new AttributeValuePair(Attribute.DAMAGE, 10.0F, 0.0F, 30.0F, 1.0F, 4.0F), new AttributeValuePair(Attribute.PRECISION, 0.0F, 0.0F, 3.0F, 1.0F, 20.0F));
    }

    @Override
    public ComponentApplicationResult ApplyEffect(SpellSource source, SpellTarget target, IModifiedSpellPart<SpellEffect> modificationData, SpellContext context) {
        if (context.countAffectedBlocks(this) <= 0 && source.getCaster() != null) {
            int precisionInt = (int) modificationData.getValue(Attribute.PRECISION);
            Vec3 impact = context.getSpawnedTargetEntity() != null ? context.getSpawnedTargetEntity().position() : target.getPosition();
            if (MAExplosion.make(source.getCaster(), context.getServerLevel(), impact.x, impact.y, impact.z, modificationData.getValue(Attribute.RADIUS), modificationData.getValue(Attribute.DAMAGE) * GeneralConfig.getDamageMultiplier(), (precisionInt & 1) != 1, (precisionInt & 2) == 2 ? Explosion.BlockInteraction.KEEP : (context.getServerLevel().getServer().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING) ? Explosion.BlockInteraction.DESTROY : Explosion.BlockInteraction.KEEP), source.getCaster().m_269291_().explosion(source.getCaster(), null)) == null) {
                return ComponentApplicationResult.FAIL;
            } else {
                context.addAffectedBlock(this, target.getBlock());
                return ComponentApplicationResult.SUCCESS;
            }
        } else {
            return ComponentApplicationResult.FAIL;
        }
    }

    @Override
    public SoundEvent SoundEffect() {
        return null;
    }

    @Override
    public Affinity getAffinity() {
        return Affinity.FIRE;
    }

    @Override
    public void SpawnParticles(Level world, Vec3 impact_position, Vec3 normal, int age, LivingEntity caster, ISpellDefinition recipe) {
        if (age <= 10) {
            float particle_spread = 1.0F;
            float v = 0.4F;
            int particleCount = 10;
            for (int i = 0; i < particleCount; i++) {
                Vec3 velocity = new Vec3(0.0, Math.random() * (double) v, 0.0);
                MAParticleType mpt = recipe.colorParticle(new MAParticleType(world.random.nextBoolean() ? ParticleInit.EARTH.get() : ParticleInit.DUST.get()), caster);
                world.addParticle(mpt, impact_position.x + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, impact_position.y + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, impact_position.z + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, velocity.x, velocity.y, velocity.z);
            }
        }
    }

    @Override
    public float initialComplexity() {
        return 25.0F;
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
        return Factions.DEMONS;
    }

    @Override
    public int requiredXPForRote() {
        return 500;
    }

    @Override
    public SpellPartTags getUseTag() {
        return SpellPartTags.HARMFUL;
    }
}