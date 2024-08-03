package de.keksuccino.konkrete.objecthunter.exp4j.operator;

public abstract class Operator {

    public static final int PRECEDENCE_ADDITION = 500;

    public static final int PRECEDENCE_SUBTRACTION = 500;

    public static final int PRECEDENCE_MULTIPLICATION = 1000;

    public static final int PRECEDENCE_DIVISION = 1000;

    public static final int PRECEDENCE_MODULO = 1000;

    public static final int PRECEDENCE_POWER = 10000;

    public static final int PRECEDENCE_UNARY_MINUS = 5000;

    public static final int PRECEDENCE_UNARY_PLUS = 5000;

    public static final char[] ALLOWED_OPERATOR_CHARS = new char[] { '+', '-', '*', '/', '%', '^', '!', '#', '§', '$', '&', ';', ':', '~', '<', '>', '|', '=', '÷', '√', '∛', '⌈', '⌊' };

    private final int numOperands;

    private final boolean leftAssociative;

    private final String symbol;

    private final int precedence;

    public Operator(String symbol, int numberOfOperands, boolean leftAssociative, int precedence) {
        this.numOperands = numberOfOperands;
        this.leftAssociative = leftAssociative;
        this.symbol = symbol;
        this.precedence = precedence;
    }

    public static boolean isAllowedOperatorChar(char ch) {
        for (char allowed : ALLOWED_OPERATOR_CHARS) {
            if (ch == allowed) {
                return true;
            }
        }
        return false;
    }

    public boolean isLeftAssociative() {
        return this.leftAssociative;
    }

    public int getPrecedence() {
        return this.precedence;
    }

    public abstract double apply(double... var1);

    public String getSymbol() {
        return this.symbol;
    }

    public int getNumOperands() {
        return this.numOperands;
    }
}