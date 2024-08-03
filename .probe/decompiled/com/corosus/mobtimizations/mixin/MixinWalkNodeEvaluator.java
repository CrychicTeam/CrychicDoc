package com.corosus.mobtimizations.mixin;

import com.corosus.mobtimizations.Mobtimizations;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ WalkNodeEvaluator.class })
public abstract class MixinWalkNodeEvaluator {

    @Inject(method = { "getBlockPathType(Lnet/minecraft/world/level/BlockGetter;III)Lnet/minecraft/world/level/pathfinder/BlockPathTypes;" }, at = { @At("HEAD") }, cancellable = true)
    public void getBlockPathType(BlockGetter pLevel, int pX, int pY, int pZ, CallbackInfoReturnable<BlockPathTypes> cir) {
        WalkNodeEvaluator self = (WalkNodeEvaluator) this;
        if (!Mobtimizations.canAvoidHazards(self.f_77313_)) {
            Mobtimizations.incCancel();
            cir.setReturnValue(Mobtimizations.getBlockPathTypeStatic(pLevel, new BlockPos.MutableBlockPos(pX, pY, pZ)));
        }
    }
}