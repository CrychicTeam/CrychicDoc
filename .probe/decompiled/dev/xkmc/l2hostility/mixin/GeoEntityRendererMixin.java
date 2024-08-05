package dev.xkmc.l2hostility.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

@Pseudo
@Mixin(targets = { "software.bernie.geckolib.renderer.GeoEntityRenderer" })
public class GeoEntityRendererMixin {

    @WrapOperation(remap = false, at = { @At(remap = true, value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;isInvisibleTo(Lnet/minecraft/world/entity/player/Player;)Z") }, method = { "actuallyRender*" })
    public boolean l2hostility$isInvisibleTo$geckoFix(Entity instance, Player player, Operation<Boolean> original, @Local(argsOnly = true, ordinal = 4) LocalFloatRef alpha) {
        boolean ans = (Boolean) original.call(new Object[] { instance, player });
        if (ans && instance.isCurrentlyGlowing()) {
            alpha.set(0.0F);
            return false;
        } else {
            return ans;
        }
    }
}