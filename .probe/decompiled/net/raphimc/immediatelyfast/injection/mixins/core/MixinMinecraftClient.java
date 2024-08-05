package net.raphimc.immediatelyfast.injection.mixins.core;

import net.minecraft.client.Minecraft;
import net.raphimc.immediatelyfast.ImmediatelyFast;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ Minecraft.class })
public abstract class MixinMinecraftClient {

    @Inject(method = { "<init>" }, at = { @At("RETURN") })
    private void initImmediatelyFast(CallbackInfo ci) {
        ImmediatelyFast.lateInit();
    }

    @Inject(method = { "joinWorld" }, at = { @At("HEAD") })
    private void callOnWorldJoin(CallbackInfo ci) {
        ImmediatelyFast.onWorldJoin();
    }
}