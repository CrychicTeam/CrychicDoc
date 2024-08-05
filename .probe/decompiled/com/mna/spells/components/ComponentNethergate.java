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
import com.mna.api.timing.DelayedEventQueue;
import com.mna.events.delayed.DelayedDimensionTeleportEvent;
import com.mna.factions.Factions;
import java.util.Arrays;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ComponentNethergate extends SpellEffect {

    public ComponentNethergate(ResourceLocation guiIcon) {
        super(guiIcon, new AttributeValuePair(Attribute.MAGNITUDE, 1.0F, 1.0F, 4.0F, 1.0F, 10.0F));
    }

    @Override
    public int requiredXPForRote() {
        return 100;
    }

    @Override
    public ComponentApplicationResult ApplyEffect(SpellSource source, SpellTarget target, IModifiedSpellPart<SpellEffect> modificationData, SpellContext context) {
        if (!source.isPlayerCaster()) {
            return ComponentApplicationResult.FAIL;
        } else if (target.isLivingEntity() && target.getLivingEntity().isAlive() && target.getEntity().canChangeDimensions()) {
            int magnitude = (int) modificationData.getValue(Attribute.MAGNITUDE);
            if (this.casterTeamCheck(source, target) && this.magnitudeHealthCheck(source, target, magnitude, 20)) {
                MinecraftServer server = context.getServerLevel().getServer();
                ServerLevel origin = context.getServerLevel();
                ServerLevel destination = null;
                if (context.getServerLevel().m_46472_() == Level.OVERWORLD) {
                    destination = server.getLevel(Level.NETHER);
                } else {
                    if (context.getServerLevel().m_46472_() != Level.NETHER) {
                        return ComponentApplicationResult.FAIL;
                    }
                    destination = server.getLevel(Level.OVERWORLD);
                }
                DelayedEventQueue.pushEvent(origin, new DelayedDimensionTeleportEvent(target.getLivingEntity(), origin, destination));
                return ComponentApplicationResult.SUCCESS;
            } else {
                return ComponentApplicationResult.FAIL;
            }
        } else {
            return ComponentApplicationResult.FAIL;
        }
    }

    @Override
    public void SpawnParticles(Level world, Vec3 impact_position, Vec3 normal, int age, LivingEntity caster, ISpellDefinition recipe) {
        if (age == 0) {
            float particle_spread = 1.0F;
            float v = 1.0F;
            int particleCount = 25;
            for (int i = 0; i < particleCount; i++) {
                Vec3 velocity = new Vec3(0.0, Math.random() * (double) v, 0.0);
                world.addParticle(recipe.colorParticle(new MAParticleType(ParticleInit.ENDER_VELOCITY.get()), caster), impact_position.x + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, impact_position.y + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, impact_position.z + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, velocity.x, velocity.y, velocity.z);
            }
        }
    }

    @Override
    public Affinity getAffinity() {
        return Affinity.ENDER;
    }

    @Override
    public float initialComplexity() {
        return 50.0F;
    }

    @Override
    public IFaction getFactionRequirement() {
        return Factions.DEMONS;
    }

    @Override
    public SpellPartTags getUseTag() {
        return SpellPartTags.SELF;
    }

    @Override
    public List<Affinity> getValidTinkerAffinities() {
        return Arrays.asList(Affinity.ENDER, Affinity.FIRE, Affinity.LIGHTNING);
    }
}