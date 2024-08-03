package dev.latvian.mods.kubejs.event;

@FunctionalInterface
public interface IEventHandler {

    Object onEvent(EventJS var1) throws EventExit;
}