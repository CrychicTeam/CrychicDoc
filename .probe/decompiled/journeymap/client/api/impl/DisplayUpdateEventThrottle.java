package journeymap.client.api.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import journeymap.client.api.event.DisplayUpdateEvent;

class DisplayUpdateEventThrottle {

    private final DisplayUpdateEventThrottle.Queue fullscreenQueue = new DisplayUpdateEventThrottle.Queue(1000L);

    private final DisplayUpdateEventThrottle.Queue minimapQueue = new DisplayUpdateEventThrottle.Queue(2000L);

    private final DisplayUpdateEventThrottle.Queue[] queues = new DisplayUpdateEventThrottle.Queue[] { this.fullscreenQueue, this.minimapQueue };

    private final ArrayList<DisplayUpdateEvent> readyEvents = new ArrayList(3);

    private final Comparator<DisplayUpdateEvent> comparator = new Comparator<DisplayUpdateEvent>() {

        public int compare(DisplayUpdateEvent o1, DisplayUpdateEvent o2) {
            return Long.compare(o1.timestamp, o2.timestamp);
        }
    };

    public void add(DisplayUpdateEvent event) {
        switch(event.uiState.ui) {
            case Fullscreen:
                this.fullscreenQueue.offer(event);
                break;
            case Minimap:
                this.minimapQueue.offer(event);
                break;
            default:
                throw new UnsupportedOperationException("Can't throttle events for UI." + event.uiState.ui);
        }
    }

    public Iterator<DisplayUpdateEvent> iterator() {
        long now = System.currentTimeMillis();
        for (DisplayUpdateEventThrottle.Queue queue : this.queues) {
            if (queue.lastEvent != null && now >= queue.releaseTime) {
                this.readyEvents.add(queue.remove());
            }
        }
        if (this.readyEvents.size() > 0) {
            Collections.sort(this.readyEvents, this.comparator);
        }
        return this.readyEvents.iterator();
    }

    public boolean isReady() {
        long now = System.currentTimeMillis();
        for (DisplayUpdateEventThrottle.Queue queue : this.queues) {
            if (queue.lastEvent != null && now >= queue.releaseTime) {
                return true;
            }
        }
        return false;
    }

    class Queue {

        private final long delay;

        private DisplayUpdateEvent lastEvent;

        private boolean throttleNext;

        private long releaseTime;

        Queue(long delay) {
            this.delay = delay;
        }

        void offer(DisplayUpdateEvent event) {
            if (this.releaseTime == 0L && this.lastEvent != null) {
                this.releaseTime = System.currentTimeMillis() + this.delay;
            }
            this.lastEvent = event;
        }

        DisplayUpdateEvent remove() {
            DisplayUpdateEvent event = this.lastEvent;
            this.lastEvent = null;
            this.releaseTime = 0L;
            return event;
        }
    }
}