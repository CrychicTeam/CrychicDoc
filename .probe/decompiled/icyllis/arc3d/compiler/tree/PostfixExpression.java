package icyllis.arc3d.compiler.tree;

import icyllis.arc3d.compiler.Context;
import icyllis.arc3d.compiler.Operator;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class PostfixExpression extends Expression {

    private final Expression mOperand;

    private final Operator mOperator;

    private PostfixExpression(int position, Expression operand, Operator op) {
        super(position, operand.getType());
        this.mOperand = operand;
        this.mOperator = op;
    }

    @Nullable
    public static Expression convert(@Nonnull Context context, int position, Expression base, Operator op) {
        Type baseType = base.getType();
        if (!baseType.isNumeric()) {
            context.error(position, "'" + op + "' cannot operate on '" + baseType.getName() + "'");
            return null;
        } else {
            return make(position, base, op);
        }
    }

    @Nonnull
    public static Expression make(int position, Expression base, Operator op) {
        assert base.getType().isNumeric();
        return new PostfixExpression(position, base, op);
    }

    @Override
    public Node.ExpressionKind getKind() {
        return Node.ExpressionKind.POSTFIX;
    }

    @Override
    public boolean accept(@Nonnull TreeVisitor visitor) {
        return visitor.visitPostfix(this) ? true : this.mOperand.accept(visitor);
    }

    public Expression getOperand() {
        return this.mOperand;
    }

    public Operator getOperator() {
        return this.mOperator;
    }

    @Nonnull
    @Override
    public Expression clone(int position) {
        return new PostfixExpression(position, this.mOperand.clone(), this.mOperator);
    }

    @Nonnull
    @Override
    public String toString(int parentPrecedence) {
        boolean needsParens = 2 >= parentPrecedence;
        return (needsParens ? "(" : "") + this.mOperand.toString(2) + this.mOperator.toString() + (needsParens ? ")" : "");
    }
}