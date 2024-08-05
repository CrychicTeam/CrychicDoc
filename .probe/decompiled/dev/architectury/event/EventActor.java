package dev.architectury.event;

@FunctionalInterface
public interface EventActor<T> {

    EventResult act(T var1);
}