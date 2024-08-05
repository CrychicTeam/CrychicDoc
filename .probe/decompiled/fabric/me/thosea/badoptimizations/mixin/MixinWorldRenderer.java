package fabric.me.thosea.badoptimizations.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;
import net.minecraft.class_638;
import net.minecraft.class_761;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = { class_761.class }, priority = 700)
public abstract class MixinWorldRenderer {

    @WrapOperation(method = { "renderSky(Lnet/minecraft/client/util/math/MatrixStack;Lorg/joml/Matrix4f;FLnet/minecraft/client/render/Camera;ZLjava/lang/Runnable;)V" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;getSkyAngle(F)F", ordinal = 0) })
    private float cacheSkyAngle(class_638 world, float delta, Operation<Float> original, @Share("skyAngle") LocalFloatRef skyAngle) {
        float result = (Float) original.call(new Object[] { world, delta });
        skyAngle.set(result);
        return result;
    }

    @Redirect(method = { "renderSky(Lnet/minecraft/client/util/math/MatrixStack;Lorg/joml/Matrix4f;FLnet/minecraft/client/render/Camera;ZLjava/lang/Runnable;)V" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;getSkyAngleRadians(F)F"))
    private float getSkyAngleRadians(class_638 world, float delta, @Share("skyAngle") LocalFloatRef skyAngle) {
        return skyAngle.get() * (float) (Math.PI * 2);
    }

    @Redirect(method = { "renderSky(Lnet/minecraft/client/util/math/MatrixStack;Lorg/joml/Matrix4f;FLnet/minecraft/client/render/Camera;ZLjava/lang/Runnable;)V" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;getSkyAngle(F)F", ordinal = 1))
    private float getSkyAngle(class_638 world, float delta, @Share("skyAngle") LocalFloatRef skyAngle) {
        return skyAngle.get();
    }
}