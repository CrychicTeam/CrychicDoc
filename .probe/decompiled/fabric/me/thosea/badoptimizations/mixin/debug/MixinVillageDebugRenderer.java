package fabric.me.thosea.badoptimizations.mixin.debug;

import java.util.Map;
import java.util.UUID;
import net.minecraft.class_2338;
import net.minecraft.class_4207;
import net.minecraft.class_4587;
import net.minecraft.class_4597;
import net.minecraft.class_4207.class_4232;
import net.minecraft.class_4207.class_4233;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ class_4207.class })
public class MixinVillageDebugRenderer {

    @Shadow
    @Final
    private Map<class_2338, class_4233> field_18787;

    @Shadow
    @Final
    private Map<UUID, class_4232> field_18921;

    @Inject(method = { "render" }, at = { @At("HEAD") }, cancellable = true)
    private void onRender(class_4587 matrices, class_4597 vertexConsumers, double cameraX, double cameraY, double cameraZ, CallbackInfo ci) {
        if (this.field_18787.isEmpty() && this.field_18921.isEmpty()) {
            ci.cancel();
        }
    }
}