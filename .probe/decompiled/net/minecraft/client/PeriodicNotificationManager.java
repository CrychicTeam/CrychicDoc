package net.minecraft.client;

import com.google.common.collect.ImmutableMap;
import com.google.common.math.LongMath;
import com.google.gson.JsonParser;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.Object2BooleanFunction;
import java.io.Reader;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.slf4j.Logger;

public class PeriodicNotificationManager extends SimplePreparableReloadListener<Map<String, List<PeriodicNotificationManager.Notification>>> implements AutoCloseable {

    private static final Codec<Map<String, List<PeriodicNotificationManager.Notification>>> CODEC = Codec.unboundedMap(Codec.STRING, RecordCodecBuilder.create(p_205303_ -> p_205303_.group(Codec.LONG.optionalFieldOf("delay", 0L).forGetter(PeriodicNotificationManager.Notification::f_205328_), Codec.LONG.fieldOf("period").forGetter(PeriodicNotificationManager.Notification::f_205329_), Codec.STRING.fieldOf("title").forGetter(PeriodicNotificationManager.Notification::f_205330_), Codec.STRING.fieldOf("message").forGetter(PeriodicNotificationManager.Notification::f_205331_)).apply(p_205303_, PeriodicNotificationManager.Notification::new)).listOf());

    private static final Logger LOGGER = LogUtils.getLogger();

    private final ResourceLocation notifications;

    private final Object2BooleanFunction<String> selector;

    @Nullable
    private java.util.Timer timer;

    @Nullable
    private PeriodicNotificationManager.NotificationTask notificationTask;

    public PeriodicNotificationManager(ResourceLocation resourceLocation0, Object2BooleanFunction<String> objectBooleanFunctionString1) {
        this.notifications = resourceLocation0;
        this.selector = objectBooleanFunctionString1;
    }

    protected Map<String, List<PeriodicNotificationManager.Notification>> prepare(ResourceManager resourceManager0, ProfilerFiller profilerFiller1) {
        try {
            Reader $$2 = resourceManager0.m_215597_(this.notifications);
            Map var4;
            try {
                var4 = (Map) CODEC.parse(JsonOps.INSTANCE, JsonParser.parseReader($$2)).result().orElseThrow();
            } catch (Throwable var7) {
                if ($$2 != null) {
                    try {
                        $$2.close();
                    } catch (Throwable var6) {
                        var7.addSuppressed(var6);
                    }
                }
                throw var7;
            }
            if ($$2 != null) {
                $$2.close();
            }
            return var4;
        } catch (Exception var8) {
            LOGGER.warn("Failed to load {}", this.notifications, var8);
            return ImmutableMap.of();
        }
    }

    protected void apply(Map<String, List<PeriodicNotificationManager.Notification>> mapStringListPeriodicNotificationManagerNotification0, ResourceManager resourceManager1, ProfilerFiller profilerFiller2) {
        List<PeriodicNotificationManager.Notification> $$3 = (List<PeriodicNotificationManager.Notification>) mapStringListPeriodicNotificationManagerNotification0.entrySet().stream().filter(p_205316_ -> (Boolean) this.selector.apply((String) p_205316_.getKey())).map(Entry::getValue).flatMap(Collection::stream).collect(Collectors.toList());
        if ($$3.isEmpty()) {
            this.stopTimer();
        } else if ($$3.stream().anyMatch(p_205326_ -> p_205326_.period == 0L)) {
            Util.logAndPauseIfInIde("A periodic notification in " + this.notifications + " has a period of zero minutes");
            this.stopTimer();
        } else {
            long $$4 = this.calculateInitialDelay($$3);
            long $$5 = this.calculateOptimalPeriod($$3, $$4);
            if (this.timer == null) {
                this.timer = new java.util.Timer();
            }
            if (this.notificationTask == null) {
                this.notificationTask = new PeriodicNotificationManager.NotificationTask($$3, $$4, $$5);
            } else {
                this.notificationTask = this.notificationTask.reset($$3, $$5);
            }
            this.timer.scheduleAtFixedRate(this.notificationTask, TimeUnit.MINUTES.toMillis($$4), TimeUnit.MINUTES.toMillis($$5));
        }
    }

    public void close() {
        this.stopTimer();
    }

    private void stopTimer() {
        if (this.timer != null) {
            this.timer.cancel();
        }
    }

    private long calculateOptimalPeriod(List<PeriodicNotificationManager.Notification> listPeriodicNotificationManagerNotification0, long long1) {
        return listPeriodicNotificationManagerNotification0.stream().mapToLong(p_205298_ -> {
            long $$2 = p_205298_.delay - long1;
            return LongMath.gcd($$2, p_205298_.period);
        }).reduce(LongMath::gcd).orElseThrow(() -> new IllegalStateException("Empty notifications from: " + this.notifications));
    }

    private long calculateInitialDelay(List<PeriodicNotificationManager.Notification> listPeriodicNotificationManagerNotification0) {
        return listPeriodicNotificationManagerNotification0.stream().mapToLong(p_205305_ -> p_205305_.delay).min().orElse(0L);
    }

    public static record Notification(long f_205328_, long f_205329_, String f_205330_, String f_205331_) {

        private final long delay;

        private final long period;

        private final String title;

        private final String message;

        public Notification(long f_205328_, long f_205329_, String f_205330_, String f_205331_) {
            this.delay = f_205328_ != 0L ? f_205328_ : f_205329_;
            this.period = f_205329_;
            this.title = f_205330_;
            this.message = f_205331_;
        }
    }

    static class NotificationTask extends TimerTask {

        private final Minecraft minecraft = Minecraft.getInstance();

        private final List<PeriodicNotificationManager.Notification> notifications;

        private final long period;

        private final AtomicLong elapsed;

        public NotificationTask(List<PeriodicNotificationManager.Notification> listPeriodicNotificationManagerNotification0, long long1, long long2) {
            this.notifications = listPeriodicNotificationManagerNotification0;
            this.period = long2;
            this.elapsed = new AtomicLong(long1);
        }

        public PeriodicNotificationManager.NotificationTask reset(List<PeriodicNotificationManager.Notification> listPeriodicNotificationManagerNotification0, long long1) {
            this.cancel();
            return new PeriodicNotificationManager.NotificationTask(listPeriodicNotificationManagerNotification0, this.elapsed.get(), long1);
        }

        public void run() {
            long $$0 = this.elapsed.getAndAdd(this.period);
            long $$1 = this.elapsed.get();
            for (PeriodicNotificationManager.Notification $$2 : this.notifications) {
                if ($$0 >= $$2.delay) {
                    long $$3 = $$0 / $$2.period;
                    long $$4 = $$1 / $$2.period;
                    if ($$3 != $$4) {
                        this.minecraft.execute(() -> SystemToast.add(Minecraft.getInstance().getToasts(), SystemToast.SystemToastIds.PERIODIC_NOTIFICATION, Component.translatable($$2.title, $$3), Component.translatable($$2.message, $$3)));
                        return;
                    }
                }
            }
        }
    }
}