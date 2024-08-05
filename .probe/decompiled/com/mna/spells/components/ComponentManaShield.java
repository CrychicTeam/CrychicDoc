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
import com.mna.effects.EffectInit;
import com.mna.factions.Factions;
import java.util.Arrays;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ComponentManaShield extends PotionEffectComponent {

    public static final String NBT_SHIELD_SOURCE_ID = "mana_shield_source_id";

    public static final String NBT_SHIELD_RANGE = "mana_shield_range";

    public ComponentManaShield(ResourceLocation guiIcon) {
        super(guiIcon, EffectInit.MANA_SHIELD, new AttributeValuePair(Attribute.DURATION, 120.0F, 30.0F, 600.0F, 30.0F, 5.0F), new AttributeValuePair(Attribute.MAGNITUDE, 1.0F, 1.0F, 5.0F, 1.0F, 4.0F), new AttributeValuePair(Attribute.RANGE, 16.0F, 16.0F, 64.0F, 16.0F, 5.0F));
    }

    @Override
    public ComponentApplicationResult ApplyEffect(SpellSource source, SpellTarget target, IModifiedSpellPart<SpellEffect> modificationData, SpellContext context) {
        if (!source.isPlayerCaster()) {
            return ComponentApplicationResult.FAIL;
        } else {
            ComponentApplicationResult superResult = super.ApplyEffect(source, target, modificationData, context);
            if (superResult.is_success) {
                target.getLivingEntity().getPersistentData().putInt("mana_shield_source_id", source.getPlayer().m_19879_());
                target.getLivingEntity().getPersistentData().putFloat("mana_shield_range", modificationData.getValue(Attribute.RANGE));
            }
            return superResult;
        }
    }

    @Override
    public SoundEvent SoundEffect() {
        return SFX.Spell.Buff.ARCANE;
    }

    @Override
    public Affinity getAffinity() {
        return Affinity.ARCANE;
    }

    @Override
    public void SpawnParticles(Level world, Vec3 impact_position, Vec3 normal, int age, LivingEntity caster, ISpellDefinition recipe) {
        if (age <= 10) {
            float particle_spread = 1.0F;
            float v = 0.4F;
            int particleCount = 10;
            for (int i = 0; i < particleCount; i++) {
                Vec3 velocity = new Vec3(0.0, -Math.random() * (double) v, 0.0);
                world.addParticle(recipe.colorParticle(new MAParticleType(ParticleInit.ARCANE.get()), caster), impact_position.x + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, impact_position.y + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, impact_position.z + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, velocity.x, velocity.y, velocity.z);
            }
        }
    }

    @Override
    public boolean canBeChanneled() {
        return true;
    }

    @Override
    public float initialComplexity() {
        return 20.0F;
    }

    @Override
    public boolean targetsBlocks() {
        return false;
    }

    @Override
    public IFaction getFactionRequirement() {
        return Factions.COUNCIL;
    }

    @Override
    public int requiredXPForRote() {
        return 200;
    }

    @Override
    public SpellPartTags getUseTag() {
        return SpellPartTags.FRIENDLY;
    }

    @Override
    public List<Affinity> getValidTinkerAffinities() {
        return Arrays.asList(Affinity.ARCANE);
    }
}