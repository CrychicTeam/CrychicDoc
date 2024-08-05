package com.chunksending.mixin;

import com.chunksending.IChunksendingPlayer;
import java.util.List;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ ChunkHolder.class })
public class ChunkHolderMixin {

    @Shadow
    @Final
    ChunkPos pos;

    @Inject(method = { "broadcast" }, at = { @At("HEAD") }, cancellable = true)
    private void chunksending$onBroadCastChanges(List<ServerPlayer> players, Packet<?> packet, CallbackInfo ci) {
        for (ServerPlayer player : players) {
            if (!((IChunksendingPlayer) player).attachToPending(this.pos, packet)) {
                player.connection.send(packet);
            }
        }
        ci.cancel();
    }
}