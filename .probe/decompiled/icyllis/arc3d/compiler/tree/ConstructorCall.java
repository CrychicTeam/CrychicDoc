package icyllis.arc3d.compiler.tree;

import icyllis.arc3d.compiler.Context;
import java.util.List;
import java.util.OptionalDouble;
import java.util.StringJoiner;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class ConstructorCall extends Expression {

    private final Expression[] mArguments;

    protected ConstructorCall(int position, Type type, Expression[] arguments) {
        super(position, type);
        assert arguments.length != 0;
        this.mArguments = arguments;
    }

    @Nullable
    public static Expression convert(@Nonnull Context context, int pos, @Nonnull Type type, @Nonnull List<Expression> args) {
        if (args.size() == 1 && ((Expression) args.get(0)).getType().matches(type) && !type.getElementType().isOpaque()) {
            Expression expr = (Expression) args.get(0);
            expr.mPosition = pos;
            return expr;
        } else if (type.isScalar()) {
            return ConstructorScalarCast.convert(context, pos, type, args);
        } else if (type.isVector() || type.isMatrix()) {
            return ConstructorCompound.convert(context, pos, type, args);
        } else if (type.isArray()) {
            return ConstructorArray.convert(context, pos, type, args);
        } else if (type.isStruct() && type.getFields().length > 0) {
            return ConstructorStruct.convert(context, pos, type, args);
        } else {
            context.error(pos, "cannot construct '" + type + "'");
            return null;
        }
    }

    @Override
    public boolean accept(@Nonnull TreeVisitor visitor) {
        if (visitor.visitConstructorCall(this)) {
            return true;
        } else {
            for (Expression arg : this.mArguments) {
                if (arg.accept(visitor)) {
                    return true;
                }
            }
            return false;
        }
    }

    @Override
    public final boolean isConstructorCall() {
        return true;
    }

    public final Type getComponentType() {
        return this.getType().getComponentType();
    }

    public final Expression[] getArguments() {
        return this.mArguments;
    }

    @Override
    public OptionalDouble getConstantValue(int i) {
        assert i >= 0 && i < this.getType().getComponents();
        for (Expression arg : this.mArguments) {
            int components = arg.getType().getComponents();
            if (i < components) {
                return arg.getConstantValue(i);
            }
            i -= components;
        }
        throw new AssertionError(i);
    }

    @Nonnull
    @Override
    public String toString(int parentPrecedence) {
        StringJoiner joiner = new StringJoiner(", ");
        for (Expression arg : this.mArguments) {
            joiner.add(arg.toString(17));
        }
        return this.getType().getName() + "(" + joiner + ")";
    }

    final Expression[] cloneArguments() {
        Expression[] result = (Expression[]) this.mArguments.clone();
        for (int i = 0; i < result.length; i++) {
            result[i] = result[i].clone();
        }
        return result;
    }
}