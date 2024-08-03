package de.keksuccino.konkrete.objecthunter.exp4j.operator;

public abstract class Operators {

    private static final int INDEX_ADDITION = 0;

    private static final int INDEX_SUBTRACTION = 1;

    private static final int INDEX_MULTIPLICATION = 2;

    private static final int INDEX_DIVISION = 3;

    private static final int INDEX_POWER = 4;

    private static final int INDEX_MODULO = 5;

    private static final int INDEX_UNARY_MINUS = 6;

    private static final int INDEX_UNARY_PLUS = 7;

    private static final Operator[] BUILT_IN_OPERATORS = new Operator[8];

    public static Operator getBuiltinOperator(char symbol, int numArguments) {
        switch(symbol) {
            case '%':
                return BUILT_IN_OPERATORS[5];
            case '*':
                return BUILT_IN_OPERATORS[2];
            case '+':
                if (numArguments != 1) {
                    return BUILT_IN_OPERATORS[0];
                }
                return BUILT_IN_OPERATORS[7];
            case '-':
                if (numArguments != 1) {
                    return BUILT_IN_OPERATORS[1];
                }
                return BUILT_IN_OPERATORS[6];
            case '/':
            case '÷':
                return BUILT_IN_OPERATORS[3];
            case '^':
                return BUILT_IN_OPERATORS[4];
            default:
                return null;
        }
    }

    static {
        BUILT_IN_OPERATORS[0] = new Operator("+", 2, true, 500) {

            @Override
            public double apply(double... args) {
                return args[0] + args[1];
            }
        };
        BUILT_IN_OPERATORS[1] = new Operator("-", 2, true, 500) {

            @Override
            public double apply(double... args) {
                return args[0] - args[1];
            }
        };
        BUILT_IN_OPERATORS[6] = new Operator("-", 1, false, 5000) {

            @Override
            public double apply(double... args) {
                return -args[0];
            }
        };
        BUILT_IN_OPERATORS[7] = new Operator("+", 1, false, 5000) {

            @Override
            public double apply(double... args) {
                return args[0];
            }
        };
        BUILT_IN_OPERATORS[2] = new Operator("*", 2, true, 1000) {

            @Override
            public double apply(double... args) {
                return args[0] * args[1];
            }
        };
        BUILT_IN_OPERATORS[3] = new Operator("/", 2, true, 1000) {

            @Override
            public double apply(double... args) {
                if (args[1] == 0.0) {
                    throw new ArithmeticException("Division by zero!");
                } else {
                    return args[0] / args[1];
                }
            }
        };
        BUILT_IN_OPERATORS[4] = new Operator("^", 2, false, 10000) {

            @Override
            public double apply(double... args) {
                return Math.pow(args[0], args[1]);
            }
        };
        BUILT_IN_OPERATORS[5] = new Operator("%", 2, true, 1000) {

            @Override
            public double apply(double... args) {
                if (args[1] == 0.0) {
                    throw new ArithmeticException("Division by zero!");
                } else {
                    return args[0] % args[1];
                }
            }
        };
    }
}