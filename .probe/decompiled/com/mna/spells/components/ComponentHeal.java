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
import com.mna.config.GeneralConfig;
import com.mna.factions.Factions;
import java.util.Arrays;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ComponentHeal extends SpellEffect {

    public ComponentHeal(ResourceLocation guiIcon) {
        super(guiIcon, new AttributeValuePair(Attribute.MAGNITUDE, 1.0F, 1.0F, 20.0F, 1.0F, 7.5F));
    }

    @Override
    public ComponentApplicationResult ApplyEffect(SpellSource source, SpellTarget target, IModifiedSpellPart<SpellEffect> modificationData, SpellContext context) {
        if (target.isEntity() && target.getEntity() instanceof LivingEntity) {
            float magnitude = modificationData.getValue(Attribute.MAGNITUDE);
            if (target.getLivingEntity().getMobType() == MobType.UNDEAD) {
                target.getLivingEntity().hurt(target.getLivingEntity().m_269291_().magic(), magnitude * GeneralConfig.getDamageMultiplier());
            } else {
                target.getLivingEntity().heal(magnitude);
            }
            if (magnitude >= 15.0F && target.getEntity() instanceof ZombieVillager) {
                ((ZombieVillager) target.getEntity()).startConverting(source.isPlayerCaster() ? source.getPlayer().m_20148_() : null, 20);
            }
            return ComponentApplicationResult.SUCCESS;
        } else {
            return ComponentApplicationResult.FAIL;
        }
    }

    @Override
    public Affinity getAffinity() {
        return Affinity.EARTH;
    }

    @Override
    public void SpawnParticles(Level world, Vec3 impact_position, Vec3 normal, int age, LivingEntity caster, ISpellDefinition recipe) {
        if (age <= 10) {
            float particle_spread = 1.0F;
            float v = 0.4F;
            int particleCount = 10;
            for (int i = 0; i < particleCount; i++) {
                Vec3 velocity = new Vec3(0.0, Math.random() * (double) v, 0.0);
                world.addParticle(recipe.colorParticle(new MAParticleType(ParticleInit.EARTH.get()), caster), impact_position.x + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, impact_position.y + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, impact_position.z + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, velocity.x, velocity.y, velocity.z);
            }
        }
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
    public IFaction getFactionRequirement() {
        return Factions.FEY;
    }

    @Override
    public int requiredXPForRote() {
        return 100;
    }

    @Override
    public SpellPartTags getUseTag() {
        return SpellPartTags.FRIENDLY;
    }

    @Override
    public List<Affinity> getValidTinkerAffinities() {
        return Arrays.asList(Affinity.ARCANE, Affinity.EARTH, Affinity.WATER);
    }

    @Override
    public boolean isHellfireBoosted(Attribute attr) {
        return false;
    }
}