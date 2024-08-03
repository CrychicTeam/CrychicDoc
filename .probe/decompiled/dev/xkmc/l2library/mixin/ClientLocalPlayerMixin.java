package dev.xkmc.l2library.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.xkmc.l2library.util.raytrace.FastItem;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({ LocalPlayer.class })
public abstract class ClientLocalPlayerMixin {

    @Shadow
    private boolean startedUsingItem;

    @WrapOperation(method = { "aiStep", "canStartSprinting" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isUsingItem()Z") })
    private boolean l2library_aiStep_fastUseItem(LocalPlayer player, Operation<Boolean> op) {
        if (this.startedUsingItem) {
            ItemStack stack = player.m_21211_();
            if (stack.getItem() instanceof FastItem fast && fast.isFast(stack)) {
                return false;
            }
        }
        return (Boolean) op.call(new Object[] { player });
    }
}