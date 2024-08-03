package org.violetmoon.zeta.client.event.play;

import org.violetmoon.zeta.event.bus.IZetaPlayEvent;

public interface ZRenderTick extends IZetaPlayEvent {

    float getRenderTickTime();

    boolean isEndPhase();

    default boolean isStartPhase() {
        return !this.isEndPhase();
    }
}