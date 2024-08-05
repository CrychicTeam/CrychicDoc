package com.mna.spells.components;

import com.mna.api.affinity.Affinity;
import com.mna.api.faction.IFaction;
import com.mna.api.sound.SFX;
import com.mna.api.spells.ComponentApplicationResult;
import com.mna.api.spells.SpellPartTags;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.attributes.AttributeValuePair;
import com.mna.api.spells.base.IModifiedSpellPart;
import com.mna.api.spells.parts.SpellEffect;
import com.mna.api.spells.targeting.SpellContext;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import com.mna.entities.EntityInit;
import com.mna.entities.summon.SummonedSpectralHorse;
import com.mna.factions.Factions;
import com.mna.tools.SummonUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ComponentSummonSkeletalHorse extends SpellEffect {

    public ComponentSummonSkeletalHorse(ResourceLocation guiIcon) {
        super(guiIcon, new AttributeValuePair(Attribute.DURATION, 120.0F, 30.0F, 1200.0F, 30.0F, 1.75F));
        this.addOptionalReagent(new ItemStack(Items.BONE), false, true, true, new IFaction[0]);
    }

    @Override
    public int requiredXPForRote() {
        return 200;
    }

    @Override
    public ComponentApplicationResult ApplyEffect(SpellSource source, SpellTarget target, IModifiedSpellPart<SpellEffect> modificationData, SpellContext context) {
        if (!source.isPlayerCaster()) {
            return ComponentApplicationResult.FAIL;
        } else if (context.hasEntityBeenAffected(this, source.getCaster())) {
            return ComponentApplicationResult.FAIL;
        } else {
            context.addAffectedEntity(this, source.getCaster());
            int offsetCount = 0;
            while (!context.getServerLevel().m_46859_(target.getBlock().above(offsetCount)) && offsetCount < 5) {
                offsetCount++;
            }
            if (offsetCount >= 5) {
                return ComponentApplicationResult.FAIL;
            } else {
                SummonedSpectralHorse horse = EntityInit.SPECTRAL_HORSE.get().create(context.getServerLevel());
                DifficultyInstance difficultyinstance = context.getServerLevel().m_6436_(target.getBlock());
                horse.m_6518_(context.getServerLevel(), difficultyinstance, MobSpawnType.TRIGGERED, (SpawnGroupData) null, (CompoundTag) null);
                horse.m_6034_(target.getPosition().x, target.getPosition().y + (double) offsetCount, target.getPosition().z);
                horse.f_19802_ = 60;
                horse.m_21530_();
                horse.m_30637_(source.getPlayer());
                horse.m_146762_(0);
                horse.m_5853_(null);
                SummonUtils.clampTrackedEntities(source.getPlayer());
                int duration = 20;
                if (context.isReagentMissing(Items.BONE)) {
                    duration = (int) (modificationData.getValue(Attribute.DURATION) * 20.0F);
                } else {
                    duration = -1;
                }
                SummonUtils.setSummon(horse, source.getPlayer(), false, duration);
                context.getServerLevel().addFreshEntity(horse);
                SummonUtils.addTrackedEntity(source.getPlayer(), horse);
                if (context.getSpawnedTargetEntity() != null && context.getSpawnedTargetEntity() instanceof LivingEntity) {
                    ((LivingEntity) context.getSpawnedTargetEntity()).m_7998_(horse, true);
                } else if (target.isLivingEntity() && target.getLivingEntity() == source.getCaster()) {
                    source.getCaster().m_7998_(horse, true);
                }
                return ComponentApplicationResult.SUCCESS;
            }
        }
    }

    @Override
    public IFaction getFactionRequirement() {
        return Factions.UNDEAD;
    }

    @Override
    public boolean canBeChanneled() {
        return false;
    }

    @Override
    public Affinity getAffinity() {
        return Affinity.ENDER;
    }

    @Override
    public float initialComplexity() {
        return 20.0F;
    }

    @Override
    public SoundEvent SoundEffect() {
        return SFX.Entity.SpectralHorse.SUMMON;
    }

    @Override
    public SpellPartTags getUseTag() {
        return SpellPartTags.FRIENDLY;
    }
}