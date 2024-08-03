package com.xiaohunao.createheatjs;

import com.xiaohunao.createheatjs.event.registerHeatEvent;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;

public class KubeJSCreatePlugin extends KubeJSPlugin {

    public static final EventGroup GROUP = EventGroup.of("CreateHeatJS");

    public static final EventHandler REGISTRY_HEAT = GROUP.startup("registerHeatEvent", () -> registerHeatEvent.class);

    @Override
    public void initStartup() {
        REGISTRY_HEAT.post(new registerHeatEvent());
    }

    @Override
    public void registerEvents() {
        GROUP.register();
    }
}