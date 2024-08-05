package com.simibubi.create.foundation.mixin;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.waterwheel.WaterWheelStructuralBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ FlowingFluid.class })
public class WaterWheelFluidSpreadMixin {

    @Inject(method = { "canPassThrough(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/world/level/material/Fluid;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/material/FluidState;)Z" }, at = { @At("HEAD") }, cancellable = true)
    protected void create$canPassThroughOnWaterWheel(BlockGetter pLevel, Fluid pFluid, BlockPos pFromPos, BlockState blockState0, Direction pDirection, BlockPos blockPos1, BlockState blockState2, FluidState fluidState3, CallbackInfoReturnable<Boolean> cir) {
        if (pDirection.getAxis() != Direction.Axis.Y) {
            BlockPos belowPos = pFromPos.below();
            BlockState belowState = pLevel.getBlockState(belowPos);
            if (AllBlocks.WATER_WHEEL_STRUCTURAL.has(belowState)) {
                if (((WaterWheelStructuralBlock) AllBlocks.WATER_WHEEL_STRUCTURAL.get()).stillValid(pLevel, belowPos, belowState, false)) {
                    belowState = pLevel.getBlockState(WaterWheelStructuralBlock.getMaster(pLevel, belowPos, belowState));
                }
            } else if (!AllBlocks.WATER_WHEEL.has(belowState)) {
                return;
            }
            if (belowState.m_60734_() instanceof IRotate irotate && irotate.getRotationAxis(belowState) == pDirection.getAxis()) {
                cir.setReturnValue(false);
            }
        }
    }
}