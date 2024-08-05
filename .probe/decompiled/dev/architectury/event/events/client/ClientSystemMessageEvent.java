package dev.architectury.event.events.client;

import dev.architectury.event.CompoundEventResult;
import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public interface ClientSystemMessageEvent {

    Event<ClientSystemMessageEvent.Received> RECEIVED = EventFactory.createCompoundEventResult();

    @OnlyIn(Dist.CLIENT)
    public interface Received {

        CompoundEventResult<Component> process(Component var1);
    }
}