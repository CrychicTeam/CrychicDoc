package com.mna.rituals.effects;

import com.mna.api.capabilities.IPlayerProgression;
import com.mna.api.rituals.IRitualContext;
import com.mna.api.rituals.RitualEffect;
import com.mna.api.tools.MATags;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.entities.EntityInit;
import com.mna.entities.boss.FaerieQueen;
import com.mna.entities.rituals.FeyLight;
import com.mna.factions.Factions;
import com.mna.tools.StructureUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.phys.Vec3;

public class RitualEffectFaerieCourts extends RitualEffect {

    public RitualEffectFaerieCourts(ResourceLocation ritualName) {
        super(ritualName);
    }

    @Override
    public Component canRitualStart(IRitualContext context) {
        IPlayerProgression progression = (IPlayerProgression) context.getCaster().getCapability(PlayerProgressionProvider.PROGRESSION).orElse(null);
        if (progression == null) {
            return Component.literal("Progression could not be found...this is a problem.");
        } else {
            FaerieQueen tempBoss = new FaerieQueen(context.getLevel(), Vec3.atBottomCenterOf(context.getCenter()), true);
            if (!StructureUtils.isPointInStructure((ServerLevel) context.getLevel(), context.getCenter(), tempBoss.getArenaStructureID(), tempBoss.getArenaStructureSegment())) {
                if (progression.getTierProgress(context.getLevel()) < 1.0F) {
                    return Component.translatable("ritual.mna.progression.not_ready");
                }
                if (progression != null && progression.hasAlliedFaction() && progression.getAlliedFaction() != Factions.FEY) {
                    return Component.translatable("event.mna.faction_ritual_failed");
                }
            }
            return null;
        }
    }

    @Override
    protected boolean applyRitualEffect(IRitualContext context) {
        if (context.getCaster() != null && context.getCaster().m_20148_() != null) {
            FaerieQueen summerQueen = new FaerieQueen(context.getLevel(), Vec3.atBottomCenterOf(context.getCenter()).add(3.0, 0.0, 0.0), false);
            if (StructureUtils.isPointInStructure((ServerLevel) context.getLevel(), context.getCenter(), summerQueen.getArenaStructureID(), summerQueen.getArenaStructureSegment())) {
                FaerieQueen winterQueen = new FaerieQueen(context.getLevel(), Vec3.atBottomCenterOf(context.getCenter()).add(-3.0, 0.0, 0.0), true);
                summerQueen.setupSpawn();
                winterQueen.setupSpawn();
                context.getLevel().m_7967_(summerQueen);
                context.getLevel().m_7967_(winterQueen);
                return true;
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
                    FeyLight e = EntityInit.FEY_LIGHT.get().spawn((ServerLevel) context.getLevel(), null, context.getCaster(), context.getCenter().above(12), MobSpawnType.EVENT, false, false);
                    e.setTargetPos(context.getCenter().above(3));
                    e.setCasterUUID(context.getCaster().m_20148_());
                    return true;
                }
            }
        } else {
            return false;
        }
    }

    @Override
    protected int getApplicationTicks(IRitualContext context) {
        return 140;
    }
}