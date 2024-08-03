package icyllis.arc3d.compiler;

import icyllis.arc3d.compiler.tree.Type;

public enum Operator {

    ADD,
    SUB,
    MUL,
    DIV,
    MOD,
    SHL,
    SHR,
    LOGICAL_NOT,
    LOGICAL_AND,
    LOGICAL_OR,
    LOGICAL_XOR,
    BITWISE_NOT,
    BITWISE_AND,
    BITWISE_OR,
    BITWISE_XOR,
    ASSIGN,
    EQ,
    NE,
    LT,
    GT,
    LE,
    GE,
    ADD_ASSIGN,
    SUB_ASSIGN,
    MUL_ASSIGN,
    DIV_ASSIGN,
    MOD_ASSIGN,
    SHL_ASSIGN,
    SHR_ASSIGN,
    AND_ASSIGN,
    OR_ASSIGN,
    XOR_ASSIGN,
    INC,
    DEC,
    COMMA;

    public static final int PRECEDENCE_POSTFIX = 2;

    public static final int PRECEDENCE_PREFIX = 3;

    public static final int PRECEDENCE_MULTIPLICATIVE = 4;

    public static final int PRECEDENCE_ADDITIVE = 5;

    public static final int PRECEDENCE_SHIFT = 6;

    public static final int PRECEDENCE_RELATIONAL = 7;

    public static final int PRECEDENCE_EQUALITY = 8;

    public static final int PRECEDENCE_BITWISE_AND = 9;

    public static final int PRECEDENCE_BITWISE_XOR = 10;

    public static final int PRECEDENCE_BITWISE_OR = 11;

    public static final int PRECEDENCE_LOGICAL_AND = 12;

    public static final int PRECEDENCE_LOGICAL_XOR = 13;

    public static final int PRECEDENCE_LOGICAL_OR = 14;

    public static final int PRECEDENCE_CONDITIONAL = 15;

    public static final int PRECEDENCE_ASSIGNMENT = 16;

    public static final int PRECEDENCE_SEQUENCE = 17;

    public static final int PRECEDENCE_EXPRESSION = 17;

    public static final int PRECEDENCE_STATEMENT = 18;

    public int getBinaryPrecedence() {
        return switch(this) {
            case MUL, DIV, MOD ->
                4;
            case ADD, SUB ->
                5;
            case SHL, SHR ->
                6;
            case LT, GT, LE, GE ->
                7;
            case EQ, NE ->
                8;
            case BITWISE_AND ->
                9;
            case BITWISE_XOR ->
                10;
            case BITWISE_OR ->
                11;
            case LOGICAL_AND ->
                12;
            case LOGICAL_XOR ->
                13;
            case LOGICAL_OR ->
                14;
            case ASSIGN, ADD_ASSIGN, SUB_ASSIGN, MUL_ASSIGN, DIV_ASSIGN, MOD_ASSIGN, SHL_ASSIGN, SHR_ASSIGN, AND_ASSIGN, OR_ASSIGN, XOR_ASSIGN ->
                16;
            case COMMA ->
                17;
            default ->
                throw new AssertionError(this);
        };
    }

    public String getPrettyName() {
        return switch(this) {
            case MUL ->
                " * ";
            case DIV ->
                " / ";
            case MOD ->
                " % ";
            case ADD ->
                " + ";
            case SUB ->
                " - ";
            case SHL ->
                " << ";
            case SHR ->
                " >> ";
            case LT ->
                " < ";
            case GT ->
                " > ";
            case LE ->
                " <= ";
            case GE ->
                " >= ";
            case EQ ->
                " == ";
            case NE ->
                " != ";
            case BITWISE_AND ->
                " & ";
            case BITWISE_XOR ->
                " ^ ";
            case BITWISE_OR ->
                " | ";
            case LOGICAL_AND ->
                " && ";
            case LOGICAL_XOR ->
                " ^^ ";
            case LOGICAL_OR ->
                " || ";
            case ASSIGN ->
                " = ";
            case ADD_ASSIGN ->
                " += ";
            case SUB_ASSIGN ->
                " -= ";
            case MUL_ASSIGN ->
                " *= ";
            case DIV_ASSIGN ->
                " /= ";
            case MOD_ASSIGN ->
                " %= ";
            case SHL_ASSIGN ->
                " <<= ";
            case SHR_ASSIGN ->
                " >>= ";
            case AND_ASSIGN ->
                " &= ";
            case OR_ASSIGN ->
                " |= ";
            case XOR_ASSIGN ->
                " ^= ";
            case COMMA ->
                ", ";
            case LOGICAL_NOT ->
                "!";
            case BITWISE_NOT ->
                "~";
            case INC ->
                "++";
            case DEC ->
                "--";
        };
    }

    public String toString() {
        return switch(this) {
            case MUL ->
                "*";
            case DIV ->
                "/";
            case MOD ->
                "%";
            case ADD ->
                "+";
            case SUB ->
                "-";
            case SHL ->
                "<<";
            case SHR ->
                ">>";
            case LT ->
                "<";
            case GT ->
                ">";
            case LE ->
                "<=";
            case GE ->
                ">=";
            case EQ ->
                "==";
            case NE ->
                "!=";
            case BITWISE_AND ->
                "&";
            case BITWISE_XOR ->
                "^";
            case BITWISE_OR ->
                "|";
            case LOGICAL_AND ->
                "&&";
            case LOGICAL_XOR ->
                "^^";
            case LOGICAL_OR ->
                "||";
            case ASSIGN ->
                "=";
            case ADD_ASSIGN ->
                "+=";
            case SUB_ASSIGN ->
                "-=";
            case MUL_ASSIGN ->
                "*=";
            case DIV_ASSIGN ->
                "/=";
            case MOD_ASSIGN ->
                "%=";
            case SHL_ASSIGN ->
                "<<=";
            case SHR_ASSIGN ->
                ">>=";
            case AND_ASSIGN ->
                "&=";
            case OR_ASSIGN ->
                "|=";
            case XOR_ASSIGN ->
                "^=";
            case COMMA ->
                ",";
            case LOGICAL_NOT ->
                "!";
            case BITWISE_NOT ->
                "~";
            case INC ->
                "++";
            case DEC ->
                "--";
        };
    }

    public boolean isEquality() {
        return this == EQ || this == NE;
    }

    public boolean isRelational() {
        return switch(this) {
            case LT, GT, LE, GE ->
                true;
            default ->
                false;
        };
    }

    public boolean isAssignment() {
        return switch(this) {
            case ASSIGN, ADD_ASSIGN, SUB_ASSIGN, MUL_ASSIGN, DIV_ASSIGN, MOD_ASSIGN, SHL_ASSIGN, SHR_ASSIGN, AND_ASSIGN, OR_ASSIGN, XOR_ASSIGN ->
                true;
            default ->
                false;
        };
    }

    public Operator removeAssignment() {
        return switch(this) {
            case ADD_ASSIGN ->
                ADD;
            case SUB_ASSIGN ->
                SUB;
            case MUL_ASSIGN ->
                MUL;
            case DIV_ASSIGN ->
                DIV;
            case MOD_ASSIGN ->
                MOD;
            case SHL_ASSIGN ->
                SHL;
            case SHR_ASSIGN ->
                SHR;
            case AND_ASSIGN ->
                BITWISE_AND;
            case OR_ASSIGN ->
                BITWISE_OR;
            case XOR_ASSIGN ->
                BITWISE_XOR;
            default ->
                this;
        };
    }

    public boolean isOnlyValidForIntegers() {
        return switch(this) {
            case MOD, SHL, SHR, BITWISE_AND, BITWISE_XOR, BITWISE_OR, MOD_ASSIGN, SHL_ASSIGN, SHR_ASSIGN, AND_ASSIGN, OR_ASSIGN, XOR_ASSIGN, BITWISE_NOT ->
                true;
            default ->
                false;
        };
    }

    public boolean isValidForVectorOrMatrix() {
        return switch(this) {
            case MUL, DIV, MOD, ADD, SUB, SHL, SHR, BITWISE_AND, BITWISE_XOR, BITWISE_OR, ADD_ASSIGN, SUB_ASSIGN, MUL_ASSIGN, DIV_ASSIGN, MOD_ASSIGN, SHL_ASSIGN, SHR_ASSIGN, AND_ASSIGN, OR_ASSIGN, XOR_ASSIGN ->
                true;
            default ->
                false;
        };
    }

    private boolean isMatrixMultiply(Type left, Type right) {
        if (this != MUL && this != MUL_ASSIGN) {
            return false;
        } else {
            return left.isMatrix() ? right.isMatrix() || right.isVector() : left.isVector() && right.isMatrix();
        }
    }

    public boolean determineBinaryType(Context context, Type left, Type right, Type[] out) {
        assert out.length >= 3;
        switch(this) {
            case EQ:
            case NE:
                if (!left.isVoid() && !left.isOpaque()) {
                    long rightToLeft = right.getCoercionCost(left);
                    long leftToRight = left.getCoercionCost(right);
                    if (Type.CoercionCost.compare(rightToLeft, leftToRight) < 0) {
                        if (Type.CoercionCost.accept(rightToLeft, false)) {
                            out[0] = left;
                            out[1] = left;
                            out[2] = context.getTypes().mBool;
                            return true;
                        }
                    } else if (Type.CoercionCost.accept(leftToRight, false)) {
                        out[0] = right;
                        out[1] = right;
                        out[2] = context.getTypes().mBool;
                        return true;
                    }
                    return false;
                }
                return false;
            case BITWISE_AND:
            case BITWISE_XOR:
            case BITWISE_OR:
            case ADD_ASSIGN:
            case SUB_ASSIGN:
            case MUL_ASSIGN:
            case DIV_ASSIGN:
            case MOD_ASSIGN:
            case SHL_ASSIGN:
            case SHR_ASSIGN:
            case AND_ASSIGN:
            case OR_ASSIGN:
            case XOR_ASSIGN:
            default:
                Type leftComponentType = left.getComponentType();
                Type rightComponentType = right.getComponentType();
                if (!leftComponentType.isBoolean() && !rightComponentType.isBoolean()) {
                    boolean isAssignment = this.isAssignment();
                    if (!this.isMatrixMultiply(left, right)) {
                        boolean leftIsVectorOrMatrix = (left.isVector() || left.isMatrix()) && this.isValidForVectorOrMatrix();
                        if (!leftIsVectorOrMatrix || !right.isScalar()) {
                            boolean rightIsVectorOrMatrix = (right.isVector() || right.isMatrix()) && this.isValidForVectorOrMatrix();
                            if (isAssignment || !rightIsVectorOrMatrix || !left.isScalar()) {
                                long rightToLeftCost = right.getCoercionCost(left);
                                long leftToRightCost = isAssignment ? Type.CoercionCost.saturate() : left.getCoercionCost(right);
                                if ((!left.isScalar() || !right.isScalar()) && !leftIsVectorOrMatrix) {
                                    return false;
                                } else if (!this.isOnlyValidForIntegers() || leftComponentType.isInteger() && rightComponentType.isInteger()) {
                                    if (Type.CoercionCost.compare(rightToLeftCost, leftToRightCost) < 0) {
                                        if (!Type.CoercionCost.accept(rightToLeftCost, false)) {
                                            return false;
                                        }
                                        out[0] = left;
                                        out[1] = left;
                                        out[2] = left;
                                    } else {
                                        if (!Type.CoercionCost.accept(leftToRightCost, false)) {
                                            return false;
                                        }
                                        out[0] = right;
                                        out[1] = right;
                                        out[2] = right;
                                    }
                                    if (this.isRelational()) {
                                        out[2] = context.getTypes().mBool;
                                    }
                                    return true;
                                } else {
                                    return false;
                                }
                            } else if (!this.determineBinaryType(context, left, rightComponentType, out)) {
                                return false;
                            } else {
                                out[1] = out[1].toCompound(context, right.getCols(), right.getRows());
                                assert !this.isRelational();
                                out[2] = out[2].toCompound(context, right.getCols(), right.getRows());
                                return true;
                            }
                        } else if (!this.determineBinaryType(context, leftComponentType, right, out)) {
                            return false;
                        } else {
                            out[0] = out[0].toCompound(context, left.getCols(), left.getRows());
                            assert !this.isRelational();
                            out[2] = out[2].toCompound(context, left.getCols(), left.getRows());
                            return true;
                        }
                    } else if (!this.determineBinaryType(context, leftComponentType, rightComponentType, out)) {
                        return false;
                    } else {
                        int leftCols = left.getCols();
                        int leftRows = left.getRows();
                        int rightCols = right.getCols();
                        int rightRows = right.getRows();
                        out[0] = out[2].toCompound(context, leftCols, leftRows);
                        out[1] = out[2].toCompound(context, rightCols, rightRows);
                        if (left.isVector()) {
                            int t = leftCols;
                            leftCols = leftRows;
                            leftRows = t;
                            assert t == 1;
                        }
                        if (leftRows > 1) {
                            out[2] = out[2].toCompound(context, rightCols, leftRows);
                        } else {
                            out[2] = out[2].toCompound(context, leftRows, rightCols);
                        }
                        return !isAssignment || out[2].getCols() == leftCols && out[2].getRows() == leftRows ? rightRows == leftCols : false;
                    }
                } else {
                    return false;
                }
            case LOGICAL_AND:
            case LOGICAL_XOR:
            case LOGICAL_OR:
                out[0] = context.getTypes().mBool;
                out[1] = context.getTypes().mBool;
                out[2] = context.getTypes().mBool;
                return left.canCoerceTo(context.getTypes().mBool, false) && right.canCoerceTo(context.getTypes().mBool, false);
            case ASSIGN:
                if (left.isVoid()) {
                    return false;
                }
                out[0] = left;
                out[1] = left;
                out[2] = left;
                return right.canCoerceTo(left, false);
            case COMMA:
                if (!left.isOpaque() && !right.isOpaque()) {
                    out[0] = left;
                    out[1] = right;
                    out[2] = right;
                    return true;
                } else {
                    return false;
                }
        }
    }
}