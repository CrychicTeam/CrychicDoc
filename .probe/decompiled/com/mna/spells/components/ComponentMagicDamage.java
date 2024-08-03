package com.mna.spells.components;

import com.mna.api.affinity.Affinity;
import com.mna.api.entities.DamageHelper;
import com.mna.api.faction.IFaction;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.api.sound.SFX;
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
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.config.GeneralConfig;
import com.mna.factions.Factions;
import com.mna.tools.SummonUtils;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ComponentMagicDamage extends SpellEffect implements IDamageComponent {

    public ComponentMagicDamage(ResourceLocation icon) {
        super(icon, new AttributeValuePair(Attribute.DAMAGE, 5.0F, 1.0F, 20.0F, 0.5F, 3.0F));
    }

    @Override
    public ComponentApplicationResult ApplyEffect(SpellSource source, SpellTarget target, IModifiedSpellPart<SpellEffect> modificationData, SpellContext context) {
        if (target.isLivingEntity() && !(target.getEntity() instanceof ItemEntity) && !(target.getEntity() instanceof ExperienceOrb)) {
            float damage = modificationData.getValue(Attribute.DAMAGE) * GeneralConfig.getDamageMultiplier();
            if (SummonUtils.isSummon(target.getLivingEntity())) {
                damage *= 2.0F;
            }
            target.getEntity().hurt(DamageHelper.createSourcedType(DamageTypes.MAGIC, context.getLevel().registryAccess(), source.getCaster()), damage);
            if (target.getEntity() instanceof Player) {
                ((Player) target.getEntity()).getCapability(PlayerMagicProvider.MAGIC).ifPresent(m -> m.getCastingResource().consume((Player) target.getEntity(), modificationData.getValue(Attribute.DAMAGE) * 5.0F * GeneralConfig.getDamageMultiplier()));
            }
            return ComponentApplicationResult.SUCCESS;
        } else {
            return ComponentApplicationResult.FAIL;
        }
    }

    @Override
    public void SpawnParticles(Level world, Vec3 impact_position, Vec3 normal, int age, LivingEntity caster, ISpellDefinition recipe) {
        Random rndm = new Random(1234L);
        if (age < 5) {
            for (int i = 0; i < 25; i++) {
                world.addParticle(recipe.colorParticle(new MAParticleType(ParticleInit.ARCANE_RANDOM.get()), caster), impact_position.x + (double) rndm.nextFloat() - 0.5, impact_position.y + (double) rndm.nextFloat() - 0.5, impact_position.z + (double) rndm.nextFloat() - 0.5, 0.0, 0.0, 0.0);
            }
        }
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
    public SoundEvent SoundEffect() {
        return SFX.Spell.Impact.Single.ARCANE;
    }

    @Override
    public Affinity getAffinity() {
        return Affinity.ARCANE;
    }

    @Override
    public float initialComplexity() {
        return 5.0F;
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
        return Arrays.asList(Affinity.ARCANE, Affinity.ENDER);
    }
}