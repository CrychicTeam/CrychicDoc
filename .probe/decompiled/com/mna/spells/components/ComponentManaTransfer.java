package com.mna.spells.components;

import com.mna.api.affinity.Affinity;
import com.mna.api.entities.construct.ConstructCapability;
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
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.entities.constructs.animated.Construct;
import com.mna.factions.Factions;
import java.util.Arrays;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.mutable.MutableBoolean;

public class ComponentManaTransfer extends SpellEffect {

    public ComponentManaTransfer(ResourceLocation guiIcon) {
        super(guiIcon, new AttributeValuePair(Attribute.MAGNITUDE, 100.0F, 100.0F, 1000.0F, 100.0F, 10.0F));
    }

    @Override
    public ComponentApplicationResult ApplyEffect(SpellSource source, SpellTarget target, IModifiedSpellPart<SpellEffect> modificationData, SpellContext context) {
        MutableBoolean success = new MutableBoolean(false);
        if (source.hasCasterReference() && source.isPlayerCaster() && target.isLivingEntity()) {
            Player caster = source.getPlayer();
            caster.getCapability(PlayerMagicProvider.MAGIC).ifPresent(cm -> {
                float manaTransferred = Math.min(modificationData.getValue(Attribute.MAGNITUDE), cm.getCastingResource().getAmount());
                if (target.getEntity() instanceof Player) {
                    Player targetPlayer = (Player) target.getEntity();
                    if (caster != targetPlayer) {
                        targetPlayer.getCapability(PlayerMagicProvider.MAGIC).ifPresent(tm -> {
                            cm.getCastingResource().consume(caster, manaTransferred);
                            tm.getCastingResource().restore(manaTransferred);
                        });
                    }
                    success.setTrue();
                } else if (target.getEntity() instanceof Construct) {
                    Construct construct = (Construct) target.getEntity();
                    if (construct.getConstructData().areCapabilitiesEnabled(ConstructCapability.STORE_MANA)) {
                        construct.adjustMana(manaTransferred);
                        success.setTrue();
                    }
                }
            });
        }
        return success.isTrue() ? ComponentApplicationResult.SUCCESS : ComponentApplicationResult.FAIL;
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
        return 10.0F;
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
        return 100;
    }

    @Override
    public SpellPartTags getUseTag() {
        return SpellPartTags.FRIENDLY;
    }

    @Override
    public List<Affinity> getValidTinkerAffinities() {
        return Arrays.asList(Affinity.ARCANE, Affinity.WATER, Affinity.WIND, Affinity.LIGHTNING);
    }
}