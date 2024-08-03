package icyllis.arc3d.compiler.tree;

import icyllis.arc3d.compiler.Context;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class IfStatement extends Statement {

    private Expression mCondition;

    private Statement mWhenTrue;

    private Statement mWhenFalse;

    public IfStatement(int position, Expression condition, Statement whenTrue, Statement whenFalse) {
        super(position);
        this.mCondition = condition;
        this.mWhenTrue = whenTrue;
        this.mWhenFalse = whenFalse;
    }

    @Nullable
    public static Statement convert(@Nonnull Context context, int position, Expression condition, Statement whenTrue, Statement whenFalse) {
        condition = context.getTypes().mBool.coerceExpression(context, condition);
        return condition == null ? null : make(position, condition, whenTrue, whenFalse);
    }

    public static Statement make(int position, Expression condition, Statement whenTrue, Statement whenFalse) {
        return new IfStatement(position, condition, whenTrue, whenFalse);
    }

    @Override
    public Node.StatementKind getKind() {
        return Node.StatementKind.IF;
    }

    @Override
    public boolean accept(@Nonnull TreeVisitor visitor) {
        return visitor.visitIf(this) ? true : this.mCondition != null && this.mCondition.accept(visitor) || this.mWhenTrue != null && this.mWhenTrue.accept(visitor) || this.mWhenFalse != null && this.mWhenFalse.accept(visitor);
    }

    public Expression getCondition() {
        return this.mCondition;
    }

    public void setCondition(Expression condition) {
        this.mCondition = condition;
    }

    public Statement getWhenTrue() {
        return this.mWhenTrue;
    }

    public void setWhenTrue(Statement whenTrue) {
        this.mWhenTrue = whenTrue;
    }

    public Statement getWhenFalse() {
        return this.mWhenFalse;
    }

    public void setWhenFalse(Statement whenFalse) {
        this.mWhenFalse = whenFalse;
    }

    @Nonnull
    @Override
    public String toString() {
        String result = "if (" + this.mCondition.toString() + ") " + this.mWhenTrue.toString();
        if (this.mWhenFalse != null) {
            result = result + " else " + this.mWhenFalse;
        }
        return result;
    }
}