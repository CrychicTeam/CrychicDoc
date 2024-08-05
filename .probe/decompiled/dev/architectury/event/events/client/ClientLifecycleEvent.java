package dev.architectury.event.events.client;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import dev.architectury.event.events.common.LifecycleEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public interface ClientLifecycleEvent {

    Event<ClientLifecycleEvent.ClientState> CLIENT_STARTED = EventFactory.createLoop();

    Event<ClientLifecycleEvent.ClientState> CLIENT_STOPPING = EventFactory.createLoop();

    Event<ClientLifecycleEvent.ClientLevelState> CLIENT_LEVEL_LOAD = EventFactory.createLoop();

    Event<ClientLifecycleEvent.ClientState> CLIENT_SETUP = EventFactory.createLoop();

    @OnlyIn(Dist.CLIENT)
    public interface ClientLevelState extends LifecycleEvent.LevelState<ClientLevel> {
    }

    @OnlyIn(Dist.CLIENT)
    public interface ClientState extends LifecycleEvent.InstanceState<Minecraft> {
    }
}