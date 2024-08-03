package icyllis.arc3d.compiler.tree;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class ReturnStatement extends Statement {

    @Nullable
    private Expression mExpression;

    private ReturnStatement(int position, @Nullable Expression expression) {
        super(position);
        this.mExpression = expression;
    }

    public static Statement make(int pos, @Nullable Expression expression) {
        return new ReturnStatement(pos, expression);
    }

    @Nullable
    public Expression getExpression() {
        return this.mExpression;
    }

    public void setExpression(@Nullable Expression expression) {
        this.mExpression = expression;
    }

    @Override
    public Node.StatementKind getKind() {
        return Node.StatementKind.RETURN;
    }

    @Override
    public boolean accept(@Nonnull TreeVisitor visitor) {
        return visitor.visitReturn(this) ? true : this.mExpression != null && this.mExpression.accept(visitor);
    }

    @Nonnull
    @Override
    public String toString() {
        return this.mExpression != null ? "return " + this.mExpression + ";" : "return;";
    }
}