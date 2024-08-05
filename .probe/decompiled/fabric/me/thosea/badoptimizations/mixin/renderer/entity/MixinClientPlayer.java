package fabric.me.thosea.badoptimizations.mixin.renderer.entity;

import fabric.me.thosea.badoptimizations.other.PlayerModelRendererHolder;
import net.minecraft.class_742;
import net.minecraft.class_897;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({ class_742.class })
public abstract class MixinClientPlayer extends MixinEntity {

    @Shadow
    public abstract String method_3121();

    @Override
    public class_897<?> bo$getRenderer() {
        String model = this.method_3121();
        if (model.equals("default")) {
            return PlayerModelRendererHolder.WIDE_RENDERER;
        } else if (model.equals("slim")) {
            return PlayerModelRendererHolder.SLIM_RENDERER;
        } else {
            throw new IncompatibleClassChangeError("BadOptimizations: unexpected player model type " + model);
        }
    }
}