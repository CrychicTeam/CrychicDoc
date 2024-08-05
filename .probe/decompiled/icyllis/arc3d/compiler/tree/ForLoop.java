package icyllis.arc3d.compiler.tree;

import icyllis.arc3d.compiler.Context;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class ForLoop extends Statement {

    private Statement mInit;

    private Expression mCondition;

    private Expression mStep;

    private Statement mStatement;

    public ForLoop(int position, Statement init, Expression condition, Expression step, Statement statement) {
        super(position);
        this.mInit = init;
        this.mCondition = condition;
        this.mStep = step;
        this.mStatement = statement;
    }

    @Nullable
    public static Statement convert(@Nonnull Context context, int pos, Statement init, Expression cond, Expression step, Statement statement) {
        if (cond != null) {
            cond = context.getTypes().mBool.coerceExpression(context, cond);
            if (cond == null) {
                return null;
            }
        }
        return step != null && step.isIncomplete(context) ? null : make(pos, init, cond, step, statement);
    }

    public static Statement make(int pos, Statement init, Expression cond, Expression step, Statement statement) {
        return new ForLoop(pos, init, cond, step, statement);
    }

    @Override
    public Node.StatementKind getKind() {
        return Node.StatementKind.FOR_LOOP;
    }

    @Override
    public boolean accept(@Nonnull TreeVisitor visitor) {
        return visitor.visitForLoop(this) ? true : this.mInit != null && this.mInit.accept(visitor) || this.mCondition != null && this.mCondition.accept(visitor) || this.mStep != null && this.mStep.accept(visitor) || this.mStatement.accept(visitor);
    }

    public Statement getInit() {
        return this.mInit;
    }

    public void setInit(Statement init) {
        this.mInit = init;
    }

    public Expression getCondition() {
        return this.mCondition;
    }

    public void setCondition(Expression condition) {
        this.mCondition = condition;
    }

    public Expression getStep() {
        return this.mStep;
    }

    public void setStep(Expression step) {
        this.mStep = step;
    }

    public Statement getStatement() {
        return this.mStatement;
    }

    public void setStatement(Statement statement) {
        this.mStatement = statement;
    }

    @Nonnull
    @Override
    public String toString() {
        String result = "for (";
        if (this.mInit != null) {
            result = result + this.mInit.toString();
        } else {
            result = result + ";";
        }
        result = result + " ";
        if (this.mCondition != null) {
            result = result + this.mCondition.toString();
        }
        result = result + "; ";
        if (this.mStep != null) {
            result = result + this.mStep.toString();
        }
        return result + ") " + this.mStatement.toString();
    }
}