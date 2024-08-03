package com.mna.mixins;

import com.mna.effects.EffectInit;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.WebBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ WebBlock.class })
public class WebBlockMixin {

    @Inject(at = { @At("HEAD") }, method = { "entityInside" }, cancellable = true)
    private void mna$entityInside(BlockState pState, Level pLevel, BlockPos pPos, Entity pEntity, CallbackInfo cir) {
        if (pEntity instanceof LivingEntity living && (living.hasEffect(EffectInit.SPIDER_CLIMBING.get()) || living.hasEffect(EffectInit.ENLARGE.get()))) {
            cir.cancel();
        }
    }
}