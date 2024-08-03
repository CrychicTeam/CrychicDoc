package net.minecraftforge.client.event;

import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.event.IModBusEvent;
import org.jetbrains.annotations.ApiStatus.Internal;

public class RegisterClientReloadListenersEvent extends Event implements IModBusEvent {

    private final ReloadableResourceManager resourceManager;

    @Internal
    public RegisterClientReloadListenersEvent(ReloadableResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    public void registerReloadListener(PreparableReloadListener reloadListener) {
        this.resourceManager.registerReloadListener(reloadListener);
    }
}