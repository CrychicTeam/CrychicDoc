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
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.config.GeneralConfig;
import com.mna.factions.Factions;
import java.util.Collection;
import java.util.stream.Collectors;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import org.apache.commons.lang3.mutable.MutableInt;

public class ComponentPurge extends SpellEffect {

    public ComponentPurge(ResourceLocation guiIcon) {
        super(guiIcon, new AttributeValuePair(Attribute.DAMAGE, 0.0F, 0.0F, 5.0F, 0.5F, 3.0F), new AttributeValuePair(Attribute.LESSER_MAGNITUDE, 1.0F, 1.0F, 5.0F, 1.0F, 15.0F), new AttributeValuePair(Attribute.MAGNITUDE, 0.0F, 0.0F, 5.0F, 1.0F, 15.0F));
    }

    @Override
    public int requiredXPForRote() {
        return 200;
    }

    @Override
    public ComponentApplicationResult ApplyEffect(SpellSource source, SpellTarget target, IModifiedSpellPart<SpellEffect> modificationData, SpellContext context) {
        if (!target.isLivingEntity()) {
            return ComponentApplicationResult.FAIL;
        } else {
            int removeUpto = (int) modificationData.getValue(Attribute.LESSER_MAGNITUDE) + (int) modificationData.getValue(Attribute.MAGNITUDE);
            Collection<MobEffectInstance> activeEffects = (Collection<MobEffectInstance>) target.getLivingEntity().getActiveEffects().stream().filter(e -> !e.isAmbient() && e.getDuration() >= 220 && e.getEffect().isBeneficial() && e.getAmplifier() <= removeUpto - 1).collect(Collectors.toList());
            MutableInt removedEffects = new MutableInt(0);
            activeEffects.forEach(ae -> {
                if (target.getLivingEntity().removeEffect(ae.getEffect())) {
                    removedEffects.increment();
                }
            });
            if (removedEffects.getValue() > 0) {
                float damage = (float) removedEffects.getValue().intValue() * modificationData.getValue(Attribute.DAMAGE) * GeneralConfig.getDamageMultiplier();
                target.getLivingEntity().hurt(target.getLivingEntity().m_269291_().magic(), damage);
                if (target.getLivingEntity() instanceof Player) {
                    float pct = 0.05F + (0.025F * (float) removedEffects.getValue().intValue() - 1.0F);
                    ((Player) target.getLivingEntity()).getCapability(PlayerMagicProvider.MAGIC).ifPresent(m -> m.getCastingResource().consume(target.getLivingEntity(), m.getCastingResource().getMaxAmount() * pct));
                }
            }
            return ComponentApplicationResult.SUCCESS;
        }
    }

    @Override
    public boolean targetsBlocks() {
        return false;
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
    public IFaction getFactionRequirement() {
        return Factions.UNDEAD;
    }

    @Override
    public float initialComplexity() {
        return 40.0F;
    }

    @Override
    public SpellPartTags getUseTag() {
        return SpellPartTags.HARMFUL;
    }
}