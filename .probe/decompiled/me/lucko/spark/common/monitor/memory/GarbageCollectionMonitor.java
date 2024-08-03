package me.lucko.spark.common.monitor.memory;

import com.sun.management.GarbageCollectionNotificationInfo;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;
import javax.management.ListenerNotFoundException;
import javax.management.Notification;
import javax.management.NotificationEmitter;
import javax.management.NotificationListener;
import javax.management.openmbean.CompositeData;

public class GarbageCollectionMonitor implements NotificationListener, AutoCloseable {

    private final List<GarbageCollectionMonitor.Listener> listeners = new ArrayList();

    private final List<NotificationEmitter> emitters = new ArrayList();

    public GarbageCollectionMonitor() {
        for (GarbageCollectorMXBean bean : ManagementFactory.getGarbageCollectorMXBeans()) {
            if (bean instanceof NotificationEmitter) {
                NotificationEmitter notificationEmitter = (NotificationEmitter) bean;
                notificationEmitter.addNotificationListener(this, null, null);
                this.emitters.add(notificationEmitter);
            }
        }
    }

    public void addListener(GarbageCollectionMonitor.Listener listener) {
        this.listeners.add(listener);
    }

    public void removeListener(GarbageCollectionMonitor.Listener listener) {
        this.listeners.remove(listener);
    }

    public void handleNotification(Notification notification, Object handback) {
        if (notification.getType().equals("com.sun.management.gc.notification")) {
            GarbageCollectionNotificationInfo data = GarbageCollectionNotificationInfo.from((CompositeData) notification.getUserData());
            for (GarbageCollectionMonitor.Listener listener : this.listeners) {
                listener.onGc(data);
            }
        }
    }

    public void close() {
        for (NotificationEmitter e : this.emitters) {
            try {
                e.removeNotificationListener(this);
            } catch (ListenerNotFoundException var4) {
                var4.printStackTrace();
            }
        }
        this.emitters.clear();
        this.listeners.clear();
    }

    public static String getGcType(GarbageCollectionNotificationInfo info) {
        if (info.getGcAction().equals("end of minor GC")) {
            return "Young Gen";
        } else {
            return info.getGcAction().equals("end of major GC") ? "Old Gen" : info.getGcAction();
        }
    }

    public interface Listener {

        void onGc(GarbageCollectionNotificationInfo var1);
    }
}