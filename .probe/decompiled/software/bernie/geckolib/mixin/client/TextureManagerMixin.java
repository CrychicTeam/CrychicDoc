package software.bernie.geckolib.mixin.client;

import java.util.Map;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import software.bernie.geckolib.cache.texture.AnimatableTexture;

@Mixin({ TextureManager.class })
public abstract class TextureManagerMixin {

    @Shadow
    @Final
    private Map<ResourceLocation, AbstractTexture> byPath;

    @Shadow
    public abstract void register(ResourceLocation var1, AbstractTexture var2);

    @Inject(method = { "getTexture(Lnet/minecraft/resources/ResourceLocation;)Lnet/minecraft/client/renderer/texture/AbstractTexture;" }, at = { @At("HEAD") })
    private void wrapAnimatableTexture(ResourceLocation path, CallbackInfoReturnable<AbstractTexture> callback) {
        AbstractTexture existing = (AbstractTexture) this.byPath.get(path);
        if (existing == null) {
            AbstractTexture var4 = new AnimatableTexture(path);
            this.register(path, var4);
        }
    }
}