package org.violetmoon.zetaimplforge.client.event.play;

import net.minecraftforge.event.TickEvent;
import org.violetmoon.zeta.client.event.play.ZClientTick;
import org.violetmoon.zeta.event.bus.ZPhase;

public class ForgeZClientTick implements ZClientTick {

    private final TickEvent.ClientTickEvent e;

    public ForgeZClientTick(TickEvent.ClientTickEvent e) {
        this.e = e;
    }

    @Override
    public ZPhase getPhase() {
        return from(this.e.phase);
    }

    public static ZPhase from(TickEvent.Phase r) {
        return switch(r) {
            case START ->
                ZPhase.START;
            case END ->
                ZPhase.END;
        };
    }

    public static TickEvent.Phase to(ZPhase r) {
        return switch(r) {
            case START ->
                TickEvent.Phase.START;
            case END ->
                TickEvent.Phase.END;
        };
    }
}