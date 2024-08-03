package net.mehvahdjukaar.supplementaries.mixins;

import net.mehvahdjukaar.supplementaries.reg.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ Entity.class })
public abstract class EntityMixin {

    @Shadow
    public abstract void playSound(SoundEvent var1, float var2, float var3);

    @Inject(method = { "playStepSound" }, at = { @At("HEAD") }, cancellable = true)
    private void playStepSound(BlockPos pPos, BlockState state, CallbackInfo ci) {
        if (state.m_204336_(ModTags.FAST_FALL_ROPES)) {
            Entity var5 = (Entity) this;
            if (var5 instanceof LivingEntity le && le.onClimbable() && le.yya <= 0.0F) {
                ci.cancel();
            }
        }
    }
}