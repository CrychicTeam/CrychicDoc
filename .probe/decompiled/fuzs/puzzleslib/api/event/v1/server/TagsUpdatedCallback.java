package fuzs.puzzleslib.api.event.v1.server;

import fuzs.puzzleslib.api.event.v1.core.EventInvoker;
import net.minecraft.core.RegistryAccess;

@FunctionalInterface
public interface TagsUpdatedCallback {

    EventInvoker<TagsUpdatedCallback> EVENT = EventInvoker.lookup(TagsUpdatedCallback.class);

    void onTagsUpdated(RegistryAccess var1, boolean var2);
}