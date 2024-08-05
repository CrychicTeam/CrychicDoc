package dev.shadowsoffire.placebo.util;

import dev.shadowsoffire.placebo.Placebo;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Queue;
import java.util.function.BooleanSupplier;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import org.apache.commons.lang3.tuple.Pair;

@EventBusSubscriber(modid = "placebo", bus = Bus.FORGE)
public class PlaceboTaskQueue {

    private static final Queue<Pair<String, BooleanSupplier>> TASKS = new ArrayDeque();

    @SubscribeEvent
    public static void tick(TickEvent.ServerTickEvent e) {
        if (e.phase == TickEvent.Phase.END) {
            Iterator<Pair<String, BooleanSupplier>> it = TASKS.iterator();
            Pair<String, BooleanSupplier> current = null;
            while (it.hasNext()) {
                current = (Pair<String, BooleanSupplier>) it.next();
                try {
                    if (((BooleanSupplier) current.getRight()).getAsBoolean()) {
                        it.remove();
                    }
                } catch (Exception var4) {
                    Placebo.LOGGER.error("An exception occurred while running a ticking task with ID {}.  It will be terminated.", current.getLeft());
                    it.remove();
                    var4.printStackTrace();
                }
            }
        }
    }

    @SubscribeEvent
    public static void stopped(ServerStoppedEvent e) {
        TASKS.clear();
    }

    @SubscribeEvent
    public static void started(ServerStartedEvent e) {
        TASKS.clear();
    }

    public static void submitTask(String id, BooleanSupplier task) {
        TASKS.add(Pair.of(id, task));
    }
}