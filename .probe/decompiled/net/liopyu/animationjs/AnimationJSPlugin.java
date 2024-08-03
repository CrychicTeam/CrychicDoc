package net.liopyu.animationjs;

import dev.latvian.mods.kubejs.KubeJSPlugin;
import net.liopyu.animationjs.events.EventHandlers;

public class AnimationJSPlugin extends KubeJSPlugin {

    @Override
    public void initStartup() {
    }

    @Override
    public void registerEvents() {
        EventHandlers.AnimationJS.register();
    }
}