package me.lucko.spark.forge;

import me.lucko.spark.common.tick.AbstractTickHook;
import me.lucko.spark.common.tick.TickHook;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ForgeTickHook extends AbstractTickHook implements TickHook {

    private final TickEvent.Type type;

    public ForgeTickHook(TickEvent.Type type) {
        this.type = type;
    }

    @SubscribeEvent
    public void onTick(TickEvent e) {
        if (e.phase == TickEvent.Phase.START) {
            if (e.type == this.type) {
                this.onTick();
            }
        }
    }

    @Override
    public void start() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void close() {
        MinecraftForge.EVENT_BUS.unregister(this);
    }
}