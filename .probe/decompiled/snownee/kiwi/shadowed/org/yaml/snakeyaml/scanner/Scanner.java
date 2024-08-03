package snownee.kiwi.shadowed.org.yaml.snakeyaml.scanner;

import snownee.kiwi.shadowed.org.yaml.snakeyaml.tokens.Token;

public interface Scanner {

    boolean checkToken(Token.ID... var1);

    Token peekToken();

    Token getToken();
}