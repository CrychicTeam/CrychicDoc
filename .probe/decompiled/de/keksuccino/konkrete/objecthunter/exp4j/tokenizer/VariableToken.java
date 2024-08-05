package de.keksuccino.konkrete.objecthunter.exp4j.tokenizer;

public class VariableToken extends Token {

    private final String name;

    public String getName() {
        return this.name;
    }

    public VariableToken(String name) {
        super(6);
        this.name = name;
    }
}