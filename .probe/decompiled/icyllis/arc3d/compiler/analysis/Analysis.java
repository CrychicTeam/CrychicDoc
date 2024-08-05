package icyllis.arc3d.compiler.analysis;

import icyllis.arc3d.compiler.Operator;
import icyllis.arc3d.compiler.tree.BinaryExpression;
import icyllis.arc3d.compiler.tree.ConstructorCall;
import icyllis.arc3d.compiler.tree.Expression;
import icyllis.arc3d.compiler.tree.FieldAccess;
import icyllis.arc3d.compiler.tree.FunctionCall;
import icyllis.arc3d.compiler.tree.IndexExpression;
import icyllis.arc3d.compiler.tree.Literal;
import icyllis.arc3d.compiler.tree.PostfixExpression;
import icyllis.arc3d.compiler.tree.PrefixExpression;
import icyllis.arc3d.compiler.tree.Swizzle;
import icyllis.arc3d.compiler.tree.TreeVisitor;
import icyllis.arc3d.compiler.tree.VariableReference;
import java.util.Arrays;
import javax.annotation.Nonnull;

public final class Analysis {

    public static boolean isCompileTimeConstant(Expression expr) {
        class IsCompileTimeConstantVisitor extends TreeVisitor {

            static final IsCompileTimeConstantVisitor visitor = new IsCompileTimeConstantVisitor();

            @Override
            public boolean visitLiteral(Literal expr) {
                return false;
            }

            @Override
            protected boolean visitExpression(Expression expr) {
                return switch(expr.getKind()) {
                    case CONSTRUCTOR_ARRAY, CONSTRUCTOR_COMPOUND, CONSTRUCTOR_MATRIX_TO_MATRIX, CONSTRUCTOR_SCALAR_TO_MATRIX, CONSTRUCTOR_STRUCT, CONSTRUCTOR_SCALAR_TO_VECTOR ->
                        false;
                    default ->
                        true;
                };
            }
        }
        return !expr.accept(IsCompileTimeConstantVisitor.visitor);
    }

    public static boolean updateVariableRefKind(Expression expr, int refKind) {
        return true;
    }

    public static boolean isTrivialExpression(@Nonnull Expression expr) {
        switch(expr.getKind()) {
            case CONSTRUCTOR_ARRAY:
            case CONSTRUCTOR_STRUCT:
                return expr.getType().getComponents() <= 4 && isCompileTimeConstant(expr);
            case CONSTRUCTOR_COMPOUND:
                return isCompileTimeConstant(expr);
            case CONSTRUCTOR_MATRIX_TO_MATRIX:
            case CONSTRUCTOR_ARRAY_CAST:
                return false;
            case CONSTRUCTOR_SCALAR_TO_MATRIX:
            case CONSTRUCTOR_SCALAR_TO_VECTOR:
            case CONSTRUCTOR_COMPOUND_CAST:
            case CONSTRUCTOR_SCALAR_CAST:
                ConstructorCall ctor = (ConstructorCall) expr;
                assert ctor.getArguments().length == 1;
                Expression inner = ctor.getArguments()[0];
                return isTrivialExpression(inner);
            case LITERAL:
            case VARIABLE_REFERENCE:
                return true;
            case SWIZZLE:
                return isTrivialExpression(((Swizzle) expr).getBase());
            case PREFIX:
                PrefixExpression prefix = (PrefixExpression) expr;
                return switch(prefix.getOperator()) {
                    case ADD, SUB, LOGICAL_NOT, BITWISE_NOT ->
                        isTrivialExpression(prefix.getOperand());
                    default ->
                        false;
                };
            case FIELD_ACCESS:
                return isTrivialExpression(((FieldAccess) expr).getBase());
            case INDEX:
                IndexExpression inner = (IndexExpression) expr;
                return inner.getIndex().isIntLiteral() && isTrivialExpression(inner.getBase());
            default:
                return false;
        }
    }

    public static boolean isSameExpressionTree(Expression left, Expression right) {
        if (left.getKind() == right.getKind() && left.getType().matches(right.getType())) {
            switch(left.getKind()) {
                case CONSTRUCTOR_ARRAY:
                case CONSTRUCTOR_COMPOUND:
                case CONSTRUCTOR_MATRIX_TO_MATRIX:
                case CONSTRUCTOR_SCALAR_TO_MATRIX:
                case CONSTRUCTOR_STRUCT:
                case CONSTRUCTOR_SCALAR_TO_VECTOR:
                case CONSTRUCTOR_ARRAY_CAST:
                case CONSTRUCTOR_COMPOUND_CAST:
                case CONSTRUCTOR_SCALAR_CAST:
                    if (left.getKind() != right.getKind()) {
                        return false;
                    } else {
                        Expression[] lhsArgs = ((ConstructorCall) left).getArguments();
                        Expression[] rhsArgs = ((ConstructorCall) right).getArguments();
                        if (lhsArgs.length != rhsArgs.length) {
                            return false;
                        } else {
                            for (int i = 0; i < lhsArgs.length; i++) {
                                if (!isSameExpressionTree(lhsArgs[i], rhsArgs[i])) {
                                    return false;
                                }
                            }
                            return true;
                        }
                    }
                case LITERAL:
                    return ((Literal) left).getValue() == ((Literal) right).getValue();
                case VARIABLE_REFERENCE:
                    return ((VariableReference) left).getVariable() == ((VariableReference) right).getVariable();
                case SWIZZLE:
                    {
                        Swizzle leftExpr = (Swizzle) left;
                        Swizzle rightExpr = (Swizzle) right;
                        return Arrays.equals(leftExpr.getComponents(), rightExpr.getComponents()) && isSameExpressionTree(leftExpr.getBase(), rightExpr.getBase());
                    }
                case PREFIX:
                    {
                        PrefixExpression leftExpr = (PrefixExpression) left;
                        PrefixExpression rightExpr = (PrefixExpression) right;
                        return leftExpr.getOperator() == rightExpr.getOperator() && isSameExpressionTree(leftExpr.getOperand(), rightExpr.getOperand());
                    }
                case FIELD_ACCESS:
                    {
                        FieldAccess leftExpr = (FieldAccess) left;
                        FieldAccess rightExpr = (FieldAccess) right;
                        return leftExpr.getFieldIndex() == rightExpr.getFieldIndex() && isSameExpressionTree(leftExpr.getBase(), rightExpr.getBase());
                    }
                case INDEX:
                default:
                    return false;
            }
        } else {
            return false;
        }
    }

    public static boolean hasSideEffects(Expression expr) {
        class HasSideEffectsVisitor extends TreeVisitor {

            @Override
            public boolean visitFunctionCall(FunctionCall expr) {
                return (expr.getFunction().getModifiers().flags() & 32768) == 0;
            }

            @Override
            public boolean visitPrefix(PrefixExpression expr) {
                return expr.getOperator() == Operator.INC || expr.getOperator() == Operator.DEC;
            }

            @Override
            public boolean visitPostfix(PostfixExpression expr) {
                return expr.getOperator() == Operator.INC || expr.getOperator() == Operator.DEC;
            }

            @Override
            public boolean visitBinary(BinaryExpression expr) {
                return expr.getOperator().isAssignment();
            }
        }
        return expr.accept(new HasSideEffectsVisitor());
    }
}