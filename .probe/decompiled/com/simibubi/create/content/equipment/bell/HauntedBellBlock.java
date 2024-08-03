package com.simibubi.create.content.equipment.bell;

import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class HauntedBellBlock extends AbstractBellBlock<HauntedBellBlockEntity> {

    public HauntedBellBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntityType<? extends HauntedBellBlockEntity> getBlockEntityType() {
        return (BlockEntityType<? extends HauntedBellBlockEntity>) AllBlockEntityTypes.HAUNTED_BELL.get();
    }

    @Override
    protected boolean ring(Level world, BlockPos pos, Direction direction, Player player) {
        boolean ring = super.ring(world, pos, direction, player);
        if (ring) {
            AllAdvancements.HAUNTED_BELL.awardTo(player);
        }
        return ring;
    }

    @Override
    public Class<HauntedBellBlockEntity> getBlockEntityClass() {
        return HauntedBellBlockEntity.class;
    }

    @Override
    public void playSound(Level world, BlockPos pos) {
        AllSoundEvents.HAUNTED_BELL_USE.playOnServer(world, pos, 4.0F, 1.0F);
    }

    @Override
    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (oldState.m_60734_() != this && !world.isClientSide) {
            this.withBlockEntityDo(world, pos, hbte -> {
                hbte.effectTicks = 20;
                hbte.sendData();
            });
        }
    }
}