package dev.xkmc.l2complements.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.xkmc.l2complements.content.feature.EntityFeature;
import dev.xkmc.l2complements.events.SpecialEquipmentEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ LivingEntity.class })
public abstract class LivingEntityMixin {

    @WrapOperation(at = { @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isEmpty()Z") }, method = { "getArmorCoverPercentage" })
    public boolean l2complements_getArmorCoverPercentage_hideInvisibleArmorsFromMobs(ItemStack stack, Operation<Boolean> op) {
        LivingEntity self = (LivingEntity) this;
        return (Boolean) op.call(new Object[] { stack }) || !SpecialEquipmentEvents.isVisible(self, stack);
    }

    @Inject(at = { @At("HEAD") }, method = { "canStandOnFluid" }, cancellable = true)
    public void l2complements_canStandOnFluid_pandora(FluidState state, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity self = (LivingEntity) this;
        if (SpecialEquipmentEvents.canWalkOn(state, self)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(at = { @At("HEAD") }, method = { "canFreeze" }, cancellable = true)
    public void l2complments_canFreeze_checkFeature(CallbackInfoReturnable<Boolean> cir) {
        LivingEntity self = (LivingEntity) this;
        if (EntityFeature.SNOW_WALKER.test(self)) {
            cir.setReturnValue(false);
        }
    }
}