package net.minecraft.world.level.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;

public class ComparatorBlockEntity extends BlockEntity {

    private int output;

    public ComparatorBlockEntity(BlockPos blockPos0, BlockState blockState1) {
        super(BlockEntityType.COMPARATOR, blockPos0, blockState1);
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag0) {
        super.saveAdditional(compoundTag0);
        compoundTag0.putInt("OutputSignal", this.output);
    }

    @Override
    public void load(CompoundTag compoundTag0) {
        super.load(compoundTag0);
        this.output = compoundTag0.getInt("OutputSignal");
    }

    public int getOutputSignal() {
        return this.output;
    }

    public void setOutputSignal(int int0) {
        this.output = int0;
    }
}