package yalter.mousetweaks;

public enum ScrollHandling {

    SIMPLE(0), EVENT_BASED(1);

    private final int id;

    private ScrollHandling(int id) {
        this.id = id;
    }

    public int getValue() {
        return this.id;
    }

    public static ScrollHandling fromId(int id) {
        return id == EVENT_BASED.id ? EVENT_BASED : SIMPLE;
    }
}