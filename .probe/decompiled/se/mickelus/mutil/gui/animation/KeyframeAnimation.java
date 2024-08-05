package se.mickelus.mutil.gui.animation;

import java.util.Arrays;
import java.util.function.Consumer;
import se.mickelus.mutil.gui.GuiElement;

public class KeyframeAnimation implements GuiAnimation {

    private final int duration;

    private int delay = 0;

    private final GuiElement element;

    private Consumer<Boolean> handler;

    private Applier[] appliers;

    private long startTime;

    private boolean isActive = false;

    public KeyframeAnimation(int duration, GuiElement element) {
        this.duration = duration;
        this.element = element;
    }

    public KeyframeAnimation applyTo(Applier... appliers) {
        this.appliers = appliers;
        Arrays.stream(this.appliers).forEach(applier -> applier.setElement(this.element));
        return this;
    }

    public KeyframeAnimation withDelay(int delay) {
        this.delay = delay;
        return this;
    }

    public KeyframeAnimation onStop(Consumer<Boolean> handler) {
        this.handler = handler;
        return this;
    }

    @Override
    public void start() {
        this.startTime = System.currentTimeMillis();
        Arrays.stream(this.appliers).forEach(applier -> applier.start(this.duration));
        this.isActive = true;
        this.element.addAnimation(this);
    }

    @Override
    public void stop() {
        if (this.handler != null) {
            this.handler.accept(!this.isActive);
        }
        this.isActive = false;
    }

    public void preDraw() {
        long currentTime = System.currentTimeMillis();
        if (this.startTime + (long) this.delay < currentTime) {
            if (this.startTime + (long) this.delay + (long) this.duration > currentTime) {
                float progress = (float) (currentTime - (long) this.delay - this.startTime) * 1.0F / (float) this.duration;
                Arrays.stream(this.appliers).forEach(applier -> applier.preDraw(progress));
            } else {
                Arrays.stream(this.appliers).forEach(applier -> applier.preDraw(1.0F));
                this.isActive = false;
                this.stop();
            }
        }
    }

    public boolean isActive() {
        return this.isActive;
    }
}