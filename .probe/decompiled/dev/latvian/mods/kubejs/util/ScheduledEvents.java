package dev.latvian.mods.kubejs.util;

import dev.latvian.mods.rhino.BaseFunction;
import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.NativeJavaObject;
import dev.latvian.mods.rhino.RhinoException;
import dev.latvian.mods.rhino.ScriptRuntime;
import dev.latvian.mods.rhino.Scriptable;
import dev.latvian.mods.rhino.ScriptableObject;
import dev.latvian.mods.rhino.Undefined;
import java.time.Duration;
import java.time.temporal.TemporalAmount;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class ScheduledEvents {

    public final Supplier<ScheduledEvents.ScheduledEvent> factory;

    public final LinkedList<ScheduledEvents.ScheduledEvent> events;

    public final LinkedList<ScheduledEvents.ScheduledEvent> futureEvents;

    public final AtomicInteger nextId;

    public long currentMillis;

    public long currentTick;

    public ScheduledEvents(Supplier<ScheduledEvents.ScheduledEvent> factory) {
        this.factory = factory;
        this.events = new LinkedList();
        this.futureEvents = new LinkedList();
        this.nextId = new AtomicInteger(0);
        this.currentMillis = 0L;
        this.currentTick = 0L;
    }

    public ScheduledEvents.ScheduledEvent schedule(TemporalAmount timer, boolean repeating, ScheduledEvents.Callback callback) {
        if (timer instanceof TickDuration duration) {
            return this.schedule(duration.ticks(), true, repeating, callback);
        } else if (timer instanceof Duration duration) {
            return this.schedule(duration.toMillis(), false, repeating, callback);
        } else {
            throw new IllegalArgumentException("Unsupported TemporalAmount: " + timer);
        }
    }

    public ScheduledEvents.ScheduledEvent schedule(long timer, boolean ofTicks, boolean repeating, ScheduledEvents.Callback callback) {
        ScheduledEvents.ScheduledEvent e = new ScheduledEvents.ScheduledEvent();
        e.scheduledEvents = this;
        e.id = this.nextId.incrementAndGet();
        e.ofTicks = ofTicks;
        e.repeating = repeating;
        e.timer = timer;
        e.callback = callback;
        e.reschedule();
        this.futureEvents.add(e);
        return e;
    }

    public void tickAll(long nowTicks) {
        this.currentMillis = System.currentTimeMillis();
        this.currentTick = nowTicks;
        if (!this.futureEvents.isEmpty()) {
            this.events.addAll(this.futureEvents);
            this.futureEvents.clear();
        }
        if (!this.events.isEmpty()) {
            this.events.removeIf(ScheduledEvents.ScheduledEvent.TICK);
        }
    }

    public void clear(int id) {
        for (ScheduledEvents.ScheduledEvent event : this.events) {
            if (event.id == id) {
                event.callback = null;
                break;
            }
        }
        for (ScheduledEvents.ScheduledEvent eventx : this.futureEvents) {
            if (eventx.id == id) {
                eventx.callback = null;
                return;
            }
        }
    }

    @FunctionalInterface
    public interface Callback {

        void onCallback(ScheduledEvents.ScheduledEvent var1);
    }

    public static class ScheduledEvent {

        private static final Predicate<ScheduledEvents.ScheduledEvent> TICK = ScheduledEvents.ScheduledEvent::tick;

        public ScheduledEvents scheduledEvents;

        public int id;

        public boolean ofTicks;

        public boolean repeating;

        public long timer;

        public long endTime;

        public transient ScheduledEvents.Callback callback;

        public ScheduledEvents.ScheduledEvent reschedule() {
            this.endTime = (this.ofTicks ? this.scheduledEvents.currentTick : this.scheduledEvents.currentMillis) + this.timer;
            return this;
        }

        public ScheduledEvents.ScheduledEvent reschedule(long timer) {
            this.timer = timer;
            return this.reschedule();
        }

        public void clear() {
            this.callback = null;
        }

        private boolean tick() {
            if (this.callback == null) {
                return true;
            } else if ((this.ofTicks ? this.scheduledEvents.currentTick : this.scheduledEvents.currentMillis) >= this.endTime) {
                try {
                    this.callback.onCallback(this);
                } catch (RhinoException var2) {
                    ConsoleJS.SERVER.error("Error occurred while handling scheduled event callback: " + var2.getMessage());
                } catch (Throwable var3) {
                    var3.printStackTrace();
                }
                if (this.repeating) {
                    this.reschedule();
                    return false;
                } else {
                    return true;
                }
            } else {
                return false;
            }
        }
    }

    public static class TimeoutJSFunction extends BaseFunction {

        public final ScheduledEvents scheduledEvents;

        public final boolean clear;

        public final boolean interval;

        public TimeoutJSFunction(ScheduledEvents scheduledEvents, boolean clear, boolean interval) {
            this.scheduledEvents = scheduledEvents;
            this.clear = clear;
            this.interval = interval;
        }

        @Override
        public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
            if (this.clear) {
                this.scheduledEvents.clear(ScriptRuntime.toInt32(cx, args[0]));
                return Undefined.instance;
            } else {
                TemporalAmount timer = (TemporalAmount) Context.jsToJava(cx, args[1], TemporalAmount.class);
                ScheduledEvents.Callback callback = (ScheduledEvents.Callback) NativeJavaObject.createInterfaceAdapter(cx, ScheduledEvents.Callback.class, (ScriptableObject) args[0]);
                return this.scheduledEvents.schedule(timer, this.interval, callback).id;
            }
        }
    }
}