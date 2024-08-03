package dev.architectury.event.forge;

import dev.architectury.event.Event;
import dev.architectury.event.EventActor;
import dev.architectury.event.EventResult;
import java.util.function.Consumer;
import net.minecraftforge.common.MinecraftForge;
import org.jetbrains.annotations.ApiStatus.Internal;

public class EventFactoryImpl {

    public static <T> Event<Consumer<T>> attachToForge(Event<Consumer<T>> event) {
        event.register(eventObj -> {
            if (!(eventObj instanceof net.minecraftforge.eventbus.api.Event)) {
                throw new ClassCastException(eventObj.getClass() + " is not an instance of forge Event!");
            } else {
                MinecraftForge.EVENT_BUS.post((net.minecraftforge.eventbus.api.Event) eventObj);
            }
        });
        return event;
    }

    @Internal
    public static <T> Event<EventActor<T>> attachToForgeEventActor(Event<EventActor<T>> event) {
        event.register(eventObj -> {
            if (!(eventObj instanceof net.minecraftforge.eventbus.api.Event)) {
                throw new ClassCastException(eventObj.getClass() + " is not an instance of forge Event!");
            } else if (!((net.minecraftforge.eventbus.api.Event) eventObj).isCancelable()) {
                throw new ClassCastException(eventObj.getClass() + " is not cancellable Event!");
            } else {
                MinecraftForge.EVENT_BUS.post((net.minecraftforge.eventbus.api.Event) eventObj);
                return EventResult.pass();
            }
        });
        return event;
    }

    @Internal
    public static <T> Event<EventActor<T>> attachToForgeEventActorCancellable(Event<EventActor<T>> event) {
        event.register(eventObj -> {
            if (!(eventObj instanceof net.minecraftforge.eventbus.api.Event)) {
                throw new ClassCastException(eventObj.getClass() + " is not an instance of forge Event!");
            } else if (!((net.minecraftforge.eventbus.api.Event) eventObj).isCancelable()) {
                throw new ClassCastException(eventObj.getClass() + " is not cancellable Event!");
            } else {
                return MinecraftForge.EVENT_BUS.post((net.minecraftforge.eventbus.api.Event) eventObj) ? EventResult.interrupt(false) : EventResult.pass();
            }
        });
        return event;
    }
}