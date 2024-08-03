package yalter.mousetweaks;

public enum ScrollItemScaling {

    PROPORTIONAL(0), ALWAYS_ONE(1);

    private final int id;

    private ScrollItemScaling(int id) {
        this.id = id;
    }

    public int getValue() {
        return this.id;
    }

    public static ScrollItemScaling fromId(int id) {
        return id == PROPORTIONAL.id ? PROPORTIONAL : ALWAYS_ONE;
    }

    public double scale(double scrollDelta) {
        switch(this) {
            case PROPORTIONAL:
                return scrollDelta;
            case ALWAYS_ONE:
                return Math.signum(scrollDelta);
            default:
                throw new AssertionError();
        }
    }
}