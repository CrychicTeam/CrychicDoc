package icyllis.arc3d.compiler.tree;

import icyllis.arc3d.compiler.ConstantFolder;
import icyllis.arc3d.compiler.Context;
import icyllis.arc3d.compiler.Operator;
import icyllis.arc3d.compiler.analysis.Analysis;
import javax.annotation.Nonnull;

public final class PrefixExpression extends Expression {

    private final Operator mOperator;

    private final Expression mOperand;

    public PrefixExpression(int position, Operator op, Expression operand) {
        super(position, operand.getType());
        this.mOperator = op;
        this.mOperand = operand;
    }

    public static Expression convert(@Nonnull Context context, int position, Operator op, Expression base) {
        Type baseType = base.getType();
        switch(op) {
            case ADD:
            case SUB:
                if (baseType.isArray() || !baseType.getComponentType().isNumeric()) {
                    context.error(position, "'" + op + "' cannot operate on '" + baseType.getName() + "'");
                    return null;
                }
                break;
            case INC:
            case DEC:
                if (!baseType.isNumeric()) {
                    context.error(position, "'" + op + "' cannot operate on '" + baseType.getName() + "'");
                    return null;
                }
                if (!Analysis.updateVariableRefKind(base, 2)) {
                    return null;
                }
                break;
            case LOGICAL_NOT:
                if (!baseType.isBoolean()) {
                    context.error(position, "'" + op + "' cannot operate on '" + baseType.getName() + "'");
                    return null;
                }
                break;
            case BITWISE_NOT:
                if (baseType.isArray() || !baseType.getComponentType().isInteger()) {
                    context.error(position, "'" + op + "' cannot operate on '" + baseType.getName() + "'");
                    return null;
                }
                if (base.isLiteral()) {
                    base = baseType.coerceExpression(context, base);
                    if (base == null) {
                        return null;
                    }
                }
                break;
            default:
                throw new AssertionError(op);
        }
        Expression result = make(context, position, op, base);
        assert result.mPosition == position;
        return result;
    }

    @Nonnull
    public static Expression make(@Nonnull Context context, int position, Operator op, Expression base) {
        Expression folded = ConstantFolder.fold(context, position, op, base);
        return (Expression) (folded != null ? folded : new PrefixExpression(position, op, base));
    }

    @Override
    public Node.ExpressionKind getKind() {
        return Node.ExpressionKind.PREFIX;
    }

    @Override
    public boolean accept(@Nonnull TreeVisitor visitor) {
        return visitor.visitPrefix(this) ? true : this.mOperand.accept(visitor);
    }

    public Operator getOperator() {
        return this.mOperator;
    }

    public Expression getOperand() {
        return this.mOperand;
    }

    @Nonnull
    @Override
    public Expression clone(int position) {
        return new PrefixExpression(position, this.mOperator, this.mOperand.clone());
    }

    @Nonnull
    @Override
    public String toString(int parentPrecedence) {
        boolean needsParens = 3 >= parentPrecedence;
        return (needsParens ? "(" : "") + this.mOperator.toString() + this.mOperand.toString(3) + (needsParens ? ")" : "");
    }
}