package forge.me.thosea.badoptimizations.mixin.tick;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ GameRenderer.class })
public final class MixinGameRenderer {

    @Shadow
    @Final
    Minecraft minecraft;

    private OptionInstance<Double> bo$fovEffectScale;

    @Inject(method = { "<init>" }, at = { @At("TAIL") })
    private void afterCreate(Minecraft client, ItemInHandRenderer heldItemRenderer, ResourceManager resourceManager, RenderBuffers buffers, CallbackInfo ci) {
        this.bo$fovEffectScale = client.options.fovEffectScale();
    }

    @WrapOperation(method = { "updateFovMultiplier" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;getFovMultiplier()F") })
    private float getPlayerFov(AbstractClientPlayer player, Operation<Float> original) {
        if (this.bo$fovEffectScale.get() == 0.0) {
            return this.minecraft.options.getCameraType() == CameraType.FIRST_PERSON && player.m_150108_() ? 0.1F : 1.0F;
        } else {
            return (Float) original.call(new Object[] { player });
        }
    }
}