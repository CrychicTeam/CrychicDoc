package io.redspace.ironsspellbooks.mixin;

import io.redspace.ironsspellbooks.registries.ItemRegistry;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ Entity.class })
public class ClientEntityMixin {

    @Inject(method = { "getTeamColor" }, at = { @At("HEAD") }, cancellable = true)
    public void changeGlowOutline(CallbackInfoReturnable<Integer> cir) {
        if (Minecraft.getInstance().player != null && Minecraft.getInstance().player.m_21023_(MobEffectRegistry.PLANAR_SIGHT.get())) {
            cir.setReturnValue(7095029);
        } else {
            Entity var3 = (Entity) this;
            if (var3 instanceof ItemEntity item) {
                if (item.getItem().is(ItemRegistry.DRAGONSKIN.get())) {
                    cir.setReturnValue(13769983);
                }
                if (item.getItem().is(ItemRegistry.LIGHTNING_ROD_STAFF.get())) {
                    cir.setReturnValue(5636095);
                }
            }
        }
    }
}