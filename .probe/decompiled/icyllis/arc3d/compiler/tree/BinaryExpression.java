package icyllis.arc3d.compiler.tree;

import icyllis.arc3d.compiler.ConstantFolder;
import icyllis.arc3d.compiler.Context;
import icyllis.arc3d.compiler.Operator;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class BinaryExpression extends Expression {

    private Expression mLeft;

    private final Operator mOperator;

    private Expression mRight;

    private BinaryExpression(int position, Expression left, Operator op, Expression right, Type type) {
        super(position, type);
        this.mLeft = left;
        this.mOperator = op;
        this.mRight = right;
    }

    @Nullable
    public static Expression convert(@Nonnull Context context, int position, Expression left, Operator op, Expression right) {
        Type rawLeftType = left.isIntLiteral() && right.getType().isInteger() ? right.getType() : left.getType();
        Type rawRightType = right.isIntLiteral() && left.getType().isInteger() ? left.getType() : right.getType();
        boolean isAssignment = op.isAssignment();
        Type[] types = new Type[3];
        if (!op.determineBinaryType(context, rawLeftType, rawRightType, types)) {
            context.error(position, "type mismatch: '" + op + "' cannot operate on '" + left.getType().getName() + "', '" + right.getType().getName() + "'");
            return null;
        } else {
            Type leftType = types[0];
            Type rightType = types[1];
            Type resultType = types[2];
            if (isAssignment && leftType.getComponentType().isOpaque()) {
                context.error(position, "assignments to opaque type '" + left.getType().getName() + "' are not permitted");
                return null;
            } else {
                left = leftType.coerceExpression(context, left);
                if (left == null) {
                    return null;
                } else {
                    right = rightType.coerceExpression(context, right);
                    return right == null ? null : make(context, position, left, op, right, resultType);
                }
            }
        }
    }

    @Nonnull
    public static Expression make(@Nonnull Context context, int pos, Expression left, Operator op, Expression right, Type resultType) {
        Expression folded = ConstantFolder.fold(context, pos, left, op, right, resultType);
        return (Expression) (folded != null ? folded : new BinaryExpression(pos, left, op, right, resultType));
    }

    @Override
    public Node.ExpressionKind getKind() {
        return Node.ExpressionKind.BINARY;
    }

    @Override
    public boolean accept(@Nonnull TreeVisitor visitor) {
        return visitor.visitBinary(this) ? true : this.mLeft != null && this.mLeft.accept(visitor) || this.mRight != null && this.mRight.accept(visitor);
    }

    public Expression getLeft() {
        return this.mLeft;
    }

    public Operator getOperator() {
        return this.mOperator;
    }

    public Expression getRight() {
        return this.mRight;
    }

    public void setLeft(Expression left) {
        this.mLeft = left;
    }

    public void setRight(Expression right) {
        this.mRight = right;
    }

    @Nonnull
    @Override
    public Expression clone(int position) {
        return new BinaryExpression(position, this.mLeft.clone(), this.mOperator, this.mRight.clone(), this.getType());
    }

    @Nonnull
    @Override
    public String toString(int parentPrecedence) {
        int operatorPrecedence = this.mOperator.getBinaryPrecedence();
        boolean needsParens = operatorPrecedence >= parentPrecedence;
        return (needsParens ? "(" : "") + this.mLeft.toString(operatorPrecedence) + this.mOperator.getPrettyName() + this.mRight.toString(operatorPrecedence) + (needsParens ? ")" : "");
    }
}