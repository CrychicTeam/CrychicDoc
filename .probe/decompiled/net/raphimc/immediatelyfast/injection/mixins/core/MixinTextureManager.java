package net.raphimc.immediatelyfast.injection.mixins.core;

import java.util.Map;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ TextureManager.class })
public abstract class MixinTextureManager {

    @Shadow
    @Final
    private Map<ResourceLocation, AbstractTexture> byPath;

    @Inject(method = { "destroyTexture" }, at = { @At("RETURN") })
    private void removeDestroyedTexture(ResourceLocation id, CallbackInfo ci) {
        this.byPath.remove(id);
    }
}