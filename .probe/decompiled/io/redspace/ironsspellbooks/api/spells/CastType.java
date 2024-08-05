package io.redspace.ironsspellbooks.api.spells;

public enum CastType {

    NONE(0), INSTANT(1), LONG(2), CONTINUOUS(3);

    private final int value;

    private CastType(int newValue) {
        this.value = newValue;
    }

    public int getValue() {
        return this.value;
    }

    @Deprecated(forRemoval = true)
    public boolean holdToCast() {
        return false;
    }

    public boolean immediatelySuppressRightClicks() {
        return this == LONG || this == CONTINUOUS;
    }
}