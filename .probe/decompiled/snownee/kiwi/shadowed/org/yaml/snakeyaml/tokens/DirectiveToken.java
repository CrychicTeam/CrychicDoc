package snownee.kiwi.shadowed.org.yaml.snakeyaml.tokens;

import java.util.List;
import snownee.kiwi.shadowed.org.yaml.snakeyaml.error.Mark;
import snownee.kiwi.shadowed.org.yaml.snakeyaml.error.YAMLException;

public final class DirectiveToken<T> extends Token {

    private final String name;

    private final List<T> value;

    public DirectiveToken(String name, List<T> value, Mark startMark, Mark endMark) {
        super(startMark, endMark);
        this.name = name;
        if (value != null && value.size() != 2) {
            throw new YAMLException("Two strings must be provided instead of " + value.size());
        } else {
            this.value = value;
        }
    }

    public String getName() {
        return this.name;
    }

    public List<T> getValue() {
        return this.value;
    }

    @Override
    public Token.ID getTokenId() {
        return Token.ID.Directive;
    }
}