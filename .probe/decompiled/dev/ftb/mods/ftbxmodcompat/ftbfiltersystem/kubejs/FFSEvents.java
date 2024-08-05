package dev.ftb.mods.ftbxmodcompat.ftbfiltersystem.kubejs;

import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;
import dev.latvian.mods.kubejs.event.Extra;

public interface FFSEvents {

    EventGroup EVENT_GROUP = EventGroup.of("FTBFilterSystemEvents");

    EventHandler CUSTOM_FILTER = EVENT_GROUP.server("customFilter", () -> CustomFilterEventJS.class).extra(Extra.STRING).hasResult();
}