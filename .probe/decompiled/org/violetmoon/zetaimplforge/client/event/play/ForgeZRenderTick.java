package org.violetmoon.zetaimplforge.client.event.play;

import net.minecraftforge.event.TickEvent;
import org.violetmoon.zeta.client.event.play.ZRenderTick;

public record ForgeZRenderTick(TickEvent.RenderTickEvent e) implements ZRenderTick {

    @Override
    public float getRenderTickTime() {
        return this.e.renderTickTime;
    }

    @Override
    public boolean isEndPhase() {
        return this.e.phase == TickEvent.Phase.END;
    }
}