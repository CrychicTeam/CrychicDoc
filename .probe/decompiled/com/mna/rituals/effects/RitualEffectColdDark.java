package com.mna.rituals.effects;

import com.mna.api.capabilities.IPlayerProgression;
import com.mna.api.rituals.IRitualContext;
import com.mna.api.rituals.RitualEffect;
import com.mna.blocks.BlockInit;
import com.mna.blocks.tileentities.CoffinTile;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.entities.boss.WitherLich;
import com.mna.factions.Factions;
import com.mna.tools.StructureUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.phys.Vec3;

public class RitualEffectColdDark extends RitualEffect {

    public RitualEffectColdDark(ResourceLocation ritualName) {
        super(ritualName);
    }

    @Override
    public Component canRitualStart(IRitualContext context) {
        IPlayerProgression progression = (IPlayerProgression) context.getCaster().getCapability(PlayerProgressionProvider.PROGRESSION).orElse(null);
        if (progression == null) {
            return Component.translatable("Progression could not be found...this is a problem.");
        } else {
            WitherLich tempBoss = new WitherLich(context.getLevel(), Vec3.atBottomCenterOf(context.getCenter()));
            if (!StructureUtils.isPointInStructure((ServerLevel) context.getLevel(), context.getCenter(), tempBoss.getArenaStructureID(), tempBoss.getArenaStructureSegment())) {
                if (progression.getTierProgress(context.getLevel()) < 1.0F) {
                    return Component.translatable("ritual.mna.progression.not_ready");
                }
                if (progression != null && progression.hasAlliedFaction() && progression.getAlliedFaction() != Factions.UNDEAD) {
                    return Component.translatable("event.mna.faction_ritual_failed");
                }
            }
            return null;
        }
    }

    @Override
    protected boolean applyRitualEffect(IRitualContext context) {
        if (context.getCaster() != null && context.getCaster().m_20148_() != null) {
            BlockPos pos = context.getCenter();
            WitherLich lich = new WitherLich(context.getLevel());
            if (StructureUtils.isPointInStructure((ServerLevel) context.getLevel(), context.getCenter(), lich.getArenaStructureID(), lich.getArenaStructureSegment())) {
                lich.m_6034_((double) ((float) pos.m_123341_() + 0.5F), (double) pos.m_123342_(), (double) ((float) pos.m_123343_() + 0.5F));
                context.getLevel().m_7967_(lich);
                return true;
            } else {
                BlockPos top_pos = context.getCenter();
                BlockPos other_pos = null;
                Direction coffin_facing = null;
                if (context.getLevel().m_46859_(top_pos.north())) {
                    other_pos = top_pos.north();
                    coffin_facing = Direction.NORTH;
                } else if (context.getLevel().m_46859_(top_pos.east())) {
                    other_pos = top_pos.east();
                    coffin_facing = Direction.EAST;
                } else if (context.getLevel().m_46859_(top_pos.south())) {
                    other_pos = top_pos.south();
                    coffin_facing = Direction.SOUTH;
                } else if (context.getLevel().m_46859_(top_pos.west())) {
                    other_pos = top_pos.west();
                    coffin_facing = Direction.WEST;
                } else {
                    other_pos = top_pos.north();
                    coffin_facing = Direction.NORTH;
                }
                if (other_pos != null && coffin_facing != null) {
                    context.getLevel().setBlockAndUpdate(top_pos, (BlockState) ((BlockState) BlockInit.COFFIN.get().m_49966_().m_61124_(BedBlock.PART, BedPart.FOOT)).m_61124_(BedBlock.f_54117_, coffin_facing));
                    context.getLevel().setBlockAndUpdate(other_pos, (BlockState) ((BlockState) BlockInit.COFFIN.get().m_49966_().m_61124_(BedBlock.PART, BedPart.HEAD)).m_61124_(BedBlock.f_54117_, coffin_facing));
                    BlockEntity te = context.getLevel().getBlockEntity(other_pos);
                    if (te != null && te instanceof CoffinTile) {
                        ((CoffinTile) te).setRitualPlayer(context.getCaster());
                        context.getCaster().m_213846_(Component.translatable("event.mna.cold_dark_prompt").withStyle(ChatFormatting.AQUA));
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
    }

    @Override
    protected int getApplicationTicks(IRitualContext context) {
        return 100;
    }
}