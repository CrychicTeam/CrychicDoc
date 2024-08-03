package io.redspace.ironsspellbooks.mixin;

import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ Minecraft.class })
public class MinecraftMixin {

    @Inject(method = { "shouldEntityAppearGlowing" }, at = { @At("HEAD") }, cancellable = true)
    public void changeGlowOutline(Entity pEntity, CallbackInfoReturnable<Boolean> cir) {
        if (Minecraft.getInstance().player != null && Minecraft.getInstance().player.m_21023_(MobEffectRegistry.PLANAR_SIGHT.get()) && pEntity instanceof LivingEntity && Mth.abs((float) (pEntity.getY() - Minecraft.getInstance().player.m_20186_())) < 18.0F) {
            cir.setReturnValue(true);
        }
    }
}