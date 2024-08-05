package com.mna.spells.components;

import com.mna.api.affinity.Affinity;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.api.sound.SFX;
import com.mna.api.spells.SpellPartTags;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.attributes.AttributeValuePair;
import com.mna.api.spells.base.ISpellDefinition;
import java.util.Arrays;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ComponentSlowfall extends PotionEffectComponent {

    public ComponentSlowfall(ResourceLocation guiIcon) {
        super(guiIcon, MobEffects.SLOW_FALLING, new AttributeValuePair(Attribute.DURATION, 120.0F, 30.0F, 600.0F, 30.0F, 2.0F), new AttributeValuePair(Attribute.LESSER_MAGNITUDE, 1.0F, 1.0F, 3.0F, 1.0F, 10.0F));
    }

    @Override
    public SoundEvent SoundEffect() {
        return SFX.Spell.Buff.WIND;
    }

    @Override
    public Affinity getAffinity() {
        return Affinity.WIND;
    }

    @Override
    public void SpawnParticles(Level world, Vec3 impact_position, Vec3 normal, int age, LivingEntity caster, ISpellDefinition recipe) {
        if (age <= 10) {
            float particle_spread = 0.25F;
            int particleCount = 10;
            for (int i = 0; i < particleCount; i++) {
                world.addParticle(recipe.colorParticle(new MAParticleType(ParticleInit.AIR_ORBIT.get()).setScale(0.2F).setColor(10, 10, 10), caster), impact_position.x + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, impact_position.y, impact_position.z + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, 0.2F, 0.05F, Math.random() * 0.5);
            }
        }
    }

    @Override
    public float initialComplexity() {
        return 5.0F;
    }

    @Override
    public boolean targetsBlocks() {
        return false;
    }

    @Override
    public int requiredXPForRote() {
        return 100;
    }

    @Override
    public List<Affinity> getValidTinkerAffinities() {
        return Arrays.asList(Affinity.ARCANE, Affinity.ENDER, Affinity.WATER, Affinity.WIND, Affinity.LIGHTNING);
    }

    @Override
    public SpellPartTags getUseTag() {
        return SpellPartTags.FRIENDLY;
    }
}