package icyllis.arc3d.compiler.tree;

import icyllis.arc3d.compiler.Context;
import java.util.OptionalDouble;
import javax.annotation.Nonnull;

public final class Literal extends Expression {

    private final double mValue;

    private Literal(int position, double value, Type type) {
        super(position, type);
        this.mValue = value;
    }

    @Nonnull
    public static Literal makeFloat(@Nonnull Context context, int position, float value) {
        return new Literal(position, (double) value, context.getTypes().mFloat);
    }

    @Nonnull
    public static Literal makeFloat(int position, float value, Type type) {
        if (type.isFloat()) {
            return new Literal(position, (double) value, type);
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Nonnull
    public static Literal makeInteger(@Nonnull Context context, int position, long value) {
        return new Literal(position, (double) value, context.getTypes().mInt);
    }

    @Nonnull
    public static Literal makeInteger(int position, long value, Type type) {
        if (type.isInteger() && (double) value >= type.getMinValue() && (double) value <= type.getMaxValue()) {
            return new Literal(position, (double) value, type);
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Nonnull
    public static Literal makeBoolean(@Nonnull Context context, int position, boolean value) {
        return new Literal(position, value ? 1.0 : 0.0, context.getTypes().mBool);
    }

    @Nonnull
    public static Literal makeBoolean(int position, boolean value, Type type) {
        if (type.isBoolean()) {
            return new Literal(position, value ? 1.0 : 0.0, type);
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Nonnull
    public static Literal make(int position, double value, Type type) {
        if (type.isFloat()) {
            return makeFloat(position, (float) value, type);
        } else if (type.isInteger()) {
            return makeInteger(position, (long) value, type);
        } else if (type.isBoolean()) {
            return makeBoolean(position, value != 0.0, type);
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public Node.ExpressionKind getKind() {
        return Node.ExpressionKind.LITERAL;
    }

    @Override
    public boolean accept(@Nonnull TreeVisitor visitor) {
        return visitor.visitLiteral(this);
    }

    @Override
    public boolean isLiteral() {
        return true;
    }

    public float getFloatValue() {
        assert this.getType().isFloat();
        return (float) this.mValue;
    }

    public long getIntegerValue() {
        assert this.getType().isInteger();
        return (long) this.mValue;
    }

    public boolean getBooleanValue() {
        assert this.getType().isBoolean();
        return this.mValue != 0.0;
    }

    public double getValue() {
        return this.mValue;
    }

    @Override
    public OptionalDouble getConstantValue(int i) {
        assert i == 0;
        return OptionalDouble.of(this.mValue);
    }

    @Nonnull
    @Override
    public Expression clone(int position) {
        return new Literal(position, this.mValue, this.getType());
    }

    @Nonnull
    @Override
    public String toString(int parentPrecedence) {
        if (this.getType().isFloat()) {
            return String.valueOf(this.getFloatValue());
        } else if (this.getType().isInteger()) {
            return String.valueOf(this.getIntegerValue());
        } else {
            assert this.getType().isBoolean();
            return String.valueOf(this.getBooleanValue());
        }
    }
}