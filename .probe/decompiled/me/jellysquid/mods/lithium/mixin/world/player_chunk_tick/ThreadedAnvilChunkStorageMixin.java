package me.jellysquid.mods.lithium.mixin.world.player_chunk_tick;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import me.jellysquid.mods.lithium.common.util.Pos;
import net.minecraft.core.SectionPos;
import net.minecraft.network.protocol.game.ClientboundLevelChunkWithLightPacket;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.PlayerMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;
import org.apache.commons.lang3.mutable.MutableObject;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({ ChunkMap.class })
public abstract class ThreadedAnvilChunkStorageMixin {

    @Shadow
    @Final
    private Int2ObjectMap<ChunkMap.TrackedEntity> entityMap;

    @Shadow
    @Final
    ServerLevel level;

    @Shadow
    @Final
    private PlayerMap playerMap;

    @Shadow
    @Final
    private ChunkMap.DistanceManager distanceManager;

    @Shadow
    int viewDistance;

    @Shadow
    protected abstract void playerLoadedChunk(ServerPlayer var1, MutableObject<ClientboundLevelChunkWithLightPacket> var2, LevelChunk var3);

    @Shadow
    protected abstract SectionPos updatePlayerPos(ServerPlayer var1);

    @Overwrite
    public void move(ServerPlayer player) {
        ObjectIterator oldPos = this.entityMap.values().iterator();
        while (oldPos.hasNext()) {
            ChunkMap.TrackedEntity tracker = (ChunkMap.TrackedEntity) oldPos.next();
            if (tracker.entity == player) {
                tracker.updatePlayers(this.level.players());
            } else {
                tracker.updatePlayer(player);
            }
        }
        SectionPos oldPosx = player.getLastSectionPos();
        SectionPos newPos = SectionPos.of(player);
        boolean isWatchingWorld = this.playerMap.ignored(player);
        boolean doesNotGenerateChunks = this.skipPlayer(player);
        boolean movedChunks = oldPosx.m_123341_() != newPos.m_123341_() || oldPosx.m_123343_() != newPos.m_123343_();
        boolean movedSections = movedChunks || oldPosx.m_123342_() != newPos.m_123342_();
        if (movedSections || isWatchingWorld != doesNotGenerateChunks) {
            this.updatePlayerPos(player);
            if (!isWatchingWorld) {
                this.distanceManager.m_140828_(oldPosx, player);
            }
            if (!doesNotGenerateChunks) {
                this.distanceManager.m_140802_(newPos, player);
            }
            if (!isWatchingWorld && doesNotGenerateChunks) {
                this.playerMap.ignorePlayer(player);
            }
            if (isWatchingWorld && !doesNotGenerateChunks) {
                this.playerMap.unIgnorePlayer(player);
            }
            if (movedChunks) {
                long oldChunkPos = ChunkPos.asLong(oldPosx.m_123341_(), oldPosx.m_123343_());
                long newChunkPos = ChunkPos.asLong(newPos.m_123341_(), newPos.m_123343_());
                this.playerMap.updatePlayer(oldChunkPos, newChunkPos, player);
            }
        }
        if (movedChunks && player.m_9236_() == this.level) {
            this.sendChunks(oldPosx, player);
        }
    }

    private void sendChunks(SectionPos oldPos, ServerPlayer player) {
        int newCenterX = Pos.ChunkCoord.fromBlockCoord(Mth.floor(player.m_20185_()));
        int newCenterZ = Pos.ChunkCoord.fromBlockCoord(Mth.floor(player.m_20189_()));
        int oldCenterX = oldPos.x();
        int oldCenterZ = oldPos.z();
        int watchRadius = this.viewDistance;
        int watchRadiusIncr = watchRadius + 1;
        int watchDiameter = watchRadiusIncr * 2;
        if (Math.abs(oldCenterX - newCenterX) <= watchDiameter && Math.abs(oldCenterZ - newCenterZ) <= watchDiameter) {
            int minX = Math.min(newCenterX, oldCenterX) - watchRadiusIncr;
            int minZ = Math.min(newCenterZ, oldCenterZ) - watchRadiusIncr;
            int maxX = Math.max(newCenterX, oldCenterX) + watchRadiusIncr;
            int maxZ = Math.max(newCenterZ, oldCenterZ) + watchRadiusIncr;
            for (int x = minX; x <= maxX; x++) {
                for (int z = minZ; z <= maxZ; z++) {
                    boolean isWithinOldRadius = ChunkMap.isChunkInRange(x, z, oldCenterX, oldCenterZ, watchRadius);
                    boolean isWithinNewRadius = ChunkMap.isChunkInRange(x, z, newCenterX, newCenterZ, watchRadius);
                    if (isWithinNewRadius && !isWithinOldRadius) {
                        this.startWatchingChunk(player, x, z);
                    }
                    if (isWithinOldRadius && !isWithinNewRadius) {
                        this.stopWatchingChunk(player, x, z);
                    }
                }
            }
        } else {
            for (int x = oldCenterX - watchRadiusIncr; x <= oldCenterX + watchRadiusIncr; x++) {
                for (int z = oldCenterZ - watchRadiusIncr; z <= oldCenterZ + watchRadiusIncr; z++) {
                    if (ChunkMap.isChunkInRange(x, z, oldCenterX, oldCenterZ, watchRadius)) {
                        this.stopWatchingChunk(player, x, z);
                    }
                }
            }
            for (int x = newCenterX - watchRadiusIncr; x <= newCenterX + watchRadiusIncr; x++) {
                for (int zx = newCenterZ - watchRadiusIncr; zx <= newCenterZ + watchRadiusIncr; zx++) {
                    if (ChunkMap.isChunkInRange(x, zx, newCenterX, newCenterZ, watchRadius)) {
                        this.startWatchingChunk(player, x, zx);
                    }
                }
            }
        }
    }

    protected void startWatchingChunk(ServerPlayer player, int x, int z) {
        ChunkHolder holder = this.getVisibleChunkIfPresent(ChunkPos.asLong(x, z));
        if (holder != null) {
            LevelChunk chunk = holder.getTickingChunk();
            if (chunk != null) {
                this.playerLoadedChunk(player, new MutableObject(), chunk);
            }
        }
    }

    protected void stopWatchingChunk(ServerPlayer player, int x, int z) {
        player.untrackChunk(new ChunkPos(x, z));
    }

    @Shadow
    protected abstract boolean skipPlayer(ServerPlayer var1);

    @Shadow
    protected abstract ChunkHolder getVisibleChunkIfPresent(long var1);
}