package me.jellysquid.mods.sodium.mixin.core.world.map;

import java.util.function.Consumer;
import me.jellysquid.mods.sodium.client.render.chunk.map.ChunkTrackerHolder;
import net.minecraft.client.multiplayer.ClientChunkCache;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundLevelChunkPacketData;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ ClientChunkCache.class })
public class ClientChunkManagerMixin {

    @Shadow
    @Final
    ClientLevel level;

    @Inject(method = { "drop" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientChunkCache$Storage;replace(ILnet/minecraft/world/level/chunk/LevelChunk;Lnet/minecraft/world/level/chunk/LevelChunk;)Lnet/minecraft/world/level/chunk/LevelChunk;", shift = Shift.AFTER) })
    private void onChunkUnloaded(int chunkX, int chunkZ, CallbackInfo ci) {
        ChunkTrackerHolder.get(this.level).onChunkStatusRemoved(chunkX, chunkZ, 1);
    }

    @Inject(method = { "replaceWithPacketData" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;onChunkLoaded(Lnet/minecraft/world/level/ChunkPos;)V", shift = Shift.AFTER) })
    private void onChunkLoaded(int chunkX, int chunkZ, FriendlyByteBuf buf, CompoundTag nbt, Consumer<ClientboundLevelChunkPacketData.BlockEntityTagOutput> consumer, CallbackInfoReturnable<LevelChunk> cir) {
        ChunkTrackerHolder.get(this.level).onChunkStatusAdded(chunkX, chunkZ, 1);
    }
}