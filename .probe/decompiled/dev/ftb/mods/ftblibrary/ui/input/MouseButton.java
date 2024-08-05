package dev.ftb.mods.ftblibrary.ui.input;

public class MouseButton {

    private static final MouseButton[] BUTTONS = new MouseButton[16];

    public static final MouseButton LEFT;

    public static final MouseButton RIGHT;

    public static final MouseButton MIDDLE;

    public static final MouseButton BACK;

    public static final MouseButton NEXT;

    public final int id;

    public static MouseButton get(int i) {
        return i >= 0 && i < BUTTONS.length ? BUTTONS[i] : BUTTONS[BUTTONS.length - 1];
    }

    private MouseButton(int b) {
        this.id = b;
    }

    public int hashCode() {
        return this.id;
    }

    public boolean isLeft() {
        return this.id == LEFT.id;
    }

    public boolean isRight() {
        return this.id == RIGHT.id;
    }

    public boolean isMiddle() {
        return this.id == MIDDLE.id;
    }

    public int getId() {
        return this.id;
    }

    static {
        for (int i = 0; i < BUTTONS.length; i++) {
            BUTTONS[i] = new MouseButton(i);
        }
        LEFT = get(0);
        RIGHT = get(1);
        MIDDLE = get(2);
        BACK = get(3);
        NEXT = get(4);
    }
}