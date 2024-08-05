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
import com.mna.effects.EffectInit;
import com.mna.factions.Factions;
import com.mna.network.ServerMessageDispatcher;
import com.mna.tools.SummonUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;

public class ComponentPossession extends SpellEffect {

    public ComponentPossession(ResourceLocation guiIcon) {
        super(guiIcon, new AttributeValuePair(Attribute.DURATION, 120.0F, 30.0F, 600.0F, 30.0F, 10.0F), new AttributeValuePair(Attribute.MAGNITUDE, 1.0F, 1.0F, 3.0F, 1.0F, 10.0F));
    }

    @Override
    public int requiredXPForRote() {
        return 200;
    }

    @Override
    public boolean canBeChanneled() {
        return false;
    }

    @Override
    public ComponentApplicationResult ApplyEffect(SpellSource source, SpellTarget target, IModifiedSpellPart<SpellEffect> modificationData, SpellContext context) {
        if (!context.getServerLevel().m_5776_() && source.isPlayerCaster() && target.isLivingEntity() && target.getEntity() != source.getPlayer() && source.getPlayer().m_21124_(EffectInit.MIND_VISION.get()) == null) {
            int magnitude = (int) modificationData.getValue(Attribute.MAGNITUDE);
            if (!SummonUtils.isSummon(target.getLivingEntity()) && !this.magnitudeHealthCheck(source, target, magnitude, 20)) {
                return ComponentApplicationResult.FAIL;
            } else {
                source.getPlayer().m_7292_(new MobEffectInstance(EffectInit.POSSESSION.get(), (int) modificationData.getValue(Attribute.DURATION) * 20, 1));
                target.getLivingEntity().addEffect(new MobEffectInstance(EffectInit.POSSESSION.get(), (int) (modificationData.getValue(Attribute.DURATION) * 20.0F)));
                ServerMessageDispatcher.sendPlayerPosessionMessage((ServerPlayer) source.getPlayer(), target.getEntity());
                source.getPlayer().getPersistentData().putInt("posessed_entity_id", target.getLivingEntity().m_19879_());
                return ComponentApplicationResult.SUCCESS;
            }
        } else {
            return ComponentApplicationResult.FAIL;
        }
    }

    @Override
    public IFaction getFactionRequirement() {
        return Factions.UNDEAD;
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
    public SpellPartTags getUseTag() {
        return SpellPartTags.HARMFUL;
    }
}