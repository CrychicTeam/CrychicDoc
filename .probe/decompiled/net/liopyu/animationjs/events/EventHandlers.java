package net.liopyu.animationjs.events;

import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;

public class EventHandlers {

    public static final EventGroup AnimationJS = EventGroup.of("AnimationJS");

    public static final EventHandler universalController = AnimationJS.server("universalController", () -> UniversalController.class);

    public static final EventHandler playerRenderer = AnimationJS.client("playerRenderer", () -> PlayerRenderer.class);
}