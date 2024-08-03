package me.srrapero720.embeddiumplus.mixins.impl.darkness;

import com.mojang.blaze3d.vertex.PoseStack;
import me.srrapero720.embeddiumplus.foundation.darkness.DarknessPlus;
import me.srrapero720.embeddiumplus.mixins.impl.darkness.accessors.LightTextureAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ GameRenderer.class })
public abstract class GameRendererMixin {

    @Shadow
    @Final
    public LightTexture lightTexture;

    @Shadow
    @Final
    private Minecraft minecraft;

    @Inject(method = { "renderLevel" }, at = { @At("HEAD") })
    private void inject$renderLevel(float tickDelta, long nanos, PoseStack matrixStack, CallbackInfo ci) {
        LightTextureAccessor lightTexAccessor = (LightTextureAccessor) this.lightTexture;
        if (lightTexAccessor.embPlus$isDirty()) {
            this.minecraft.getProfiler().push("lightTex");
            DarknessPlus.updateLuminance(tickDelta, this.minecraft, (GameRenderer) this, lightTexAccessor.embPlus$getFlicker());
            this.minecraft.getProfiler().pop();
        }
    }
}