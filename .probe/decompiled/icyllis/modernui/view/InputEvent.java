package icyllis.modernui.view;

public abstract class InputEvent {

    InputEvent() {
    }

    public abstract InputEvent copy();

    public abstract void recycle();

    public abstract long getEventTime();

    public abstract long getEventTimeNano();

    public abstract void cancel();
}