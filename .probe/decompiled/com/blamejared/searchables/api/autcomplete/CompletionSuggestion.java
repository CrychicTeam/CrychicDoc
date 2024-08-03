package com.blamejared.searchables.api.autcomplete;

import com.blamejared.searchables.api.TokenRange;
import net.minecraft.network.chat.Component;

public record CompletionSuggestion(String suggestion, Component display, String suffix, TokenRange replacementRange) {

    public String replaceIn(String into) {
        return this.replacementRange.replace(into, this.toInsert());
    }

    public String toInsert() {
        return this.suggestion + this.suffix;
    }
}