package dev.xkmc.l2complements.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.l2complements.events.SpecialEquipmentEvents;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ HumanoidArmorLayer.class })
public class HumanoidArmorLayerMixin<T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>> {

    @Inject(at = { @At("HEAD") }, method = { "renderArmorPiece" }, cancellable = true)
    public void l2complements_renderArmorPiece_hideInvisibleArmors(PoseStack matrix, MultiBufferSource source, T entity, EquipmentSlot slot, int light, A model, CallbackInfo ci) {
        ItemStack stack = entity.getItemBySlot(slot);
        if (!SpecialEquipmentEvents.isVisible(entity, stack)) {
            ci.cancel();
        }
    }
}