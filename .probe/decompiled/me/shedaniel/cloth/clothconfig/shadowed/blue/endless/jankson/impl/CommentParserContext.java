package me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.impl;

import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Jankson;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.api.SyntaxError;

public class CommentParserContext implements ParserContext<String> {

    int firstChar = -1;

    int secondChar = -1;

    StringBuilder result = new StringBuilder();

    int prevChar = -1;

    boolean multiLine = false;

    boolean done = false;

    public CommentParserContext(int codePoint) {
        this.firstChar = codePoint;
    }

    @Override
    public boolean consume(int codePoint, Jankson loader) throws SyntaxError {
        if (this.done) {
            return false;
        } else if (this.firstChar == -1) {
            if (codePoint != 47 && codePoint != 35) {
                throw new SyntaxError("Was expecting the start of a comment, but found '" + (char) codePoint + "' instead.");
            } else {
                this.firstChar = codePoint;
                if (this.firstChar == 35) {
                    this.multiLine = false;
                }
                return true;
            }
        } else if (this.secondChar == -1 && this.firstChar != 35) {
            this.secondChar = codePoint;
            if (codePoint == 42) {
                this.multiLine = true;
                return true;
            } else if (codePoint == 47) {
                this.multiLine = false;
                return true;
            } else if (Character.isWhitespace(codePoint)) {
                throw new SyntaxError("Was expecting the start of a comment, but found whitespace instead.");
            } else {
                throw new SyntaxError("Was expecting the start of a comment, but found '" + (char) codePoint + "' instead.");
            }
        } else if (this.multiLine) {
            if (codePoint == 47 && this.prevChar == 42) {
                this.result.deleteCharAt(this.result.length() - 1);
                this.done = true;
                return true;
            } else {
                this.prevChar = codePoint;
                this.result.append((char) codePoint);
                return true;
            }
        } else if (codePoint == 10) {
            this.done = true;
            return true;
        } else {
            this.prevChar = codePoint;
            this.result.append((char) codePoint);
            return true;
        }
    }

    @Override
    public void eof() throws SyntaxError {
        if (this.multiLine) {
            throw new SyntaxError("Unexpected end-of-file while reading a multiline comment.");
        }
    }

    @Override
    public boolean isComplete() {
        return this.done;
    }

    public String getResult() throws SyntaxError {
        return this.result.toString().trim();
    }
}