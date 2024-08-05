package com.mojang.realmsclient;

import java.util.Arrays;

public class KeyCombo {

    private final char[] chars;

    private int matchIndex;

    private final Runnable onCompletion;

    public KeyCombo(char[] char0, Runnable runnable1) {
        this.onCompletion = runnable1;
        if (char0.length < 1) {
            throw new IllegalArgumentException("Must have at least one char");
        } else {
            this.chars = char0;
        }
    }

    public KeyCombo(char[] char0) {
        this(char0, () -> {
        });
    }

    public boolean keyPressed(char char0) {
        if (char0 == this.chars[this.matchIndex++]) {
            if (this.matchIndex == this.chars.length) {
                this.reset();
                this.onCompletion.run();
                return true;
            }
        } else {
            this.reset();
        }
        return false;
    }

    public void reset() {
        this.matchIndex = 0;
    }

    public String toString() {
        return "KeyCombo{chars=" + Arrays.toString(this.chars) + ", matchIndex=" + this.matchIndex + "}";
    }
}