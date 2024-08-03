package forge.me.thosea.badoptimizations.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import java.util.Deque;
import java.util.List;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({ ToastComponent.class })
public abstract class MixinToastManager {

    @Shadow
    @Final
    private List<Object> visible;

    @Shadow
    @Final
    private Deque<Toast> queued;

    @ModifyExpressionValue(method = { "draw" }, at = { @At(value = "FIELD", target = "Lnet/minecraft/client/option/GameOptions;hudHidden:Z") })
    private boolean shouldSkipDraw(boolean hudHidden) {
        return hudHidden || this.visible.isEmpty() && this.queued.isEmpty();
    }
}