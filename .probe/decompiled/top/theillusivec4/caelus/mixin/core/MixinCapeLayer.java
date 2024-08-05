package top.theillusivec4.caelus.mixin.core;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.CapeLayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.theillusivec4.caelus.mixin.util.ClientMixinHooks;

@Mixin({ CapeLayer.class })
public class MixinCapeLayer {

    @Inject(at = { @At(value = "INVOKE", target = "net/minecraft/client/player/AbstractClientPlayer.getItemBySlot(Lnet/minecraft/world/entity/EquipmentSlot;)Lnet/minecraft/world/item/ItemStack;") }, method = { "render" }, cancellable = true)
    private void caelus$canRenderCape(PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight, AbstractClientPlayer pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch, CallbackInfo cb) {
        if (!ClientMixinHooks.canRenderCape(pLivingEntity)) {
            cb.cancel();
        }
    }
}