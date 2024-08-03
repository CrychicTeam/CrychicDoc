package me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.impl;

import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Jankson;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.api.SyntaxError;

public interface ParserContext<T> {

    boolean consume(int var1, Jankson var2) throws SyntaxError;

    void eof() throws SyntaxError;

    boolean isComplete();

    T getResult() throws SyntaxError;
}