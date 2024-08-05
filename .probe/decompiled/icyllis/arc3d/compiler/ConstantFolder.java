package icyllis.arc3d.compiler;

import icyllis.arc3d.compiler.analysis.Analysis;
import icyllis.arc3d.compiler.tree.ConstructorArray;
import icyllis.arc3d.compiler.tree.ConstructorCompound;
import icyllis.arc3d.compiler.tree.ConstructorScalar2Matrix;
import icyllis.arc3d.compiler.tree.Expression;
import icyllis.arc3d.compiler.tree.Literal;
import icyllis.arc3d.compiler.tree.PrefixExpression;
import icyllis.arc3d.compiler.tree.Type;
import icyllis.arc3d.compiler.tree.Variable;
import icyllis.arc3d.compiler.tree.VariableReference;
import java.util.OptionalDouble;
import java.util.OptionalLong;
import java.util.function.DoubleUnaryOperator;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ConstantFolder {

    public static OptionalLong getConstantInt(Expression value) {
        Expression expr = getConstantValueForVariable(value);
        return expr.isIntLiteral() ? OptionalLong.of(((Literal) expr).getIntegerValue()) : OptionalLong.empty();
    }

    public static Expression getConstantValueForVariable(Expression value) {
        Expression expr = getConstantValueOrNullForVariable(value);
        return expr != null ? expr : value;
    }

    public static Expression makeConstantValueForVariable(int pos, Expression value) {
        Expression expr = getConstantValueOrNullForVariable(value);
        return expr != null ? expr.clone(pos) : value;
    }

    @Nullable
    public static Expression getConstantValueOrNullForVariable(Expression value) {
        while (value instanceof VariableReference r && r.getReferenceKind() == 0) {
            Variable variable = r.getVariable();
            if ((variable.getModifiers().flags() & 8) != 0) {
                value = variable.initialValue();
                if (value != null) {
                    if (!Analysis.isCompileTimeConstant(value)) {
                        continue;
                    }
                    return value;
                }
            }
            break;
        }
        return null;
    }

    @Nullable
    public static Expression fold(@Nonnull Context context, int pos, Expression left, Operator op, Expression right, Type resultType) {
        left = getConstantValueForVariable(left);
        right = getConstantValueForVariable(right);
        if (op == Operator.ASSIGN && Analysis.isSameExpressionTree(left, right)) {
            return right.clone(pos);
        } else if (left.isBooleanLiteral() && right.isBooleanLiteral()) {
            boolean leftVal = ((Literal) left).getBooleanValue();
            boolean rightVal = ((Literal) right).getBooleanValue();
            boolean result;
            switch(op) {
                case LOGICAL_AND:
                    result = leftVal & rightVal;
                    break;
                case LOGICAL_OR:
                    result = leftVal | rightVal;
                    break;
                case LOGICAL_XOR:
                    result = leftVal ^ rightVal;
                    break;
                case EQ:
                    result = leftVal == rightVal;
                    break;
                case NE:
                    result = leftVal != rightVal;
                    break;
                default:
                    return null;
            }
            return Literal.makeBoolean(context, pos, result);
        } else if (left.isBooleanLiteral()) {
            boolean leftVal = ((Literal) left).getBooleanValue();
            if ((op != Operator.LOGICAL_AND || leftVal) && (op != Operator.LOGICAL_OR || !leftVal)) {
                return op != Operator.LOGICAL_AND && op != Operator.LOGICAL_OR && (op != Operator.LOGICAL_XOR || leftVal) && (op != Operator.EQ || !leftVal) && (op != Operator.NE || leftVal) ? null : right.clone(pos);
            } else {
                return left.clone(pos);
            }
        } else if (right.isBooleanLiteral()) {
            boolean rightVal = ((Literal) right).getBooleanValue();
            if (Analysis.hasSideEffects(left) || (op != Operator.LOGICAL_AND || rightVal) && (op != Operator.LOGICAL_OR || !rightVal)) {
                return (op != Operator.LOGICAL_AND || !rightVal) && (op != Operator.LOGICAL_OR || rightVal) && (op != Operator.LOGICAL_XOR || rightVal) && (op != Operator.EQ || !rightVal) && (op != Operator.NE || rightVal) ? null : left.clone(pos);
            } else {
                return right.clone(pos);
            }
        } else if (op == Operator.EQ && Analysis.isSameExpressionTree(left, right)) {
            return Literal.makeBoolean(context, pos, true);
        } else if (op == Operator.NE && Analysis.isSameExpressionTree(left, right)) {
            return Literal.makeBoolean(context, pos, false);
        } else {
            Type leftType = left.getType();
            Type rightType = right.getType();
            switch(op) {
                case DIV:
                case DIV_ASSIGN:
                case MOD:
                case MOD_ASSIGN:
                    int components = rightType.getComponents();
                    for (int i = 0; i < components; i++) {
                        OptionalDouble value = right.getConstantValue(i);
                        if (value.isPresent() && value.getAsDouble() == 0.0) {
                            context.error(pos, "division by zero");
                            return null;
                        }
                    }
                default:
                    boolean leftSideIsConstant = Analysis.isCompileTimeConstant(left);
                    boolean rightSideIsConstant = Analysis.isCompileTimeConstant(right);
                    if (leftSideIsConstant && rightSideIsConstant && left.isIntLiteral() && right.isIntLiteral()) {
                        long leftVal = ((Literal) left).getIntegerValue();
                        long rightVal = ((Literal) right).getIntegerValue();
                        long resultVal;
                        switch(op) {
                            case EQ:
                                resultVal = leftVal == rightVal ? 1L : 0L;
                                break;
                            case NE:
                                resultVal = leftVal != rightVal ? 1L : 0L;
                                break;
                            case DIV:
                                resultVal = leftVal / rightVal;
                                break;
                            case DIV_ASSIGN:
                            case MOD_ASSIGN:
                            default:
                                return null;
                            case MOD:
                                resultVal = leftVal % rightVal;
                                break;
                            case ADD:
                                resultVal = leftVal + rightVal;
                                break;
                            case SUB:
                                resultVal = leftVal - rightVal;
                                break;
                            case MUL:
                                resultVal = leftVal * rightVal;
                                break;
                            case BITWISE_AND:
                                resultVal = leftVal & rightVal;
                                break;
                            case BITWISE_OR:
                                resultVal = leftVal | rightVal;
                                break;
                            case BITWISE_XOR:
                                resultVal = leftVal ^ rightVal;
                                break;
                            case GT:
                                resultVal = leftVal > rightVal ? 1L : 0L;
                                break;
                            case GE:
                                resultVal = leftVal >= rightVal ? 1L : 0L;
                                break;
                            case LT:
                                resultVal = leftVal < rightVal ? 1L : 0L;
                                break;
                            case LE:
                                resultVal = leftVal <= rightVal ? 1L : 0L;
                                break;
                            case SHL:
                                if (rightVal < 0L || rightVal >= (long) leftType.getWidth()) {
                                    context.warning(pos, "shift value out of range");
                                    return null;
                                }
                                resultVal = leftVal << (int) rightVal;
                                break;
                            case SHR:
                                if (rightVal < 0L || rightVal >= (long) leftType.getWidth()) {
                                    context.warning(pos, "shift value out of range");
                                    return null;
                                }
                                resultVal = leftVal >> (int) rightVal;
                        }
                        return makeConstant(pos, (double) resultVal, resultType);
                    } else {
                        return null;
                    }
            }
        }
    }

    @Nullable
    private static Expression makeConstant(int pos, double result, Type resultType) {
        return !resultType.isNumeric() || result >= resultType.getMinValue() && result <= resultType.getMaxValue() ? Literal.make(pos, result, resultType) : null;
    }

    @Nullable
    public static Expression fold(@Nonnull Context context, int pos, @Nonnull Operator op, Expression base) {
        Expression value = getConstantValueForVariable(base);
        Type type = value.getType();
        switch(op) {
            case ADD:
                assert !type.isArray();
                assert type.getComponentType().isNumeric();
                value.mPosition = pos;
                return value;
            case SUB:
                assert !type.isArray();
                assert type.getComponentType().isNumeric();
                return fold_negation(context, pos, value);
            case MUL:
            case BITWISE_AND:
            case BITWISE_OR:
            case BITWISE_XOR:
            case GT:
            case GE:
            case LT:
            case LE:
            case SHL:
            case SHR:
            default:
                throw new AssertionError(op);
            case LOGICAL_NOT:
                assert type.isBoolean();
                switch(value.getKind()) {
                    case LITERAL:
                        Literal literal = (Literal) value;
                        return Literal.makeBoolean(pos, !literal.getBooleanValue(), value.getType());
                    case PREFIX:
                        PrefixExpression prefix = (PrefixExpression) value;
                        if (prefix.getOperator() == Operator.LOGICAL_NOT) {
                            prefix.getOperand().mPosition = pos;
                            return prefix.getOperand();
                        }
                        return null;
                    default:
                        return null;
                }
            case BITWISE_NOT:
                assert !type.isArray();
                assert type.getComponentType().isInteger();
                switch(value.getKind()) {
                    case LITERAL:
                    case CONSTRUCTOR_SCALAR_TO_VECTOR:
                    case CONSTRUCTOR_COMPOUND:
                        Expression expr = apply_to_components(context, pos, value, v -> (double) (~((long) v)));
                        if (expr != null) {
                            return expr;
                        }
                        return null;
                    case PREFIX:
                        PrefixExpression prefix = (PrefixExpression) value;
                        if (prefix.getOperator() == Operator.BITWISE_NOT) {
                            prefix.getOperand().mPosition = pos;
                            return prefix.getOperand();
                        }
                        return null;
                    default:
                        return null;
                }
            case INC:
            case DEC:
                assert type.isNumeric();
                return null;
        }
    }

    @Nullable
    private static Expression fold_negation(Context context, int pos, Expression base) {
        Expression value = getConstantValueForVariable(base);
        switch(value.getKind()) {
            case LITERAL:
            case CONSTRUCTOR_SCALAR_TO_VECTOR:
            case CONSTRUCTOR_COMPOUND:
                Expression expr = apply_to_components(context, pos, value, v -> -v);
                if (expr != null) {
                    return expr;
                }
                break;
            case PREFIX:
                PrefixExpression prefix = (PrefixExpression) value;
                if (prefix.getOperator() == Operator.SUB) {
                    return prefix.getOperand().clone(pos);
                }
                break;
            case CONSTRUCTOR_ARRAY:
                if (Analysis.isCompileTimeConstant(value)) {
                    ConstructorArray ctor = (ConstructorArray) value;
                    Expression[] ctorArgs = ctor.getArguments();
                    Expression[] newArgs = new Expression[ctorArgs.length];
                    for (int i = 0; i < ctorArgs.length; i++) {
                        Expression arg = ctorArgs[i];
                        Expression folded = fold_negation(context, pos, arg);
                        if (folded == null) {
                            folded = new PrefixExpression(pos, Operator.SUB, arg.clone());
                        }
                        newArgs[i] = folded;
                    }
                    return ConstructorArray.make(pos, ctor.getType(), newArgs);
                }
                break;
            case CONSTRUCTOR_SCALAR_TO_MATRIX:
                if (Analysis.isCompileTimeConstant(value)) {
                    ConstructorScalar2Matrix ctor = (ConstructorScalar2Matrix) value;
                    Expression folded = fold_negation(context, pos, ctor.getArguments()[0]);
                    if (folded != null) {
                        return ConstructorScalar2Matrix.make(pos, ctor.getType(), folded);
                    }
                }
        }
        return null;
    }

    private static Expression apply_to_components(Context context, int pos, Expression expr, DoubleUnaryOperator op) {
        int components = expr.getType().getComponents();
        if (components > 16) {
            return null;
        } else {
            double[] values = new double[components];
            Type componentType = expr.getType().getComponentType();
            for (int index = 0; index < components; index++) {
                OptionalDouble value = expr.getConstantValue(index);
                if (!value.isPresent()) {
                    return null;
                }
                values[index] = op.applyAsDouble(value.getAsDouble());
                if (componentType.checkLiteralOutOfRange(context, pos, values[index])) {
                    return null;
                }
            }
            return ConstructorCompound.makeFromConstants(context, pos, expr.getType(), values);
        }
    }
}