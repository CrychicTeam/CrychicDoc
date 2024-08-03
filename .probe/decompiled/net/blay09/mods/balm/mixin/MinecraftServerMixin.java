package net.blay09.mods.balm.mixin;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.event.server.ServerReloadFinishedEvent;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ MinecraftServer.class })
public class MinecraftServerMixin {

    @Inject(method = { "reloadResources(Ljava/util/Collection;)Ljava/util/concurrent/CompletableFuture;" }, at = { @At("RETURN") }, cancellable = true)
    private void reloadResources(Collection<String> collectionString0, CallbackInfoReturnable<CompletableFuture<Void>> callbackInfo) {
        ((CompletableFuture) callbackInfo.getReturnValue()).thenAccept(it -> Balm.getEvents().fireEvent(new ServerReloadFinishedEvent((MinecraftServer) this)));
    }
}