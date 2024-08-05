package me.fengming.renderjs.events;

import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;
import me.fengming.renderjs.events.level.RenderEntityEventJS;
import me.fengming.renderjs.events.level.RenderLevelEventJS;

public interface RenderJsEvents {

    EventGroup GROUP_LEVEL = EventGroup.of("RenderLevelEvents");

    EventGroup GROUP_ENTITY = EventGroup.of("RenderEntityEvents");

    EventHandler AFTER_RENDER_SOLID_BLOCK = GROUP_LEVEL.client("afterSolidBlockRender", () -> RenderLevelEventJS.After.class);

    EventHandler BEFORE_RENDER_ENTITY = GROUP_ENTITY.client("beforeRender", () -> RenderEntityEventJS.Before.class);

    EventHandler AFTER_RENDER_ENTITY = GROUP_ENTITY.client("afterRender", () -> RenderEntityEventJS.After.class);
}