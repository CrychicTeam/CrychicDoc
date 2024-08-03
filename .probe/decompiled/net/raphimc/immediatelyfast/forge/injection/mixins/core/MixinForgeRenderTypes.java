package net.raphimc.immediatelyfast.forge.injection.mixins.core;

import net.minecraft.client.renderer.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = { "net.minecraftforge.client.ForgeRenderTypes$Internal" }, priority = 500)
public abstract class MixinForgeRenderTypes {

    @Inject(method = { "getText", "getTextIntensity", "getTextPolygonOffset", "getTextIntensityPolygonOffset", "getTextSeeThrough", "getTextIntensitySeeThrough" }, at = { @At("RETURN") }, remap = false)
    private static void changeTranslucency(CallbackInfoReturnable<RenderType> cir) {
        ((RenderType) cir.getReturnValue()).sortOnUpload = false;
    }
}