package com.sihenzhang.crockpot.block.food;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class CrockPot6StacksFoodBlock extends AbstractStackableFoodBlock {

    public static final IntegerProperty STACKS = IntegerProperty.create("stacks", 1, 6);

    public CrockPot6StacksFoodBlock() {
    }

    public CrockPot6StacksFoodBlock(BlockBehaviour.Properties pProperties) {
        super(pProperties);
    }

    @Override
    public int getMaxStacks() {
        return 6;
    }

    @Override
    public IntegerProperty getStacksProperty() {
        return STACKS;
    }
}