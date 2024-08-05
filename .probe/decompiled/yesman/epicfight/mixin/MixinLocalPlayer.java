package yesman.epicfight.mixin;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.game.ServerboundPlayerInputPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ LocalPlayer.class })
public abstract class MixinLocalPlayer {

    @Unique
    private final LocalPlayer epicfight$entity = (LocalPlayer) this;

    @Inject(at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;sendPosition()V", shift = Shift.BEFORE) }, method = { "tick()V" })
    private void epicfight_tick(CallbackInfo ci) {
        this.epicfight$entity.connection.send(new ServerboundPlayerInputPacket(this.epicfight$entity.f_20900_, this.epicfight$entity.f_20902_, this.epicfight$entity.input.jumping, this.epicfight$entity.input.shiftKeyDown));
    }
}