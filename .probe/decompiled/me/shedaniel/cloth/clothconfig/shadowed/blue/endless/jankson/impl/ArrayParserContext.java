package me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.impl;

import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Jankson;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.JsonArray;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.api.SyntaxError;

public class ArrayParserContext implements ParserContext<JsonArray> {

    private JsonArray result = new JsonArray();

    private boolean foundClosingBrace = false;

    private String comment = null;

    @Override
    public boolean consume(int codePoint, Jankson loader) throws SyntaxError {
        this.result.setMarshaller(loader.getMarshaller());
        if (this.foundClosingBrace) {
            return false;
        } else if (Character.isWhitespace(codePoint) || codePoint == 44) {
            return true;
        } else if (codePoint == 93) {
            this.foundClosingBrace = true;
            return true;
        } else {
            loader.push(new ElementParserContext(), it -> {
                if (it.getElement() != null) {
                    this.result.add(it.getElement(), it.getComment());
                } else {
                    String existing = this.result.getComment(this.result.size() - 1);
                    if (existing == null) {
                        existing = "";
                    }
                    String combined = existing + "\n" + it.getComment();
                    this.result.setComment(this.result.size() - 1, combined);
                }
            });
            return false;
        }
    }

    @Override
    public void eof() throws SyntaxError {
        if (!this.foundClosingBrace) {
            throw new SyntaxError("Unexpected end-of-file in the middle of a list! Are you missing a ']'?");
        }
    }

    @Override
    public boolean isComplete() {
        return this.foundClosingBrace;
    }

    public JsonArray getResult() throws SyntaxError {
        return this.result;
    }
}