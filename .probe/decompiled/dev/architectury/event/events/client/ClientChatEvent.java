package dev.architectury.event.events.client;

import dev.architectury.event.CompoundEventResult;
import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import dev.architectury.event.EventResult;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

@OnlyIn(Dist.CLIENT)
public interface ClientChatEvent {

    Event<ClientChatEvent.Send> SEND = EventFactory.createEventResult();

    Event<ClientChatEvent.Received> RECEIVED = EventFactory.createCompoundEventResult();

    @OnlyIn(Dist.CLIENT)
    public interface Received {

        CompoundEventResult<Component> process(ChatType.Bound var1, Component var2);
    }

    @OnlyIn(Dist.CLIENT)
    public interface Send {

        EventResult send(String var1, @Nullable Component var2);
    }
}