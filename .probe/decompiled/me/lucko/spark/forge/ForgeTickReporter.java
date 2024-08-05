package me.lucko.spark.forge;

import me.lucko.spark.common.tick.SimpleTickReporter;
import me.lucko.spark.common.tick.TickReporter;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ForgeTickReporter extends SimpleTickReporter implements TickReporter {

    private final TickEvent.Type type;

    public ForgeTickReporter(TickEvent.Type type) {
        this.type = type;
    }

    @SubscribeEvent
    public void onTick(TickEvent e) {
        if (e.type == this.type) {
            switch(e.phase) {
                case START:
                    this.onStart();
                    break;
                case END:
                    this.onEnd();
                    break;
                default:
                    throw new AssertionError(e.phase);
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
        super.close();
    }
}