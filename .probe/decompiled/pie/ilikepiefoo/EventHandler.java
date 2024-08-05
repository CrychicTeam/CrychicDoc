package pie.ilikepiefoo;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.EntityEvent;
import dev.architectury.event.events.common.PlayerEvent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import pie.ilikepiefoo.events.AdditionalEvents;
import pie.ilikepiefoo.events.EntityEnterChunkEventJS;
import pie.ilikepiefoo.events.EntityTameEventJS;
import pie.ilikepiefoo.events.PlayerChangeDimensionEventJS;
import pie.ilikepiefoo.events.PlayerCloneEventJS;
import pie.ilikepiefoo.events.PlayerRespawnEventJS;

public class EventHandler {

    public static void init() {
        PlayerEvent.CHANGE_DIMENSION.register(EventHandler::onPlayerChangeDimension);
        PlayerEvent.PLAYER_CLONE.register(EventHandler::onPlayerClone);
        PlayerEvent.PLAYER_RESPAWN.register(EventHandler::onPlayerRespawn);
        EntityEvent.ENTER_SECTION.register(EventHandler::onEntityEnterChunk);
        EntityEvent.ANIMAL_TAME.register(EventHandler::onEntityTame);
    }

    public static void onPlayerChangeDimension(ServerPlayer player, ResourceKey<Level> oldLevel, ResourceKey<Level> newLevel) {
        AdditionalEvents.PLAYER_CHANGE_DIMENSION.post(new PlayerChangeDimensionEventJS(player, oldLevel, newLevel));
    }

    private static void onPlayerClone(ServerPlayer oldPlayer, ServerPlayer newPlayer, boolean conqueredEnd) {
        AdditionalEvents.PLAYER_CLONE.post(new PlayerCloneEventJS(oldPlayer, newPlayer, conqueredEnd));
    }

    private static void onPlayerRespawn(ServerPlayer serverPlayer, boolean conqueredEnd) {
        AdditionalEvents.PLAYER_RESPAWN.post(new PlayerRespawnEventJS(serverPlayer, conqueredEnd));
    }

    private static void onEntityEnterChunk(Entity entity, int chunkX, int chunkY, int chunkZ, int prevX, int prevY, int prevZ) {
        AdditionalEvents.ENTITY_ENTER_CHUNK.post(new EntityEnterChunkEventJS(entity, chunkX, chunkY, chunkZ, prevX, prevY, prevZ));
    }

    private static EventResult onEntityTame(Animal animal, Player player) {
        return player instanceof ServerPlayer serverPlayer ? AdditionalEvents.ENTITY_TAME.post(new EntityTameEventJS(animal, player)).arch() : EventResult.pass();
    }
}