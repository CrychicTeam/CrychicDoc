package icyllis.modernui.view;

public class DragEvent {

    public static final int ACTION_DRAG_STARTED = 1;

    public static final int ACTION_DRAG_ENTERED = 2;

    public static final int ACTION_DRAG_MOVED = 3;

    public static final int ACTION_DRAG_EXITED = 4;

    public static final int ACTION_DROP = 5;

    public static final int ACTION_DRAG_ENDED = 6;

    private int action;

    private double x;

    private double y;

    private final Object mLocalState;

    private boolean result;

    DragEvent(Object localState) {
        this.mLocalState = localState;
    }

    public int getAction() {
        return this.action;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public Object getLocalState() {
        return this.mLocalState;
    }

    public boolean getResult() {
        return this.result;
    }

    void setAction(int action) {
        this.action = action;
    }

    void setPoint(float x, float y) {
        this.x = (double) x;
        this.y = (double) y;
    }

    void setResult(boolean result) {
        this.result = result;
    }

    public String toString() {
        return "DragEvent{action=" + this.action + ", x=" + this.x + ", y=" + this.y + ", mLocalState=" + this.mLocalState + ", result=" + this.result + "}";
    }
}