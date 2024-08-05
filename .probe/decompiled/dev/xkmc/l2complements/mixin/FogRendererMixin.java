package dev.xkmc.l2complements.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.xkmc.l2complements.events.SpecialEquipmentEvents;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({ FogRenderer.class })
public class FogRendererMixin {

    @WrapOperation(at = { @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;isSpectator()Z") }, method = { "setupFog" })
    private static boolean l2complements$setupFog$lavaWalker(Entity instance, Operation<Boolean> original) {
        return SpecialEquipmentEvents.canSee(instance, original);
    }
}