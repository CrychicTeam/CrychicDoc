package com.mna.rituals.effects;

import com.mna.api.capabilities.IPlayerProgression;
import com.mna.api.rituals.IRitualContext;
import com.mna.api.rituals.RitualEffect;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.entities.EntityInit;
import com.mna.entities.boss.DemonLord;
import com.mna.entities.rituals.DemonStone;
import com.mna.factions.Factions;
import com.mna.tools.StructureUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;

public class RitualEffectBurningHells extends RitualEffect {

    public RitualEffectBurningHells(ResourceLocation ritualName) {
        super(ritualName);
    }

    @Override
    public Component canRitualStart(IRitualContext context) {
        IPlayerProgression progression = (IPlayerProgression) context.getCaster().getCapability(PlayerProgressionProvider.PROGRESSION).orElse(null);
        if (progression == null) {
            return Component.translatable("Progression could not be found...this is a problem.");
        } else {
            DemonLord tempBoss = new DemonLord(context.getLevel(), Vec3.atBottomCenterOf(context.getCenter()));
            if (!StructureUtils.isPointInStructure((ServerLevel) context.getLevel(), context.getCenter(), tempBoss.getArenaStructureID(), tempBoss.getArenaStructureSegment())) {
                if (progression.getTierProgress(context.getLevel()) < 1.0F) {
                    return Component.translatable("ritual.mna.progression.not_ready");
                }
                if (progression != null && progression.hasAlliedFaction() && progression.getAlliedFaction() != Factions.DEMONS) {
                    return Component.translatable("event.mna.faction_ritual_failed");
                }
            }
            return null;
        }
    }

    @Override
    protected boolean applyRitualEffect(IRitualContext context) {
        BlockPos pos = context.getCenter();
        if (context.getCaster() != null && context.getCaster().m_20148_() != null) {
            DemonStone e = new DemonStone(EntityInit.DEMON_STONE.get(), context.getLevel());
            e.m_7678_((double) ((float) pos.m_123341_() + 0.5F), (double) pos.m_123342_(), (double) ((float) pos.m_123343_() + 0.5F), 0.0F, 0.0F);
            e.setCasterUUID(context.getCaster().m_20148_());
            DemonLord tempDemon = new DemonLord(context.getLevel());
            if (StructureUtils.isPointInStructure((ServerLevel) context.getLevel(), pos, tempDemon.getArenaStructureID(), tempDemon.getArenaStructureSegment())) {
                e.setSummonAsHostile();
            }
            context.getLevel().m_7967_(e);
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected int getApplicationTicks(IRitualContext context) {
        return 200;
    }
}