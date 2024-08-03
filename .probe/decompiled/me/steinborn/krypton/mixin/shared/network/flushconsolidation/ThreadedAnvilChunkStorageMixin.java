package me.steinborn.krypton.mixin.shared.network.flushconsolidation;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import me.steinborn.krypton.mod.shared.network.util.AutoFlushUtil;
import me.steinborn.krypton.mod.shared.player.KryptonServerPlayerEntity;
import net.minecraft.core.SectionPos;
import net.minecraft.network.protocol.game.ClientboundLevelChunkWithLightPacket;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.PlayerMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import org.apache.commons.lang3.mutable.MutableObject;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ ChunkMap.class })
public abstract class ThreadedAnvilChunkStorageMixin {

    @Shadow
    @Final
    private Int2ObjectMap<ChunkMap.TrackedEntity> entityMap;

    @Shadow
    @Final
    private PlayerMap playerMap;

    @Shadow
    @Final
    private ServerLevel level;

    @Shadow
    @Final
    private ChunkMap.DistanceManager distanceManager;

    @Shadow
    private int viewDistance;

    @Shadow
    public static boolean isChunkInRange(int x1, int y1, int x2, int y2, int maxDistance) {
        throw new AssertionError("pedantic");
    }

    @Overwrite
    public void updatePlayerStatus(ServerPlayer player, boolean added) {
        boolean doesNotGenerateChunks = this.skipPlayer(player);
        boolean isWatchingWorld = !this.playerMap.ignoredOrUnknown(player);
        int chunkPosX = SectionPos.blockToSectionCoord(player.m_146903_());
        int chunkPosZ = SectionPos.blockToSectionCoord(player.m_146907_());
        AutoFlushUtil.setAutoFlush(player, false);
        try {
            if (added) {
                this.playerMap.addPlayer(ChunkPos.asLong(chunkPosX, chunkPosZ), player, doesNotGenerateChunks);
                this.updatePlayerPos(player);
                if (!doesNotGenerateChunks) {
                    this.distanceManager.m_140802_(SectionPos.of(player), player);
                }
                this.sendSpiralChunkWatchPackets(player);
            } else {
                SectionPos chunkSectionPos = player.getLastSectionPos();
                this.playerMap.removePlayer(chunkSectionPos.chunk().toLong(), player);
                if (isWatchingWorld) {
                    this.distanceManager.m_140828_(chunkSectionPos, player);
                }
                this.unloadChunks(player, chunkPosX, chunkPosZ, this.viewDistance);
            }
        } finally {
            AutoFlushUtil.setAutoFlush(player, true);
        }
    }

    @Overwrite
    public void move(ServerPlayer player) {
        ObjectIterator oldPos = this.entityMap.values().iterator();
        while (oldPos.hasNext()) {
            ChunkMap.TrackedEntity entityTracker = (ChunkMap.TrackedEntity) oldPos.next();
            if (entityTracker.entity == player) {
                entityTracker.updatePlayers(this.level.players());
            } else {
                entityTracker.updatePlayer(player);
            }
        }
        SectionPos oldPosx = player.getLastSectionPos();
        SectionPos newPos = SectionPos.of(player);
        boolean isWatchingWorld = this.playerMap.ignored(player);
        boolean noChunkGen = this.skipPlayer(player);
        boolean movedSections = !oldPosx.equals(newPos);
        if (movedSections || isWatchingWorld != noChunkGen) {
            this.updatePlayerPos(player);
            if (!isWatchingWorld) {
                this.distanceManager.m_140828_(oldPosx, player);
            }
            if (!noChunkGen) {
                this.distanceManager.m_140802_(newPos, player);
            }
            if (!isWatchingWorld && noChunkGen) {
                this.playerMap.ignorePlayer(player);
            }
            if (isWatchingWorld && !noChunkGen) {
                this.playerMap.unIgnorePlayer(player);
            }
            long oldChunkPos = ChunkPos.asLong(oldPosx.m_123341_(), oldPosx.m_123343_());
            long newChunkPos = ChunkPos.asLong(newPos.m_123341_(), newPos.m_123343_());
            this.playerMap.updatePlayer(oldChunkPos, newChunkPos, player);
        }
        if (player.m_9236_() == this.level) {
            this.sendChunkWatchPackets(oldPosx, player);
        }
    }

    @Inject(method = { "tickEntityMovement" }, at = { @At("HEAD") })
    public void disableAutoFlushForEntityTracking(CallbackInfo info) {
        for (ServerPlayer player : this.level.players()) {
            AutoFlushUtil.setAutoFlush(player, false);
        }
    }

    @Inject(method = { "tickEntityMovement" }, at = { @At("RETURN") })
    public void enableAutoFlushForEntityTracking(CallbackInfo info) {
        for (ServerPlayer player : this.level.players()) {
            AutoFlushUtil.setAutoFlush(player, true);
        }
    }

    @Shadow
    public abstract void updateChunkTracking(ServerPlayer var1, ChunkPos var2, MutableObject<ClientboundLevelChunkWithLightPacket> var3, boolean var4, boolean var5);

    @Shadow
    protected abstract boolean skipPlayer(ServerPlayer var1);

    @Shadow
    protected abstract SectionPos updatePlayerPos(ServerPlayer var1);

    private void sendChunkWatchPackets(SectionPos oldPos, ServerPlayer player) {
        AutoFlushUtil.setAutoFlush(player, false);
        try {
            int oldChunkX = oldPos.x();
            int oldChunkZ = oldPos.z();
            int newChunkX = SectionPos.blockToSectionCoord(player.m_146903_());
            int newChunkZ = SectionPos.blockToSectionCoord(player.m_146907_());
            int playerViewDistance = this.getPlayerViewDistance(player);
            if (this.shouldReloadAllChunks(player)) {
                if (player instanceof KryptonServerPlayerEntity kryptonPlayer) {
                    kryptonPlayer.setNeedsChunksReloaded(false);
                }
                for (int curX = newChunkX - this.viewDistance - 1; curX <= newChunkX + this.viewDistance + 1; curX++) {
                    for (int curZ = newChunkZ - this.viewDistance - 1; curZ <= newChunkZ + this.viewDistance + 1; curZ++) {
                        ChunkPos chunkPos = new ChunkPos(curX, curZ);
                        boolean inNew = isChunkInRange(curX, curZ, newChunkX, newChunkZ, playerViewDistance);
                        this.updateChunkTracking(player, chunkPos, new MutableObject(), true, inNew);
                    }
                }
                this.sendSpiralChunkWatchPackets(player);
            } else if (Math.abs(oldChunkX - newChunkX) <= playerViewDistance * 2 && Math.abs(oldChunkZ - newChunkZ) <= playerViewDistance * 2) {
                int minSendChunkX = Math.min(newChunkX, oldChunkX) - playerViewDistance - 1;
                int minSendChunkZ = Math.min(newChunkZ, oldChunkZ) - playerViewDistance - 1;
                int maxSendChunkX = Math.max(newChunkX, oldChunkX) + playerViewDistance + 1;
                int maxSendChunkZ = Math.max(newChunkZ, oldChunkZ) + playerViewDistance + 1;
                for (int curX = minSendChunkX; curX <= maxSendChunkX; curX++) {
                    for (int curZ = minSendChunkZ; curZ <= maxSendChunkZ; curZ++) {
                        ChunkPos chunkPos = new ChunkPos(curX, curZ);
                        boolean inOld = isChunkInRange(curX, curZ, oldChunkX, oldChunkZ, playerViewDistance);
                        boolean inNew = isChunkInRange(curX, curZ, newChunkX, newChunkZ, playerViewDistance);
                        this.updateChunkTracking(player, chunkPos, new MutableObject(), inOld, inNew);
                    }
                }
            } else {
                this.unloadChunks(player, oldChunkX, oldChunkZ, this.viewDistance);
                this.sendSpiralChunkWatchPackets(player);
            }
        } finally {
            AutoFlushUtil.setAutoFlush(player, true);
        }
    }

    private void sendSpiralChunkWatchPackets(ServerPlayer player) {
        int chunkPosX = SectionPos.blockToSectionCoord(player.m_146903_());
        int chunkPosZ = SectionPos.blockToSectionCoord(player.m_146907_());
        int playerViewDistance = this.getPlayerViewDistance(player) + 1;
        int x = 0;
        int z = 0;
        int dx = 0;
        int dz = -1;
        int t = playerViewDistance * 2;
        int maxI = t * t * 2;
        for (int i = 0; i < maxI; i++) {
            if (-playerViewDistance <= x && x <= playerViewDistance && -playerViewDistance <= z && z <= playerViewDistance) {
                boolean inNew = isChunkInRange(chunkPosX, chunkPosZ, chunkPosX + x, chunkPosZ + z, playerViewDistance);
                this.updateChunkTracking(player, new ChunkPos(chunkPosX + x, chunkPosZ + z), new MutableObject(), false, inNew);
            }
            if (x == z || x < 0 && x == -z || x > 0 && x == 1 - z) {
                t = dx;
                dx = -dz;
                dz = t;
            }
            x += dx;
            z += dz;
        }
    }

    private void unloadChunks(ServerPlayer player, int chunkPosX, int chunkPosZ, int distance) {
        for (int curX = chunkPosX - distance - 1; curX <= chunkPosX + distance + 1; curX++) {
            for (int curZ = chunkPosZ - distance - 1; curZ <= chunkPosZ + distance + 1; curZ++) {
                ChunkPos chunkPos = new ChunkPos(curX, curZ);
                this.updateChunkTracking(player, chunkPos, new MutableObject(), true, false);
            }
        }
    }

    private int getPlayerViewDistance(ServerPlayer playerEntity) {
        return playerEntity instanceof KryptonServerPlayerEntity kryptonPlayerEntity ? (kryptonPlayerEntity.getPlayerViewDistance() != -1 ? Math.min(this.viewDistance, kryptonPlayerEntity.getPlayerViewDistance() + 1) : this.viewDistance) : this.viewDistance;
    }

    private boolean shouldReloadAllChunks(ServerPlayer playerEntity) {
        if (playerEntity instanceof KryptonServerPlayerEntity kryptonPlayerEntity && kryptonPlayerEntity.getNeedsChunksReloaded()) {
            return true;
        }
        return false;
    }
}