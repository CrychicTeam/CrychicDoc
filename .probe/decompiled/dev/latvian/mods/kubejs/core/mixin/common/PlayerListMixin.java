package dev.latvian.mods.kubejs.core.mixin.common;

import dev.latvian.mods.kubejs.player.KubeJSPlayerEventHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ PlayerList.class })
public abstract class PlayerListMixin {

    @Inject(method = { "respawn" }, at = { @At("RETURN") })
    private void kjs$respawn(ServerPlayer serverPlayer, boolean keepData, CallbackInfoReturnable<ServerPlayer> cir) {
        KubeJSPlayerEventHandler.respawn(serverPlayer, (ServerPlayer) cir.getReturnValue(), keepData);
    }
}