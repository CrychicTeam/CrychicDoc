package me.shedaniel.cloth.clothconfig.shadowed.org.yaml.snakeyaml.tokens;

import me.shedaniel.cloth.clothconfig.shadowed.org.yaml.snakeyaml.error.Mark;

/**
 * @deprecated
 */
public class WhitespaceToken extends Token {

    public WhitespaceToken(Mark startMark, Mark endMark) {
        super(startMark, endMark);
    }

    @Override
    public Token.ID getTokenId() {
        return Token.ID.Whitespace;
    }
}