package icyllis.arc3d.compiler.tree;

import icyllis.arc3d.compiler.Context;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class ExpressionStatement extends Statement {

    private Expression mExpression;

    public ExpressionStatement(Expression expression) {
        super(expression.mPosition);
        this.mExpression = expression;
    }

    @Nullable
    public static Statement convert(@Nonnull Context context, Expression expr) {
        return expr.isIncomplete(context) ? null : make(expr);
    }

    public static Statement make(Expression expr) {
        return new ExpressionStatement(expr);
    }

    @Override
    public Node.StatementKind getKind() {
        return Node.StatementKind.EXPRESSION;
    }

    @Override
    public boolean accept(@Nonnull TreeVisitor visitor) {
        return visitor.visitExpression(this) ? true : this.mExpression.accept(visitor);
    }

    public Expression getExpression() {
        return this.mExpression;
    }

    public void setExpression(Expression expression) {
        this.mExpression = expression;
    }

    @Nonnull
    @Override
    public String toString() {
        return this.mExpression.toString(18) + ";";
    }
}