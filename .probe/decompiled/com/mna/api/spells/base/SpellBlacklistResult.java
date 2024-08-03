package com.mna.api.spells.base;

public enum SpellBlacklistResult {

    ALLOWED(""), BIOME_BLOCKED("item.mna.spell.biome-blacklisted"), DIMENSION_BLOCKED("item.mna.spell.dimension-blacklisted");

    private String messageTranslationKey;

    private SpellBlacklistResult(String messageTranslationKey) {
        this.messageTranslationKey = messageTranslationKey;
    }

    public String getMessageTranslationKey() {
        return this.messageTranslationKey;
    }
}