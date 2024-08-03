package net.minecraft.world.level.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;

public class BedBlockEntity extends BlockEntity {

    private DyeColor color;

    public BedBlockEntity(BlockPos blockPos0, BlockState blockState1) {
        super(BlockEntityType.BED, blockPos0, blockState1);
        this.color = ((BedBlock) blockState1.m_60734_()).getColor();
    }

    public BedBlockEntity(BlockPos blockPos0, BlockState blockState1, DyeColor dyeColor2) {
        super(BlockEntityType.BED, blockPos0, blockState1);
        this.color = dyeColor2;
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public DyeColor getColor() {
        return this.color;
    }

    public void setColor(DyeColor dyeColor0) {
        this.color = dyeColor0;
    }
}