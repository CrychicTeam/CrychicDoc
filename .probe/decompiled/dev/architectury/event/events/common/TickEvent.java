package dev.architectury.event.events.common;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

public interface TickEvent<T> {

    Event<TickEvent.Server> SERVER_PRE = EventFactory.createLoop();

    Event<TickEvent.Server> SERVER_POST = EventFactory.createLoop();

    Event<TickEvent.ServerLevelTick> SERVER_LEVEL_PRE = EventFactory.createLoop();

    Event<TickEvent.ServerLevelTick> SERVER_LEVEL_POST = EventFactory.createLoop();

    Event<TickEvent.Player> PLAYER_PRE = EventFactory.createLoop();

    Event<TickEvent.Player> PLAYER_POST = EventFactory.createLoop();

    void tick(T var1);

    public interface LevelTick<T extends Level> extends TickEvent<T> {
    }

    public interface Player extends TickEvent<net.minecraft.world.entity.player.Player> {
    }

    public interface Server extends TickEvent<MinecraftServer> {
    }

    public interface ServerLevelTick extends TickEvent.LevelTick<ServerLevel> {
    }
}