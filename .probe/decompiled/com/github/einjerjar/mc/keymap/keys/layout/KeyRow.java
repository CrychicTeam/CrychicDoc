package com.github.einjerjar.mc.keymap.keys.layout;

import java.util.List;

public class KeyRow {

    protected List<KeyData> row;

    public List<KeyData> row() {
        return this.row;
    }

    public String toString() {
        return "KeyRow(row=" + this.row() + ")";
    }

    public KeyRow(List<KeyData> row) {
        this.row = row;
    }
}