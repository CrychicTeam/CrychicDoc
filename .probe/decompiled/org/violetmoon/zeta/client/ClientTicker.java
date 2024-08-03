package org.violetmoon.zeta.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import org.violetmoon.zeta.client.event.play.ZClientTick;
import org.violetmoon.zeta.client.event.play.ZRenderTick;
import org.violetmoon.zeta.event.bus.PlayEvent;
import org.violetmoon.zeta.event.bus.ZPhase;

public final class ClientTicker {

    public int ticksInGame = 0;

    public float partialTicks = 0.0F;

    public float delta = 0.0F;

    public float total = 0.0F;

    @PlayEvent
    public void onRenderTick(ZRenderTick event) {
        if (event.isStartPhase()) {
            this.partialTicks = event.getRenderTickTime();
        } else {
            this.endRenderTick();
        }
    }

    @PlayEvent
    public void onEndClientTick(ZClientTick event) {
        if (event.getPhase() == ZPhase.END) {
            Screen gui = Minecraft.getInstance().screen;
            if (gui == null || !gui.isPauseScreen()) {
                this.ticksInGame++;
                this.partialTicks = 0.0F;
            }
            this.endRenderTick();
        }
    }

    public void endRenderTick() {
        float oldTotal = this.total;
        this.total = (float) this.ticksInGame + this.partialTicks;
        this.delta = this.total - oldTotal;
    }
}