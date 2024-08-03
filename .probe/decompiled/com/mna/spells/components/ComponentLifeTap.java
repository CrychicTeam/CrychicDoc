package com.mna.spells.components;

import com.mna.api.affinity.Affinity;
import com.mna.api.faction.IFaction;
import com.mna.api.sound.SFX;
import com.mna.api.spells.SpellPartTags;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.attributes.AttributeValuePair;
import com.mna.api.spells.base.ISpellDefinition;
import com.mna.effects.EffectInit;
import com.mna.factions.Factions;
import java.util.Arrays;
import java.util.List;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ComponentLifeTap extends PotionEffectComponent {

    public ComponentLifeTap(ResourceLocation guiIcon) {
        super(guiIcon, EffectInit.LIFE_TAP, new AttributeValuePair(Attribute.DURATION, 15.0F, 5.0F, 45.0F, 5.0F, 2.0F), new AttributeValuePair(Attribute.LESSER_MAGNITUDE, 1.0F, 1.0F, 4.0F, 1.0F, 10.0F));
    }

    @Override
    public SoundEvent SoundEffect() {
        return SFX.Spell.Buff.ENDER;
    }

    @Override
    public Affinity getAffinity() {
        return Affinity.ENDER;
    }

    @Override
    public void SpawnParticles(Level world, Vec3 impact_position, Vec3 normal, int age, LivingEntity caster, ISpellDefinition recipe) {
        if (age <= 1) {
            float particle_spread = 1.0F;
            float v = 0.4F;
            int particleCount = 3;
            for (int i = 0; i < particleCount; i++) {
                Vec3 velocity = new Vec3(0.0, -Math.random() * (double) v, 0.0);
                world.addParticle(ParticleTypes.ANGRY_VILLAGER, impact_position.x + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, impact_position.y + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, impact_position.z + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, velocity.x, velocity.y, velocity.z);
            }
        }
    }

    @Override
    public boolean canBeChanneled() {
        return false;
    }

    @Override
    public float initialComplexity() {
        return 25.0F;
    }

    @Override
    public boolean targetsBlocks() {
        return false;
    }

    @Override
    public IFaction getFactionRequirement() {
        return Factions.DEMONS;
    }

    @Override
    public int requiredXPForRote() {
        return 100;
    }

    @Override
    public SpellPartTags getUseTag() {
        return SpellPartTags.HARMFUL;
    }

    @Override
    public List<Affinity> getValidTinkerAffinities() {
        return Arrays.asList(Affinity.ENDER, Affinity.WATER, Affinity.ICE, Affinity.FIRE, Affinity.LIGHTNING);
    }
}