package dev.architectury.event.events.common;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import dev.architectury.event.EventResult;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public interface ChatEvent {

    Event<ChatEvent.Decorate> DECORATE = EventFactory.createLoop();

    Event<ChatEvent.Received> RECEIVED = EventFactory.createEventResult();

    public interface ChatComponent {

        Component get();

        void set(Component var1);
    }

    @FunctionalInterface
    public interface Decorate {

        void decorate(@Nullable ServerPlayer var1, ChatEvent.ChatComponent var2);
    }

    @FunctionalInterface
    public interface Received {

        EventResult received(@Nullable ServerPlayer var1, Component var2);
    }
}