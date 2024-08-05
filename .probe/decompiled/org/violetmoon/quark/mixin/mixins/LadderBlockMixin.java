package org.violetmoon.quark.mixin.mixins;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.violetmoon.quark.content.tweaks.module.EnhancedLaddersModule;

@Mixin({ LadderBlock.class })
public class LadderBlockMixin {

    @Inject(method = { "canSurvive" }, at = { @At("HEAD") }, cancellable = true)
    private void canSurvive(BlockState state, LevelReader level, BlockPos pos, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (EnhancedLaddersModule.canLadderSurvive(state, level, pos)) {
            callbackInfoReturnable.setReturnValue(true);
            callbackInfoReturnable.cancel();
        }
    }
}