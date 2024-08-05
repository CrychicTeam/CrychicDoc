package fabric.me.thosea.badoptimizations.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import java.util.Deque;
import java.util.List;
import net.minecraft.class_368;
import net.minecraft.class_374;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({ class_374.class })
public abstract class MixinToastManager {

    @Shadow
    @Final
    private List<Object> field_2239;

    @Shadow
    @Final
    private Deque<class_368> field_2240;

    @ModifyExpressionValue(method = { "draw" }, at = { @At(value = "FIELD", target = "Lnet/minecraft/client/option/GameOptions;hudHidden:Z") })
    private boolean shouldSkipDraw(boolean hudHidden) {
        return hudHidden || this.field_2239.isEmpty() && this.field_2240.isEmpty();
    }
}