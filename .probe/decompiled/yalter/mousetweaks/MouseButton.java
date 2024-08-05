package yalter.mousetweaks;

public enum MouseButton {

    LEFT(0), RIGHT(1);

    private final int id;

    private MouseButton(int id) {
        this.id = id;
    }

    public int getValue() {
        return this.id;
    }

    public static MouseButton fromEventButton(int eventButton) {
        return switch(eventButton) {
            case 0 ->
                LEFT;
            case 1 ->
                RIGHT;
            default ->
                null;
        };
    }
}