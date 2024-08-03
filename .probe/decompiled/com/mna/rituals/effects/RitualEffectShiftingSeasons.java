package com.mna.rituals.effects;

import com.mna.api.capabilities.IPlayerMagic;
import com.mna.api.capabilities.IPlayerProgression;
import com.mna.api.rituals.IRitualContext;
import com.mna.api.rituals.RitualEffect;
import com.mna.api.tools.MATags;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.factions.Factions;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class RitualEffectShiftingSeasons extends RitualEffect {

    public RitualEffectShiftingSeasons(ResourceLocation ritualName) {
        super(ritualName);
    }

    @Override
    public Component canRitualStart(IRitualContext context) {
        IPlayerProgression progression = (IPlayerProgression) context.getCaster().getCapability(PlayerProgressionProvider.PROGRESSION).orElse(null);
        if (progression == null) {
            return Component.literal("Progression could not be found...this is a problem.");
        } else {
            return progression.getTier() > 2 && progression.getAlliedFaction() != Factions.FEY ? Component.translatable("ritual.mna.shifting_seasons.wrong_faction") : null;
        }
    }

    @Override
    protected boolean applyRitualEffect(IRitualContext context) {
        if (context.getCaster() != null && context.getCaster().m_20148_() != null) {
            IPlayerProgression progression = (IPlayerProgression) context.getCaster().getCapability(PlayerProgressionProvider.PROGRESSION).orElse(null);
            IPlayerMagic magic = (IPlayerMagic) context.getCaster().getCapability(PlayerMagicProvider.MAGIC).orElse(null);
            if (progression != null && magic != null) {
                if (progression != null && progression.hasAlliedFaction() && progression.getAlliedFaction() != Factions.FEY) {
                    context.getCaster().m_213846_(Component.translatable("ritual.mna.shifting_seasons.wrong_faction"));
                    return false;
                } else {
                    boolean summer = context.getCollectedReagents(i -> MATags.isItemIn(i.getItem(), MATags.Items.Ritual.SUMMER_FLOWERS)).size() > 0;
                    boolean winter = context.getCollectedReagents(i -> MATags.isItemIn(i.getItem(), MATags.Items.Ritual.WINTER_FLOWERS)).size() > 0;
                    if (!summer && !winter) {
                        context.getCaster().m_213846_(Component.translatable("event.mna.fey_ritual_no_decider"));
                        return false;
                    } else if (summer && winter) {
                        context.getCaster().m_213846_(Component.translatable("event.mna.fey_ritual_conflicting_decider"));
                        return false;
                    } else {
                        if (summer && !winter) {
                            context.getCaster().getPersistentData().putInt("faction_casting_resource_idx", 0);
                        } else {
                            context.getCaster().getPersistentData().putInt("faction_casting_resource_idx", 1);
                        }
                        magic.setCastingResourceType(Factions.FEY.getCastingResource(context.getCaster()));
                        return true;
                    }
                }
            } else {
                context.getCaster().m_213846_(Component.literal("Progression or Magic capabilities are missing.  Aborting ritual to prevent a crash.  You should save and quit, something is wrong."));
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    protected int getApplicationTicks(IRitualContext context) {
        return 60;
    }
}