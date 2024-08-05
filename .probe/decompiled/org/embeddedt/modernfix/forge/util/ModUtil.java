package org.embeddedt.modernfix.forge.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinWorkerThread;
import java.util.function.Supplier;
import net.minecraftforge.eventbus.EventBus;
import net.minecraftforge.eventbus.api.EventListenerHelper;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import org.embeddedt.modernfix.ModernFix;

public class ModUtil {

    private static final Set<Class<?>> erroredContexts = new HashSet();

    private static final ClassLoader targetClassLoader = Thread.currentThread().getContextClassLoader();

    public static ForkJoinPool commonPool = new ForkJoinPool(ForkJoinPool.getCommonPoolParallelism(), ModUtil.ModernFixForkJoinWorkerThread::new, null, false);

    private static boolean busListensToEvent(EventBus bus, Class<?> eventClazz) {
        try {
            int busID = (Integer) ObfuscationReflectionHelper.getPrivateValue(EventBus.class, bus, "busID");
            return EventListenerHelper.getListenerList(eventClazz).getListeners(busID).length > 0;
        } catch (Exception var3) {
            ModernFix.LOGGER.error(var3);
            return false;
        }
    }

    public static Collection<String> findAllModsListeningToEvent(Class<?> eventClazz) {
        Set<String> modsListening = new HashSet();
        ModList.get().forEachModContainer((modId, container) -> {
            Supplier<?> languageExtensionSupplier = (Supplier<?>) ObfuscationReflectionHelper.getPrivateValue(ModContainer.class, container, "contextExtension");
            Object context = languageExtensionSupplier.get();
            if (context != null) {
                if (context instanceof FMLJavaModLoadingContext) {
                    if (busListensToEvent((EventBus) ((FMLJavaModLoadingContext) context).getModEventBus(), eventClazz)) {
                        modsListening.add(modId);
                    }
                } else {
                    synchronized (erroredContexts) {
                        if (!erroredContexts.contains(context.getClass())) {
                            ModernFix.LOGGER.warn("Unknown modloading context: " + context.getClass().getName());
                            erroredContexts.add(context.getClass());
                        }
                    }
                }
            }
        });
        return modsListening;
    }

    private static class ModernFixForkJoinWorkerThread extends ForkJoinWorkerThread {

        ModernFixForkJoinWorkerThread(ForkJoinPool pool) {
            super(pool);
            this.setContextClassLoader(ModUtil.targetClassLoader);
        }
    }
}