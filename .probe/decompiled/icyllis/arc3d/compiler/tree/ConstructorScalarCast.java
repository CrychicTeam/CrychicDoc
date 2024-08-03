package icyllis.arc3d.compiler.tree;

import icyllis.arc3d.compiler.ConstantFolder;
import icyllis.arc3d.compiler.Context;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class ConstructorScalarCast extends ConstructorCall {

    private ConstructorScalarCast(int position, Type type, Expression... arguments) {
        super(position, type, arguments);
        assert arguments.length == 1;
    }

    @Nullable
    public static Expression convert(@Nonnull Context context, int pos, @Nonnull Type type, @Nonnull List<Expression> args) {
        assert type.isScalar();
        if (args.size() != 1) {
            context.error(pos, "invalid arguments to '" + type + "' constructor, (expected exactly 1 argument, but found " + args.size() + ")");
            return null;
        } else {
            Type argType = ((Expression) args.get(0)).getType();
            if (!argType.isScalar()) {
                String swizzleHint = "";
                if (argType.getComponentType().matches(type)) {
                    if (argType.isVector()) {
                        swizzleHint = "; use '.x' instead";
                    } else if (argType.isMatrix()) {
                        swizzleHint = "; use '[0][0]' instead";
                    }
                }
                context.error(pos, "'" + argType + "' is not a valid parameter to '" + type + "' constructor" + swizzleHint);
                return null;
            } else {
                return make(context, pos, type, (Expression) args.get(0));
            }
        }
    }

    public static Expression make(@Nonnull Context context, int position, Type type, Expression arg) {
        assert type.isScalar();
        assert arg.getType().isScalar();
        if (arg.getType().matches(type)) {
            return arg;
        } else {
            arg = ConstantFolder.makeConstantValueForVariable(position, arg);
            if (!(arg instanceof Literal literal)) {
                return new ConstructorScalarCast(position, type, arg);
            } else {
                double value = literal.getValue();
                if (type.isNumeric() && (value < type.getMinValue() || value > type.getMaxValue())) {
                    context.error(position, String.format("value is out of range for type '%s': %.0f", type.getName(), value));
                    value = 0.0;
                }
                return Literal.make(position, value, type);
            }
        }
    }

    @Override
    public Node.ExpressionKind getKind() {
        return Node.ExpressionKind.CONSTRUCTOR_SCALAR_CAST;
    }

    @Nonnull
    @Override
    public Expression clone(int position) {
        return new ConstructorScalarCast(position, this.getType(), this.cloneArguments());
    }
}