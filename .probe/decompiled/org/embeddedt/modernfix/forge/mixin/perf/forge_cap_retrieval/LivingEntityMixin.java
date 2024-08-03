package org.embeddedt.modernfix.forge.mixin.perf.forge_cap_retrieval;

import javax.annotation.Nullable;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({ LivingEntity.class })
public class LivingEntityMixin {

    @Redirect(method = { "getCapability" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;isAlive()Z"))
    private <T> boolean checkAliveAfterCap(LivingEntity entity, Capability<T> capability, @Nullable Direction facing) {
        return capability == ForgeCapabilities.ITEM_HANDLER && entity.isAlive();
    }
}