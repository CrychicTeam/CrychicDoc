package dev.latvian.mods.kubejs.create.events;

import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;

public interface CreateEvents {

    EventGroup GROUP = EventGroup.of("CreateEvents");

    EventHandler SPECIAL_FLUID = GROUP.startup("pipeFluidEffect", () -> SpecialFluidHandlerEvent.class);

    EventHandler SPECIAL_SPOUT = GROUP.startup("spoutHandler", () -> SpecialSpoutHandlerEvent.class);

    EventHandler BOILER_HEATER = GROUP.startup("boilerHeatHandler", () -> BoilerHeaterHandlerEvent.class);
}