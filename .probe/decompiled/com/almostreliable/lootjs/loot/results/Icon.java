package com.almostreliable.lootjs.loot.results;

public enum Icon {

    SUCCEED("✔️"),
    FAILED("❌"),
    ACTION("\ud83d\udcdd"),
    WRENCH("\ud83d\udd27"),
    BOLT("\ud83d\udd29"),
    DICE("\ud83c\udfb2");

    private final String icon;

    private Icon(String icon) {
        this.icon = icon;
    }

    public String toString() {
        return this.icon;
    }
}