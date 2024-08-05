package icyllis.arc3d.compiler.tree;

import icyllis.arc3d.compiler.Context;
import icyllis.arc3d.compiler.Operator;
import icyllis.arc3d.compiler.Position;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class ConditionalExpression extends Expression {

    private Expression mCondition;

    private Expression mWhenTrue;

    private Expression mWhenFalse;

    private ConditionalExpression(int position, Expression condition, Expression whenTrue, Expression whenFalse) {
        super(position, whenTrue.getType());
        this.mCondition = condition;
        this.mWhenTrue = whenTrue;
        this.mWhenFalse = whenFalse;
        assert whenTrue.getType().matches(whenFalse.getType());
    }

    @Nullable
    public static Expression convert(@Nonnull Context context, int position, Expression condition, Expression whenTrue, Expression whenFalse) {
        condition = context.getTypes().mBool.coerceExpression(context, condition);
        if (condition == null || whenTrue == null || whenFalse == null) {
            return null;
        } else if (whenTrue.getType().getComponentType().isOpaque()) {
            context.error(position, "ternary expression of opaque type '" + whenTrue.getType().getName() + "' not allowed");
            return null;
        } else {
            Type[] types = new Type[3];
            if (Operator.EQ.determineBinaryType(context, whenTrue.getType(), whenFalse.getType(), types) && types[0].matches(types[1])) {
                whenTrue = types[0].coerceExpression(context, whenTrue);
                if (whenTrue == null) {
                    return null;
                } else {
                    whenFalse = types[1].coerceExpression(context, whenFalse);
                    return whenFalse == null ? null : new ConditionalExpression(position, condition, whenTrue, whenFalse);
                }
            } else {
                context.error(Position.range(whenTrue.getStartOffset(), whenFalse.getEndOffset()), "conditional operator result mismatch: '" + whenTrue.getType().getName() + "', '" + whenFalse.getType().getName() + "'");
                return null;
            }
        }
    }

    @Override
    public Node.ExpressionKind getKind() {
        return Node.ExpressionKind.CONDITIONAL;
    }

    @Override
    public boolean accept(@Nonnull TreeVisitor visitor) {
        return visitor.visitConditional(this) ? true : this.mCondition.accept(visitor) || this.mWhenTrue != null && this.mWhenTrue.accept(visitor) || this.mWhenFalse != null && this.mWhenFalse.accept(visitor);
    }

    public Expression getCondition() {
        return this.mCondition;
    }

    public void setCondition(Expression condition) {
        this.mCondition = condition;
    }

    public Expression getWhenTrue() {
        return this.mWhenTrue;
    }

    public void setWhenTrue(Expression whenTrue) {
        this.mWhenTrue = whenTrue;
    }

    public Expression getWhenFalse() {
        return this.mWhenFalse;
    }

    public void setWhenFalse(Expression whenFalse) {
        this.mWhenFalse = whenFalse;
    }

    @Nonnull
    @Override
    public Expression clone(int position) {
        return new ConditionalExpression(position, this.mCondition.clone(), this.mWhenTrue.clone(), this.mWhenFalse.clone());
    }

    @Nonnull
    @Override
    public String toString(int parentPrecedence) {
        boolean needsParens = 15 >= parentPrecedence;
        return (needsParens ? "(" : "") + this.mCondition.toString(15) + " ? " + this.mWhenTrue.toString(15) + " : " + this.mWhenFalse.toString(15) + (needsParens ? ")" : "");
    }
}