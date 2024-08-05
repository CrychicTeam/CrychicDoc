package de.keksuccino.konkrete.json.jsonpath.internal.path;

import de.keksuccino.konkrete.json.jsonpath.InvalidPathException;

public class ArraySliceOperation {

    private final Integer from;

    private final Integer to;

    private final ArraySliceOperation.Operation operation;

    private ArraySliceOperation(Integer from, Integer to, ArraySliceOperation.Operation operation) {
        this.from = from;
        this.to = to;
        this.operation = operation;
    }

    public Integer from() {
        return this.from;
    }

    public Integer to() {
        return this.to;
    }

    public ArraySliceOperation.Operation operation() {
        return this.operation;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(this.from == null ? "" : this.from.toString());
        sb.append(":");
        sb.append(this.to == null ? "" : this.to.toString());
        sb.append("]");
        return sb.toString();
    }

    public static ArraySliceOperation parse(String operation) {
        for (int i = 0; i < operation.length(); i++) {
            char c = operation.charAt(i);
            if (!Character.isDigit(c) && c != '-' && c != ':') {
                throw new InvalidPathException("Failed to parse SliceOperation: " + operation);
            }
        }
        String[] tokens = operation.split(":");
        Integer tempFrom = tryRead(tokens, 0);
        Integer tempTo = tryRead(tokens, 1);
        ArraySliceOperation.Operation tempOperation;
        if (tempFrom != null && tempTo == null) {
            tempOperation = ArraySliceOperation.Operation.SLICE_FROM;
        } else if (tempFrom != null) {
            tempOperation = ArraySliceOperation.Operation.SLICE_BETWEEN;
        } else {
            if (tempTo == null) {
                throw new InvalidPathException("Failed to parse SliceOperation: " + operation);
            }
            tempOperation = ArraySliceOperation.Operation.SLICE_TO;
        }
        return new ArraySliceOperation(tempFrom, tempTo, tempOperation);
    }

    private static Integer tryRead(String[] tokens, int idx) {
        if (tokens.length > idx) {
            return tokens[idx].equals("") ? null : Integer.parseInt(tokens[idx]);
        } else {
            return null;
        }
    }

    public static enum Operation {

        SLICE_FROM, SLICE_TO, SLICE_BETWEEN
    }
}