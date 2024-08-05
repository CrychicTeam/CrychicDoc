package com.mna.rituals.effects;

import com.mna.api.rituals.IRitualContext;
import com.mna.api.rituals.RitualEffect;
import com.mna.blocks.BlockInit;
import com.mna.blocks.tileentities.SanctumTile;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class RitualEffectCircleOfPower extends RitualEffect {

    public RitualEffectCircleOfPower(ResourceLocation ritualName) {
        super(ritualName);
    }

    @Override
    protected boolean applyRitualEffect(IRitualContext context) {
        if (context.getCaster() == null) {
            return false;
        } else {
            context.getCaster().getCapability(PlayerProgressionProvider.PROGRESSION).ifPresent(p -> {
                BlockState state = context.getLevel().getBlockState(context.getCenter().below());
                if (state.m_60734_() != Blocks.BEDROCK && context.getLevel().getBlockEntity(context.getCenter().below()) == null) {
                    context.getLevel().setBlockAndUpdate(context.getCenter().below(), BlockInit.CIRCLE_OF_POWER.get().m_49966_());
                    BlockEntity te = context.getLevel().getBlockEntity(context.getCenter().below());
                    if (te != null && te instanceof SanctumTile) {
                        ((SanctumTile) te).setFaction(p.getAlliedFaction());
                    }
                }
            });
            return true;
        }
    }

    @Override
    protected int getApplicationTicks(IRitualContext context) {
        return 0;
    }
}