package com.mna.mixins;

import com.mna.effects.EffectInit;
import com.mna.items.ItemInit;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ LivingEntity.class })
public class LivingEntityFireWalkerMixin {

    @Inject(at = { @At("RETURN") }, method = { "canStandOnFluid" }, cancellable = true)
    public void mna$canStandOnFluid(FluidState state, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity self = (LivingEntity) this;
        if (state.getTags().anyMatch(t -> t == FluidTags.LAVA) && ItemInit.DEMON_ARMOR_CHEST.get().isSetEquipped(self)) {
            cir.setReturnValue(!self.m_6047_());
        }
        if (state.getTags().anyMatch(t -> t == FluidTags.WATER) && self.hasEffect(EffectInit.WATER_WALKING.get()) && self.m_204036_(FluidTags.WATER) < self.m_20204_()) {
            cir.setReturnValue(!self.m_6047_());
        }
    }

    @Inject(at = { @At("RETURN") }, method = { "isAffectedByFluids" }, cancellable = true)
    public void mna$isAffectedByFluids(CallbackInfoReturnable<Boolean> cir) {
        LivingEntity self = (LivingEntity) this;
        if (self.m_20077_() && ItemInit.DEMON_ARMOR_CHEST.get().isSetEquipped(self)) {
            cir.setReturnValue(!self.m_20077_());
        }
    }

    @Inject(at = { @At("HEAD") }, method = { "jumpInLiquid" }, cancellable = true)
    public void mna$jumpInLiquid(TagKey<Fluid> fluid, CallbackInfo ci) {
        if (fluid == FluidTags.LAVA) {
            LivingEntity self = (LivingEntity) this;
            if (ItemInit.DEMON_ARMOR_CHEST.get().isSetEquipped(self) && self instanceof Player) {
                ((Player) self).jumpFromGround();
                ci.cancel();
            }
        }
    }
}