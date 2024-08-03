package me.steinborn.krypton.mixin.shared.network.avoidwork;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import me.steinborn.krypton.mod.shared.WorldEntityByChunkAccess;
import net.minecraft.network.protocol.game.ClientboundLevelChunkWithLightPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityLinkPacket;
import net.minecraft.network.protocol.game.ClientboundSetPassengersPacket;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.chunk.LevelChunk;
import org.apache.commons.lang3.mutable.MutableObject;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ ChunkMap.class })
public class ThreadedAnvilChunkStorageMixin {

    @Shadow
    @Final
    private Int2ObjectMap<ChunkMap.TrackedEntity> entityMap;

    @Inject(method = { "sendChunkDataPackets" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/server/network/DebugInfoSender;sendChunkWatchingChange(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/ChunkPos;)V", shift = Shift.AFTER, by = 1) })
    public void sendChunkDataPackets$beSmart(ServerPlayer player, MutableObject<ClientboundLevelChunkWithLightPacket> mutableObject, LevelChunk chunk, CallbackInfo ci) {
        Collection<Entity> entitiesInChunk = ((WorldEntityByChunkAccess) chunk.getLevel()).getEntitiesInChunk(chunk.m_7697_().x, chunk.m_7697_().z);
        List<Entity> attachmentsToSend = new ArrayList();
        List<Entity> passengersToSend = new ArrayList();
        for (Entity entity : entitiesInChunk) {
            ChunkMap.TrackedEntity entityTracker = (ChunkMap.TrackedEntity) this.entityMap.get(entity.getId());
            if (entityTracker != null) {
                entityTracker.updatePlayer(player);
                if (entity instanceof Mob && ((Mob) entity).getLeashHolder() != null) {
                    attachmentsToSend.add(entity);
                }
                if (!entity.getPassengers().isEmpty()) {
                    passengersToSend.add(entity);
                }
            }
        }
        if (!attachmentsToSend.isEmpty()) {
            for (Entity entityx : attachmentsToSend) {
                player.connection.send(new ClientboundSetEntityLinkPacket(entityx, ((Mob) entityx).getLeashHolder()));
            }
        }
        if (!passengersToSend.isEmpty()) {
            for (Entity entityx : passengersToSend) {
                player.connection.send(new ClientboundSetPassengersPacket(entityx));
            }
        }
    }

    @Redirect(method = { "sendChunkDataPackets" }, at = @At(value = "FIELD", target = "Lnet/minecraft/server/world/ThreadedAnvilChunkStorage;entityTrackers:Lit/unimi/dsi/fastutil/ints/Int2ObjectMap;", opcode = 180))
    public Int2ObjectMap<Entity> sendChunkDataPackets$nullifyRest(ChunkMap tacs) {
        return Int2ObjectMaps.emptyMap();
    }
}