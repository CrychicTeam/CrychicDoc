package com.mna.spells.components;

import com.mna.api.affinity.Affinity;
import com.mna.api.faction.IFaction;
import com.mna.api.spells.ComponentApplicationResult;
import com.mna.api.spells.SpellPartTags;
import com.mna.api.spells.base.IModifiedSpellPart;
import com.mna.api.spells.parts.SpellEffect;
import com.mna.api.spells.targeting.SpellContext;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import com.mna.factions.Factions;
import com.mna.tools.SummonUtils;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

public class ComponentSacrifice extends SpellEffect {

    public ComponentSacrifice(ResourceLocation guiIcon) {
        super(guiIcon);
    }

    @Override
    public int requiredXPForRote() {
        return 100;
    }

    @Override
    public ComponentApplicationResult ApplyEffect(SpellSource source, SpellTarget target, IModifiedSpellPart<SpellEffect> modificationData, SpellContext context) {
        if (source.isPlayerCaster() && target.isLivingEntity()) {
            LivingEntity summon = null;
            if (source.getPlayer() == target.getLivingEntity()) {
                List<Mob> summons = SummonUtils.getSummons(source.getPlayer(), context.getServerLevel());
                if (summons.size() > 0) {
                    summon = (LivingEntity) summons.get(summons.size() - 1);
                }
            } else if (SummonUtils.isSummon(target.getLivingEntity()) && SummonUtils.getSummoner(target.getLivingEntity()) == source.getPlayer()) {
                summon = target.getLivingEntity();
            }
            if (summon == null) {
                return ComponentApplicationResult.FAIL;
            } else {
                summon.kill();
                if (!summon.isAlive()) {
                    source.getPlayer().m_5634_(summon.getMaxHealth() / 10.0F);
                }
                return ComponentApplicationResult.SUCCESS;
            }
        } else {
            return ComponentApplicationResult.FAIL;
        }
    }

    @Override
    public Affinity getAffinity() {
        return Affinity.ENDER;
    }

    @Override
    public SpellPartTags getUseTag() {
        return SpellPartTags.UTILITY;
    }

    @Override
    public IFaction getFactionRequirement() {
        return Factions.UNDEAD;
    }

    @Override
    public float initialComplexity() {
        return 35.0F;
    }

    @Override
    public boolean canBeChanneled() {
        return false;
    }
}