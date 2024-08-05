package com.mna.api.spells.attributes;

public enum Attribute {

    SPEED("mna:modifiers/speed"),
    RANGE("mna:modifiers/range"),
    DAMAGE("mna:modifiers/damage"),
    RADIUS("mna:modifiers/radius"),
    WIDTH("mna:modifiers/radius.width"),
    HEIGHT("mna:modifiers/radius.height"),
    DEPTH("mna:modifiers/radius.depth"),
    MAGNITUDE("mna:modifiers/magnitude"),
    PRECISION("mna:modifiers/precision"),
    LESSER_MAGNITUDE("mna:modifiers/lesser_magnitude"),
    DURATION("mna:modifiers/duration"),
    DELAY("mna:modifiers/delay");

    private String localeKey;

    private Attribute(String localeKey) {
        this.localeKey = localeKey;
    }

    public String getLocaleKey() {
        return this.localeKey;
    }
}