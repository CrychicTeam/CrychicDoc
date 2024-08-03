package lio.playeranimatorapi.mixin;

import lio.playeranimatorapi.events.ClientPlayerTickEvent;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ Player.class })
public abstract class PlayerMixin {

    @Inject(method = { "tick" }, at = { @At("HEAD") })
    private void inject(CallbackInfo ci) {
        ClientPlayerTickEvent.tick((Player) this);
    }
}