package de.keksuccino.konkrete.objecthunter.exp4j.function;

public class Functions {

    private static final int INDEX_SIN = 0;

    private static final int INDEX_COS = 1;

    private static final int INDEX_TAN = 2;

    private static final int INDEX_CSC = 3;

    private static final int INDEX_SEC = 4;

    private static final int INDEX_COT = 5;

    private static final int INDEX_SINH = 6;

    private static final int INDEX_COSH = 7;

    private static final int INDEX_TANH = 8;

    private static final int INDEX_CSCH = 9;

    private static final int INDEX_SECH = 10;

    private static final int INDEX_COTH = 11;

    private static final int INDEX_ASIN = 12;

    private static final int INDEX_ACOS = 13;

    private static final int INDEX_ATAN = 14;

    private static final int INDEX_SQRT = 15;

    private static final int INDEX_CBRT = 16;

    private static final int INDEX_ABS = 17;

    private static final int INDEX_CEIL = 18;

    private static final int INDEX_FLOOR = 19;

    private static final int INDEX_POW = 20;

    private static final int INDEX_EXP = 21;

    private static final int INDEX_EXPM1 = 22;

    private static final int INDEX_LOG10 = 23;

    private static final int INDEX_LOG2 = 24;

    private static final int INDEX_LOG = 25;

    private static final int INDEX_LOG1P = 26;

    private static final int INDEX_LOGB = 27;

    private static final int INDEX_SGN = 28;

    private static final int INDEX_TO_RADIAN = 29;

    private static final int INDEX_TO_DEGREE = 30;

    private static final Function[] BUILT_IN_FUNCTIONS = new Function[31];

    public static Function getBuiltinFunction(String name) {
        switch(name) {
            case "sin":
                return BUILT_IN_FUNCTIONS[0];
            case "cos":
                return BUILT_IN_FUNCTIONS[1];
            case "tan":
                return BUILT_IN_FUNCTIONS[2];
            case "cot":
                return BUILT_IN_FUNCTIONS[5];
            case "asin":
                return BUILT_IN_FUNCTIONS[12];
            case "acos":
                return BUILT_IN_FUNCTIONS[13];
            case "atan":
                return BUILT_IN_FUNCTIONS[14];
            case "sinh":
                return BUILT_IN_FUNCTIONS[6];
            case "cosh":
                return BUILT_IN_FUNCTIONS[7];
            case "tanh":
                return BUILT_IN_FUNCTIONS[8];
            case "abs":
                return BUILT_IN_FUNCTIONS[17];
            case "log":
                return BUILT_IN_FUNCTIONS[25];
            case "log10":
                return BUILT_IN_FUNCTIONS[23];
            case "log2":
                return BUILT_IN_FUNCTIONS[24];
            case "log1p":
                return BUILT_IN_FUNCTIONS[26];
            case "ceil":
                return BUILT_IN_FUNCTIONS[18];
            case "floor":
                return BUILT_IN_FUNCTIONS[19];
            case "sqrt":
                return BUILT_IN_FUNCTIONS[15];
            case "cbrt":
                return BUILT_IN_FUNCTIONS[16];
            case "pow":
                return BUILT_IN_FUNCTIONS[20];
            case "exp":
                return BUILT_IN_FUNCTIONS[21];
            case "expm1":
                return BUILT_IN_FUNCTIONS[22];
            case "signum":
                return BUILT_IN_FUNCTIONS[28];
            case "csc":
                return BUILT_IN_FUNCTIONS[3];
            case "sec":
                return BUILT_IN_FUNCTIONS[4];
            case "csch":
                return BUILT_IN_FUNCTIONS[9];
            case "sech":
                return BUILT_IN_FUNCTIONS[10];
            case "coth":
                return BUILT_IN_FUNCTIONS[11];
            case "toradian":
                return BUILT_IN_FUNCTIONS[29];
            case "todegree":
                return BUILT_IN_FUNCTIONS[30];
            default:
                return null;
        }
    }

    static {
        BUILT_IN_FUNCTIONS[0] = new Function("sin") {

            @Override
            public double apply(double... args) {
                return Math.sin(args[0]);
            }
        };
        BUILT_IN_FUNCTIONS[1] = new Function("cos") {

            @Override
            public double apply(double... args) {
                return Math.cos(args[0]);
            }
        };
        BUILT_IN_FUNCTIONS[2] = new Function("tan") {

            @Override
            public double apply(double... args) {
                return Math.tan(args[0]);
            }
        };
        BUILT_IN_FUNCTIONS[5] = new Function("cot") {

            @Override
            public double apply(double... args) {
                double tan = Math.tan(args[0]);
                if (tan == 0.0) {
                    throw new ArithmeticException("Division by zero in cotangent!");
                } else {
                    return 1.0 / tan;
                }
            }
        };
        BUILT_IN_FUNCTIONS[25] = new Function("log") {

            @Override
            public double apply(double... args) {
                return Math.log(args[0]);
            }
        };
        BUILT_IN_FUNCTIONS[24] = new Function("log2") {

            @Override
            public double apply(double... args) {
                return Math.log(args[0]) / Math.log(2.0);
            }
        };
        BUILT_IN_FUNCTIONS[23] = new Function("log10") {

            @Override
            public double apply(double... args) {
                return Math.log10(args[0]);
            }
        };
        BUILT_IN_FUNCTIONS[26] = new Function("log1p") {

            @Override
            public double apply(double... args) {
                return Math.log1p(args[0]);
            }
        };
        BUILT_IN_FUNCTIONS[17] = new Function("abs") {

            @Override
            public double apply(double... args) {
                return Math.abs(args[0]);
            }
        };
        BUILT_IN_FUNCTIONS[13] = new Function("acos") {

            @Override
            public double apply(double... args) {
                return Math.acos(args[0]);
            }
        };
        BUILT_IN_FUNCTIONS[12] = new Function("asin") {

            @Override
            public double apply(double... args) {
                return Math.asin(args[0]);
            }
        };
        BUILT_IN_FUNCTIONS[14] = new Function("atan") {

            @Override
            public double apply(double... args) {
                return Math.atan(args[0]);
            }
        };
        BUILT_IN_FUNCTIONS[16] = new Function("cbrt") {

            @Override
            public double apply(double... args) {
                return Math.cbrt(args[0]);
            }
        };
        BUILT_IN_FUNCTIONS[19] = new Function("floor") {

            @Override
            public double apply(double... args) {
                return Math.floor(args[0]);
            }
        };
        BUILT_IN_FUNCTIONS[6] = new Function("sinh") {

            @Override
            public double apply(double... args) {
                return Math.sinh(args[0]);
            }
        };
        BUILT_IN_FUNCTIONS[15] = new Function("sqrt") {

            @Override
            public double apply(double... args) {
                return Math.sqrt(args[0]);
            }
        };
        BUILT_IN_FUNCTIONS[8] = new Function("tanh") {

            @Override
            public double apply(double... args) {
                return Math.tanh(args[0]);
            }
        };
        BUILT_IN_FUNCTIONS[7] = new Function("cosh") {

            @Override
            public double apply(double... args) {
                return Math.cosh(args[0]);
            }
        };
        BUILT_IN_FUNCTIONS[18] = new Function("ceil") {

            @Override
            public double apply(double... args) {
                return Math.ceil(args[0]);
            }
        };
        BUILT_IN_FUNCTIONS[20] = new Function("pow", 2) {

            @Override
            public double apply(double... args) {
                return Math.pow(args[0], args[1]);
            }
        };
        BUILT_IN_FUNCTIONS[21] = new Function("exp", 1) {

            @Override
            public double apply(double... args) {
                return Math.exp(args[0]);
            }
        };
        BUILT_IN_FUNCTIONS[22] = new Function("expm1", 1) {

            @Override
            public double apply(double... args) {
                return Math.expm1(args[0]);
            }
        };
        BUILT_IN_FUNCTIONS[28] = new Function("signum", 1) {

            @Override
            public double apply(double... args) {
                if (args[0] > 0.0) {
                    return 1.0;
                } else {
                    return args[0] < 0.0 ? -1.0 : 0.0;
                }
            }
        };
        BUILT_IN_FUNCTIONS[3] = new Function("csc") {

            @Override
            public double apply(double... args) {
                double sin = Math.sin(args[0]);
                if (sin == 0.0) {
                    throw new ArithmeticException("Division by zero in cosecant!");
                } else {
                    return 1.0 / sin;
                }
            }
        };
        BUILT_IN_FUNCTIONS[4] = new Function("sec") {

            @Override
            public double apply(double... args) {
                double cos = Math.cos(args[0]);
                if (cos == 0.0) {
                    throw new ArithmeticException("Division by zero in secant!");
                } else {
                    return 1.0 / cos;
                }
            }
        };
        BUILT_IN_FUNCTIONS[9] = new Function("csch") {

            @Override
            public double apply(double... args) {
                return args[0] == 0.0 ? 0.0 : 1.0 / Math.sinh(args[0]);
            }
        };
        BUILT_IN_FUNCTIONS[10] = new Function("sech") {

            @Override
            public double apply(double... args) {
                return 1.0 / Math.cosh(args[0]);
            }
        };
        BUILT_IN_FUNCTIONS[11] = new Function("coth") {

            @Override
            public double apply(double... args) {
                return Math.cosh(args[0]) / Math.sinh(args[0]);
            }
        };
        BUILT_IN_FUNCTIONS[27] = new Function("logb", 2) {

            @Override
            public double apply(double... args) {
                return Math.log(args[1]) / Math.log(args[0]);
            }
        };
        BUILT_IN_FUNCTIONS[29] = new Function("toradian") {

            @Override
            public double apply(double... args) {
                return Math.toRadians(args[0]);
            }
        };
        BUILT_IN_FUNCTIONS[30] = new Function("todegree") {

            @Override
            public double apply(double... args) {
                return Math.toDegrees(args[0]);
            }
        };
    }
}