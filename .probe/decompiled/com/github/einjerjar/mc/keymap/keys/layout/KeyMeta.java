package com.github.einjerjar.mc.keymap.keys.layout;

public class KeyMeta {

    protected String author;

    protected String name;

    protected String code;

    public String author() {
        return this.author;
    }

    public String name() {
        return this.name;
    }

    public String code() {
        return this.code;
    }

    public String toString() {
        return "KeyMeta(author=" + this.author() + ", name=" + this.name() + ", code=" + this.code() + ")";
    }

    public KeyMeta(String author, String name, String code) {
        this.author = author;
        this.name = name;
        this.code = code;
    }
}