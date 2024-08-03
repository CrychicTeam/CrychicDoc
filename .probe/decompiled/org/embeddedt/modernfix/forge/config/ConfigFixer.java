package org.embeddedt.modernfix.forge.config;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.config.IConfigEvent;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import org.embeddedt.modernfix.ModernFix;
import org.embeddedt.modernfix.core.ModernFixMixinPlugin;

public class ConfigFixer {

    public static void replaceConfigHandlers() {
        if (ModernFixMixinPlugin.instance.isOptionEnabled("bugfix.fix_config_crashes.ConfigFixerMixin")) {
            ModList.get().forEachModContainer((id, container) -> {
                try {
                    Optional<Consumer<IConfigEvent>> configOpt = (Optional<Consumer<IConfigEvent>>) ObfuscationReflectionHelper.getPrivateValue(ModContainer.class, container, "configHandler");
                    if (configOpt.isPresent()) {
                        ObfuscationReflectionHelper.setPrivateValue(ModContainer.class, container, Optional.of(new ConfigFixer.LockingConfigHandler(id, (Consumer<IConfigEvent>) configOpt.get())), "configHandler");
                    }
                } catch (RuntimeException var3) {
                    ModernFix.LOGGER.error("Error replacing config handler", var3);
                }
            });
        }
    }

    private static class LockingConfigHandler implements Consumer<IConfigEvent> {

        private final Consumer<IConfigEvent> actualHandler;

        private final String modId;

        private final Lock lock = new ReentrantLock();

        LockingConfigHandler(String id, Consumer<IConfigEvent> actualHandler) {
            this.modId = id;
            this.actualHandler = actualHandler;
        }

        public void accept(IConfigEvent modConfigEvent) {
            try {
                if (this.lock.tryLock(2L, TimeUnit.SECONDS)) {
                    try {
                        this.actualHandler.accept(modConfigEvent);
                    } finally {
                        this.lock.unlock();
                    }
                } else {
                    ModernFix.LOGGER.error("Failed to post config event for {}, someone else is holding the lock", this.modId);
                }
            } catch (InterruptedException var6) {
                Thread.currentThread().interrupt();
            }
        }

        public String toString() {
            return "LockingConfigHandler{id=" + this.modId + "}";
        }
    }
}