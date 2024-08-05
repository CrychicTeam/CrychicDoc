package net.minecraftforge.event;

import java.util.function.BooleanSupplier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.LogicalSide;

public class TickEvent extends Event {

    public final TickEvent.Type type;

    public final LogicalSide side;

    public final TickEvent.Phase phase;

    public TickEvent(TickEvent.Type type, LogicalSide side, TickEvent.Phase phase) {
        this.type = type;
        this.side = side;
        this.phase = phase;
    }

    public static class ClientTickEvent extends TickEvent {

        public ClientTickEvent(TickEvent.Phase phase) {
            super(TickEvent.Type.CLIENT, LogicalSide.CLIENT, phase);
        }
    }

    public static class LevelTickEvent extends TickEvent {

        public final Level level;

        private final BooleanSupplier haveTime;

        public LevelTickEvent(LogicalSide side, TickEvent.Phase phase, Level level, BooleanSupplier haveTime) {
            super(TickEvent.Type.LEVEL, side, phase);
            this.level = level;
            this.haveTime = haveTime;
        }

        public boolean haveTime() {
            return this.haveTime.getAsBoolean();
        }
    }

    public static enum Phase {

        START, END
    }

    public static class PlayerTickEvent extends TickEvent {

        public final Player player;

        public PlayerTickEvent(TickEvent.Phase phase, Player player) {
            super(TickEvent.Type.PLAYER, player instanceof ServerPlayer ? LogicalSide.SERVER : LogicalSide.CLIENT, phase);
            this.player = player;
        }
    }

    public static class RenderTickEvent extends TickEvent {

        public final float renderTickTime;

        public RenderTickEvent(TickEvent.Phase phase, float renderTickTime) {
            super(TickEvent.Type.RENDER, LogicalSide.CLIENT, phase);
            this.renderTickTime = renderTickTime;
        }
    }

    public static class ServerTickEvent extends TickEvent {

        private final BooleanSupplier haveTime;

        private final MinecraftServer server;

        public ServerTickEvent(TickEvent.Phase phase, BooleanSupplier haveTime, MinecraftServer server) {
            super(TickEvent.Type.SERVER, LogicalSide.SERVER, phase);
            this.haveTime = haveTime;
            this.server = server;
        }

        public boolean haveTime() {
            return this.haveTime.getAsBoolean();
        }

        public MinecraftServer getServer() {
            return this.server;
        }
    }

    public static enum Type {

        LEVEL, PLAYER, CLIENT, SERVER, RENDER
    }
}