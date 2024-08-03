package dev.latvian.mods.kubejs.forge;

import java.util.function.Consumer;
import net.minecraftforge.eventbus.api.Event;

@FunctionalInterface
public interface ForgeEventConsumer extends Consumer<Event> {
}