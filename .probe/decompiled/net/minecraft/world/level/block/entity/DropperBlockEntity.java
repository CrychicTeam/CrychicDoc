package net.minecraft.world.level.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;

public class DropperBlockEntity extends DispenserBlockEntity {

    public DropperBlockEntity(BlockPos blockPos0, BlockState blockState1) {
        super(BlockEntityType.DROPPER, blockPos0, blockState1);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.dropper");
    }
}