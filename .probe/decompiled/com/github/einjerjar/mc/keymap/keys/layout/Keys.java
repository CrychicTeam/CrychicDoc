package com.github.einjerjar.mc.keymap.keys.layout;

import java.util.ArrayList;
import java.util.List;

public class Keys {

    protected List<KeyRow> basic = new ArrayList();

    protected List<KeyRow> mouse = new ArrayList();

    protected List<KeyRow> extra = new ArrayList();

    protected List<KeyRow> numpad = new ArrayList();

    public List<KeyRow> basic() {
        return this.basic;
    }

    public List<KeyRow> mouse() {
        return this.mouse;
    }

    public List<KeyRow> extra() {
        return this.extra;
    }

    public List<KeyRow> numpad() {
        return this.numpad;
    }

    public String toString() {
        return "Keys(basic=" + this.basic() + ", mouse=" + this.mouse() + ", extra=" + this.extra() + ", numpad=" + this.numpad() + ")";
    }
}