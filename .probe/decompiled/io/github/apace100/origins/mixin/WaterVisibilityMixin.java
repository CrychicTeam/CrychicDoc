package io.github.apace100.origins.mixin;

import com.mojang.authlib.GameProfile;
import io.github.edwinmindcraft.origins.common.power.WaterVisionPower;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@OnlyIn(Dist.CLIENT)
@Mixin({ LocalPlayer.class })
public abstract class WaterVisibilityMixin extends AbstractClientPlayer {

    public WaterVisibilityMixin(ClientLevel level, GameProfile profile) {
        super(level, profile);
    }

    @Inject(at = { @At("HEAD") }, method = { "getWaterVision" }, cancellable = true)
    private void getUnderwaterVisibility(CallbackInfoReturnable<Float> info) {
        WaterVisionPower.getWaterVisionStrength(this).ifPresent(info::setReturnValue);
    }
}