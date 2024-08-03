package com.mna.rituals.effects;

import com.mna.api.capabilities.IPlayerProgression;
import com.mna.api.rituals.IRitualContext;
import com.mna.api.rituals.RitualEffect;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.entities.EntityInit;
import com.mna.entities.boss.CouncilWarden;
import com.mna.entities.rituals.AncientCouncil;
import com.mna.factions.Factions;
import com.mna.tools.StructureUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.phys.Vec3;

public class RitualEffectAncientCouncil extends RitualEffect {

    public RitualEffectAncientCouncil(ResourceLocation ritualName) {
        super(ritualName);
    }

    @Override
    public Component canRitualStart(IRitualContext context) {
        IPlayerProgression progression = (IPlayerProgression) context.getCaster().getCapability(PlayerProgressionProvider.PROGRESSION).orElse(null);
        if (progression == null) {
            return Component.literal("Progression could not be found...this is a problem.");
        } else {
            CouncilWarden tempBoss = new CouncilWarden(context.getLevel(), Vec3.atBottomCenterOf(context.getCenter()));
            if (!StructureUtils.isPointInStructure((ServerLevel) context.getLevel(), context.getCenter(), tempBoss.getArenaStructureID(), tempBoss.getArenaStructureSegment())) {
                if (progression.getTierProgress(context.getLevel()) < 1.0F) {
                    return Component.translatable("ritual.mna.progression.not_ready");
                }
                if (progression != null && progression.hasAlliedFaction() && progression.getAlliedFaction() != Factions.COUNCIL) {
                    return Component.translatable("event.mna.faction_ritual_failed");
                }
            }
            return null;
        }
    }

    @Override
    protected boolean applyRitualEffect(IRitualContext context) {
        if (context.getCaster() != null && context.getCaster().m_20148_() != null) {
            CouncilWarden warden = new CouncilWarden(context.getLevel(), Vec3.atBottomCenterOf(context.getCenter()));
            if (StructureUtils.isPointInStructure((ServerLevel) context.getLevel(), context.getCenter(), warden.getArenaStructureID(), warden.getArenaStructureSegment())) {
                context.getLevel().m_7967_(warden);
            } else {
                AncientCouncil e = EntityInit.ANCIENT_COUNCIL.get().spawn((ServerLevel) context.getLevel(), null, context.getCaster(), context.getCenter(), MobSpawnType.EVENT, false, false);
                e.setCasterUUID(context.getCaster().m_20148_());
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected int getApplicationTicks(IRitualContext context) {
        return 100;
    }
}