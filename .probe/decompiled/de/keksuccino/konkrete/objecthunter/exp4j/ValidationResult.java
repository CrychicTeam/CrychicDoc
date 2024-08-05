package de.keksuccino.konkrete.objecthunter.exp4j;

import java.util.List;

public class ValidationResult {

    private final boolean valid;

    private final List<String> errors;

    public static final ValidationResult SUCCESS = new ValidationResult(true, null);

    public ValidationResult(boolean valid, List<String> errors) {
        this.valid = valid;
        this.errors = errors;
    }

    public boolean isValid() {
        return this.valid;
    }

    public List<String> getErrors() {
        return this.errors;
    }
}