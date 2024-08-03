package dev.xkmc.l2complements.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.xkmc.l2complements.events.SpecialEquipmentEvents;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({ ElytraLayer.class })
public class ElytraLayerMixin {

    @WrapOperation(at = { @At(value = "INVOKE", remap = false, target = "Lnet/minecraft/client/renderer/entity/layers/ElytraLayer;shouldRender(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/LivingEntity;)Z") }, method = { "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/LivingEntity;FFFFFF)V" })
    public boolean l2complements$shouldRender$hideTransparent(ElytraLayer<?, ?> instance, ItemStack stack, LivingEntity entity, Operation<Boolean> original) {
        return (Boolean) original.call(new Object[] { instance, stack, entity }) && SpecialEquipmentEvents.isVisible(entity, stack);
    }
}