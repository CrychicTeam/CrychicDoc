package com.mna.spells.components;

import com.mna.api.affinity.Affinity;
import com.mna.api.faction.IFaction;
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
import com.mna.entities.summon.InsectSwarm;
import com.mna.factions.Factions;
import com.mna.tools.SummonUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;

public class ComponentSummonInsectSwarm extends SpellEffect {

    public ComponentSummonInsectSwarm(ResourceLocation guiIcon) {
        super(guiIcon, new AttributeValuePair(Attribute.DURATION, 120.0F, 30.0F, 1200.0F, 30.0F, 1.75F));
    }

    @Override
    public int requiredXPForRote() {
        return 200;
    }

    @Override
    public ComponentApplicationResult ApplyEffect(SpellSource source, SpellTarget target, IModifiedSpellPart<SpellEffect> modificationData, SpellContext context) {
        if (context.hasEntityBeenAffected(this, source.getCaster())) {
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
                InsectSwarm swarm = EntityInit.INSECT_SWARM.get().create(context.getServerLevel());
                DifficultyInstance difficultyinstance = context.getServerLevel().m_6436_(target.getBlock());
                swarm.m_6518_(context.getServerLevel(), difficultyinstance, MobSpawnType.TRIGGERED, (SpawnGroupData) null, (CompoundTag) null);
                swarm.m_6034_(target.getPosition().x, target.getPosition().y + (double) offsetCount, target.getPosition().z);
                swarm.f_19802_ = 60;
                swarm.m_21530_();
                SummonUtils.clampTrackedEntities(source.getCaster());
                SummonUtils.setSummon(swarm, source.getCaster(), true, (int) (modificationData.getValue(Attribute.DURATION) * 20.0F));
                context.getServerLevel().addFreshEntity(swarm);
                SummonUtils.addTrackedEntity(source.getCaster(), swarm);
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
    public SpellPartTags getUseTag() {
        return SpellPartTags.FRIENDLY;
    }
}