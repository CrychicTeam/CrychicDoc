package fabric.me.thosea.badoptimizations.mixin.renderer.blockentity;

import fabric.me.thosea.badoptimizations.interfaces.BlockEntityTypeMethods;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.class_2586;
import net.minecraft.class_2591;
import net.minecraft.class_3300;
import net.minecraft.class_824;
import net.minecraft.class_827;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = { class_824.class }, priority = 700)
public abstract class MixinBlockEntityRenderDispatcher {

    @Shadow
    private Map<class_2591<?>, class_827<?>> field_4345;

    @Overwrite
    @Nullable
    public <E extends class_2586> class_827<E> method_3550(E blockEntity) {
        return ((BlockEntityTypeMethods) blockEntity.method_11017()).bo$getRenderer();
    }

    @Inject(method = { "reload" }, at = { @At("RETURN") })
    private void afterReload(class_3300 manager, CallbackInfo ci) {
        for (Entry<class_2591<?>, class_827<?>> entry : this.field_4345.entrySet()) {
            ((BlockEntityTypeMethods) entry.getKey()).bo$setRenderer((class_827<?>) entry.getValue());
        }
    }
}