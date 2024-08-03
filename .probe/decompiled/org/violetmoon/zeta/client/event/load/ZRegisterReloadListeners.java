package org.violetmoon.zeta.client.event.load;

import java.util.function.Consumer;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import org.violetmoon.zeta.event.bus.IZetaLoadEvent;

public record ZRegisterReloadListeners(Consumer<PreparableReloadListener> manager) implements IZetaLoadEvent, Consumer<PreparableReloadListener> {

    public void accept(PreparableReloadListener bleh) {
        this.manager.accept(bleh);
    }
}