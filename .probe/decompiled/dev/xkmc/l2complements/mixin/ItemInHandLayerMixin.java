package dev.xkmc.l2complements.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.l2complements.events.SpecialEquipmentEvents;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ ItemInHandLayer.class })
public class ItemInHandLayerMixin {

    @Inject(at = { @At("HEAD") }, method = { "renderArmWithItem" }, cancellable = true)
    public void l2complement_renderArmWithItem_hideInvisibleItem(LivingEntity entity, ItemStack stack, ItemDisplayContext type, HumanoidArm arm, PoseStack pose, MultiBufferSource buffer, int light, CallbackInfo ci) {
        if (!SpecialEquipmentEvents.isVisible(entity, stack)) {
            ci.cancel();
        }
    }
}