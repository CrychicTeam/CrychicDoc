package me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.impl;

import java.util.Locale;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Jankson;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.JsonElement;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.JsonNull;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.JsonPrimitive;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.api.SyntaxError;

public class ElementParserContext implements ParserContext<AnnotatedElement> {

    String comment = null;

    AnnotatedElement result = null;

    boolean childActive = false;

    @Override
    public boolean consume(int codePoint, Jankson loader) throws SyntaxError {
        if (Character.isWhitespace(codePoint)) {
            return true;
        } else {
            switch(codePoint) {
                case 34:
                case 39:
                    loader.push(new StringParserContext(codePoint), this::setResult);
                    this.childActive = true;
                    return true;
                case 35:
                case 47:
                    loader.push(new CommentParserContext(codePoint), it -> this.comment = it);
                    return true;
                case 91:
                    loader.push(new ArrayParserContext(), this::setResult);
                    this.childActive = true;
                    return true;
                case 93:
                    this.result = new AnnotatedElement(null, this.comment);
                    return false;
                case 123:
                    loader.push(new ObjectParserContext(), this::setResult);
                    this.childActive = true;
                    return false;
                case 125:
                    loader.throwDelayed(new SyntaxError("Found '" + (char) codePoint + "' while parsing an element - this shouldn't happen!"));
                    return false;
                default:
                    if (!Character.isDigit(codePoint) && codePoint != 45 && codePoint != 43 && codePoint != 46) {
                        loader.push(new TokenParserContext(codePoint), it -> {
                            String token = it.asString().toLowerCase(Locale.ROOT);
                            switch(token) {
                                case "null":
                                    this.setResult(JsonNull.INSTANCE);
                                    break;
                                case "true":
                                    this.setResult(JsonPrimitive.TRUE);
                                    break;
                                case "false":
                                    this.setResult(JsonPrimitive.FALSE);
                                    break;
                                case "infinity":
                                case "+infinity":
                                    this.setResult(new JsonPrimitive(Double.POSITIVE_INFINITY));
                                    break;
                                case "-infinity":
                                    this.setResult(new JsonPrimitive(Double.NEGATIVE_INFINITY));
                                    break;
                                case "nan":
                                    this.setResult(new JsonPrimitive(Double.NaN));
                                    break;
                                default:
                                    this.setResult(it);
                            }
                        });
                        this.childActive = true;
                        return true;
                    } else {
                        loader.push(new NumberParserContext(codePoint), this::setResult);
                        this.childActive = true;
                        return true;
                    }
            }
        }
    }

    public void setResult(JsonElement elem) {
        this.result = new AnnotatedElement(elem, this.comment);
    }

    @Override
    public void eof() throws SyntaxError {
        if (!this.childActive) {
            throw new SyntaxError("Unexpected end-of-file while looking for a json element!");
        }
    }

    @Override
    public boolean isComplete() {
        return this.result != null;
    }

    public AnnotatedElement getResult() throws SyntaxError {
        return this.result;
    }
}