package com.github.einjerjar.mc.keymap.keys.layout;

public class KeyData {

    protected int code;

    protected String name;

    protected boolean enabled;

    protected boolean mouse;

    protected int width;

    protected int height;

    public int code() {
        return this.code;
    }

    public String name() {
        return this.name;
    }

    public boolean enabled() {
        return this.enabled;
    }

    public boolean mouse() {
        return this.mouse;
    }

    public int width() {
        return this.width;
    }

    public int height() {
        return this.height;
    }

    public KeyData(int code, String name, boolean enabled, boolean mouse, int width, int height) {
        this.code = code;
        this.name = name;
        this.enabled = enabled;
        this.mouse = mouse;
        this.width = width;
        this.height = height;
    }

    public String toString() {
        return "KeyData(code=" + this.code() + ", name=" + this.name() + ", enabled=" + this.enabled() + ", mouse=" + this.mouse() + ", width=" + this.width() + ", height=" + this.height() + ")";
    }

    public KeyData mouse(boolean mouse) {
        this.mouse = mouse;
        return this;
    }
}