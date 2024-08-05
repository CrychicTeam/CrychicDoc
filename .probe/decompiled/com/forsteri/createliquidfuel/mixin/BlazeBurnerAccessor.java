package com.forsteri.createliquidfuel.mixin;

import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value = { BlazeBurnerBlockEntity.class }, remap = false)
public interface BlazeBurnerAccessor {

    @Accessor("remainingBurnTime")
    int createliquidfuel$getRemainingBurnTime();

    @Accessor("remainingBurnTime")
    void createliquidfuel$setRemainingBurnTime(int var1);

    @Invoker("setBlockHeat")
    void createliquidfuel$invokeSetBlockHeat(BlazeBurnerBlock.HeatLevel var1);
}