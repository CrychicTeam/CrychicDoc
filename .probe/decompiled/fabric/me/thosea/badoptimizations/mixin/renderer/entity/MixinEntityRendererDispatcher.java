package fabric.me.thosea.badoptimizations.mixin.renderer.entity;

import fabric.me.thosea.badoptimizations.interfaces.EntityMethods;
import fabric.me.thosea.badoptimizations.interfaces.EntityTypeMethods;
import fabric.me.thosea.badoptimizations.other.PlayerModelRendererHolder;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.class_1297;
import net.minecraft.class_1299;
import net.minecraft.class_1657;
import net.minecraft.class_3300;
import net.minecraft.class_897;
import net.minecraft.class_898;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = { class_898.class }, priority = 700)
public abstract class MixinEntityRendererDispatcher {

    @Shadow
    private Map<class_1299<?>, class_897<?>> field_4696;

    @Shadow
    private Map<String, class_897<? extends class_1657>> field_4687;

    @Overwrite
    public <T extends class_1297 & EntityMethods> class_897<? super T> method_3953(T entity) {
        return entity.bo$getRenderer();
    }

    @Inject(method = { "reload" }, at = { @At("RETURN") })
    private void afterReload(class_3300 manager, CallbackInfo ci) {
        for (Entry<class_1299<?>, class_897<?>> entry : this.field_4696.entrySet()) {
            ((EntityTypeMethods) entry.getKey()).bo$setRenderer((class_897<?>) entry.getValue());
        }
        PlayerModelRendererHolder.WIDE_RENDERER = (class_897<? extends class_1657>) this.field_4687.get("default");
        PlayerModelRendererHolder.SLIM_RENDERER = (class_897<? extends class_1657>) this.field_4687.get("slim");
    }
}