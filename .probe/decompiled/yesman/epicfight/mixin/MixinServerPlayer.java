package yesman.epicfight.mixin;

import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.network.EpicFightNetworkManager;
import yesman.epicfight.network.server.SPUpdatePlayerInput;

@Mixin({ ServerPlayer.class })
public abstract class MixinServerPlayer {

    @Inject(at = { @At("HEAD") }, method = { "setPlayerInput(FFZZ)V" }, cancellable = true)
    private void epicfight_setPlayerInput(float xxa, float zza, boolean jump, boolean shift, CallbackInfo info) {
        ServerPlayer self = (ServerPlayer) this;
        if (xxa >= -1.0F && xxa <= 1.0F) {
            self.f_20900_ = xxa;
        }
        if (zza >= -1.0F && zza <= 1.0F) {
            self.f_20902_ = zza;
        }
        self.m_6862_(jump);
        self.m_20260_(shift);
        SPUpdatePlayerInput packet = new SPUpdatePlayerInput(self.m_19879_(), xxa, zza);
        EpicFightNetworkManager.sendToAllPlayerTrackingThisEntity(packet, self);
        info.cancel();
    }
}