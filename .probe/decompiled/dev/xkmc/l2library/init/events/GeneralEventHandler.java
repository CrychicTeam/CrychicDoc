package dev.xkmc.l2library.init.events;

import dev.xkmc.l2library.init.explosion.BaseExplosion;
import dev.xkmc.l2library.serial.config.PacketHandlerWithConfig;
import dev.xkmc.l2library.util.raytrace.RayTraceUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = "l2library", bus = Bus.FORGE)
public class GeneralEventHandler {

    private static List<BooleanSupplier> TASKS = new ArrayList();

    @SubscribeEvent
    public static void addReloadListeners(AddReloadListenerEvent event) {
        PacketHandlerWithConfig.addReloadListeners(event);
    }

    @SubscribeEvent
    public static void onDatapackSync(OnDatapackSyncEvent event) {
        PacketHandlerWithConfig.onDatapackSync(event);
    }

    @SubscribeEvent
    public static void serverTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            RayTraceUtil.serverTick();
            execute();
        }
    }

    @SubscribeEvent
    public static void onDetonate(ExplosionEvent.Detonate event) {
        if (event.getExplosion() instanceof BaseExplosion exp) {
            event.getAffectedEntities().removeIf(e -> !exp.hurtEntity(e));
        }
    }

    public static synchronized void schedule(Runnable runnable) {
        TASKS.add((BooleanSupplier) () -> {
            runnable.run();
            return true;
        });
    }

    public static synchronized void schedulePersistent(BooleanSupplier runnable) {
        TASKS.add(runnable);
    }

    private static synchronized void execute() {
        if (!TASKS.isEmpty()) {
            List<BooleanSupplier> temp = TASKS;
            TASKS = new ArrayList();
            temp.removeIf(BooleanSupplier::getAsBoolean);
            temp.addAll(TASKS);
            TASKS = temp;
        }
    }
}