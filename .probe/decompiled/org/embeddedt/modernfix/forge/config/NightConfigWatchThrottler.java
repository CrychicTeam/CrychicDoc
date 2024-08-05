package org.embeddedt.modernfix.forge.config;

import com.electronwill.nightconfig.core.file.FileWatcher;
import com.google.common.collect.ForwardingCollection;
import com.google.common.collect.ForwardingMap;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

public class NightConfigWatchThrottler {

    private static final long DELAY = TimeUnit.MILLISECONDS.toNanos(1000L);

    public static void throttle() {
        final Map watchedDirs = (Map) ObfuscationReflectionHelper.getPrivateValue(FileWatcher.class, FileWatcher.defaultInstance(), "watchedDirs");
        ObfuscationReflectionHelper.setPrivateValue(FileWatcher.class, FileWatcher.defaultInstance(), new ForwardingMap() {

            private Collection cachedValues;

            protected Map delegate() {
                return watchedDirs;
            }

            public Collection values() {
                if (this.cachedValues == null) {
                    final Collection values = super.values();
                    this.cachedValues = new ForwardingCollection() {

                        protected Collection delegate() {
                            return values;
                        }

                        public Iterator iterator() {
                            LockSupport.parkNanos(NightConfigWatchThrottler.DELAY);
                            return super.iterator();
                        }
                    };
                }
                return this.cachedValues;
            }
        }, "watchedDirs");
    }
}