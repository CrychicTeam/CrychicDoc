package icyllis.arc3d.compiler.tree;

import icyllis.arc3d.compiler.Context;
import icyllis.arc3d.compiler.Position;
import java.util.OptionalDouble;
import javax.annotation.Nonnull;

public abstract class Expression extends Node {

    private final Type mType;

    protected Expression(int position, Type type) {
        super(position);
        this.mType = type;
    }

    public abstract Node.ExpressionKind getKind();

    @Nonnull
    public Type getType() {
        assert this.mType != null;
        return this.mType;
    }

    public boolean isConstructorCall() {
        return false;
    }

    public boolean isLiteral() {
        return false;
    }

    public final boolean isIntLiteral() {
        return this.isLiteral() && this.getType().isInteger();
    }

    public final boolean isFloatLiteral() {
        return this.isLiteral() && this.getType().isFloat();
    }

    public final boolean isBooleanLiteral() {
        return this.isLiteral() && this.getType().isBoolean();
    }

    public final long getCoercionCost(Type other) {
        return this.isIntLiteral() && other.isNumeric() ? Type.CoercionCost.free() : this.getType().getCoercionCost(other);
    }

    public final boolean isIncomplete(@Nonnull Context context) {
        return switch(this.getKind()) {
            case FUNCTION_REFERENCE ->
                {
                }
            case TYPE_REFERENCE ->
                {
                }
            default ->
                false;
        };
    }

    public OptionalDouble getConstantValue(int i) {
        return OptionalDouble.empty();
    }

    @Nonnull
    public final Expression clone() {
        return this.clone(this.mPosition);
    }

    @Nonnull
    public abstract Expression clone(int var1);

    @Nonnull
    @Override
    public final String toString() {
        return this.toString(17);
    }

    @Nonnull
    public abstract String toString(int var1);
}