package com.craisinlord.integrated_api.events.lifecycle;

import com.craisinlord.integrated_api.events.base.EventHandler;
import java.util.function.BiConsumer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;

public record RegisterReloadListenerEvent(BiConsumer<ResourceLocation, PreparableReloadListener> registrar) {

    public static final EventHandler<RegisterReloadListenerEvent> EVENT = new EventHandler<>();

    public void register(ResourceLocation id, PreparableReloadListener listener) {
        this.registrar.accept(id, listener);
    }
}