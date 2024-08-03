package me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.impl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.JsonElement;

public class AnnotatedElement {

    protected String comment;

    protected JsonElement elem;

    public AnnotatedElement(@Nonnull JsonElement elem, @Nullable String comment) {
        this.comment = comment;
        this.elem = elem;
    }

    @Nullable
    public String getComment() {
        return this.comment;
    }

    @Nonnull
    public JsonElement getElement() {
        return this.elem;
    }
}