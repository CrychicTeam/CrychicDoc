package org.embeddedt.embeddium.api.eventbus;

import net.minecraftforge.eventbus.api.Event;

public abstract class EmbeddiumEvent extends Event {

    public boolean isCancelable() {
        return false;
    }

    public boolean isCanceled() {
        return super.isCanceled();
    }

    public void setCanceled(boolean cancel) {
        super.setCanceled(cancel);
    }
}