package forge.me.thosea.badoptimizations.mixin.renderer.entity;

import forge.me.thosea.badoptimizations.other.PlayerModelRendererHolder;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({ AbstractClientPlayer.class })
public abstract class MixinClientPlayer extends MixinEntity {

    @Shadow
    public abstract String getModelName();

    @Override
    public EntityRenderer<?> bo$getRenderer() {
        String model = this.getModelName();
        if (model.equals("default")) {
            return PlayerModelRendererHolder.WIDE_RENDERER;
        } else if (model.equals("slim")) {
            return PlayerModelRendererHolder.SLIM_RENDERER;
        } else {
            throw new IncompatibleClassChangeError("BadOptimizations: unexpected player model type " + model);
        }
    }
}