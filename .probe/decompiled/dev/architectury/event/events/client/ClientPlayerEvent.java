package dev.architectury.event.events.client;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

@OnlyIn(Dist.CLIENT)
public interface ClientPlayerEvent {

    Event<ClientPlayerEvent.ClientPlayerJoin> CLIENT_PLAYER_JOIN = EventFactory.createLoop();

    Event<ClientPlayerEvent.ClientPlayerQuit> CLIENT_PLAYER_QUIT = EventFactory.createLoop();

    Event<ClientPlayerEvent.ClientPlayerRespawn> CLIENT_PLAYER_RESPAWN = EventFactory.createLoop();

    @OnlyIn(Dist.CLIENT)
    public interface ClientPlayerJoin {

        void join(LocalPlayer var1);
    }

    @OnlyIn(Dist.CLIENT)
    public interface ClientPlayerQuit {

        void quit(@Nullable LocalPlayer var1);
    }

    @OnlyIn(Dist.CLIENT)
    public interface ClientPlayerRespawn {

        void respawn(LocalPlayer var1, LocalPlayer var2);
    }
}