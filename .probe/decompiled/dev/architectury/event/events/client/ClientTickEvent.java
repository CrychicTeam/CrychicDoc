package dev.architectury.event.events.client;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public interface ClientTickEvent<T> {

    Event<ClientTickEvent.Client> CLIENT_PRE = EventFactory.createLoop();

    Event<ClientTickEvent.Client> CLIENT_POST = EventFactory.createLoop();

    Event<ClientTickEvent.ClientLevel> CLIENT_LEVEL_PRE = EventFactory.createLoop();

    Event<ClientTickEvent.ClientLevel> CLIENT_LEVEL_POST = EventFactory.createLoop();

    void tick(T var1);

    @OnlyIn(Dist.CLIENT)
    public interface Client extends ClientTickEvent<Minecraft> {
    }

    @OnlyIn(Dist.CLIENT)
    public interface ClientLevel extends ClientTickEvent<net.minecraft.client.multiplayer.ClientLevel> {
    }
}