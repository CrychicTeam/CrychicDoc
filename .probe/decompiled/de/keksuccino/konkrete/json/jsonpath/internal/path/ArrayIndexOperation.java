package de.keksuccino.konkrete.json.jsonpath.internal.path;

import de.keksuccino.konkrete.json.jsonpath.InvalidPathException;
import de.keksuccino.konkrete.json.jsonpath.internal.Utils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class ArrayIndexOperation {

    private static final Pattern COMMA = Pattern.compile("\\s*,\\s*");

    private final List<Integer> indexes;

    private ArrayIndexOperation(List<Integer> indexes) {
        this.indexes = Collections.unmodifiableList(indexes);
    }

    public List<Integer> indexes() {
        return this.indexes;
    }

    public boolean isSingleIndexOperation() {
        return this.indexes.size() == 1;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(Utils.join(",", this.indexes));
        sb.append("]");
        return sb.toString();
    }

    public static ArrayIndexOperation parse(String operation) {
        for (int i = 0; i < operation.length(); i++) {
            char c = operation.charAt(i);
            if (!Character.isDigit(c) && c != ',' && c != ' ' && c != '-') {
                throw new InvalidPathException("Failed to parse ArrayIndexOperation: " + operation);
            }
        }
        String[] tokens = COMMA.split(operation, -1);
        List<Integer> tempIndexes = new ArrayList(tokens.length);
        for (String token : tokens) {
            tempIndexes.add(parseInteger(token));
        }
        return new ArrayIndexOperation(tempIndexes);
    }

    private static Integer parseInteger(String token) {
        try {
            return Integer.parseInt(token);
        } catch (Exception var2) {
            throw new InvalidPathException("Failed to parse token in ArrayIndexOperation: " + token, var2);
        }
    }
}