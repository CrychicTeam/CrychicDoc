package journeymap.common.mixin.client;

import journeymap.client.event.handlers.DeathPointHandler;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ LocalPlayer.class })
public class LocalPlayerMixin {

    private final DeathPointHandler deathPointHandler = new DeathPointHandler();

    @Inject(method = { "handleEntityEvent(B)V" }, at = { @At("HEAD") })
    public void journeymap_handleEntityEvent(byte id, CallbackInfo ci) {
        if (id == 3) {
            this.deathPointHandler.handlePlayerDeath();
        }
    }
}