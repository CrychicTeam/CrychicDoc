package com.simibubi.create.content.contraptions.behaviour;

import com.simibubi.create.content.contraptions.Contraption;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.state.BlockState;

public class LeverMovingInteraction extends SimpleBlockMovingInteraction {

    @Override
    protected BlockState handle(Player player, Contraption contraption, BlockPos pos, BlockState currentState) {
        this.playSound(player, SoundEvents.LEVER_CLICK, currentState.m_61143_(LeverBlock.POWERED) ? 0.5F : 0.6F);
        return (BlockState) currentState.m_61122_(LeverBlock.POWERED);
    }
}