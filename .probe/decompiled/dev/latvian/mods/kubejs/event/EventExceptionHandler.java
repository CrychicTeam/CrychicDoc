package dev.latvian.mods.kubejs.event;

@FunctionalInterface
public interface EventExceptionHandler {

    Throwable handle(EventJS var1, EventHandlerContainer var2, Throwable var3);
}